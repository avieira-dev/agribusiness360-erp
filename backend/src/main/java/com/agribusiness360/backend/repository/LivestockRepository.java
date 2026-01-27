package com.agribusiness360.backend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.agribusiness360.backend.model.AnimalSex;
import com.agribusiness360.backend.model.AnimalType;
import com.agribusiness360.backend.model.HealthStatus;
import com.agribusiness360.backend.model.Livestock;

@Repository
public interface LivestockRepository extends JpaRepository<Livestock, Integer> {

    /**
     *  List all animals from a specific rural property
     */
    List<Livestock> findByRuralPropertyId(Integer propertyId);

    /**
     *  List all animals of a specific type
     */
    List<Livestock> findByAnimalType(AnimalType animalType);

    /**
     *  List all animals of specific type belonging to a specific
     *  rural property
     */
    List<Livestock> findByRuralPropertyIdAndAnimalType(Integer propertyId, AnimalType animalType);

    /**
     *  List all animals of the same sex
     */
    List<Livestock> findBySex(AnimalSex animalSex);

    /**
     *  List all animals of the same sex belonging to specific
     *  rural property
     */
    List<Livestock> findByRuralPropertyIdAndSex(Integer propertyId, AnimalSex animalSex);

    /**
     *  List all animals with the same health status
     */
    List<Livestock> findByHealthStatus(HealthStatus healthStatus);

    /**
     *  List all animals with the same health status belonging to
     *  a specific rural property
     */
    List<Livestock> findByRuralPropertyIdAndHealthStatus(Integer propertyId, HealthStatus healthStatus);

    /**
     *  Search for an animal using the unique code
     */
    Optional<Livestock> findByCode(String code);

    /**
     *  Search for an animal using the traceability code
     */
    Optional<Livestock> findByTraceability(String traceability);
}
