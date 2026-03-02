package com.agribusiness360.backend.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.agribusiness360.backend.dto.InventoryItemRequestDTO;
import com.agribusiness360.backend.dto.InventoryItemResponseDTO;
import com.agribusiness360.backend.exception.BusinessException;
import com.agribusiness360.backend.exception.ResourceNotFoundException;
import com.agribusiness360.backend.model.InventoryItem;
import com.agribusiness360.backend.model.ItemCategory;
import com.agribusiness360.backend.model.ItemStatus;
import com.agribusiness360.backend.model.RuralProperty;
import com.agribusiness360.backend.repository.InventoryItemRepository;
import com.agribusiness360.backend.repository.RuralPropertyRepository;

@Service
public class InventoryItemService {

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Autowired
    private RuralPropertyRepository ruralPropertyRepository;

    /**
     *  Convert entity to DTO
     */
    private InventoryItemResponseDTO toResponse(InventoryItem inventoryItem) {
        return new InventoryItemResponseDTO(
            inventoryItem.getId(),
            inventoryItem.getRuralProperty().getId(),
            inventoryItem.getRuralProperty().getName(),
            inventoryItem.getRuralProperty().getCode(),
            inventoryItem.getName(),
            inventoryItem.getItemCategory(),
            inventoryItem.getItemUnit(),
            inventoryItem.getItemStatus(),
            inventoryItem.getQuantity(),
            inventoryItem.getUnitCost(),
            inventoryItem.getTotalCost(),
            inventoryItem.getCreatedAt()
        );
    }

    /** 
     *  Convert DTO to entity
     */
    private InventoryItem toEntity(InventoryItemRequestDTO dto) {
        InventoryItem item = new InventoryItem();

        if(dto.quantity().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Quantity cannot be negative.");
        }

        if(dto.unitCost().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Unit cost cannot be negative.");
        }

        RuralProperty property = ruralPropertyRepository.findById(dto.propertyId())
            .orElseThrow(() -> new ResourceNotFoundException("Property not found."));

        item.setRuralProperty(property);
        item.setName(dto.name());
        item.setItemCategory(dto.itemCategory());
        item.setItemUnit(dto.itemUnit());
        item.setItemStatus(dto.itemStatus());
        item.setQuantity(dto.quantity());
        item.setUnitCost(dto.unitCost());
        item.setTotalCost(dto.quantity().multiply(dto.unitCost()));
        
        return item;
    }

    /**
     *  Retrieves all inventory items
     */
    @Transactional(readOnly = true)
    public List<InventoryItemResponseDTO> getAllItems() {
        return inventoryItemRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    /** 
     *  Retrieves an item by ID
     */
    @Transactional(readOnly = true)
    public InventoryItemResponseDTO getItemById(Integer itemId) {
        InventoryItem item = inventoryItemRepository.findById(itemId)
            .orElseThrow(() -> new ResourceNotFoundException("No item found with this ID."));

        return toResponse(item);
    }

    /**
     *  Retrieves items belonging to a specific property
     */
    @Transactional(readOnly = true)
    public List<InventoryItemResponseDTO> getItemsByProperty(Integer propertyId) {
        return inventoryItemRepository.findByRuralPropertyId(propertyId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     *  Retrieves items based on the provided name
     */
    @Transactional(readOnly = true)
    public List<InventoryItemResponseDTO> getItemByName(String name) {
        return inventoryItemRepository.findByNameContainingIgnoreCase(name).stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     *  Retrieve items according to a specific category
     */
    @Transactional(readOnly = true)
    public List<InventoryItemResponseDTO> getItemByCategory(ItemCategory itemCategory) {
        return inventoryItemRepository.findByItemCategory(itemCategory).stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     *  Retrieve items according to their status
     */
    @Transactional(readOnly = true)
    public List<InventoryItemResponseDTO> getItemByStatus(ItemStatus itemStatus) {
        return inventoryItemRepository.findByItemStatus(itemStatus).stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     *  Saves a new inventory item with automated cost calculation
     */
    @Transactional
    public InventoryItemResponseDTO saveItem(InventoryItemRequestDTO dto) {
        if(inventoryItemRepository.existsByNameAndRuralPropertyId(dto.name(), dto.propertyId())) {
            throw new BusinessException("An item with this name already exists for this property.");
        }

        InventoryItem item = toEntity(dto);

        return toResponse(inventoryItemRepository.save(item));
    }

    /**
     *  Updates an existing inventory item
     */
    @Transactional
    public InventoryItemResponseDTO updateItem(Integer id, InventoryItemRequestDTO dto) {
        if(!inventoryItemRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot update: Item not found.");
        }

        inventoryItemRepository.findByNameAndRuralPropertyId(dto.name(), dto.propertyId())
            .ifPresent(existing -> {
                if(!existing.getId().equals(id)) {
                    throw new BusinessException("Another item already uses this name in this property.");
                }
            });

        InventoryItem item = toEntity(dto);

        item.setId(id);

        return toResponse(inventoryItemRepository.save(item));
    }

    /**
     *  Deletes an item from inventory
     */
    public void deleteItem(Integer id) {
        if(!inventoryItemRepository.existsById(id)) {
             throw new BusinessException("Cannot delete: Item not found.");
        }

        inventoryItemRepository.deleteById(id);
    }
}
