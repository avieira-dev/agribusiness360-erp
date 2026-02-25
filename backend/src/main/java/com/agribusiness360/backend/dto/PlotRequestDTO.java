package com.agribusiness360.backend.dto;

import java.math.BigDecimal;
import com.agribusiness360.backend.model.SoilType;

public record PlotRequestDTO(
    Integer propertyId,
    String name,
    String code,
    BigDecimal area,
    SoilType soilType
) {}