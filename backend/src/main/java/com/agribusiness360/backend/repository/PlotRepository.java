package com.agribusiness360.backend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.agribusiness360.backend.model.Plot;
import com.agribusiness360.backend.model.SoilType;

@Repository
public interface PlotRepository extends JpaRepository<Plot, Integer> {

    /**
     * Search plots by rural property ID
     */
    List<Plot> findByRuralPropertyId(Integer propertyId);

    /**
     * Search plots by name ignoring case
     */
    List<Plot> findByNameContainingIgnoreCase(String name);

    /**
     * Search plot by unique identifier code
     */
    Optional<Plot> findByCode(String code);

    /**
     * Search plots by soil type
     */
    List<Plot> findBySoilType(SoilType soilType);
}
