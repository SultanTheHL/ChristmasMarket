package de.tum.cit.aet.pse.repository;

import de.tum.cit.aet.pse.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
    Optional<Vendor> findByEmail(String email);

    Optional<Vendor> getVendorById(Long id);
}
