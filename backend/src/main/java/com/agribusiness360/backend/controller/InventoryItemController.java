package com.agribusiness360.backend.controller;

import java.util.List;
import com.agribusiness360.backend.dto.InventoryItemRequestDTO;
import com.agribusiness360.backend.dto.InventoryItemResponseDTO;
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
    public ResponseEntity<List<InventoryItemResponseDTO>> getAllItems() {
        return ResponseEntity.ok(inventoryItemService.getAllItems());
    }

    /** 
     *  Search for item by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<InventoryItemResponseDTO> getItemById(@PathVariable Integer id) {
        return ResponseEntity.ok(inventoryItemService.getItemById(id));
    }

    /**
     *  Search for all items of a specific property
     */
    @GetMapping("/property/{id}")
    public ResponseEntity<List<InventoryItemResponseDTO>> getAllItemsByProperty(@PathVariable Integer id) {
        return ResponseEntity.ok(inventoryItemService.getItemsByProperty(id));
    }

    /**
     *  Search for all items by name
     */
    @GetMapping("/item-name")
    public ResponseEntity<List<InventoryItemResponseDTO>> getItemsByName(@RequestParam String itemName) {
        return ResponseEntity.ok(inventoryItemService.getItemByName(itemName));
    }

    /**
     *  Search for all items by category
     */
    @GetMapping("/item-category")
    public ResponseEntity<List<InventoryItemResponseDTO>> getItemsByCategory(@RequestParam ItemCategory itemCategory) {
        return ResponseEntity.ok(inventoryItemService.getItemByCategory(itemCategory));
    }

    /**
     *  Search for all items by status
     */
    @GetMapping("/item-status")
    public ResponseEntity<List<InventoryItemResponseDTO>> getItemsByStatus(@RequestParam ItemStatus itemStatus) {
        return ResponseEntity.ok(inventoryItemService.getItemByStatus(itemStatus));
    }

    /**
     *  Create a new item
     */
    @PostMapping
    public ResponseEntity<InventoryItemResponseDTO> createItem(@RequestBody InventoryItemRequestDTO dto) {
        InventoryItemResponseDTO itemCreated = inventoryItemService.saveItem(dto);

        return new ResponseEntity<>(itemCreated, HttpStatus.CREATED);
    }

    /**
     *  Update an existing item
     */
    @PutMapping("/{id}")
    public ResponseEntity<InventoryItemResponseDTO> updateItem(@PathVariable Integer id, @RequestBody InventoryItemRequestDTO dto) {
        return ResponseEntity.ok(inventoryItemService.updateItem(id, dto));
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
