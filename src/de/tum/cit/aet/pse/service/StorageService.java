package de.tum.cit.aet.pse.service;

import de.tum.cit.aet.pse.entity.Item;
import de.tum.cit.aet.pse.entity.Vendor;
import de.tum.cit.aet.pse.entity.VendorItem;
import de.tum.cit.aet.pse.repository.ItemRepository;
import de.tum.cit.aet.pse.repository.VendorItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StorageService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private VendorItemRepository vendorItemRepository;
    public VendorItem buyItemFromStorage(Vendor vendor, Long itemId, int quantity) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));
        VendorItem vendorItem = vendorItemRepository.findByVendorAndItem(vendor, item)
                .orElse(null);
        double totalCost = item.getBasePrice() * quantity;
        vendor.deductFromWallet(totalCost);
        if (vendorItem != null) {
            vendorItem.setStock(vendorItem.getStock() + quantity);
        }else{
         vendorItem = new VendorItem(vendor,item,quantity,item.getBasePrice());
        }
        return vendorItemRepository.save(vendorItem);
    }
}
