package de.tum.cit.aet.pse.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@DiscriminatorValue("SANTA_TOY")
@Entity
public class SantaToy extends Item implements Storable{
    public SantaToy() {
        super("Santa Toy", 10.00,false);
    }


}
