package com.agribusiness360.backend.service;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.agribusiness360.backend.model.InventoryItem;
import com.agribusiness360.backend.model.ItemCategory;
import com.agribusiness360.backend.model.ItemStatus;
import com.agribusiness360.backend.repository.InventoryItemRepository;

@Service
public class InventoryItemService {

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    /**
     *  Helper method to calculate total cost
     */
    private void calculateTotalCost(InventoryItem item) {
        if(item.getQuantity() != null && item.getUnitCost() != null) {
            BigDecimal total = item.getQuantity().multiply(item.getUnitCost());
            item.setTotalCost(total);
        }
    }

    /**
     *  Retrieves all inventory items
     */
    public List<InventoryItem> getAllItems() {
        return inventoryItemRepository.findAll();
    }

    /** 
     *  Retrieves an item by ID
     */
    public InventoryItem getItemById(Integer itemId) {
        return inventoryItemRepository.findById(itemId)
            .orElseThrow(()-> new RuntimeException("No item found with the provided ID."));
    }

    /**
     *  Retrieves items belonging to a specific property
     */
    public List<InventoryItem> getItemsByProperty(Integer propertyId) {
        return inventoryItemRepository.findByRuralPropertyId(propertyId);
    }

    /**
     *  Retrieves items based on the provided name
     */
    public List<InventoryItem> getItemByName(String name) {
        return inventoryItemRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     *  Retrieve items according to a specific category
     */
    public List<InventoryItem> getItemByCategory(ItemCategory itemCategory) {
        return inventoryItemRepository.findByItemCategory(itemCategory);
    }

    /**
     *  Retrieve items according to their status
     */
    public List<InventoryItem> getItemByStatus(ItemStatus itemStatus) {
        return inventoryItemRepository.findByItemStatus(itemStatus);
    }

    /**
     *  Saves a new inventory item with automated cost calculation
     */
    @Transactional
    public InventoryItem saveItem(InventoryItem inventoryItem) {
        List<InventoryItem> itemsWithSameName = inventoryItemRepository.findByNameContainingIgnoreCase(inventoryItem.getName());

        for(InventoryItem existingItem : itemsWithSameName) {
            if(existingItem.getRuralProperty().getId().equals(inventoryItem.getRuralProperty().getId())) {
                throw new RuntimeException("This property already has an item registered with this name.");
            }
        }

        if(inventoryItem.getQuantity() != null && inventoryItem.getUnitCost() != null) {
            calculateTotalCost(inventoryItem);
        }

        return inventoryItemRepository.save(inventoryItem);
    }

    /**
     *  Updates an existing inventory item
     */
    @Transactional
    public InventoryItem updateItem(Integer id, InventoryItem details) {
        InventoryItem item = inventoryItemRepository.findById(id)
            .orElseThrow(()->  new RuntimeException("Item not found."));

        List<InventoryItem> itemsInProperty = inventoryItemRepository.findByRuralPropertyId(item.getRuralProperty().getId());

        for(InventoryItem existingItem : itemsInProperty) {
            if(existingItem.getName().equals(details.getName()) && !existingItem.getId().equals(id)) {
                throw new RuntimeException("This property already has another item registered with this name.");
            }
        }

        item.setName(details.getName());
        item.setItemCategory(details.getItemCategory());
        item.setItemStatus(details.getItemStatus());
        item.setItemUnit(details.getItemUnit());
        item.setQuantity(details.getQuantity());
        item.setUnitCost(details.getUnitCost());

        calculateTotalCost(item);

        return inventoryItemRepository.save(item);
    }

    /**
     *  Deletes an item from inventory
     */
    public void deleteItem(Integer itemId) {
        if(!inventoryItemRepository.existsById(itemId)) {
             throw new RuntimeException("Cannot delete: Item not found.");
        }

        inventoryItemRepository.deleteById(itemId);
    }
}
