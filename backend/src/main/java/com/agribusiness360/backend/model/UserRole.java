package com.agribusiness360.backend.model;

public enum UserRole {
    PROPRIETARIO,
    GERENTE,
    LAVRADOR,
    AGRONOMO,
    ZOOTECNISTA,
    VAQUEIRO,
    ORDENHADOR,
    CONTADOR,
    ADMINISTRADOR,
    TEMPORARIO;

    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
