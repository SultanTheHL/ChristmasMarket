package de.tum.cit.aet.pse.service;

import de.tum.cit.aet.pse.entity.command.CartCommand;
import de.tum.cit.aet.pse.entity.command.ShoppingCart;
import de.tum.cit.aet.pse.repository.CustomerRepository;
import de.tum.cit.aet.pse.repository.ShoppingCartRepository;
import de.tum.cit.aet.pse.repository.VendorItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    @Autowired
    private ShoppingCartRepository cartRepository;
    @Autowired
    private CustomerRepository customerRepository;

    public ShoppingCart getOrCreateCart(Long customerId) {
        return cartRepository.findByCustomerId(customerId)
                .orElseGet(() -> {
                    ShoppingCart newCart = new ShoppingCart();
                    newCart.setCustomer(customerRepository.findById(customerId)
                            .orElseThrow(() -> new RuntimeException("Customer not found")));
                    return cartRepository.save(newCart);
                });
    }

    public void executeCartCommand(CartCommand command) {
        command.execute();
    }
}