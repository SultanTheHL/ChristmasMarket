package de.tum.cit.aet.pse.controller;

import de.tum.cit.aet.pse.entity.CustomerItem;
import de.tum.cit.aet.pse.entity.Vendor;
import de.tum.cit.aet.pse.entity.VendorItem;
import de.tum.cit.aet.pse.service.NotificationService;
import de.tum.cit.aet.pse.service.StorageService;
import de.tum.cit.aet.pse.service.VendorItemService;
import de.tum.cit.aet.pse.service.VendorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/vendor-items")
public class VendorItemController {
    @Autowired
    private VendorItemService vendorItemService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private VendorService vendorService;
    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public List<VendorItem> getAllVendorItems() {
        return vendorItemService.getAllVendorItems();
    }

    @GetMapping("/{id}")
    public Optional<VendorItem> getVendorItemById(@PathVariable Long id) {
        return vendorItemService.getVendorItemById(id);
    }

    @PostMapping
    public VendorItem createVendorItem(@RequestBody VendorItem vendorItem) {
        return vendorItemService.createVendorItem(vendorItem);
    }

    @PutMapping("/{id}")
    public VendorItem updateVendorItem(@PathVariable Long id, @RequestBody VendorItem vendorItem) {
        return vendorItemService.updateVendorItem(id, vendorItem);
    }

    @DeleteMapping("/{id}")
    public void deleteVendorItem(@PathVariable Long id) {
        vendorItemService.deleteVendorItem(id);
    }
    @GetMapping("/vendor/{vendorId}")
    public List<VendorItem> getVendorItemByVendorId(@PathVariable Long vendorId) {
        return vendorItemService.getItemsForVendor(vendorId);
    }

    //these endpoints are used for testing
    @PostMapping("/buy-test")
    public VendorItem buyItemFromStorage(@RequestParam Long vendorId, @RequestParam Long itemId, @RequestParam int quantity) {
        Optional<Vendor> vendorOpt = vendorService.getVendorById(vendorId);
        if(vendorOpt.isEmpty()) {
            return null;
        }else{
            Vendor vendor = vendorOpt.get();
            VendorItem vendorItem = storageService.buyItemFromStorage(vendor, itemId, quantity);
            notificationService.notifyItemStock(vendorItem);

        return vendorItem;
        }
    }

    @PostMapping("/{id}/set-price-test")
    public VendorItem setCustomPrice(@PathVariable Long id, @RequestParam double price) {
        VendorItem vendorItem = vendorItemService.getVendorItemById(id)
                .orElseThrow(() -> new RuntimeException("VendorItem not found"));
        boolean isDiscounted = price < vendorItem.getCustomPrice();
        vendorItem.setCustomPrice(price);
        if(isDiscounted) {
            notificationService.notifyDiscount(vendorItem,price);
        }

        return vendorItemService.updateVendorItem(vendorItem.getId(), vendorItem);
    }

    // these endpoints should be called from frontend
    @PostMapping("/buy")
    public VendorItem buyItemFromStorage(HttpSession session, @RequestParam Long itemId, @RequestParam int quantity) {
        Long vendorId = (Long) session.getAttribute("userId");
        if (!"vendor".equals(session.getAttribute("userType"))) {
            throw new RuntimeException("Unauthorized access");
        }

        Vendor vendor = vendorService.getVendorById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
        VendorItem vendorItem = storageService.buyItemFromStorage(vendor, itemId, quantity);
        notificationService.notifyItemStock(vendorItem);

        return vendorItem;
    }
    @PostMapping("/{id}/set-price")
    public VendorItem setCustomPrice(HttpSession session, @PathVariable Long id, @RequestParam double price) {
        Long vendorId = (Long) session.getAttribute("userId");
        if (!"vendor".equals(session.getAttribute("userType"))) {
            throw new RuntimeException("Unauthorized access");
        }

        VendorItem vendorItem = vendorItemService.getVendorItemById(id)
                .orElseThrow(() -> new RuntimeException("VendorItem not found"));
        if(!Objects.equals(vendorItem.getVendor().getId(), vendorId)){
            throw new RuntimeException("Vendor id mismatch");
        }
        boolean isDiscounted = price < vendorItem.getCustomPrice();
        vendorItem.setCustomPrice(price);
        if (isDiscounted) {
            notificationService.notifyDiscount(vendorItem, price);
        }

        return vendorItemService.updateVendorItem(vendorItem.getId(), vendorItem);
    }
    @GetMapping("/me")
    public List<VendorItem> getAllVendorItemsOfAVendor(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        return vendorItemService.getItemsForVendor(userId);
    }
}
