package de.tum.cit.aet.pse.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "vendor_items")
public class VendorItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @ManyToOne
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false)
    private int soldCount;
    @Column(nullable = false)
    private double customPrice;
    public VendorItem(Vendor vendor, Item item, int stock,double customPrice) {
        this.vendor = vendor;
        this.item = item;
        this.stock = stock;
        this.soldCount = 0;
        this.customPrice = customPrice;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VendorItem that = (VendorItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    @Override
    public String toString() {
        return "This is a " + item.toString() + " belonging to " + vendor.toString() +".";
    }
}