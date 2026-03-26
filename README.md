# Agribusiness360 ERP

> A modular ERP system for agribusiness management, focused on scalability and clean architecture.

![status](https://img.shields.io/badge/status-in%20development-orange)
![backend](https://img.shields.io/badge/backend-Spring%20Boot-green)
![frontend](https://img.shields.io/badge/frontend-Vue.js-42b883)
![license](https://img.shields.io/badge/license-MIT-blue)

> [!NOTE]  
> Designed as a learning tool and a foundation for future expansion

## Table of Contents
- [Overview](#overview)
- [Project Objective](#project-objective)
- [Initial Scope (MVP)](#initial-scope-mvp)
- [Technologies Used](#technologies-used)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Developer](#developer)
- [License](#license)

## Overview
**Agribusiness360 ERP** is an agribusiness-oriented enterprise resource planning system developed as a **portfolio-driven project**, focusing on domain modeling, fullstack architecture, and software engineering best practices applied to the agricultural context.

The system is designed to be **modular and extensible**, allowing gradual expansion of features as the project evolves and new concepts are explored.

## Project Objective
This project aims to:
- Serve as a portfolio and learning project
- Simulate a real-world agricultural ERP on a reduced scale
- Apply concepts of software architecture, persistence, and code organization
- Continuously evolve with new modules and improvements

## Initial Scope (MVP)
The initial MVP includes:
- Rural property management
- Productive areas (fields) management
- Input (supplies) management
- Crop season control
- Operational cost tracking and analysis

> [!NOTE]  
> Additional features may be added in the future, such as suppliers,  
> analytical reports, and external integrations.

## Technologies Used
### Backend
- Java
- Spring Boot (Backend Framework)
- JPA and Hibernate (ORM)
- MySQL (DBMS)

### Frontend
- Vue.js 3 (Composition API)
- TypeScript
- Vite (Build Tool)
- Pinia (State Management)
- Vue Router (Navigation)
- Axios (HTTP Client)
- Tailwind CSS (Styling)

## Project Structure
```text
agribusiness360-erp/
├── backend/ 
├── frontend/
├── database/
│   ├── conceptual/
│   ├── logical/
│   └── physical/
├── .gitignore 
├── LICENSE 
└── README.md
```

## Getting Started

### Prerequisites
- Java 17 or higher
- Node.js (v18+) and npm
- MySQL Server

### Backend Setup (Spring Boot)

1. **Database Configuration**

Create a file named `application.properties` in `backend/src/main/resources/`.

> [!IMPORTANT]
> - This file must not be committed to the repository
> - Make sure application.properties is listed in .gitignore
> - The database db_agribusiness must exist before running the application
> - MySQL must be running locally

```properties
spring.application.name=backend
spring.datasource.url=jdbc:mysql://localhost:3306/db_agribusiness?serverTimezone=UTC&useSSL=false

# Replace USER and PASSWORD with your local credentials
spring.datasource.username=YOUR_USER
spring.datasource.password=YOUR_PASSWORD

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

2. **Run the Application**

```bash
cd backend
# Linux/macOS
./mvnw spring-boot:run
# Windows
mvnw.cmd spring-boot:run
```

**On Linux/macOS, you may need to grant execute permission to the Maven Wrapper:**
```bash
chmod +x mvnw
```

### Frontend Setup

1. **Install Dependencies**

Navigate to the frontend folder and install all necessary modules (including Axios, Router, Pinia, and Tailwind):

```bash
cd frontend
npm install
```

2. **Manual Installation**

If you are setting up a new environment and need to ensure all modules are present:

```bash
# Core dependencies
npm install axios vue-router pinia

# Styling (Tailwind CSS)
npm install tailwindcss @tailwindcss/vite
```

3. **Run for Development**

```bash
npm run dev
```

> [!NOTE]  
> The application will be available at http://localhost:5173.

> [!TIP]    
> **Keep your dependencies secure:**   
> It is a best practice to periodically run `npm audit` in the `frontend` folder to check for known 
> vulnerabilities in your packages.   
> If issues are found, you can often fix them automatically by running `npm audit fix`.

## Developer
**Alexandre Vieira**  
GitHub: [@avieira-dev](https://github.com/avieira-dev)

## License
Distributed under the license [MIT License](LICENSE). See the **LICENSE** file for more details.