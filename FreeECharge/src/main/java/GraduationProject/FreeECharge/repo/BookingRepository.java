package GraduationProject.FreeECharge.repo;

import GraduationProject.FreeECharge.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    // Additional query methods if needed
}
