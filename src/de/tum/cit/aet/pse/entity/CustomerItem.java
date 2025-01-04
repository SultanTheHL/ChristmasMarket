package de.tum.cit.aet.pse.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CustomerItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;


    @Column(nullable = false)
    private int quantity;
    public CustomerItem(Customer customer, Item item, int quantity) {
        this.customer = customer;
        this.item = item;
        this.quantity = quantity;
    }


    @Override
    public String toString() {
        return "CustomerItem{" +
                "id=" + id +
                ", customer=" + customer.getName() +
                ", item=" + item.getName() +
                ", quantity=" + quantity +
                '}';
    }
}
