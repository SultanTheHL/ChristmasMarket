package de.tum.cit.aet.pse.controller;
import de.tum.cit.aet.pse.entity.Loan;
import de.tum.cit.aet.pse.entity.Vendor;
import de.tum.cit.aet.pse.service.VendorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vendors")
public class VendorController {
    @Autowired
    private VendorService vendorService;

    @GetMapping
    public List<Vendor> getAllVendors() {
        return vendorService.getAllVendors();
    }

    @GetMapping("/{id}")
    public Optional<Vendor> getVendorById(@PathVariable Long id) {
        return vendorService.getVendorById(id);
    }
    @GetMapping("/getBySession")
    public Optional<Vendor> getVendorById(HttpSession session) {
        Long vendorId = (Long) session.getAttribute("userId");
        return vendorService.getVendorById(vendorId);
    }

    @PostMapping
    public Vendor createVendor(@RequestBody Vendor vendor) {
        return vendorService.createVendor(vendor);
    }

    @PutMapping("/{id}")
    public Vendor updateVendor(@PathVariable Long id, @RequestBody Vendor vendor) {
        return vendorService.updateVendor(id, vendor);
    }

    @DeleteMapping("/{id}")
    public void deleteVendor(@PathVariable Long id) {
        vendorService.deleteVendor(id);
    }
    @PostMapping("/{vendorId}/loan")
    public Loan requestLoan(@PathVariable Long vendorId, @RequestParam double sum) {
        return vendorService.requestLoan(vendorId, sum);
    }

    @PostMapping("/loan/{loanId}/pay")
    public String payLoan(@PathVariable Long loanId) {
        return vendorService.payLoan(loanId);
    }
}
