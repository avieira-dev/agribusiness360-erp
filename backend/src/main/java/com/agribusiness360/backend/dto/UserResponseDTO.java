package com.agribusiness360.backend.dto;

import java.time.LocalDateTime;
import com.agribusiness360.backend.model.UserRole;

public record UserResponseDTO(
    Integer id,
    String name,
    String username,
    String email,
    String code,
    UserRole userRole,
    LocalDateTime createdAt
) {}
