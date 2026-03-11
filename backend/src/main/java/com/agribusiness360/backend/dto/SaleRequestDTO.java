package com.agribusiness360.backend.dto;

import java.util.List;
import com.agribusiness360.backend.model.PaymentMethod;

public record SaleRequestDTO(
    Integer propertyId,
    String buyerName,
    PaymentMethod paymentMethod,
    String notes,
    List<ItemRequestDTO> items
) {}