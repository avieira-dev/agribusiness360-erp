package com.agribusiness360.backend.dto;

import com.agribusiness360.backend.model.PermissionLevel;

public record AccessRequestDTO(
    Integer userId,
    Integer propertyId,
    PermissionLevel permissionLevel
) {}