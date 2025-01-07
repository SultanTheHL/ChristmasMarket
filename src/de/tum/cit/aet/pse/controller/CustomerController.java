package de.tum.cit.aet.pse.controller;
import de.tum.cit.aet.pse.entity.Customer;
import de.tum.cit.aet.pse.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Optional<Customer> getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerService.createCustomer(customer);
    }

    @PutMapping("/{id}")
    public Customer updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        return customerService.updateCustomer(id, customer);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
    }
    @PostMapping("/{customerId}/add-money")
    public String addMoney(@PathVariable Long customerId, @RequestParam double amount) {
        return customerService.addMoney(customerId, amount);
    }
    @PostMapping("/add-money")
    public String addMoney(HttpSession session, @RequestParam double amount) {
        Long customerId = (Long) session.getAttribute("userId");
        return customerService.addMoney(customerId, amount);
    }
}
