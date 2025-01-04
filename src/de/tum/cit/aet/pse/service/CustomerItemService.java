package de.tum.cit.aet.pse.service;
import de.tum.cit.aet.pse.entity.*;
import de.tum.cit.aet.pse.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerItemService {
    @Autowired
    private CustomerItemRepository customerItemRepository;
    @Autowired
    private VendorItemRepository vendorItemRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private VendorRepository vendorRepository;
    @Autowired
    private ItemRepository itemRepository;

    public String purchaseItem(Long customerId, Long vendorItemId, int quantity) {
        VendorItem vendorItem = vendorItemRepository.findById(vendorItemId)
                .orElseThrow(() -> new RuntimeException("VendorItem not found"));

        if (vendorItem.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock available");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        double totalCost = vendorItem.getCustomPrice() * quantity;

        if (customer.getWallet() < totalCost) {
            throw new RuntimeException("Insufficient funds");
        }
        Vendor vendor = vendorRepository.findById(vendorItem.getVendor().getId())
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        customer.deductFromWallet(totalCost);
        vendorItem.setStock(vendorItem.getStock() - quantity);
        vendor.setWallet(vendor.getWallet() + totalCost);
        vendorItem.setSoldCount(vendorItem.getSoldCount() + quantity);
        vendorItemRepository.save(vendorItem);
        vendorRepository.save(vendor);

        CustomerItem customerItem = customerItemRepository.findByCustomerAndItem(customer, vendorItem.getItem())
                .orElse(new CustomerItem(customer, vendorItem.getItem(), 0));

        customerItem.setQuantity(customerItem.getQuantity() + quantity);
        customerItemRepository.save(customerItem);

        return "Purchase successful!";
    }
    public String tradeItem(Long sellerId, Long buyerId, Long itemId, int quantity, double price) {
        Customer seller = customerRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        Customer buyer = customerRepository.findById(buyerId)
                .orElseThrow(() -> new RuntimeException("Buyer not found"));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        CustomerItem customerItem = customerItemRepository.findByCustomerAndItem(seller, item)
                .orElseThrow(() -> new RuntimeException("Seller does not own the specified item"));

        if (customerItem.getQuantity() < quantity) {
            throw new RuntimeException("Seller does not have enough quantity to trade");
        }

        if (buyer.getWallet() < price) {
            throw new RuntimeException("Buyer has insufficient funds");
        }
        customerItem.setQuantity(customerItem.getQuantity() - quantity);
        if (customerItem.getQuantity() == 0) {
            customerItemRepository.delete(customerItem);
        } else {
            customerItemRepository.save(customerItem);
        }
        CustomerItem buyerItem = customerItemRepository.findByCustomerAndItem(buyer, item)
                .orElse(new CustomerItem(buyer, customerItem.getItem(), 0));
        buyerItem.setQuantity(buyerItem.getQuantity() + quantity);
        customerItemRepository.save(buyerItem);

        return "Trade successful!";


    }
    public String throwOutItem(Long customerId, Long itemId, int quantity) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        CustomerItem customerItem = customerItemRepository.findByCustomerAndItem(customer, item)
                .orElseThrow(() -> new RuntimeException("Customer does not own the specified item"));

        customerItem.setQuantity(customerItem.getQuantity() - quantity);
        if (customerItem.getQuantity() <= 0) {
            customerItemRepository.delete(customerItem);
        } else {
            customerItemRepository.save(customerItem);
        }
        return "Item discarded successfully!";
    }
}
