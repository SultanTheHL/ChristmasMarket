package de.tum.cit.aet.pse;

import de.tum.cit.aet.pse.entity.*;
import de.tum.cit.aet.pse.repository.ItemRepository;
import de.tum.cit.aet.pse.repository.VendorItemRepository;
import de.tum.cit.aet.pse.repository.VendorRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.List;

@SpringBootApplication
public class Main {
    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private VendorItemRepository vendorItemRepository;
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
//    @Transactional
//    @EventListener(ApplicationReadyEvent.class)
////    public void execCodeAfterStartup() {
////        Item gluhwein = new Gluhwein();
////        Item applePie = new ApplePie();
////        Item santaToy = new SantaToy();
////
//////        itemRepository.save(gluhwein);
////        itemRepository.save(applePie);
////        itemRepository.save(santaToy);
//
//        // Step 2: Create and Save a Vendor
////        Vendor vendor = new Vendor();
////        vendor.setName("Vendor 1");
////        vendor.setEmail("vendor1@example.com");
////        vendorRepository.save(vendor);
////
////        // Step 3: Create VendorItems and Save
////        VendorItem vendorGluhwein = new VendorItem(vendor, gluhwein, 100, gluhwein.getBasePrice());
////        VendorItem vendorApplePie = new VendorItem(vendor, applePie, 200, applePie.getBasePrice());
////        VendorItem vendorSantaToy = new VendorItem(vendor, santaToy, 50,santaToy.getBasePrice());
////
////        vendorItemRepository.save(vendorGluhwein);
////        vendorItemRepository.save(vendorApplePie);
////        vendorItemRepository.save(vendorSantaToy);
////
////        // Step 4: Retrieve and Validate Data
////        List<VendorItem> vendorItems = vendorItemRepository.findAll();
////        vendorItems.forEach(System.out::println);
////        List<Gluhwein> gluhweins = itemRepository.findAllGluhweinItems();
////        gluhweins.forEach(System.out::println);
//
//    }
}
