package GraduationProject.FreeECharge.repo;

import GraduationProject.FreeECharge.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProviderRepository extends JpaRepository<Provider, Integer> {
    boolean existsByEmail(String email);
    Optional<Provider> findByEmail(String email);
    Optional<Provider> findById(Long id);

}
