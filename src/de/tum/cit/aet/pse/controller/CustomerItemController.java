package de.tum.cit.aet.pse.controller;
import de.tum.cit.aet.pse.service.CustomerItemService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer-item")
public class CustomerItemController {
    @Autowired
    private CustomerItemService customerItemService;


    // this endpoints are used for testing
    @PostMapping("/{customerId}/{vendorItemId}")
    public String purchaseItem(@PathVariable Long customerId,
                               @PathVariable Long vendorItemId,
                               @RequestParam int quantity) {
        return customerItemService.purchaseItem(customerId, vendorItemId, quantity);
    }
    @PostMapping("/{sellerId}/trade/{buyerId}/{itemId}")
    public String tradeItem(@PathVariable Long sellerId,
                            @PathVariable Long buyerId,
                            @PathVariable Long itemId,
                            @RequestParam int quantity,
                            @RequestParam double price) {
        return customerItemService.tradeItem(sellerId, buyerId, itemId, quantity, price);
    }
    @DeleteMapping("/{customerId}/throw/{itemId}")
    public String throwOutItem(@PathVariable Long customerId, @PathVariable Long itemId,@RequestParam int quantity) {
        return customerItemService.throwOutItem(customerId, itemId,quantity);
    }

    // these endpoints should be called from frontend

    @PostMapping("/purchase")
    public String purchaseItemUsingSession(HttpSession session, @RequestParam Long vendorItemId, @RequestParam int quantity) {
        Long customerId = (Long) session.getAttribute("userId");
        if (!"customer".equals(session.getAttribute("userType"))) {
            throw new RuntimeException("Unauthorized access");
        }
        return customerItemService.purchaseItem(customerId, vendorItemId, quantity);
    }
    @PostMapping("/trade/{buyerId}/{itemId}")
    public String tradeItem(HttpSession session,
                            @PathVariable Long buyerId,
                            @PathVariable Long itemId,
                            @RequestParam int quantity,
                            @RequestParam double price) {
        Long sellerId = (Long) session.getAttribute("userId");
        if (!"customer".equals(session.getAttribute("userType"))) {
            throw new RuntimeException("Unauthorized access");
        }
        return customerItemService.tradeItem(sellerId, buyerId, itemId, quantity, price);
    }
    @DeleteMapping("/throw/{itemId}")
    public String throwOutItem(HttpSession session, @PathVariable Long itemId,@RequestParam int quantity) {
        Long customerId = (Long) session.getAttribute("userId");
        if (!"customer".equals(session.getAttribute("userType"))) {
            throw new RuntimeException("Unauthorized access");
        }
        return customerItemService.throwOutItem(customerId, itemId,quantity);
    }
}
