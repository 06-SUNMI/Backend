package capstone.everyhealth.repository;

import capstone.everyhealth.domain.stakeholder.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByAdminIdAndAdminPassword(String adminId, String adminPassword);

    Optional<Admin> findByAdminId(String adminId);
}
