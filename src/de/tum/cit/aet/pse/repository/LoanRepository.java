package de.tum.cit.aet.pse.repository;
import de.tum.cit.aet.pse.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByVendorId(Long vendorId);
}