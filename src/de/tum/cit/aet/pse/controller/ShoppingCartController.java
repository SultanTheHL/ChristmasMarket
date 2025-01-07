package de.tum.cit.aet.pse.controller;

import de.tum.cit.aet.pse.entity.VendorItem;
import de.tum.cit.aet.pse.entity.command.*;
import de.tum.cit.aet.pse.repository.CartItemRepository;
import de.tum.cit.aet.pse.repository.ShoppingCartRepository;
import de.tum.cit.aet.pse.repository.VendorItemRepository;
import de.tum.cit.aet.pse.service.CartService;
import de.tum.cit.aet.pse.service.CustomerItemService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class ShoppingCartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private VendorItemRepository vendorItemRepository;
    @Autowired
    private CustomerItemService customerItemService;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    @PostMapping("/{customerId}/add/{vendorItemId}")
    public String addToCart(@PathVariable Long customerId,
                            @PathVariable Long vendorItemId,
                            @RequestParam int quantity) {
        ShoppingCart cart = cartService.getOrCreateCart(customerId);
        VendorItem vendorItem = vendorItemRepository.findById(vendorItemId)
                .orElseThrow(() -> new RuntimeException("VendorItem not found"));
        if(vendorItem.getStock() < quantity) {
            return "Not enough stock";
        }

        CartCommand command = new AddToCartCommand(cart, vendorItem, quantity,shoppingCartRepository);
        cartService.executeCartCommand(command);


        return "Item added to cart!";
    }

    @PostMapping("/{customerId}/remove/{vendorItemId}")
    public String removeFromCart(@PathVariable Long customerId,
                                 @PathVariable Long vendorItemId) {
        ShoppingCart cart = cartService.getOrCreateCart(customerId);
        if(cart.getItems().isEmpty()) {
            return "No items to checkout!";
        }
        VendorItem vendorItem = vendorItemRepository.findById(vendorItemId)
                .orElseThrow(() -> new RuntimeException("VendorItem not found"));


        CartCommand command = new RemoveFromCartCommand(cart, vendorItem,shoppingCartRepository);
        cartService.executeCartCommand(command);

        return "Item removed from cart!";
    }

    @PostMapping("/{customerId}/checkout")
    public String checkoutCart(@PathVariable Long customerId) {
        ShoppingCart cart = cartService.getOrCreateCart(customerId);
        if(cart.getItems().isEmpty()) {
            return "No items to checkout!";
        }

        CartCommand command = new CheckoutCartCommand(cart, customerItemService,shoppingCartRepository,cartItemRepository);
        cartService.executeCartCommand(command);

        return "Cart checked out successfully!";
    }

    //actual ones
    @PostMapping("/add/{vendorItemId}")
    public String addToCart(HttpSession session,
                            @PathVariable Long vendorItemId,
                            @RequestParam int quantity) {
        Long customerId = (Long) session.getAttribute("userId");
        if (!"customer".equals(session.getAttribute("userType"))) {
            throw new RuntimeException("Unauthorized access");
        }

        ShoppingCart cart = cartService.getOrCreateCart(customerId);
        VendorItem vendorItem = vendorItemRepository.findById(vendorItemId)
                .orElseThrow(() -> new RuntimeException("VendorItem not found"));
        if (vendorItem.getStock() < quantity) {
            return "Not enough stock";
        }

        CartCommand command = new AddToCartCommand(cart, vendorItem, quantity, shoppingCartRepository);
        cartService.executeCartCommand(command);

        return "Item added to cart!";
    }

    @PostMapping("/remove/{vendorItemId}")
    public String removeFromCart(HttpSession session, @PathVariable Long vendorItemId) {
        Long customerId = (Long) session.getAttribute("userId");
        if (!"customer".equals(session.getAttribute("userType"))) {
            throw new RuntimeException("Unauthorized access");
        }

        ShoppingCart cart = cartService.getOrCreateCart(customerId);
        VendorItem vendorItem = vendorItemRepository.findById(vendorItemId)
                .orElseThrow(() -> new RuntimeException("VendorItem not found"));

        CartCommand command = new RemoveFromCartCommand(cart, vendorItem, shoppingCartRepository);
        cartService.executeCartCommand(command);

        return "Item removed from cart!";
    }

    @PostMapping("/checkout")
    public String checkoutCart(HttpSession session) {
        Long customerId = (Long) session.getAttribute("userId");
        if (!"customer".equals(session.getAttribute("userType"))) {
            throw new RuntimeException("Unauthorized access");
        }

        ShoppingCart cart = cartService.getOrCreateCart(customerId);
        if (cart.getItems().isEmpty()) {
            return "No items to checkout!";
        }

        CartCommand command = new CheckoutCartCommand(cart, customerItemService, shoppingCartRepository, cartItemRepository);
        cartService.executeCartCommand(command);

        return "Cart checked out successfully!";
    }
    @GetMapping("/items")
    public List<CartItem> getCartItems(HttpSession session) {
        Long customerId = (Long) session.getAttribute("userId");
        ShoppingCart cart = cartService.getOrCreateCart(customerId);
        System.out.println("Cart items fetched");


        return cart.getItems();
    }
}