# Inventory Management System (IMS)

Week 1 deliverable for the Junior Java Developer Internship: **Project Planning and Architecture Design** for a simple Inventory Management System for a small retail business.

## 📄 Week 1 Deliverables (docs/)

| File | Description |
|------|-------------|
| `docs/Week1_Inventory_Management_System_Design_Document.docx` | Full design document (~2,000 words): requirements, module descriptions, 10 design patterns with rationale, database design, API endpoints, code scaffolding, testing strategy, and CI/CD plan |
| `docs/architecture_diagram.png` | High-level layered architecture diagram (Controller → Service → Repository → MySQL, with Security & cross-cutting concerns) |
| `docs/er_diagram.png` | Entity Relationship diagram for the normalized MySQL schema (products, categories, suppliers, stock_movements, orders, order_items, users, roles) |

## 🛠️ Planned Technology Stack
- **Backend:** Java 17, Spring Boot (REST APIs)
- **Security:** Spring Security + JWT + Role-Based Access Control (ADMIN / STAFF)
- **Data:** Spring Data JPA / Hibernate, MySQL (normalized, indexed)
- **Testing:** JUnit 5, Mockito, Spring Boot Test
- **DevOps:** Docker, GitHub Actions CI/CD

## 📋 Design Patterns Applied
Layered Architecture (MVC), Repository, Dependency Injection, Builder, Singleton, Factory, Strategy, Observer, DTO, and @ControllerAdvice.

## 🗓️ Internship Timeline
- **Week 1:** Project Planning & Architecture Design ✅ *(this repo)*
- **Week 2:** Core module implementation (Product & Stock management)
- **Week 3:** Order processing & security (JWT/RBAC)
- **Week 4:** Reporting module & testing
- **Week 5:** Deployment, documentation & final submission
