package com.agribusiness360.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.agribusiness360.backend.model.AnimalSex;
import com.agribusiness360.backend.model.AnimalType;
import com.agribusiness360.backend.model.HealthStatus;

public record LivestockRequestDTO(
    Integer propertyId,
    AnimalType animalType,
    AnimalSex sex,
    LocalDate birthDate,
    BigDecimal weight,
    String code,
    String traceability,
    HealthStatus healthStatus
) {}