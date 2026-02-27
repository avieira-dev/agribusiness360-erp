package com.agribusiness360.backend.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.agribusiness360.backend.dto.RuralPropertyRequestDTO;
import com.agribusiness360.backend.dto.RuralPropertyResponseDTO;
import com.agribusiness360.backend.exception.BusinessException;
import com.agribusiness360.backend.exception.ResourceNotFoundException;
import com.agribusiness360.backend.model.RuralProperty;
import com.agribusiness360.backend.repository.RuralPropertyRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RuralPropertyService {

    @Autowired
    private RuralPropertyRepository ruralPropertyRepository;

    /**
     *  Convert entity to ResponseDTO
     */
    private RuralPropertyResponseDTO toResponse(RuralProperty ruralProperty) {
        return new RuralPropertyResponseDTO(
            ruralProperty.getId(),
            ruralProperty.getName(),
            ruralProperty.getCode(),
            ruralProperty.getArea(),
            ruralProperty.getLocation(),
            ruralProperty.getDescription(),
            ruralProperty.getCreatedAt()
        );
    }

    /** 
     *  Convert DTO to entity
     */
    private RuralProperty toEntity(RuralPropertyRequestDTO dto) {
        RuralProperty property = new RuralProperty();

        property.setName(dto.name());
        property.setCode(dto.code());
        property.setArea(dto.area());
        property.setDescription(dto.description());
        property.setLocation(dto.location());

        return property;
    }

    /**
     *  Retrieves all registered rural properties
     */
    @Transactional(readOnly = true)
    public List<RuralPropertyResponseDTO> getAllRuralProperties() {
        return ruralPropertyRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     *  Retrieves the rural property associated with the given ID
     */
    @Transactional(readOnly = true)
    public RuralPropertyResponseDTO getRuralPropertyId(Integer id) {
        RuralProperty property = ruralPropertyRepository.findById(id)
            .orElseThrow(()-> new ResourceNotFoundException("No rural property found with this id."));

        return toResponse(property);
    }

    /**
     *  Retrieves the rural property identified by the given name
     */
    @Transactional(readOnly = true)
    public RuralPropertyResponseDTO getByName(String name) {
        RuralProperty property = ruralPropertyRepository.findByName(name)
            .orElseThrow(()-> new ResourceNotFoundException("No rural property found with this name."));

        return toResponse(property);
    }

    /**
     *  Retrieves the rural property identified by the given
     *  unique verification code
     */
    @Transactional(readOnly = true)
    public RuralPropertyResponseDTO getByCode(String code) {
        RuralProperty property = ruralPropertyRepository.findByCode(code)
            .orElseThrow(()-> new ResourceNotFoundException("No rural property found with this code."));

        return toResponse(property);
    }

    /**
     *  Saves the given rural property
     */
    @Transactional
    public RuralPropertyResponseDTO saveRuralProperty(RuralPropertyRequestDTO dto) {
        if(ruralPropertyRepository.findByName(dto.name()).isPresent()) {
            throw new BusinessException("A rural property with this name is already registered.");
        }

        if(ruralPropertyRepository.findByCode(dto.code()).isPresent()) {
            throw new BusinessException("A rural property with this code is already registered.");
        }

        if (dto.area() == null) {
            throw new BusinessException("The area field is mandatory.");
        }

        if(dto.area().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("The area cannot be negative.");
        }

        RuralProperty property = toEntity(dto);

        return toResponse(ruralPropertyRepository.save(property));
    }

    /** 
     *  Updates an existing rural property
     */
    @Transactional
    public RuralPropertyResponseDTO updateRuralProperty(Integer id, RuralPropertyRequestDTO dto) {
        RuralProperty ruralProperty = ruralPropertyRepository.findById(id)
            .orElseThrow(()-> new ResourceNotFoundException("No rural property found with this id."));

        ruralPropertyRepository.findByName(dto.name()).ifPresent(propertyExisting -> {
            if(!propertyExisting.getId().equals(id)) {
                throw new BusinessException("A rural property with this name is already registered.");
            }
        });

        ruralPropertyRepository.findByCode(dto.code()).ifPresent(propertyExisting -> {
            if(!propertyExisting.getId().equals(id)) {
                throw new BusinessException("A rural property with this code is already registered.");
            }
        });

        if (dto.area() == null) {
            throw new BusinessException("The area field is mandatory.");
        }

        if(dto.area().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("The area cannot be negative.");
        }

        ruralProperty.setName(dto.name());
        ruralProperty.setCode(dto.code());
        ruralProperty.setArea(dto.area());
        ruralProperty.setLocation(dto.location());
        ruralProperty.setDescription(dto.description());

        return toResponse(ruralPropertyRepository.save(ruralProperty));
    }

    /**
     *  Deletes the rural property associated with the given ID
     */
    @Transactional
    public void deleteRuralProperty(Integer id) {
        RuralProperty property = ruralPropertyRepository.findById(id)
            .orElseThrow(()-> new ResourceNotFoundException("No rural property found with this id."));

        ruralPropertyRepository.delete(property);
    }
}