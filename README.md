<div align="center">

<h1>Agribusiness360</h1>

<p>A modular ERP system for agribusiness management, focused on scalability and clean architecture.</p>

<p>
  <img src="https://img.shields.io/badge/status-in%20development-f39c12?style=flat-square"/>
  <img src="https://img.shields.io/badge/backend-Spring%20Boot-6DB33F?style=flat-square&logo=springboot&logoColor=white"/>
  <img src="https://img.shields.io/badge/frontend-Vue.js-42b883?style=flat-square&logo=vuedotjs&logoColor=white"/>
  <img src="https://img.shields.io/badge/license-MIT-6e7781?style=flat-square"/>
</p>

</div>

---

## Table of Contents

- [Overview](#overview)
- [MVP Scope](#mvp-scope)
- [Technical Stack](#technical-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Developer](#developer)
- [License](#license)

---

## Overview

**Agribusiness360 ERP** is a portfolio-driven enterprise resource planning system oriented to the agricultural domain. It focuses on domain modeling, fullstack architecture, and software engineering best practices applied to an agribusiness context.

The system is designed to be modular and extensible, allowing gradual expansion of features as the project evolves. It also serves as a learning environment for concepts of software architecture, persistence, and code organization.

> [!NOTE]  
> Designed as a learning tool and a foundation for future expansion.

---

## MVP Scope

| Module | Description |
|---|---|
| Rural Properties | Registration and management of rural properties |
| Productive Areas | Field and productive area management |
| Inputs | Supply and input inventory control |
| Crop Seasons | Crop cycle tracking and control |
| Operational Costs | Cost tracking and basic analysis |

Additional features planned for future iterations include supplier management, analytical reports, and external integrations.

---

## Technical Stack

### Backend

| Technology | Role |
|---|---|
| Java 17 | Primary language |
| Spring Boot | Backend framework |
| JPA & Hibernate | ORM layer |
| MySQL | Database |

### Frontend

| Technology | Role |
|---|---|
| Vue.js 3 (Composition API) | UI framework |
| TypeScript | Language |
| Vite | Build tool |
| Pinia | State management |
| Vue Router | Client-side navigation |
| Axios | HTTP client |
| Tailwind CSS | Styling |

---

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

---

## Getting Started

### Prerequisites

| Dependency | Version |
|---|---|
| Java | 17 or higher |
| Node.js & npm | v18+ |
| MySQL Server | Latest stable |

---

### Backend Setup

**1. Configure the database**

Create `application.properties` in `backend/src/main/resources/`:

> [!IMPORTANT]  
> Do not commit this file. Ensure `application.properties` is listed in `.gitignore`.
> The database `db_agribusiness` must exist before running the application, and MySQL must be running locally.

```properties
spring.application.name=backend
spring.datasource.url=jdbc:mysql://localhost:3306/db_agribusiness?serverTimezone=UTC&useSSL=false

# Replace with your local credentials
spring.datasource.username=YOUR_USER
spring.datasource.password=YOUR_PASSWORD

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

**2. Run the application**

```bash
cd backend

# Linux / macOS
chmod +x mvnw
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

---

### Frontend Setup

**1. Install dependencies**

```bash
cd frontend
npm install
```

If setting up a new environment manually:

```bash
# Core dependencies
npm install axios vue-router pinia

# Styling
npm install tailwindcss @tailwindcss/vite
```

**2. Start the development server**

```bash
npm run dev
```

The application will be available at `http://localhost:5173`.

> [!TIP]  
> Run `npm audit` periodically to check for known vulnerabilities in your packages. Use `npm audit fix` to resolve issues automatically.

---

## Developer

**Alexandre Vieira**
GitHub: [@avieira-dev](https://github.com/avieira-dev)

---

## License

Distributed under the [MIT License](LICENSE). See `LICENSE` for details.