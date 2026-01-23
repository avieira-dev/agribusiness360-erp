package com.agribusiness360.backend.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
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
@Table(name = "livestock")
@PrimaryKeyJoinColumn(name = "product_id")
public class Livestock extends Product {

    @ManyToOne
    @JoinColumn(name = "property_id")
    private RuralProperty ruralProperty;

    @Enumerated(EnumType.STRING)
    @Column(name = "animal_type", nullable = false)
    private AnimalType animalType = AnimalType.OUTRO;

    @Enumerated(EnumType.STRING)
    @Column(name = "sex", nullable = false)
    private AnimalSex sex = AnimalSex.INDEFINIDO;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "weight", nullable = false, precision = 10, scale = 2)
    private BigDecimal weight = BigDecimal.ZERO;

    @Column(name = "code", nullable = false, unique = true, length = 10)
    private String code;

    @Column(name = "traceability", unique = true, length = 50)
    private String traceability;

    @Enumerated(EnumType.STRING)
    @Column(name = "health_status", nullable = false)
    private HealthStatus healthStatus = HealthStatus.SAUDAVEL;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
