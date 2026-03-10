package com.agribusiness360.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.agribusiness360.backend.model.AnimalSex;
import com.agribusiness360.backend.model.AnimalType;
import com.agribusiness360.backend.model.HealthStatus;
import com.agribusiness360.backend.model.ProductStatus;

public record LivestockRequestDTO(
    Integer propertyId,
    Integer productId,
    ProductStatus productStatus,
    BigDecimal basePrice,
    AnimalType animalType,
    AnimalSex sex,
    LocalDate birthDate,
    BigDecimal weight,
    String code,
    String traceability,
    HealthStatus healthStatus
) {}