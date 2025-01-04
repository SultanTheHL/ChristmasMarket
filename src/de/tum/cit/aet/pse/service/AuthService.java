package de.tum.cit.aet.pse.service;

import de.tum.cit.aet.pse.entity.Customer;
import de.tum.cit.aet.pse.entity.PasswordUtil;
import de.tum.cit.aet.pse.entity.Vendor;
import de.tum.cit.aet.pse.repository.CustomerRepository;
import de.tum.cit.aet.pse.repository.VendorRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private VendorRepository vendorRepository;

    public String register(String name, String email, String password, String userType) {
        String hashedPassword = PasswordUtil.hashPassword(password);

        if ("customer".equalsIgnoreCase(userType)) {
            if (customerRepository.findByEmail(email).isPresent()) {
                throw new RuntimeException("Email already exists");
            }
            Customer customer = new Customer();
            customer.setName(name);
            customer.setEmail(email);
            customer.setPasswordHash(hashedPassword);
            customer.setWallet(750);
            customerRepository.save(customer);
        } else if ("vendor".equalsIgnoreCase(userType)) {
            if (vendorRepository.findByEmail(email).isPresent()) {
                throw new RuntimeException("Email already exists");
            }
            Vendor vendor = new Vendor();
            vendor.setName(name);
            vendor.setEmail(email);
            vendor.setPasswordHash(hashedPassword);
            vendor.setWallet(1000);
            vendorRepository.save(vendor);
        } else {
            throw new RuntimeException("Invalid user type");
        }
        return "Registration successful";
    }

    public String login(String email, String password, HttpSession session) {
        Optional<Customer> customerOpt = customerRepository.findByEmail(email);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            if (!PasswordUtil.verifyPassword(password, customer.getPasswordHash())) {
                throw new RuntimeException("Invalid email or password");
            }
            session.setAttribute("userId", customer.getId());
            session.setAttribute("userType", "customer");
            return "Login successful as Customer";
        }

        Optional<Vendor> vendorOpt = vendorRepository.findByEmail(email);
        if (vendorOpt.isPresent()) {
            Vendor vendor = vendorOpt.get();
            if (!PasswordUtil.verifyPassword(password, vendor.getPasswordHash())) {
                throw new RuntimeException("Invalid email or password");
            }
            session.setAttribute("userId", vendor.getId());
            session.setAttribute("userType", "vendor");
            return "Login successful as Vendor";
        }

        throw new RuntimeException("Invalid email or password");
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }
}
