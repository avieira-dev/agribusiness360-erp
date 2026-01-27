package com.agribusiness360.backend.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.agribusiness360.backend.model.Crop;
import com.agribusiness360.backend.model.CropStatus;
import com.agribusiness360.backend.model.CultureType;

@Repository
public interface CropRepository extends JpaRepository<Crop, Integer> {

    /**
     * Search for crops of specific plot
     */
    List<Crop> findByPlotId(Integer plotId);

    /**
     * Search for a crop by name
     */
    List<Crop> findByName(String name);

    /**
     * List crops by specific culture type
     */
    List<Crop> findByCultureType(CultureType cultureType);

    /**
     * Search crops of a specific culture
     */
    List<Crop> findBySpecificCultureContainingIgnoreCase(String specificCulture);

    /**
     * Search crops by status
     */
    List<Crop> findByStatus(CropStatus cropStatus);

    /**
     * Search all crops during a specific planting period
     */
    List<Crop> findByPlantDateBetween(LocalDate startDate, LocalDate endDate);
}
