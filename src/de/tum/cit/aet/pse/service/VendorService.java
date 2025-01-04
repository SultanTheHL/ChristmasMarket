package de.tum.cit.aet.pse.service;

import de.tum.cit.aet.pse.entity.Loan;
import de.tum.cit.aet.pse.entity.Vendor;
import de.tum.cit.aet.pse.repository.LoanRepository;
import de.tum.cit.aet.pse.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class VendorService {
    @Autowired
    private VendorRepository vendorRepository;
    @Autowired
    private LoanRepository loanRepository;
    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    public Optional<Vendor> getVendorById(Long id) {
        return vendorRepository.findById(id);
    }

    public Vendor createVendor(Vendor vendor) {
        return vendorRepository.save(vendor);
    }

    public Vendor updateVendor(Long id, Vendor updatedVendor) {
        return vendorRepository.findById(id).map(vendor -> {
            vendor.setName(updatedVendor.getName());
            vendor.setEmail(updatedVendor.getEmail());
            return vendorRepository.save(vendor);
        }).orElseThrow(() -> new RuntimeException("Vendor not found!"));
    }

    public void deleteVendor(Long id) {
        vendorRepository.deleteById(id);
    }
    public Loan requestLoan(Long vendorId, double sum) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found!"));
        double interest = new Random().nextDouble(0,20);
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = interest > 10
                ? startTime.plusDays(2)
                : startTime.plusDays(1);
        Loan loan = new Loan(vendorId, sum, interest, startTime, endTime);
        loanRepository.save(loan);

        vendor.addToWallet(sum);
        vendorRepository.save(vendor);

        return loan;
    }
    public String payLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found!"));

        Vendor vendor = vendorRepository.findById(loan.getVendorId())
                .orElseThrow(() -> new RuntimeException("Vendor not found!"));
        double repaymentAmount = loan.getSum() + (loan.getSum() * loan.getInterest() / 100);
        if (vendor.getWallet() >= repaymentAmount) {
            vendor.deductFromWallet(repaymentAmount);
            loan.setSettled(true);
            loanRepository.save(loan);
            vendorRepository.save(vendor);
            return "Loan repaid successfully!";
        } else {
            vendor.setWallet(-1);
            vendorRepository.save(vendor);
            return "You are bankrupt!";
        }
    }
}
