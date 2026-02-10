package com.agribusiness360.backend.controller;

import java.util.List;
import com.agribusiness360.backend.model.InventoryItem;
import com.agribusiness360.backend.model.ItemCategory;
import com.agribusiness360.backend.model.ItemStatus;
import com.agribusiness360.backend.service.InventoryItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/inventory-items")
/** SECURITY CONFIGURATION (CORS):
 *  - Currently configured to accept requests from the Vue/Vite Frontend
 *  (localhost:5173).
 *  - IMPORTANT: For production environments,
 *  replace "http://localhost:5173" with the official URL where your
 *  Frontend is hosted.
 */
@CrossOrigin(origins = "http://localhost:5173")
public class InventoryItemController {

    @Autowired
    private InventoryItemService inventoryItemService;

    /**
     *  Search all items
     */
    @GetMapping
    public ResponseEntity<List<InventoryItem>> getAllItems() {
        List<InventoryItem> items = inventoryItemService.getAllItems();

        return ResponseEntity.ok(items);
    }

    /**
     *  Search for all items of a specific property
     */
    @GetMapping("/property/{id}")
    public ResponseEntity<List<InventoryItem>> getAllItemsByProperty(@PathVariable Integer id) {
        List<InventoryItem> items = inventoryItemService.getItemsByProperty(id);

        return ResponseEntity.ok(items);
    }

    /**
     *  Search for all items by name
     */
    @GetMapping("/item-name")
    public ResponseEntity<List<InventoryItem>> getItemsByName(@RequestParam String itemName) {
        List<InventoryItem> items = inventoryItemService.getItemByName(itemName);

        return ResponseEntity.ok(items);
    }

    /**
     *  Search for all items by category
     */
    @GetMapping("/item-category")
    public ResponseEntity<List<InventoryItem>> getItemsByCategory(@RequestParam ItemCategory itemCategory) {
        List<InventoryItem> items = inventoryItemService.getItemByCategory(itemCategory);

        return ResponseEntity.ok(items);
    }

    /**
     *  Search for all items by status
     */
    @GetMapping("/item-status")
    public ResponseEntity<List<InventoryItem>> getItemsByStatus(@RequestParam ItemStatus itemStatus) {
        List<InventoryItem> items = inventoryItemService.getItemByStatus(itemStatus);

        return ResponseEntity.ok(items);
    }

    /**
     *  Create a new item
     */
    @PostMapping
    public ResponseEntity<InventoryItem> createItem(@RequestBody InventoryItem item) {
        InventoryItem createdItem = inventoryItemService.saveItem(item);

        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    /**
     *  Update an existing item
     */
    @PutMapping("/{id}")
    public ResponseEntity<InventoryItem> updateItem(@PathVariable Integer id, @RequestBody InventoryItem itemDetails) {
        return ResponseEntity.ok(inventoryItemService.updateItem(id, itemDetails));
    }

    /**
     *  Delete item
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Integer id) {
        inventoryItemService.deleteItem(id);

        return ResponseEntity.noContent().build();
    }
}
