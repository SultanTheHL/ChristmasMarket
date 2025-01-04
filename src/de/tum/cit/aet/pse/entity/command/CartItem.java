package de.tum.cit.aet.pse.entity.command;
import de.tum.cit.aet.pse.entity.Customer;
import de.tum.cit.aet.pse.entity.VendorItem;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter
@Setter
@NoArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private VendorItem vendorItem;

    @Column(nullable = false)
    private int quantity;
}
