package GraduationProject.FreeECharge.rest;

import GraduationProject.FreeECharge.entity.Booking;
import GraduationProject.FreeECharge.entity.Provider;
import GraduationProject.FreeECharge.repo.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

@RestController
@RequestMapping("/api/providers")
public class ProviderController {

    @Autowired
    private ProviderRepository providerRepository;

    @PostMapping("/register")
    public ResponseEntity<String> registerProvider(@RequestBody Provider provider) {
        // Validate user data if needed

        // Check if the user already exists
        if (providerRepository.existsByEmail(provider.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
        }

        // Save the new user
        providerRepository.save(provider);

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<List<Provider>> getAllProviders() {
        List<Provider> providers = providerRepository.findAll();
        return ResponseEntity.ok(providers);
    }

    @GetMapping("/{providerId}")
    public ResponseEntity<Provider> getProviderById(@PathVariable int providerId) {
        // Validate providerId if needed

        // Find the provider by ID
        Optional<Provider> optionalProvider = providerRepository.findById(providerId);

        if (optionalProvider.isPresent()) {
            Provider provider = optionalProvider.get();
            return ResponseEntity.ok(provider);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    /*@PostMapping("/login")
    public ResponseEntity<String> loginProvider(@RequestBody Provider loginProvider) {
        // Validate user data if needed

        // Find the user by email
        Optional<Provider> optionalProvider = providerRepository.findByEmail(loginProvider.getEmail());

        if (optionalProvider.isPresent()) {
            Provider provider = optionalProvider.get();

            // Check if the password matches
            if (provider.getPassword().equals(loginProvider.getPassword())) {
                return ResponseEntity.ok("Login successful");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Provider not found");
        }
    }*/

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginProvider(@RequestBody Provider loginProvider) {
        // Validate user data if needed

        // Find the user by email
        Optional<Provider> optionalProvider = providerRepository.findByEmail(loginProvider.getEmail());

        if (optionalProvider.isPresent()) {
            Provider provider = optionalProvider.get();

            // Check if the password matches
            if (provider.getPassword().equals(loginProvider.getPassword())) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Login successful");
                response.put("providerId", provider.getId());

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "Incorrect password"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "Provider not found"));
        }
    }

    @PutMapping("/update/{providerId}")
    public ResponseEntity<String> updateProviderInformation(
            @PathVariable Long providerId,
            @RequestBody Map<String, Object> updatedInfo) {
        // Find the provider by ID
        Optional<Provider> optionalProvider = providerRepository.findById(providerId);

        if (optionalProvider.isPresent()) {
            Provider provider = optionalProvider.get();

            // Update the provider information
            if (updatedInfo.containsKey("latitude")) {
                provider.setLatitude((Double) updatedInfo.get("latitude"));
            }
            if (updatedInfo.containsKey("longitude")) {
                provider.setLongitude((Double) updatedInfo.get("longitude"));
            }
            if (updatedInfo.containsKey("portNumber")) {
                provider.setNumberOfPorts((Integer) updatedInfo.get("portNumber"));
            }
            if (updatedInfo.containsKey("providerName")) {
                provider.setStoreName((String) updatedInfo.get("providerName"));
            }
            if (updatedInfo.containsKey("address")) {
                provider.setAddress((String) updatedInfo.get("address"));
            }

            // Save the updated provider
            providerRepository.save(provider);

            return ResponseEntity.ok("Provider information updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Provider not found");
        }
    }

    @PostMapping("/bookings")
    public ResponseEntity<String> createBooking(
            @RequestParam int providerId,
            @RequestBody Map<String, Object> requestData) {
        // Validate parameters if needed

        // Find the provider by ID
        Optional<Provider> optionalProvider = providerRepository.findById(providerId);
        System.out.println(optionalProvider);


        if (optionalProvider.isPresent()) {
            Provider provider = optionalProvider.get();
            //System.out.println(provider);
            //System.out.println(booking);
            // Set the provider for the booking
            Map<String, Object> bookingData = (Map<String, Object>) requestData.get("booking");
            //System.out.println(bookingData);

            if (bookingData != null) {
                Object selectedDateObject = bookingData.get("selectedDate");
                Object selectedDurationObject = bookingData.get("selectedDuration");
                // System.out.println(bookingData);

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
                            booking.setProvider(provider);
                            System.out.println(booking);

                            // Save the booking
                            // Make sure you have a BookingRepository for saving bookings
                            // For example: bookingRepository.save(booking);

                            // Add the booking to the user's list of bookings
                            provider.getBookings().add(booking);

                            // Save the updated user
                            providerRepository.save(provider);

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

    @GetMapping("/bookings/{providerId}")
    public ResponseEntity<List<Booking>> getProviderBookings(@PathVariable int providerId) {
        // Validate providerId if needed

        // Find the provider by ID
        Optional<Provider> optionalProvider = providerRepository.findById(providerId);

        if (optionalProvider.isPresent()) {
            Provider provider = optionalProvider.get();
            List<Booking> bookings = provider.getBookings();
            return ResponseEntity.ok(bookings);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }

}
