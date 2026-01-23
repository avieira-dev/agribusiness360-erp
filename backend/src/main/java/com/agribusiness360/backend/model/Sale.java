package com.agribusiness360.backend.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sale")
public class Sale implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private RuralProperty ruralProperty;

    @Column(name = "sale_date", updatable = false, nullable = false)
    private LocalDateTime saleDate = LocalDateTime.now();

    @Column(name = "total_value", nullable = false, precision = 14, scale = 2)
    private BigDecimal totalValue = BigDecimal.ZERO;

    @Column(name = "buyer_name", nullable = false, length = 100)
    private String buyerName;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod = PaymentMethod.DINHEIRO;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}
