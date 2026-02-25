package com.agribusiness360.backend.dto;

import java.math.BigDecimal;
import com.agribusiness360.backend.model.ItemCategory;
import com.agribusiness360.backend.model.ItemStatus;
import com.agribusiness360.backend.model.ItemUnit;

public record InventoryItemRequestDTO(
    Integer propertyId,
    String name,
    ItemCategory itemCategory,
    ItemUnit itemUnit,
    ItemStatus itemStatus,
    BigDecimal quantity,
    BigDecimal unitCost
) {}