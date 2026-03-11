package com.agribusiness360.backend.dto;

import java.math.BigDecimal;

public record ItemRequestDTO(
    Integer productId,
    BigDecimal finalPrice
) {}