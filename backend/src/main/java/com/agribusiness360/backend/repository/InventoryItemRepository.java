package com.agribusiness360.backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.agribusiness360.backend.model.InventoryItem;
import com.agribusiness360.backend.model.ItemCategory;
import com.agribusiness360.backend.model.ItemStatus;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Integer> {

    /**
     * Search items by rural property ID
     */
    List<InventoryItem> findByRuralPropertyId(Integer propertyId);

    /**
     * Search items by name ignoring case
     */
    List<InventoryItem> findByNameContainingIgnoreCase(String name);

    /**
     * Filter items by category
     */
    List<InventoryItem> findByItemCategory(ItemCategory itemCategory);

    /**
     * Filter item by current status
     */
    List<InventoryItem> findByItemStatus(ItemStatus itemStatus);
}
