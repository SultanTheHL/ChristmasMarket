package de.tum.cit.aet.pse.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "item_type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Gluhwein.class, name = "GLUHWEIN"),
        @JsonSubTypes.Type(value = ApplePie.class, name = "APPLIE_PIE"),
        @JsonSubTypes.Type(value = SantaToy.class, name = "SANTA_TOY")
})

@NoArgsConstructor
@Setter
@Getter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "item_type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "items")
public abstract class Item implements Storable {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private double basePrice;
    @Column(nullable = false)
    private boolean storable;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Item(String name, double v, boolean storable) {
        this.name = name;
        this.basePrice = v;
        this.storable = storable;
    }
    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", basePrice=" + basePrice +
                ", storable=" + storable +
                '}';}
}
