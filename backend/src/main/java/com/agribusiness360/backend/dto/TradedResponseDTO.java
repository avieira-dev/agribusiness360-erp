package com.agribusiness360.backend.dto;

import java.math.BigDecimal;

public record TradedResponseDTO(
    Integer saleId,
    Integer productId,
    String productName,
    BigDecimal finalPrice
) {}