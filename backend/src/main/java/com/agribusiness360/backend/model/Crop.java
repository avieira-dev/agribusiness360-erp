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
@Table(name = "crops")
@PrimaryKeyJoinColumn(name = "product_id")
public class Crop extends Product {

    @ManyToOne
    @JoinColumn(name = "plot_id", nullable = false)
    private Plot plot;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "expected_yield", precision = 12, scale = 2)
    private BigDecimal expectedYield = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "culture_type", nullable = false)
    private CultureType cultureType = CultureType.OUTROS;

    @Column(name = "specific_culture", nullable = false, length = 50)
    private String specificCulture;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CropStatus status = CropStatus.PLANTIO;

    @Column(name = "plant_date")
    private LocalDate plantDate;

    @Column(name = "harvest_date")
    private LocalDate harvestDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
