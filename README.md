# Spring Boot Example

A Spring Boot 4 example application with Vaadin UI, JPA, and H2 database.

## Tech Stack

- **Spring Boot 4.0.6** with Java 21
- **Vaadin 25.1.4** – Server-side UI
- **Spring Data JPA** + **Lombok**
- **H2** (in-memory, development) / **PostgreSQL** (production)
- **Gradle** (Kotlin DSL)

## Domain Model

- `Employee` – Vorname, Nachname, Telefon, Eintrittsdatum, Filiale
- `Branch` – Filialname, Adresse, Manager (Employee)

## Running

```bash
./gradlew bootRun
```

App runs at http://localhost:8080

| Route | View |
|-------|------|
| `/` | MainView |
| `/employees` | EmployeeView |
| `/branches` | BranchView |
| `/h2-console` | H2 Console (dev only) |
