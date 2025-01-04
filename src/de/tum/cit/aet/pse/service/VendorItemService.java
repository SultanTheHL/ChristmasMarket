package de.tum.cit.aet.pse.service;

import de.tum.cit.aet.pse.entity.VendorItem;
import de.tum.cit.aet.pse.repository.VendorItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VendorItemService {
    @Autowired
    private VendorItemRepository vendorItemRepository;
    public List<VendorItem> getAllVendorItems() {
        return vendorItemRepository.findAll();
    }

    public Optional<VendorItem> getVendorItemById(Long id) {
        return vendorItemRepository.findById(id);
    }

    public VendorItem createVendorItem(VendorItem vendorItem) {
        return vendorItemRepository.save(vendorItem);
    }

    public VendorItem updateVendorItem(Long id, VendorItem updatedVendorItem) {
        return vendorItemRepository.findById(id).map(vendorItem -> {
            vendorItem.setStock(updatedVendorItem.getStock());
            vendorItem.setSoldCount(updatedVendorItem.getSoldCount());
            return vendorItemRepository.save(vendorItem);
        }).orElseThrow(() -> new RuntimeException("VendorItem not found!"));
    }

    public void deleteVendorItem(Long id) {
        vendorItemRepository.deleteById(id);
    }
    public List<VendorItem> getItemsForVendor(Long vendorId) {
        return vendorItemRepository.findItemsByVendorId(vendorId);
    }
}
