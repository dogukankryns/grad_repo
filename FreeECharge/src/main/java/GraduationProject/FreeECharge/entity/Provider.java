package GraduationProject.FreeECharge.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "providers")
@NoArgsConstructor
@Data
public class Provider {
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

    @Column(name = "score")
    private double score;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "numberOfPorts")
    private int numberOfPorts;

    @Column(name = "storeName")
    private String storeName;

    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "provider")
    @JsonBackReference("provider-bookings")
    private List<Booking> bookings;

    /*@ManyToMany(mappedBy = "favoriteProviders")
    @JsonIgnore
    private List<User> favoritedByUsers;
*/

    public Provider(String name, String email, String phoneNumber, String password) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    // Getter and setter for bookings
    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
