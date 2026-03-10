package com.agribusiness360.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.agribusiness360.backend.model.AnimalSex;
import com.agribusiness360.backend.model.AnimalType;
import com.agribusiness360.backend.model.HealthStatus;
import com.agribusiness360.backend.model.ProductStatus;

public record LivestockResponseDTO(
    Integer id,
    Integer propertyId,
    String propertyName,
    String propertyCode,
    BigDecimal basePrice,
    ProductStatus productStatus,
    AnimalType animalType,
    AnimalSex sex,
    LocalDate birthDate,
    BigDecimal weight,
    String code,
    String traceability,
    HealthStatus healthStatus,
    LocalDateTime createdAt
) {}