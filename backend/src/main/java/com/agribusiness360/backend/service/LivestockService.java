package com.agribusiness360.backend.service;

import java.math.BigDecimal;
import java.util.List;
import com.agribusiness360.backend.model.AnimalSex;
import com.agribusiness360.backend.model.AnimalType;
import com.agribusiness360.backend.model.HealthStatus;
import com.agribusiness360.backend.model.Livestock;
import com.agribusiness360.backend.repository.LivestockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LivestockService {

    @Autowired
    private LivestockRepository livestockRepository;

    @Autowired
    private  ProductService productService;

    /**
     *  Retrieves all animals
     */
    public List<Livestock> getAllAnimals() {
        return livestockRepository.findAll();
    }

    /**
     *  Retrieves all animals from a specific property
     */
    public List<Livestock> getAllAnimalsByProperty(Integer id) {
        return livestockRepository.findByRuralPropertyId(id);
    }

    /**
     *  Retrieves all animals from a specific type
     */
    public List<Livestock> getAllAnimalsSpecificType(AnimalType animalType) {
        return livestockRepository.findByAnimalType(animalType);
    }

    /**
     *  Retrieves all animals of a specific type from a specific property
     */
    public List<Livestock> getAllAnimalsByPropertyIdAndAnimalType(Integer id, AnimalType animalType) {
        return livestockRepository.findByRuralPropertyIdAndAnimalType(id, animalType);
    }

    /**
     *  Retrieves all animals of a specific sex
     */
    public List<Livestock> getAnimalsBySpecificSex(AnimalSex animalSex) {
        return livestockRepository.findBySex(animalSex);
    }

    /**
     *  Retrieves all animals of a specific sex from a specific property
     */
    public List<Livestock> getAllAnimalsByPropertyIdAndAnimalSex(Integer id, AnimalSex animalSex) {
        return livestockRepository.findByRuralPropertyIdAndSex(id, animalSex);
    }

    /**
     *  Retrieves all animals by health status
     */
    public List<Livestock> getAllAnimalsByHealthStatus(HealthStatus healthStatus) {
        return livestockRepository.findByHealthStatus(healthStatus);
    }

    /**
     *  Retrieves all animals from a specific property by health status
     */
    public List<Livestock> getAllAnimalsByPropertyIdAndHealthStatus(Integer id, HealthStatus healthStatus) {
        return  livestockRepository.findByRuralPropertyIdAndHealthStatus(id, healthStatus);
    }

    /**
     *  Retrieves the animal by its unique code
     */
    public Livestock getAnimalByUniqueCode(String code) {
        return livestockRepository.findByCode(code)
                .orElseThrow(()-> new RuntimeException("No animal found with this code."));
    }

    /**
     *  Retrieves the animal by its traceability code
     */
    public Livestock getAnimalByTraceability(String traceability) {
        return livestockRepository.findByTraceability(traceability)
                .orElseThrow(()-> new RuntimeException("No animal found with this traceability."));
    }

    /**
     *  Saves a new animal
     */
    @Transactional
    public Livestock saveAnimal(Livestock livestock) {
        if(livestockRepository.findByCode(livestock.getCode()).isPresent()) {
            throw new RuntimeException("This code is already assigned to an animal on this property.");
        }

        if(livestockRepository.findByTraceability(livestock.getTraceability()).isPresent()) {
            throw new RuntimeException("This traceability is already assigned to an animal on this property.");
        }

        if(livestock.getWeight() != null && livestock.getWeight().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Weight cannot be negative.");
        }


        return livestockRepository.save(livestock);
    }

    /**
     *  Update the registered animal
     */
    @Transactional
    public Livestock updateAnimal(Integer id, Livestock details) {
        Livestock existingAnimal = livestockRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("No animal found for the given ID."));

        if(!existingAnimal.getCode().equals(details.getCode())) {
            throw new RuntimeException("Animal identification code cannot be modified.");
        }

        if(!existingAnimal.getTraceability().equals(details.getTraceability())) {
            if(livestockRepository.findByTraceability(details.getTraceability()).isPresent()) {
                throw new RuntimeException("This traceability code is already assigned to another animal.");
            }
        }

        existingAnimal.setAnimalType(details.getAnimalType());
        existingAnimal.setBirthDate(details.getBirthDate());
        existingAnimal.setSex(details.getSex());
        existingAnimal.setWeight(details.getWeight());
        existingAnimal.setHealthStatus(details.getHealthStatus());
        existingAnimal.setTraceability(details.getTraceability());
        existingAnimal.setBasePrice(details.getBasePrice());
        existingAnimal.setProductStatus(details.getProductStatus());

        return livestockRepository.save(existingAnimal);
    }

    /**
     *  Delete animal
     */
    @Transactional
    public void deleteAnimal(Integer id) {
        if(!livestockRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete: Animal record not found.");
        }

        productService.deleteProduct(id);
    }
}
