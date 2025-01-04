package de.tum.cit.aet.pse.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class NotificationSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vendor_item_id", nullable = false)
    private VendorItem vendorItem;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    public NotificationSubscription(VendorItem vendorItem, Customer customer) {
        this.vendorItem = vendorItem;
        this.customer = customer;
    }
}
