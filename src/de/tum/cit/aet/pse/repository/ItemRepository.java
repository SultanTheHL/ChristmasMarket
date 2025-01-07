package de.tum.cit.aet.pse.repository;

import de.tum.cit.aet.pse.entity.Gluhwein;
import de.tum.cit.aet.pse.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("SELECT i FROM Gluhwein i")
    List<Gluhwein> findAllGluhweinItems();
    @Query("SELECT i FROM Gluhwein i WHERE i.name = :name")
    Gluhwein findGluhweinByName(String name);
    @Query("SELECT i from Item i WHERE i.name = :name")
    Item findItemByName(String name);

}
