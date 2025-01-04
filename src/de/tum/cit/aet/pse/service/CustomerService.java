package de.tum.cit.aet.pse.service;
import de.tum.cit.aet.pse.entity.Customer;
import de.tum.cit.aet.pse.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        return customerRepository.findById(id).map(customer -> {
            customer.setName(updatedCustomer.getName());
            customer.setEmail(updatedCustomer.getEmail());
            customer.setWallet(updatedCustomer.getWallet());
            return customerRepository.save(customer);
        }).orElseThrow(() -> new RuntimeException("Customer not found!"));
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
    public String addMoney(Long customerId, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customer.addToWallet(amount);
        customerRepository.save(customer);

        return "Customer's balance has been updated. New balance: " + customer.getWallet();
    }
}
