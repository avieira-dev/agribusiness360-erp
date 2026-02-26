package com.agribusiness360.backend.dto;

import java.math.BigDecimal;

public record TradedRequestDTO(
    Integer saleId,
    Integer productId,
    BigDecimal finalPrice
) {}
