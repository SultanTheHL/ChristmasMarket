package de.tum.cit.aet.pse.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("APPLE_PIE")
public class ApplePie extends Item implements Consumable {
    public ApplePie() {
        super("Apple Pie", 4,true);
    }



    @Override
    public void consume() {
        System.out.println("Eating Apple Pie!");
    }


}
