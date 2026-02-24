package com.agribusiness360.backend.dto;

import com.agribusiness360.backend.model.UserRole;

public record UserRequestDTO(
    String name,
    String username,
    String email,
    String password,
    String code,
    UserRole userRole
) {}
