package com.agribusiness360.backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.agribusiness360.backend.model.RuralProperty;

@Repository
public interface RuralPropertyRepository extends JpaRepository<RuralProperty, Integer> {

    /**
     * Find a property by its exact name.
     * Useful for a duplicity verification or
     * textual search.
     */
    Optional<RuralProperty> findByName(String name);

    /**
     * Search for a the property through its unique
     * identifier code
     */
    Optional<RuralProperty> findByCode(String code);
}
