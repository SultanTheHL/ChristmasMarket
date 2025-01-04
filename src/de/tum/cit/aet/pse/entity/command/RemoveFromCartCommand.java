package de.tum.cit.aet.pse.entity.command;

import de.tum.cit.aet.pse.entity.VendorItem;
import de.tum.cit.aet.pse.repository.ShoppingCartRepository;

public class RemoveFromCartCommand implements CartCommand {
    private ShoppingCart cart;
    private VendorItem vendorItem;
    private ShoppingCartRepository shoppingCartRepository;

    public RemoveFromCartCommand(ShoppingCart cart, VendorItem vendorItem, ShoppingCartRepository shoppingCartRepository) {
        this.cart = cart;
        this.vendorItem = vendorItem;
        this.shoppingCartRepository = shoppingCartRepository;
    }

    @Override
    public void execute() {
        cart.getItems().removeIf(item -> item.getVendorItem().getId().equals(vendorItem.getId()));
        shoppingCartRepository.save(cart);
    }
}
