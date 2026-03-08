package com.agribusiness360.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.agribusiness360.backend.model.CropStatus;
import com.agribusiness360.backend.model.CultureType;
import com.agribusiness360.backend.model.ProductStatus;

public record CropRequestDTO(
    BigDecimal basePrice,
    ProductStatus productStatus,
    Integer plotId,
    String name,
    BigDecimal expectedYield,
    CultureType cultureType,
    String specificCulture,
    CropStatus status,
    LocalDate plantDate,
    LocalDate harvestDate
) {}