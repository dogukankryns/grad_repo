package GraduationProject.FreeECharge.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@NoArgsConstructor
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "selected_date")
    private LocalDateTime selectedDate;

    @Column(name = "selected_duration")
    private int selectedDuration;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-bookings")// name of the foreign key column in the bookings table
    private User user;

    @ManyToOne // Many bookings can be associated with one provider
    @JoinColumn(name = "provider_id")
    @JsonBackReference("provider-bookings")
    private Provider provider;

    // Constructors, getters, setters

    public Booking(LocalDateTime selectedDate, int selectedDuration) {
        this.selectedDate = selectedDate;
        this.selectedDuration = selectedDuration;
    }
}
