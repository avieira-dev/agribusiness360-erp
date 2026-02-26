package com.agribusiness360.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RuralPropertyResponseDTO(
    Integer id,
    String name,
    String code,
    BigDecimal area,
    String location,
    String description,
    LocalDateTime createdAt
) {}