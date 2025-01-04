package de.tum.cit.aet.pse.repository;

import de.tum.cit.aet.pse.entity.Customer;
import de.tum.cit.aet.pse.entity.CustomerItem;
import de.tum.cit.aet.pse.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerItemRepository extends JpaRepository<CustomerItem, Long> {
    Optional<CustomerItem> findByCustomerAndItem(Customer customer, Item item);
}