package com.agribusiness360.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.agribusiness360.backend.model.SoilType;

public record PlotResponseDTO(
    Integer id,
    Integer propertyId,
    String propertyName,
    String propertyCode,
    String name,
    String code,
    BigDecimal area,
    SoilType soilType,
    LocalDateTime createdAt
) {}