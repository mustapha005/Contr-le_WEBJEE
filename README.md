# Backend Spring Boot - Vehicle Rental Exam JEE

Backend REST securise par JWT pour gerer les agences, les vehicules et l'historique des locations.

## Stack

- Java cible: 17 (`maven.compiler.release=17`) compatible avec un JDK 24 installe localement.
- Spring Boot 3.5.x
- Spring Data JPA, Hibernate, JDBC
- Spring Security + JWT
- H2 Database en memoire
- Swagger/OpenAPI via springdoc

## Lancement

```bash
cd backend
mvn clean spring-boot:run
```

Backend: <http://localhost:8085>
Swagger UI: <http://localhost:8085/swagger-ui.html>
H2 console: <http://localhost:8085/h2-console>

Connexion H2:

- JDBC URL: `jdbc:h2:mem:vehicle_rental_db`
- User: `sa`
- Password: vide

## Comptes de demonstration

| Role | Username | Password |
| --- | --- | --- |
| ROLE_ADMIN | admin | admin123 |
| ROLE_EMPLOYE | employee | employee123 |
| ROLE_CLIENT | client | client123 |

## Endpoints principaux

- `POST /api/auth/login`
- `POST /api/auth/register`
- `GET/POST/PUT/DELETE /api/agencies`
- `GET/POST/PUT/DELETE /api/vehicles`
- `GET/POST/PATCH /api/rentals`
- `GET /api/dashboard/stats`
