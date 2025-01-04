package de.tum.cit.aet.pse.entity.command;

import de.tum.cit.aet.pse.entity.VendorItem;
import de.tum.cit.aet.pse.repository.ShoppingCartRepository;

public class AddToCartCommand implements CartCommand{
    private ShoppingCart cart;
    private VendorItem vendorItem;
    private int quantity;
    private ShoppingCartRepository shoppingCartRepository;

    public AddToCartCommand(ShoppingCart cart, VendorItem vendorItem, int quantity, ShoppingCartRepository shoppingCartRepository) {
        this.cart = cart;
        this.vendorItem = vendorItem;
        this.quantity = quantity;
        this.shoppingCartRepository = shoppingCartRepository;
    }

    @Override
    public void execute() {
        CartItem item = cart.getItems().stream()
                .filter(cartItem -> cartItem.getVendorItem().getId().equals(vendorItem.getId()))
                .findFirst()
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setVendorItem(vendorItem);
                    cart.getItems().add(newItem);
                    return newItem;
                });
        item.setQuantity(item.getQuantity() + quantity);
        shoppingCartRepository.save(cart);
    }
}
