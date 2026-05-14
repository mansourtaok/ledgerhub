# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```bash
# Build (skip tests)
./gradlew build -x test

# Run locally
./gradlew bootRun

# Run tests
./gradlew test

# Run a single test class
./gradlew test --tests "com.ledgerhub.SomeTestClass"

# Docker
docker build -t ledgerhub .
docker run -p 8080:8080 ledgerhub
```

Requires PostgreSQL running on `localhost:5432`, database `postgres`. Configure credentials via environment variables `SPRING_DATASOURCE_PASSWORD` and `JWT_SECRET` (do not rely on the hardcoded values in `application.properties`).

Swagger UI is available at `http://localhost:8080/swagger-ui.html` once running.

## Architecture

**Layered Spring Boot REST API** — no frontend. Java 21, Spring Boot 3.3.5, PostgreSQL, Flyway.

```
controller → service (interface + impl) → repository (JpaRepository) → PostgreSQL
                                ↕
                           model/db (entities)  model/dto (request/response)
```

### Package layout

| Package | Purpose |
|---------|---------|
| `controller` | REST endpoints — one controller per domain |
| `service/<domain>/` | Interface + `impl/` subpackage per domain |
| `service/impl/` | Company, User services (flat, not domain-subpackaged) |
| `model/db/` | JPA entities; `db/expenses/` and `db/items/` for sub-domains |
| `model/dto/` | DTOs grouped by domain subfolder |
| `repository` | All `JpaRepository` interfaces in one flat package |
| `config/` | `SecurityConfig`, `GlobalExceptionHandler`, `OpenApiConfig` |
| `config/jwt/` | `JwtTokenUtil` |
| `security/` | `TokenAuthenticationFilter` |
| `utils/` | `StaffSpecification`, `ProfileSpecification` — JPA Criteria filters for paginated list endpoints |
| `specification/expense/`, `specification/item/` | `ExpenseSpecification`, `ItemSpecification` — additional Criteria filters |

### Auth flow

1. `POST /auth/login` → `AuthService` validates credentials → returns JWT (HS512, 5-hour expiry).
2. All other endpoints require `Authorization: Bearer <token>`.
3. `TokenAuthenticationFilter` (runs before `UsernamePasswordAuthenticationFilter`) extracts and validates the token via `JwtTokenUtil`, then sets the `SecurityContext`.
4. The JWT payload includes `sub` (email) and a custom `user_id` claim. **Roles are not currently embedded in the token** — `GrantedAuthority` list is always empty.

### Database migrations

Flyway migrations live in `src/main/resources/db/migration/`. `ddl-auto=none` — all schema changes must go through a new `V{n}__description.sql` file. Never modify existing migration files.

| Migration | Content |
|-----------|---------|
| V1 | Full schema init: `system_lookup`, `countries`, `users`, `companies`, `banks`, `staff`, `profiles`, `items`, `expenses`, etc. |
| V2 | Seed data: currencies, roles, system lookups |
| V3 | `users_roles` join table |
| V4 | Payment-related lookup seed data |
| V5 | `entities` base table (polymorphic document attachment) |
| V6 | Recreates `documents` table (replaces V1 version) |
| V7 | Expense type/status lookups |
| V8 | Warehouses table |

### Multi-tenancy pattern

Data is scoped per company. Most list endpoints are `GET /api/{domain}/{companyId}` and most create endpoints are `POST /api/{domain}` with `companyId` inside the request body. The `companyId` is **not** derived from the JWT — it is trusted from the client.

### Excel import/export

`ItemService`, `StaffExcelService`, and `ProfileExcelService` use Apache POI. Import endpoints accept `multipart/form-data`. The upload directory is currently hardcoded in `ItemService` and must be externalized via `app.upload-dir` in `application.properties`.

## Known Issues (active bugs — fix before shipping)

- `WarehouseService.delete` instantiates `new Warehouse()` instead of fetching the existing one — inserts a corrupt row.
- `ProfileService.mapToDTO` will NPE on profiles where `categoryId`, `typeId`, `currencyId`, or `paymentTerm` is null (DB allows null; entity mapping does not guard it).
- `ExpenseController.getAll` accepts `companyId` as a path variable but never passes it to the service — all companies' expenses are returned to everyone.
- `profiles.country_id` FK references `companies(id)` instead of `countries(id)` — must be corrected in a new migration.
- `CATEGORY_ID` in `V4__system_data_for_pymt.sql` does not match the `"CATEGORY"` string queried in `ProfileExcelService` — profile Excel import will silently find no category.
- `spring.main.allow-circular-references=true` is set in `application.properties` — a circular bean dependency exists in the service layer that must be resolved before this flag can be removed.
- `ProfilePersonsController` is mapped to `/api/profiles/{companyId}/banks` — the path says "banks" but the resource is persons; the path should be corrected to `/api/profiles/{companyId}/persons`.
