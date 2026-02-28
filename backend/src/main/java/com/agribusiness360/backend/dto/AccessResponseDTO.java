package com.agribusiness360.backend.dto;

import com.agribusiness360.backend.model.PermissionLevel;

public record AccessResponseDTO(
    Integer userId,
    String userName,
    Integer propertyId,
    String propertyName,
    PermissionLevel permissionLevel
) {}