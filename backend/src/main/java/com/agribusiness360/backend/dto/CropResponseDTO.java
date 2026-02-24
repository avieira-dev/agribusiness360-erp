package com.agribusiness360.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.agribusiness360.backend.model.CropStatus;
import com.agribusiness360.backend.model.CultureType;

public record CropResponseDTO(
    Integer id,
    Integer plotId,
    String plotName,
    String name,
    BigDecimal expectedYield,
    CultureType cultureType,
    String specificCulture,
    CropStatus status,
    LocalDate plantDate,
    LocalDate harvestDate,
    LocalDateTime createdAt
) {}