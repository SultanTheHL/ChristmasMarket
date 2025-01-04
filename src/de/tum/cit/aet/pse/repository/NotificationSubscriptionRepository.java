package de.tum.cit.aet.pse.repository;

import org.springframework.stereotype.Repository;
import de.tum.cit.aet.pse.entity.NotificationSubscription;
import de.tum.cit.aet.pse.entity.VendorItem;
import de.tum.cit.aet.pse.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


@Repository
public interface NotificationSubscriptionRepository extends JpaRepository<NotificationSubscription, Long>{
    List<NotificationSubscription> findByVendorItem(VendorItem vendorItem);
    List<NotificationSubscription> findByCustomer(Customer customer);
    List<NotificationSubscription> findByVendorItemAndCustomer(VendorItem vendorItem, Customer customer);
}
