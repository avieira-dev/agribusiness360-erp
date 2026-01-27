package com.agribusiness360.backend.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.agribusiness360.backend.model.PaymentMethod;
import com.agribusiness360.backend.model.Sale;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Integer> {

    /**
     * List all sales for a specific property 
     */
    List<Sale> findByRuralPropertyId(Integer propertyId);

    /**
     * Search all sales during a specific period
     */
    List<Sale> findBySaleDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Search all sales by buyer name
     */
    List<Sale> findByBuyerNameContainingIgnoreCase(String buyerName);

    /**
     * Search all sales using specific payment method
     */
    List<Sale> findByPaymentMethod(PaymentMethod paymentMethod);
}
