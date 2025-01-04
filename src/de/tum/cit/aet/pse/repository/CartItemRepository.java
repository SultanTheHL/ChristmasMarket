package de.tum.cit.aet.pse.repository;

import de.tum.cit.aet.pse.entity.command.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
