package com.agribusiness360.backend.service;

import java.util.List;
import java.util.stream.Collectors;
import com.agribusiness360.backend.dto.LivestockRequestDTO;
import com.agribusiness360.backend.dto.LivestockResponseDTO;
import com.agribusiness360.backend.exception.BusinessException;
import com.agribusiness360.backend.exception.ResourceNotFoundException;
import com.agribusiness360.backend.model.AnimalSex;
import com.agribusiness360.backend.model.AnimalType;
import com.agribusiness360.backend.model.HealthStatus;
import com.agribusiness360.backend.model.Livestock;
import com.agribusiness360.backend.model.RuralProperty;
import com.agribusiness360.backend.repository.LivestockRepository;
import com.agribusiness360.backend.repository.RuralPropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LivestockService {

    @Autowired
    private LivestockRepository livestockRepository;

    @Autowired
    private RuralPropertyRepository ruralPropertyRepository;

    /**
     *  Convert entity to DTO
     */
    private LivestockResponseDTO toResponse(Livestock livestock) {
        return new LivestockResponseDTO(
            livestock.getId(),
            livestock.getRuralProperty().getId(),
            livestock.getRuralProperty().getName(),
            livestock.getRuralProperty().getCode(),
            livestock.getBasePrice(),
            livestock.getProductStatus(),
            livestock.getAnimalType(),
            livestock.getSex(),
            livestock.getBirthDate(),
            livestock.getWeight(),
            livestock.getCode(),
            livestock.getTraceability(),
            livestock.getHealthStatus(),
            livestock.getCreatedAt()
        );
    }

    /** 
     *  Updates entity data based on DTO (JPA Inheritance)
     */
    private void updateEntityFromDto(Livestock animal, LivestockRequestDTO dto, RuralProperty property) {
        animal.setBasePrice(dto.basePrice());
        animal.setProductStatus(dto.productStatus());
        animal.setRuralProperty(property);
        animal.setAnimalType(dto.animalType());
        animal.setSex(dto.sex());
        animal.setBirthDate(dto.birthDate());
        animal.setWeight(dto.weight());
        animal.setCode(dto.code());
        animal.setTraceability(dto.traceability());
        animal.setHealthStatus(dto.healthStatus());
    }

    /**
     *  Retrieves all animals
     */
    @Transactional(readOnly = true)
    public List<LivestockResponseDTO> getAllAnimals() {
        return livestockRepository.findAllWithProperty().stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     *  Retrieves all animals from a specific property
     */
    @Transactional(readOnly = true)
    public List<LivestockResponseDTO> getAllAnimalsByProperty(Integer id) {
        return livestockRepository.findByRuralPropertyId(id).stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     *  Retrieves all animals from a specific type
     */
    @Transactional(readOnly = true)
    public List<LivestockResponseDTO> getAllAnimalsSpecificType(AnimalType animalType) {
        return livestockRepository.findByAnimalType(animalType).stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     *  Retrieves all animals of a specific type from a specific property
     */
    @Transactional(readOnly = true)
    public List<LivestockResponseDTO> getAllAnimalsByPropertyIdAndAnimalType(Integer id, AnimalType animalType) {
        return livestockRepository.findByRuralPropertyIdAndAnimalType(id, animalType).stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     *  Retrieves all animals of a specific sex
     */
    @Transactional(readOnly = true)
    public List<LivestockResponseDTO> getAnimalsBySpecificSex(AnimalSex animalSex) {
        return livestockRepository.findBySex(animalSex).stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     *  Retrieves all animals of a specific sex from a specific property
     */
    @Transactional(readOnly = true)
    public List<LivestockResponseDTO> getAllAnimalsByPropertyIdAndAnimalSex(Integer id, AnimalSex animalSex) {
        return livestockRepository.findByRuralPropertyIdAndSex(id, animalSex).stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     *  Retrieves all animals by health status
     */
    @Transactional(readOnly = true)
    public List<LivestockResponseDTO> getAllAnimalsByHealthStatus(HealthStatus healthStatus) {
        return livestockRepository.findByHealthStatus(healthStatus).stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     *  Retrieves all animals from a specific property by health status
     */
    @Transactional(readOnly = true)
    public List<LivestockResponseDTO> getAllAnimalsByPropertyIdAndHealthStatus(Integer id, HealthStatus healthStatus) {
        return  livestockRepository.findByRuralPropertyIdAndHealthStatus(id, healthStatus).stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     *  Retrieves the animal by its unique code
     */
    @Transactional(readOnly = true)
    public LivestockResponseDTO getAnimalByUniqueCode(String code) {
        Livestock animal = livestockRepository.findByCode(code)
            .orElseThrow(()-> new ResourceNotFoundException("No animal found with this code."));

        return toResponse(animal);
    }

    /**
     *  Retrieves the animal by its traceability code
     */
    @Transactional(readOnly = true)
    public LivestockResponseDTO getAnimalByTraceability(String traceability) {
        Livestock animal = livestockRepository.findByTraceability(traceability)
            .orElseThrow(()-> new ResourceNotFoundException("No animal found with this traceability."));

        return toResponse(animal);
    }

    /**
     *  Saves a new animal
     */
    @Transactional
    public LivestockResponseDTO saveAnimal(LivestockRequestDTO dto) {
        RuralProperty property = ruralPropertyRepository.findById(dto.propertyId())
            .orElseThrow(() -> new ResourceNotFoundException("Rural property not found."));

        if (livestockRepository.existsByCodeAndRuralPropertyId(dto.code(), dto.propertyId())) {
            throw new BusinessException("An animal with this code already exists in this property.");
        }

        Livestock animal = new Livestock();
        updateEntityFromDto(animal, dto, property);

        return toResponse(livestockRepository.save(animal));
    }

    /**
     *  Update the registered animal
     */
    @Transactional
    public LivestockResponseDTO updateAnimal(Integer id, LivestockRequestDTO dto) {
        Livestock animal = livestockRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Animal not found with ID: " + id));

        RuralProperty property = ruralPropertyRepository.findById(dto.propertyId())
            .orElseThrow(() -> new ResourceNotFoundException("Property not found."));

        if (!animal.getCode().equals(dto.code()) && 
            livestockRepository.existsByCodeAndRuralPropertyId(dto.code(), dto.propertyId())) {
            throw new BusinessException("The new code is already in use in this property.");
        }

        updateEntityFromDto(animal, dto, property);

        return toResponse(livestockRepository.save(animal));
    }

    /**
     *  Delete animal
     */
    @Transactional
    public void deleteAnimal(Integer id) {
        if (!livestockRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete: Animal record not found.");
        }

        livestockRepository.deleteById(id);
    }
}
