package com.agribusiness360.backend.dto;

import java.math.BigDecimal;

public record RuralPropertyRequestDTO(
    String name,
    String code,
    BigDecimal area,
    String location,
    String description
) {}
