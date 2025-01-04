package de.tum.cit.aet.pse.controller;

import de.tum.cit.aet.pse.entity.Customer;
import de.tum.cit.aet.pse.entity.VendorItem;
import de.tum.cit.aet.pse.service.CustomerService;
import de.tum.cit.aet.pse.service.NotificationService;
import de.tum.cit.aet.pse.service.VendorItemService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private VendorItemService vendorItemService;
   // test ones
    @PostMapping("/{customerId}/subscribe/{itemId}")
    public String subscribeToItem(@PathVariable Long customerId, @PathVariable Long itemId) {
        Customer customer = customerService.getCustomerById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found!"));
        VendorItem vendorItem = vendorItemService.getVendorItemById(itemId)
                .orElseThrow(() -> new RuntimeException("VendorItem not found!"));
        notificationService.addObserverForItem(vendorItem, customer);
        return "Customer " + customer.getName() + " subscribed to notifications for " + vendorItem.getItem().getName();
    }

    @PostMapping("/{customerId}/unsubscribe/{itemId}")
    public String unsubscribeFromItem(@PathVariable Long customerId, @PathVariable Long itemId) {
        Customer customer = customerService.getCustomerById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found!"));
        VendorItem vendorItem = vendorItemService.getVendorItemById(itemId)
                .orElseThrow(() -> new RuntimeException("VendorItem not found!"));
        notificationService.removeObserverForItem(vendorItem, customer);
        return "Customer " + customer.getName() + " unsubscribed from notifications for " + vendorItem.getItem().getName();
    }

    // actual ones
    @PostMapping("/subscribe/{itemId}")
    public String subscribeToItem(HttpSession session, @PathVariable Long itemId) {
        Long customerId = (Long) session.getAttribute("userId");
        if (!"customer".equals(session.getAttribute("userType"))) {
            throw new RuntimeException("Unauthorized access");
        }

        Customer customer = customerService.getCustomerById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found!"));
        VendorItem vendorItem = vendorItemService.getVendorItemById(itemId)
                .orElseThrow(() -> new RuntimeException("VendorItem not found!"));
        notificationService.addObserverForItem(vendorItem, customer);
        return "Customer " + customer.getName() + " subscribed to notifications for " + vendorItem.getItem().getName();
    }

    @PostMapping("/unsubscribe/{itemId}")
    public String unsubscribeFromItem(HttpSession session, @PathVariable Long itemId) {
        Long customerId = (Long) session.getAttribute("userId");
        if (!"customer".equals(session.getAttribute("userType"))) {
            throw new RuntimeException("Unauthorized access");
        }

        Customer customer = customerService.getCustomerById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found!"));
        VendorItem vendorItem = vendorItemService.getVendorItemById(itemId)
                .orElseThrow(() -> new RuntimeException("VendorItem not found!"));
        notificationService.removeObserverForItem(vendorItem, customer);
        return "Customer " + customer.getName() + " unsubscribed from notifications for " + vendorItem.getItem().getName();
    }
}
