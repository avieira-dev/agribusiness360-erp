package com.agribusiness360.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.agribusiness360.backend.model.ItemCategory;
import com.agribusiness360.backend.model.ItemStatus;
import com.agribusiness360.backend.model.ItemUnit;

public record InventoryItemResponseDTO(
    Integer id,
    Integer propertyId,
    String propertyName,
    String propertyCode,
    String name,
    ItemCategory itemCategory,
    ItemUnit itemUnit,
    ItemStatus itemStatus,
    BigDecimal quantity,
    BigDecimal unitCost,
    BigDecimal totalCost,
    LocalDateTime createdAt
) {}