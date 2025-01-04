package de.tum.cit.aet.pse.repository;
import de.tum.cit.aet.pse.entity.Item;
import de.tum.cit.aet.pse.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import de.tum.cit.aet.pse.entity.VendorItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorItemRepository extends JpaRepository<VendorItem, Long>{
    @Query("SELECT vi FROM VendorItem vi WHERE vi.vendor.id = :vendorId")
    List<VendorItem> findItemsByVendorId(@Param("vendorId") Long vendorId);
    Optional<VendorItem> findByVendorAndItem(Vendor vendor, Item item);
}
