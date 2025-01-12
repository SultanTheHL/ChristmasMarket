package de.tum.cit.aet.pse.service;

import de.tum.cit.aet.pse.entity.*;
import de.tum.cit.aet.pse.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class EventService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private CustomerItemRepository customerItemRepository;
    private final Random random = new Random();
    @Autowired
    private VendorItemRepository vendorItemRepository;
    @Autowired
    private ItemRepository itemRepository;

    public String triggerSantaEvent(String eventName, double amount) {
        StringBuilder result = new StringBuilder("Santa Event: ");
        result.append(eventName).append(" triggered.\n");

        switch (eventName) {
            case "Gift Everyone Gluhwein":
                giftEveryoneGluhwein(amount);
                result.append("All customers received gluhwein.\n");
                break;

            case "Bonus Money":
                addMoneyToAll(amount);
                result.append("All customers received bonus money.\n");
                break;

            case "Discount Event":
                discountAll(amount);
                result.append("Market-wide discount activated!\n");
                break;

            default:
                throw new RuntimeException("Unknown event: " + eventName);
        }

        if (random.nextInt(4) == 0) {
            String grinchEvent = triggerGrinchEvent();
            result.append(grinchEvent);
        }

        return result.toString();
    }

    private void giftEveryoneGluhwein(double amount) {
        List<Customer> customers = customerRepository.findAll();
        Item item = itemRepository.findGluhweinByName("Gluhwein");
        customers.forEach(customer -> {
            CustomerItem customerItem = customerItemRepository.findByCustomerAndItem(customer, item)
                    .orElse(new CustomerItem(customer, item, 0));

            customerItem.setQuantity(customerItem.getQuantity() + (int)amount);
            customerItemRepository.save(customerItem);
            notificationService.notifyObservers("Santa gifted gluhwein to everyone!");
        });
    }

    private void addMoneyToAll(double amount) {
        List<Customer> customers = customerRepository.findAll();
        customers.forEach(customer -> {
            customer.addToWallet(amount*10);
            customerRepository.save(customer);
            notificationService.notifyObservers("Santa gifted $" + amount + " to all customers!");
        });
    }
    private void discountAll(double amount) {

        List<VendorItem> vendorItems = vendorItemRepository.findAll();
        vendorItems.forEach(vendorItem -> {
            vendorItem.setCustomPrice(vendorItem.getCustomPrice() * (amount/10));
            vendorItemRepository.save(vendorItem);
        });

    }

    private String triggerGrinchEvent() {
        String[] grinchEvents = {
                "Steal All Gluhwein",
                "Rob Everyone's Wallet",
                "Make Items More Expensive"
        };

        String event = grinchEvents[random.nextInt(grinchEvents.length)];
        switch (event) {
            case "Steal All Gluhwein":
                stealAllGluhwein();
                return "Grinch Event: All gluhwein stolen!\n";

            case "Rob Everyone's Wallet":
                robEveryoneWallet();
                return "Grinch Event: Everyone's wallet robbed!\n";

            case "Make Items More Expensive":
                makeItemsExpensive();
                return "Grinch Event: All items are now more expensive!\n";

            default:
                throw new RuntimeException("Unknown Grinch event: " + event);
        }
    }

    private void stealAllGluhwein() {
        List<CustomerItem> customerItems = customerItemRepository.findAll();
       customerItems.forEach(customerItem -> {
            if(customerItem.getItem() instanceof Gluhwein) {
                customerItemRepository.delete(customerItem);
            }
       });
        notificationService.notifyObservers("Grinch stole all your gluhwein!");
    }

    private void robEveryoneWallet() {
        List<Customer> customers = customerRepository.findAll();
        customers.forEach(customer -> {
            customer.deductFromWallet(random.nextInt(400));
            customerRepository.save(customer);
        });
        notificationService.notifyObservers("Grinch robbed stole money from your wallet!");
    }

    private void makeItemsExpensive() {
        notificationService.notifyObservers("Grinch made items more expensive!");
        List<VendorItem> vendorItems = vendorItemRepository.findAll();
        vendorItems.forEach(vendorItem -> {
            double multiplier = random.nextDouble(1, 3);

            double newPrice = vendorItem.getCustomPrice() * multiplier;

            vendorItem.setCustomPrice(newPrice);
            vendorItemRepository.save(vendorItem);
        });
        notificationService.notifyObservers("Grinch made all items more expensive!");
    }
}
