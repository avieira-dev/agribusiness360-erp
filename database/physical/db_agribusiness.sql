CREATE DATABASE IF NOT EXISTS db_agribusiness;
USE db_agribusiness;

# Users table
CREATE TABLE IF NOT EXISTS users (
        id INT PRIMARY KEY AUTO_INCREMENT,
        name VARCHAR(100) NOT NULL,
        username VARCHAR(50) UNIQUE NOT NULL,
        email VARCHAR(100) UNIQUE NOT NULL,
        password_hash VARCHAR(255) NOT NULL,
        code VARCHAR(10) NOT NULL UNIQUE,
        role ENUM('PROPRIETARIO', 'GERENTE', 'LAVRADOR', 'AGRONOMO', 'ZOOTECNISTA', 'VAQUEIRO', 'ORDENHADOR', 'CONTADOR', 'ADMINISTRADOR', 'TEMPORARIO')
        NOT NULL DEFAULT 'TEMPORARIO',
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

# Rural properties table
CREATE TABLE IF NOT EXISTS rural_properties (
        id INT PRIMARY KEY AUTO_INCREMENT,
        name VARCHAR(100) NOT NULL UNIQUE,
        code VARCHAR(9) NOT NULL UNIQUE,
        area DECIMAL(12,2) NOT NULL,
        location VARCHAR(255) NOT NULL,
        description VARCHAR(255),
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

# Access association table
CREATE TABLE IF NOT EXISTS access (
        user_id INT NOT NULL,
        property_id INT NOT NULL,
        permission_level ENUM('ADMINISTRADOR', 'SUPERVISOR', 'FUNCIONAL', 'LEITOR') NOT NULL DEFAULT 'LEITOR',
        PRIMARY KEY ( user_id, property_id),
        CONSTRAINT fk_access_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
        CONSTRAINT fk_access_property_id FOREIGN KEY (property_id) REFERENCES rural_properties(id) ON DELETE CASCADE
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

# Inventory items table
CREATE TABLE IF NOT EXISTS inventory_items (
        id INT PRIMARY KEY AUTO_INCREMENT,
        property_id INT NOT NULL,
        name VARCHAR(100) NOT NULL,
        category ENUM('SEMENTES', 'FERTILIZANTES', 'DEFENSIVOS', 'RACOES', 'EQUIPAMENTOS', 'PECAS', 'PRODUTOS QUIMICOS', 'EMBALAGENS',
        'COMBUSTIVEIS', 'OUTROS') NOT NULL DEFAULT 'OUTROS',
        unit ENUM('QUILOGRAMA', 'GRAMA', 'LITRO', 'MILILITRO', 'UNIDADE', 'CAIXA', 'SACO', 'TONELADA', 'METRO CUBICO', 'OUTRO') NOT NULL
        DEFAULT 'OUTRO',
        quantity DECIMAL(12,2) NOT NULL DEFAULT 0,
        unit_cost DECIMAL(12,2) NOT NULL DEFAULT 0.00,
        total_cost DECIMAL(14,2) NOT NULL DEFAULT 0.00,
        status ENUM('DISPONIVEL', 'EM FALTA', 'RESERVADO', 'BLOQUEADO', 'OBSOLETO') NOT NULL DEFAULT 'DISPONIVEL',
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT fk_inventory_property_id FOREIGN KEY (property_id) REFERENCES rural_properties(id) ON DELETE CASCADE
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

# Plot table
CREATE TABLE IF NOT EXISTS plot (
        id INT PRIMARY KEY AUTO_INCREMENT,
        property_id INT NOT NULL,
        name VARCHAR(50) NOT NULL,
        code VARCHAR(6) NOT NULL UNIQUE,
        area DECIMAL(10,2) NOT NULL,
        soil_type ENUM('ARGILOSO', 'ARENOSO', 'SILTOSO', 'FRANCO', 'LATERITICO', 'TURFA', 'PEDREGOSO', 'OUTRO') NOT NULL DEFAULT 'OUTRO',
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT fk_plot_property_id FOREIGN KEY (property_id) REFERENCES rural_properties(id) ON DELETE CASCADE
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

# Product table
CREATE TABLE IF NOT EXISTS product (
        id INT PRIMARY KEY AUTO_INCREMENT,
        status ENUM('DISPONIVEL', 'INDISPONIVEL') NOT NULL DEFAULT 'DISPONIVEL',
        base_price DECIMAL(12,2)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

# Sale table
CREATE TABLE IF NOT EXISTS sale (
        id INT PRIMARY KEY AUTO_INCREMENT,
        property_id INT NOT NULL,
        sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        total_value DECIMAL(14,2) NOT NULL DEFAULT 0.00,
        buyer_name VARCHAR(100) NOT NULL,
        payment_method ENUM('PIX', 'BOLETO', 'TRANSFERENCIA', 'CARTAO DE CREDITO', 'DINHEIRO', 'TROCA') NOT NULL DEFAULT 'DINHEIRO',
        notes TEXT,
        CONSTRAINT sale_property_id FOREIGN KEY (property_id) REFERENCES rural_properties(id) ON DELETE CASCADE
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

# Crops table
CREATE TABLE IF NOT EXISTS crops (
        product_id INT PRIMARY KEY,
        plot_id INT NOT NULL,
        name VARCHAR(50) NOT NULL,
        expected_yield  DECIMAL(12,2) DEFAULT 0.00,
        culture_type ENUM('FRUTAS', 'GRAOS', 'HORTALICAS', 'OLEAGINOSAS', 'FIBRAS', 'PASTAGENS', 'FLORESTAL', 'TUBERCULOS', 'LEGUMINOSAS', 'OUTROS'
        ) NOT NULL DEFAULT 'OUTROS',
        specific_culture VARCHAR(50) NOT NULL,
        status ENUM('PLANTIO', 'DESENVOLVIMENTO', 'PONTO DE COLHEITA', 'COLHIDO', 'PERDIDO') NOT NULL DEFAULT 'PLANTIO',
        plant_date DATE,
        harvest_date DATE,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT fk_crops_product_id FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE,
        CONSTRAINT fk_crops_plot_id FOREIGN KEY (plot_id) REFERENCES plot(id) ON DELETE CASCADE
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

# Livestock table
CREATE TABLE IF NOT EXISTS livestock (
        product_id INT PRIMARY KEY,
        property_id INT NOT NULL,
        animal_type ENUM('BOVINO', 'OVINO', 'CAPRINO', 'SUINO', 'EQUINO', 'AVE', 'OUTRO') NOT NULL DEFAULT 'OUTRO',
        sex ENUM('MACHO', 'FEMEA', 'INDEFINIDO') NOT NULL DEFAULT 'INDEFINIDO',
        birth_date DATE,
        weight DECIMAL(10,2)  DEFAULT 0.00,
        code VARCHAR(10) NOT NULL UNIQUE,
        traceability VARCHAR(50) UNIQUE,
        health_status ENUM('SAUDAVEL', 'DOENTE', 'TRATAMENTO', 'VACINACAO PENDENTE', 'ABATIDO', 'OBITO') NOT NULL DEFAULT 'SAUDAVEL',
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT fk_livestock_product_id FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE,
        CONSTRAINT fk_livestock_property_id FOREIGN KEY (property_id) REFERENCES rural_properties(id) ON DELETE CASCADE
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

# Trades association table
CREATE TABLE IF NOT EXISTS traded (
        sale_id INT NOT NULL,
        product_id INT NOT NULL,
        final_price DECIMAL(14,2) NOT NULL DEFAULT 0.00,
        PRIMARY KEY(sale_id, product_id),
        CONSTRAINT fk_traded_sale_id FOREIGN KEY (sale_id) REFERENCES sale(id) ON DELETE CASCADE,
        CONSTRAINT fk_traded_product_id FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;