package de.tum.cit.aet.pse.service;

import de.tum.cit.aet.pse.entity.*;
import de.tum.cit.aet.pse.repository.CustomerRepository;
import de.tum.cit.aet.pse.repository.NotificationSubscriptionRepository;
import de.tum.cit.aet.pse.repository.VendorItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationService implements NotificationSubject {
    private List<CustomerObserver> observers = new ArrayList<>();
    private Map<VendorItem, List<CustomerObserver>> itemObservers = new HashMap<>();
    @Autowired
    private NotificationSubscriptionRepository subscriptionRepository;
    @Autowired
    private CustomerRepository customerRepository;


    @Override
    public void addObserver(CustomerObserver observer) {
        System.out.println("Added an observer");
        observers.add(observer);
    }

    @Override
    public void removeObserver(CustomerObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        List<Customer> customers = customerRepository.findAll();

        for (Customer customer : customers) {
            customer.notify(message);
        }
    }
    public void addObserverForItem(VendorItem vendorItem, Customer customer) {
        List<NotificationSubscription> existingSubscriptions = subscriptionRepository.findByVendorItemAndCustomer(vendorItem, customer);
        if (!existingSubscriptions.isEmpty()) {
            System.out.println("Customer " + customer.getName() + " is already subscribed to " + vendorItem.getItem().getName());
            return;
        }
        System.out.println("Trying to add observer for item " + vendorItem);
        NotificationSubscription subscription = new NotificationSubscription(vendorItem, customer);
        subscriptionRepository.save(subscription);
        System.out.println("Customer " + customer.getName() + " subscribed to " + vendorItem.getItem().getName());


//        itemObservers.computeIfAbsent(vendorItem, k -> new ArrayList<>()).add(observer);
//        List<CustomerObserver> itemObserverList = itemObservers.get(vendorItem);
//        if (itemObserverList == null || itemObserverList.isEmpty()) {
//            System.out.println("No observers for item " + vendorItem.getItem().getName());
//        } else {
//            System.out.println("Observers for item " + vendorItem.getItem().getName() + ":");
//            for (CustomerObserver itemObserver : itemObserverList) {
//                System.out.println(itemObserver);
//            }
//        }

    }

    public void removeObserverForItem(VendorItem vendorItem, Customer customer) {
//        if (itemObservers.containsKey(vendorItem)) {
//            itemObservers.get(vendorItem).remove(observer);
//        }
        List<NotificationSubscription> subscriptions = subscriptionRepository.findByVendorItem(vendorItem);
        if (subscriptions.isEmpty()) {
            System.out.println("Customer " + customer.getName() + " is not subscribed to " + vendorItem.getItem().getName());
            return;
        }
        subscriptions.stream()
                .filter(sub -> sub.getCustomer().equals(customer))
                .forEach(subscriptionRepository::delete);
        System.out.println("Customer " + customer.getName() + " unsubscribed from " + vendorItem.getItem().getName());
    }

    public void notifyItemStock(VendorItem vendorItem) {
        String message = "Item " + vendorItem.getItem().getName() +
                " is now available in stock by vendor " +
                vendorItem.getVendor().getName() + " at price " +
                vendorItem.getCustomPrice();
        List<NotificationSubscription> subscriptions = subscriptionRepository.findByVendorItem(vendorItem);
        subscriptions.forEach(sub -> sub.getCustomer().notify(message));
    }
    public void notifyDiscount(VendorItem vendorItem, double discount) {
        String message = "Discount Alert: Item " + vendorItem.getItem().getName() +
                " is now available at a discounted price of " +
                discount + " from vendor " +
                vendorItem.getVendor().getName();
        System.out.println("Notifying observers for discounted item: " + vendorItem.getItem().getName());
        List<NotificationSubscription> subscriptions = subscriptionRepository.findByVendorItem(vendorItem);
        subscriptions.forEach(sub -> sub.getCustomer().notify(message));
    }
}
