package com.agribusiness360.backend.dto;

import java.math.BigDecimal;
import com.agribusiness360.backend.model.ProductStatus;

public record ProductResponseDTO(
    Integer id,
    ProductStatus productStatus,
    BigDecimal basePrice
) {}