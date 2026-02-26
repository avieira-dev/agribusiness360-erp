package com.agribusiness360.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.agribusiness360.backend.model.PaymentMethod;

public record SaleRequestDTO(
    Integer propertyId,
    LocalDateTime saleDate,
    BigDecimal totalValue,
    String buyerName,
    PaymentMethod paymentMethod,
    String notes
) {}