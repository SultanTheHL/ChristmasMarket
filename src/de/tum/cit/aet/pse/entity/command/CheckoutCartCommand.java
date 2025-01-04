package de.tum.cit.aet.pse.entity.command;

import de.tum.cit.aet.pse.repository.CartItemRepository;
import de.tum.cit.aet.pse.repository.ShoppingCartRepository;
import de.tum.cit.aet.pse.service.CustomerItemService;
import org.springframework.beans.factory.annotation.Autowired;

public class CheckoutCartCommand implements CartCommand {
    private ShoppingCart cart;
    private CustomerItemService customerItemService;
    private ShoppingCartRepository shoppingCartRepository;
    private CartItemRepository cartItemRepository;

    public CheckoutCartCommand(ShoppingCart cart, CustomerItemService customerItemService, ShoppingCartRepository shoppingCartRepository, CartItemRepository cartItemRepository) {
        this.cart = cart;
        this.customerItemService = customerItemService;
        this.shoppingCartRepository = shoppingCartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public void execute() {
        for (CartItem cartItem : cart.getItems()) {
            customerItemService.purchaseItem(cart.getCustomer().getId(),
                    cartItem.getVendorItem().getId(),
                    cartItem.getQuantity());
        }
        cart.getItems().clear();
        shoppingCartRepository.save(cart);
    }
}
