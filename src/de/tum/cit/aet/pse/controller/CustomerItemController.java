package de.tum.cit.aet.pse.controller;
import de.tum.cit.aet.pse.entity.CustomerItem;
import de.tum.cit.aet.pse.entity.Trade;
import de.tum.cit.aet.pse.service.CustomerItemService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer-item")
public class CustomerItemController {
    @Autowired
    private CustomerItemService customerItemService;
    @GetMapping("/me")
    public List<CustomerItem> getAllCustomerItemsOfACustomer(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        return customerItemService.getAllCustomerItemsOfACustomer(userId);
    }



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
    @GetMapping()
    public List<CustomerItem> getCustomerItems(@RequestParam String email) {
        return customerItemService.getALlCustomerItemsOfAUser(email);
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
    @DeleteMapping("/trade/delete/{tradeId}")
    public String deleteTradeItem(@PathVariable Long tradeId) {
        customerItemService.deleteTrade(tradeId);
        return "Trade deleted";
    }
    @GetMapping("/get-trades")
    public List<Trade> getTrades(HttpSession session) {
        Long sellerId = (Long) session.getAttribute("userId");
        return customerItemService.getAllPendingTrades(sellerId);
    }
    @PostMapping("/close-trade/{tradeId}")
    public String changeStatus(@PathVariable Long tradeId, @RequestParam String status) {
        return customerItemService.changeStatus(tradeId,status);

    }
    @GetMapping("/{tradeId}")
    public Trade getTrade(@PathVariable Long tradeId) {
        return customerItemService.getTradeById(tradeId);

    }

    @PostMapping("/request-trade")
    public String requestTrade(HttpSession session,
                               @RequestParam String recipientEmail,
                               @RequestParam String item,
                               @RequestParam int quantity,
                               @RequestParam double price) {
        Long sellerId = (Long) session.getAttribute("userId");

        if (!"customer".equals(session.getAttribute("userType"))) {
            throw new RuntimeException("Unauthorized access");
        }
        return customerItemService.requestTrade(sellerId, recipientEmail, item, quantity, price);
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
