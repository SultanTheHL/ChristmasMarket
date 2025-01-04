package de.tum.cit.aet.pse.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("GLUHWEIN")
public class Gluhwein extends Item implements Drinkable{
    public Gluhwein() {
        super("Gluhwein", 3.5,false);
    }

    @Override
    public void drink() {
        System.out.println("Drinking Gluhwein!");
    }
}
