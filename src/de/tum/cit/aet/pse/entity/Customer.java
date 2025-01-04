package de.tum.cit.aet.pse.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Customer extends Person implements CustomerObserver {
//    @ManyToMany
//    @JoinTable(
//            name = "customer_vendoritem_subscription",
//            joinColumns = @JoinColumn(name = "customer_id"),
//            inverseJoinColumns = @JoinColumn(name = "vendoritem_id")
//    )
//    private Set<VendorItem> subscribedItems = new HashSet<>();
    @Override
    public String toString() {
        return "Customer{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", wallet=" + getWallet() +
                '}';
    }


    @Override
    public void notify(String message) {
        System.out.println("Notification for Customer " + getName() + ": " + message);
    }
}
