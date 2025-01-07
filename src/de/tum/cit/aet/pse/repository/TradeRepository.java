package de.tum.cit.aet.pse.repository;

import de.tum.cit.aet.pse.entity.Trade;
import de.tum.cit.aet.pse.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {
    List<Trade> findByRecipient(Customer recipient);

    List<Trade> getTradesById(Long id);

    Trade getTradeById(Long id);

    List<Trade> findByRecipientIdAndStatus(Long recipientId, String status);

}
