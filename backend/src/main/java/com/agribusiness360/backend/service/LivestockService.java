package com.agribusiness360.backend.service;

import java.math.BigDecimal;
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
import com.agribusiness360.backend.model.Product;
import com.agribusiness360.backend.model.RuralProperty;
import com.agribusiness360.backend.repository.LivestockRepository;
import com.agribusiness360.backend.repository.ProductRepository;
import com.agribusiness360.backend.repository.RuralPropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LivestockService {

    @Autowired
    private LivestockRepository livestockRepository;

    @Autowired
    private  ProductRepository productRepository;

    @Autowired
    private RuralPropertyRepository ruralProperty;

    @Autowired
    private ProductService productService;

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
     *  Convert DTO to entity
     */
    private Livestock toEntity(LivestockRequestDTO dto, RuralProperty property, Product product) {
        Livestock livestock = new Livestock();

        livestock.setRuralProperty(property);
        livestock.setProductStatus(product.getProductStatus());
        livestock.setBasePrice(product.getBasePrice());
        livestock.setAnimalType(dto.animalType());
        livestock.setSex(dto.sex());
        livestock.setBirthDate(dto.birthDate());
        livestock.setWeight(dto.weight());
        livestock.setCode(dto.code());
        livestock.setTraceability(dto.traceability());
        livestock.setHealthStatus(dto.healthStatus());

        return livestock;
    }

    /** 
     *  Performs business validations on animal data
     */
    private void validateAnimalData(LivestockRequestDTO dto) {
        if(dto.productStatus() == null) {
            throw new BusinessException("Product status must not be null.");
        }

        if(dto.basePrice() == null) {
            throw new BusinessException("Base price must not be null.");
        }

        if(dto.basePrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Base price must not be negative.");
        }

        if(dto.animalType() == null) {
            throw new BusinessException("Animal type must not be null.");
        }

        if(dto.sex() == null) {
            throw new BusinessException("Animal sex must not be null.");
        }

        if(dto.weight() == null) {
            throw new BusinessException("Weight must not be null.");
        }

        if(dto.weight().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Weight must not be negative.");
        }

        if(dto.healthStatus() == null) {
            throw new BusinessException("Health status must not be null.");
        }
    }

    /**
     *  Retrieves all animals
     */
    @Transactional(readOnly = true)
    public List<LivestockResponseDTO> getAllAnimals() {
        return livestockRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
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
            .orElseThrow(()-> new RuntimeException("No animal found with this code."));

        return toResponse(animal);
    }

    /**
     *  Retrieves the animal by its traceability code
     */
    @Transactional(readOnly = true)
    public LivestockResponseDTO getAnimalByTraceability(String traceability) {
        Livestock animal = livestockRepository.findByCode(traceability)
            .orElseThrow(()-> new RuntimeException("No animal found with this traceability."));

        return toResponse(animal);
    }

    /**
     *  Saves a new animal
     */
    @Transactional
    public LivestockResponseDTO saveAnimal(LivestockRequestDTO dto) {
        validateAnimalData(dto);

        RuralProperty property = ruralProperty.findById(dto.propertyId())
            .orElseThrow(() -> new ResourceNotFoundException("No rural property found with this id."));

        livestockRepository.findByCode(dto.code()).ifPresent(existingCode -> {
            if(existingCode.getRuralProperty().getId().equals(property.getId())) {
                throw new BusinessException("An animal with the given code already exists in this property.");
            }
        });

        livestockRepository.findByTraceability(dto.traceability()).ifPresent(existingTraceability -> {
            if(existingTraceability.getRuralProperty().getId().equals(property.getId())) {
                throw new BusinessException("An animal with the given traceability already exists in this property.");
            }
        });

        Product product = new Product();

        Livestock newAnimal = toEntity(dto, property, product);

        return toResponse(livestockRepository.save(newAnimal));
    }

    /**
     *  Update the registered animal
     */
    @Transactional
    public LivestockResponseDTO updateAnimal(Integer id, LivestockRequestDTO dto) {
        Livestock animal = livestockRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No animal was found with the provided id"));

        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No product was found with the provided id."));

        validateAnimalData(dto);

        if(!animal.getCode().equals(dto.code())) {
            if(livestockRepository.findByCode(dto.code()).isPresent()) {
                throw new BusinessException("An animal with the given code already exists in this property.");
            }
        }

        if(!animal.getTraceability().equals(dto.traceability())) {
            if(livestockRepository.findByTraceability(dto.traceability()).isPresent()) {
                throw new BusinessException("An animal with the given traceability already exists in this property.");
            }
        }

        if(!animal.getRuralProperty().getId().equals(dto.propertyId())) {
            if(livestockRepository.existsByCodeAndRuralPropertyId(dto.code(), dto.propertyId())) {
                throw new BusinessException("An animal with the given code already exists in the property to which the animal will be transferred.");
            }
        }

        RuralProperty property = ruralProperty.findById(dto.propertyId())
            .orElseThrow(() -> new ResourceNotFoundException("No rural property found with this id."));

        if(!animal.getTraceability().equals(dto.traceability())) {
            if(livestockRepository.existsByTraceabilityAndRuralPropertyId(dto.traceability(), property.getId())) {
                throw new BusinessException("An animal with the given traceability already exists in this property.");
            }
        }

        Livestock updatedAnimal = toEntity(dto, property, product);

        updatedAnimal.setId(id);

        return toResponse(livestockRepository.save(updatedAnimal));
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
