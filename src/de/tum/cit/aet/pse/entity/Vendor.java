package de.tum.cit.aet.pse.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Getter
@Setter
public class Vendor extends Person{
//    @OneToMany(mappedBy = "vendor")
//    private List<VendorItem> vendorItems;
    @Column(nullable = false)
    private boolean bankrupt = false;

    public Vendor() {
        addToWallet(1000);
    }
    @Override
    public String toString() {
        return "Vendor{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                '}';
    }
}
