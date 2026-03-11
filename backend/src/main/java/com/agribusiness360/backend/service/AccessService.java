package com.agribusiness360.backend.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.agribusiness360.backend.dto.AccessRequestDTO;
import com.agribusiness360.backend.dto.AccessResponseDTO;
import com.agribusiness360.backend.exception.BusinessException;
import com.agribusiness360.backend.exception.ResourceNotFoundException;
import com.agribusiness360.backend.model.Access;
import com.agribusiness360.backend.model.AccessId;
import com.agribusiness360.backend.model.RuralProperty;
import com.agribusiness360.backend.model.User;
import com.agribusiness360.backend.repository.AccessRepository;
import com.agribusiness360.backend.repository.RuralPropertyRepository;
import com.agribusiness360.backend.repository.UserRepository;

@Service
public class AccessService {

    @Autowired
    private AccessRepository accessRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RuralPropertyRepository ruralPropertyRepository;

    /**
     *  Convert entity to DTO
     */
    private AccessResponseDTO toResponse(Access access) {
        return new AccessResponseDTO(
            access.getUser().getId(),
            access.getUser().getName(),
            access.getRuralProperty().getId(),
            access.getRuralProperty().getName(),
            access.getPermissionLevel()
        );
    }

    /**
     *  Convert DTO to entity
     */
    private Access toEntity(AccessRequestDTO dto, User user, RuralProperty property, AccessId accessId) {
        Access access = new Access();

        access.setId(accessId);
        access.setUser(user);
        access.setRuralProperty(property);
        access.setPermissionLevel(dto.permissionLevel());

        return access;
    }

    /**
     *  Retrieves all access permissions in the system
     */
    @Transactional(readOnly = true)
    public List<AccessResponseDTO> getAllAccesses() {
        return accessRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     *  Retrieves all accesses for a specific user
     */
    @Transactional(readOnly = true)
    public List<AccessResponseDTO> getAccessesByUser(Integer id) {
        return accessRepository.findByIdUserId(id).stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     *  Retrieves all accesses for a specific rural property
     */
    @Transactional(readOnly = true)
    public List<AccessResponseDTO> getAccessesByRuralProperty(Integer id) {
        return accessRepository.findByIdPropertyId(id).stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     *  Grants a new access permission
     */
    @Transactional
    public AccessResponseDTO grantAccess(AccessRequestDTO dto) {
        User user = userRepository.findById(dto.userId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        RuralProperty property = ruralPropertyRepository.findById(dto.propertyId())
            .orElseThrow(() -> new ResourceNotFoundException("Property not found."));

        AccessId accessId = new AccessId(user.getId(), property.getId());

        if(accessRepository.existsById(accessId)) {
            throw new BusinessException("User already has access to this property.");
        }

        Access newAccess = toEntity(dto, user, property, accessId);

        return toResponse(accessRepository.save(newAccess));
    }

    /**
     *  Updates the permission level of an existing access
     */
    @Transactional
    public AccessResponseDTO updatePermission(AccessRequestDTO dto) {
        User user = userRepository.findById(dto.userId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found."));
        
        RuralProperty property = ruralPropertyRepository.findById(dto.propertyId())
            .orElseThrow(() -> new ResourceNotFoundException("Property not found."));

        AccessId accessId = new AccessId(user.getId(), property.getId());

        if(!accessRepository.existsById(accessId)) {
            throw new ResourceNotFoundException("This access permission does not exist.");
        }

        Access updateAccess = toEntity(dto, user, property, accessId);

        updateAccess.setId(accessId);

        return toResponse(accessRepository.save(updateAccess));
    }

    /**
     *  Revokes access (deletes the association)
     */
    @Transactional
    public void revokeAccess(AccessRequestDTO dto) {
        AccessId accessId = new AccessId(dto.userId(), dto.propertyId());

        if(!accessRepository.existsById(accessId)) {
            throw new ResourceNotFoundException("This access permission does not exist.");
        }

        accessRepository.deleteById(accessId);
    }
}
