package com.agribusiness360.backend.dto;

import com.agribusiness360.backend.model.PermissionLevel;

public record AccessDTO(
    Integer userId,
    Integer propertyId,
    PermissionLevel permissionLevel
) {}