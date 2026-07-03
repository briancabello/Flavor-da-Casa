# Flavor da Casa

A multi-module restaurant reservation and operations platform built with Spring Boot. Guests request a table without an account, staff review and approve requests from a filtered queue, and a token-secured REST API exposes the same operations for downstream integrations.

Built as an NBCC coursework project (Winter 2026) using Agile/Jira sprint cycles.

## Overview

Flavor da Casa handles the full lifecycle of a restaurant reservation: a guest submits a request, the system holds it in `PENDING` status and emails a confirmation, staff review the request behind authentication and assign a table, and the guest is emailed the outcome (`APPROVED` or `DENIED`). The same domain is also exposed through a separate, stateless REST API secured with JWT bearer tokens, documented with Swagger/OpenAPI, so a future client (mobile app, partner system) can consume it without touching the staff web app.

The codebase is organized as a multi-module Maven project following Clean Architecture: each bounded module (`auth`, `restaurant`, `common`, `email`) separates domain, application, infrastructure, and web/persistence concerns, with dependencies flowing inward toward the domain layer.

## Tech Stack

| Layer | Tech |
| --- | --- |
| Language | Java 21 |
| Framework | Spring Boot 4.0.3 |
| Security | Spring Security (form login), OAuth2 Resource Server with RSA key-pair JWT validation |
| Web UI | Thymeleaf, Bootstrap 5 |
| Persistence | Spring Data JPA, MySQL |
| API Docs | Swagger / OpenAPI (SpringDoc) |
| Build | Maven (multi-module) |
| Email | Jakarta Mail (SMTP) |

## Module Structure

```
Restaurant-Project (root pom)
  auth/                       Staff authentication and token issuance
    auth-domain
    auth-persistence
    auth-application-api / -client / -service / -token-service / -token-signer / -token-validator
    auth-rest-api              Token issuance/validation endpoints
    auth-web
  restaurant/                 Core dining domain
    domain
    application
    persistence
    rest-api
    web
  common/                     Shared cross-cutting libraries
    common-domain
    common-application
    common-infrastructure
    common-web
  email/                      Notification service
    email-domain
    email-service-api
    email-service
  app-web/                    Runnable: staff Thymeleaf UI
  app-restaurant-rest-api/    Runnable: restaurant domain REST API
  app-auth-rest-api/          Runnable: auth REST API
```

## Runnable Applications

| App | Port | Purpose |
| --- | --- | --- |
| `app-web` | 8080 | Thymeleaf staff UI: reservation queue, table assignment, event/menu management |
| `app-restaurant-rest-api` | 8081 | Stateless JWT-protected REST API over the restaurant domain, documented with Swagger |
| `app-auth-rest-api` | 8082 | Issues and validates RSA-signed JWTs for staff and API clients |

## Key Features

- Reservation approval workflow: guest request creates a UUID-tracked `PENDING` reservation, staff approve (with table assignment) or deny, each transition triggers an email.
- Spring Security form login for staff, with public access preserved for guest reservation requests and menu browsing.
- Separate JWT-protected REST API (OAuth2 resource server, RSA key-pair validation) for third-party/API clients, independent of the web app.
- Seating and dining table management with capacity tracking and overlapping-seating conflict detection.
- Menu and menu item CRUD, linked to events and displayed on the guest reservation form.
- Reservation dashboard filterable by event and status.
- Auto-generated Swagger/OpenAPI documentation with a bearer-token auth scheme.
- `InitializeDataServiceImpl` seeds default tables, events, seatings, menus, and menu items on first startup for immediate demo readiness.
- Custom validation framework (`Result`/`ValidatedResult` wrappers) per entity, binding field-level errors to `BindingResult`.

## Getting Started

Prerequisites: JDK 21, Maven, a running MySQL instance.

```bash
# Build all modules
mvn clean install

# Run each application from its module directory
cd app-auth-rest-api && mvn spring-boot:run       # port 8082
cd app-restaurant-rest-api && mvn spring-boot:run # port 8081
cd app-web && mvn spring-boot:run                 # port 8080
```

Start `app-auth-rest-api` first if you need token issuance for the protected REST API; `app-web` and `app-restaurant-rest-api` do not depend on it being up to boot, but staff login and bearer-token calls need it running.

## API Documentation

Swagger UI is available on each REST API once running (`app-restaurant-rest-api`, `app-auth-rest-api`), configured with a bearer-token auth scheme for protected endpoints. Manual request collections for both APIs are available under `/Bruno` (Bruno API client format).

## Contributors

See [CONTRIBUTORS.md](./CONTRIBUTORS.md).

## Related

This project is featured as a case study on [Brian Cabello's portfolio](https://github.com/briancabello/Flavor-da-Casa).
