package GraduationProject.FreeECharge.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "password")
    private String password;

    /*@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_favorite_providers",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "provider_id")
    )
    @JsonIgnore
    private List<Provider> favoriteProviders;*/

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonBackReference("user-bookings")// Added cascade for automatic persistence
    private List<Booking> bookings;

    public User(String name, String email, String phoneNumber, String password) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    /*// Getter and setter for favoriteProviders
    public List<Provider> getFavoriteProviders() {
        return favoriteProviders;
    }

    public void setFavoriteProviders(List<Provider> favoriteProviders) {
        this.favoriteProviders = favoriteProviders;
    }
*/
    // Getter and setter for bookings
    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
