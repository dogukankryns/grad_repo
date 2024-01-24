package GraduationProject.FreeECharge.rest;

import GraduationProject.FreeECharge.entity.Booking;
import GraduationProject.FreeECharge.entity.User;
import GraduationProject.FreeECharge.repo.ProviderRepository;
import GraduationProject.FreeECharge.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        // Validate user data if needed

        // Check if the user already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
        }

        // Save the new user
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable int userId) {
        // Validate userId if needed

        // Find the user by ID
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody User loginUser) {
        // Validate user data if needed

        // Find the user by email
        Optional<User> optionalUser = userRepository.findByEmail(loginUser.getEmail());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Check if the password matches
            if (user.getPassword().equals(loginUser.getPassword())) {
                // Login successful
                Map<String, Object> response = new HashMap<>();
                response.put("status", "Login successful");
                response.put("userId", user.getId());
                return ResponseEntity.ok(response);
            } else {
                // Incorrect password
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Incorrect password"));
            }
        } else {
            // User not found
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "User not found"));
        }
    }

    // Update the toggleFavorite method
    /*@PostMapping("/favorites")
    public ResponseEntity<String> toggleFavorite(
            @RequestParam int userId,
            @RequestParam int providerId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Optional<Provider> optionalProvider = providerRepository.findById(providerId);

            if (optionalProvider.isPresent()) {
                Provider provider = optionalProvider.get();

                // Check if the user has already favorited the provider
                if (user.getFavoriteProviders().contains(provider)) {
                    // Remove provider from favorites
                    user.getFavoriteProviders().remove(provider);
                } else {
                    // Add provider to favorites
                    user.getFavoriteProviders().add(provider);
                }

                // Save the updated user
                userRepository.save(user);

                return ResponseEntity.ok("Favorite status updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Provider not found");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }
    }

    // Update the getUserFavorites method
    @GetMapping("/favorites")
    public ResponseEntity<List<Provider>> getUserFavorites(@RequestParam int userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Provider> favoriteProviders = user.getFavoriteProviders();
            return ResponseEntity.ok(favoriteProviders);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }*/


    @GetMapping("/bookings")
    public ResponseEntity<List<Booking>> getUserBookings(@RequestParam int userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Booking> bookings = user.getBookings();

            if (bookings != null) {
                return ResponseEntity.ok(bookings);
            } else {
                return ResponseEntity.ok(Collections.emptyList());
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }


    @PostMapping("/bookings")
    public ResponseEntity<String> saveBooking(@RequestParam int userId, @RequestBody Map<String, Object> requestData) {
        // Validate parameters if needed

        // Find the user by ID
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Extract booking details from the requestData map
            Map<String, Object> bookingData = (Map<String, Object>) requestData.get("booking");

            if (bookingData != null) {
                Object selectedDateObject = bookingData.get("selectedDate");
                Object selectedDurationObject = bookingData.get("selectedDuration");

                if (selectedDateObject != null && selectedDurationObject != null) {
                    System.out.println("Selected Date Object Type: " + selectedDateObject.getClass().getName());
                    System.out.println("Selected Duration Object Type: " + selectedDurationObject.getClass().getName());

                    if (selectedDateObject instanceof String && selectedDurationObject instanceof Integer) {
                        String selectedDateString = (String) selectedDateObject;
                        int selectedDuration = (int) selectedDurationObject;

                        try {
                            // Parse the date
                            LocalDateTime selectedDate = LocalDateTime.parse(selectedDateString);

                            // Create a new Booking instance
                            Booking booking = new Booking(selectedDate, selectedDuration);

                            // Set the user for the booking
                            booking.setUser(user);

                            // Save the booking
                            // Make sure you have a BookingRepository for saving bookings
                            // For example: bookingRepository.save(booking);

                            // Add the booking to the user's list of bookings
                            user.getBookings().add(booking);

                            // Save the updated user
                            userRepository.save(user);

                            return ResponseEntity.ok("Booking saved successfully");
                        } catch (DateTimeParseException e) {
                            return ResponseEntity.badRequest().body("Invalid date format");
                        }
                    } else {
                        return ResponseEntity.badRequest().body("Invalid data types for 'selectedDate' or 'selectedDuration'");
                    }
                } else {
                    return ResponseEntity.badRequest().body("Null values for 'selectedDate' or 'selectedDuration'");
                }
            } else {
                return ResponseEntity.badRequest().body("Booking details not found in the request");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }








}