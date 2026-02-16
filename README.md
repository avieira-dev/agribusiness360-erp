# Agribusiness360 ERP

## Project Status
**In development** (Project currently in its early stages, focusing on architecture definition and core implementation).

## Table of Contents
- [Overview](#overview)
- [Project Object](#project-objective)
- [Initial Scope (MVP)](#initial-scope-mvp)
- [Technologies Used](#technologies-used)
- [Project Structure](#project-structure)
- [Backend Configuration](#backend-configuration)
- [Developer](#developer)
- [Observations](#observations)
- [License](#license)

## Overview
`Agribusiness360 ERP` is an agribusiness-oriented enterprise resource planning system developed as a `demonstrative and study project`, focusing on domain modeling, fullstack architecture, and software engineering best practices applied to the agricultural context.

The system is designed to be `modular and extensible`, allowing gradual expansion of features as the project evolves and new concepts are explored.

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

Additional features may be added in the future, such as inventory control, suppliers, analytical reports, and external integrations.

## Technologies Used
**Backend**
- Java
- Spring Boot
- JPA and Hibernate
- MySQL

**Frontend**
- Vue.js
- Vue Router
- Axios
- Bootstrap

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
└── README.md # Main documentation
```

## Backend Configuration
To run the backend application, you must manually create the application.properties file with your local database credentials.

### File Location
Create the file at the following path:
```text
backend/src/main/resources/application.properties
```

### Required configuration
To run the backend, you need to configure the database connection.

1. Create a file named `application.properties` in `backend/src/main/resources/`.
2. Add the following content and replace `USER` and `PASSWORD` with your local MySQL credentials:

```properties
spring.application.name=backend
spring.datasource.url=jdbc:mysql://localhost:3306/db_agribusiness?serverTimezone=UTC&useSSL=false

# Replace USER and PASSWORD with your local credentials
spring.datasource.username=USER
spring.datasource.password=PASSWORD

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

### Important Notes
- This file must not be committed to the repository.
- Make sure application.properties is listed in .gitignore.
- The database db_agribusiness must exist before running the application.
- MySQL must be running locally.
- On Linux/macOS, you may need to grant execute permission to the Maven Wrapper:
```bash
chmod +x mvnw
```

## Developer
**avieira-dev**

## Observations
- This project is under development.
- Designed as a learning tool and a foundation for future expansion.

## License
[MIT License](LICENSE)