package de.tum.cit.aet.pse.service;
import de.tum.cit.aet.pse.entity.*;
import de.tum.cit.aet.pse.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
    @Autowired
    private TradeRepository tradeRepository;

    public List<CustomerItem> getAllCustomerItemsOfACustomer(Long customerId) {
        return customerItemRepository.findByCustomerId(customerId);

    }

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
        seller.setWallet(seller.getWallet() + price);
        buyer.setWallet(buyer.getWallet() - price);
        buyerItem.setQuantity(buyerItem.getQuantity() + quantity);
        customerRepository.save(seller);
        customerRepository.save(buyer);
        customerItemRepository.save(buyerItem);

        return "Trade successful!";


    }
    public List<Trade> getAllPendingTrades(Long customerId) {
        return tradeRepository.findByRecipientIdAndStatus(customerId, "PENDING");
    }
    public void deleteTrade(Long id){
        tradeRepository.deleteById(id);
    }
    public String changeStatus(Long id, String status) {
        Trade trade = tradeRepository.getTradeById(id);
        trade.setStatus(status);
        tradeRepository.save(trade);
        return "Trade successful!";
    }
    public List<CustomerItem> getALlCustomerItemsOfAUser(String email) {
        Long id = customerRepository.findByEmail(email).get().getId();
        return customerItemRepository.findByCustomerId(id);
    }
    public Trade getTradeById(Long tradeId) {
        return tradeRepository.getTradeById(tradeId);
    }
    public String requestTrade(Long traderId, String otherEmail, String itemName, int quantity, double price) {
            Customer trader = customerRepository.findById(traderId)
                    .orElseThrow(() -> new RuntimeException("Trader not found"));

            Customer recipient = customerRepository.findByEmail(otherEmail)
                    .orElseThrow(() -> new RuntimeException("Recipient not found"));

        System.out.println(itemName);

            Item item = itemRepository.findItemByName(itemName);

        System.out.println(item.getId());


            Trade trade = new Trade();
            trade.setTrader(trader);
            trade.setRecipient(recipient);
            trade.setItem(item);
            trade.setQuantity(quantity);
            trade.setPrice(price);
            trade.setTimestamp(LocalDateTime.now());
            trade.setStatus("PENDING");

            tradeRepository.save(trade);

            return "Trade request sent successfully!";
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
