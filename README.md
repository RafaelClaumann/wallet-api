# Wallet API

A REST API built with Spring Boot that simulates the management of clients' financial wallets, including tracking of the expenses associated with each wallet.

## 📋 About the project

The system models a simple personal-finance domain: each **client** owns a **wallet**, and each wallet accumulates a list of **expenses**, categorized by type (house, car, water, other) and by state (open or closed).

The API implements business rules such as:
- CPF (Brazilian tax ID) validation and rejection of duplicate CPFs;
- Blocking client deletion while their wallet still has open expenses;
- Standardized error responses for both validation errors and business-rule violations, with messages in Portuguese.

## 🚀 Tech stack

- **Java 11**
- **Spring Boot 2.3.8** (Web, Data JPA, Validation, DevTools)
- **PostgreSQL** — relational database (default environment)
- **H2 Database** — in-memory database (local/test environment)
- **Lombok** — boilerplate reduction
- **Bean Validation** (Hibernate Validator, including a CPF validator)
- **JUnit + Spring Boot Test** — automated testing
- **JaCoCo** — test coverage
- **Docker Compose** — database and Adminer orchestration
- **Maven** — dependency management and build

## 🏗️ Architecture

The project follows a standard layered structure for Spring Boot applications:

```
src/main/java/br/com/application/wallet
├── controllers/     # REST endpoints (ClientController, WalletController)
├── services/        # Business logic (ClientService, WalletService)
├── repositories/     # Spring Data JPA interfaces
├── models/           # JPA entities (ClientEntity, WalletEntity, ExpenseEntity)
│   ├── dto/           # Response DTOs and input forms (form/)
│   ├── enums/          # ExpenseState, ExpenseType
│   └── api/             # Response wrapper (Data<T>) and error messages
└── handler/          # @ControllerAdvice + custom exceptions
```

### Data model

```
Client (1) ───── (1) Wallet (1) ───── (N) Expense
```

| Entity | Main fields |
|---|---|
| **Client** | id, name, cpf (unique), telephoneNumber, wallet |
| **Wallet** | id, description, balance, expenses[] |
| **Expense** | id, description, value, expenseState (OPEN/CLOSED), expenseType (HOUSE/CAR/WATER/OTHER) |

The schema (`schema.sql`) is applied automatically when the PostgreSQL container starts via Docker Compose, including the lookup tables `EXPENSE_TYPE` and `EXPENSE_STATE`, pre-populated with seed data.

## 📡 API Endpoints

### Clients — `/wallet/v1/clients`

| Method | Route | Description |
|---|---|---|
| `GET` | `/wallet/v1/clients` | Lists all clients |
| `GET` | `/wallet/v1/clients/{id_client}` | Fetches a client by id |
| `GET` | `/wallet/v1/clients/{id_client}/wallets/{id_wallet}` | Fetches a client along with their wallet and expenses |
| `POST` | `/wallet/v1/clients` | Registers a new client |
| `DELETE` | `/wallet/v1/clients/{id_client}` | Removes a client (blocked if there are open expenses) |

**Example — create client**

```http
POST /wallet/v1/clients
Content-Type: application/json

{
  "client_name": "Rafael Claumann",
  "client_cpf": "123.456.789-09",
  "client_telephone": "48999999999"
}
```

### Wallets — `/wallet/v1/wallets`

| Method | Route | Description |
|---|---|---|
| `GET` | `/wallet/v1/wallets/{id_wallet}` | Fetches a wallet by id |
| `POST` | `/wallet/v1/wallets/{id_client}` | Creates a wallet and links it to a client |

**Example — create wallet**

```http
POST /wallet/v1/wallets/1
Content-Type: application/json

{
  "wallet_description": "Main wallet",
  "wallet_balance": 1500.00
}
```

### Error handling

Business-rule errors return a standardized body:

```json
{
  "exception_name": "br.com.application.wallet.handler.exceptions.ClientNotFoundException",
  "http_status_code": "NOT_FOUND",
  "exception_message": "Cliente não encontrado, id: 99",
  "local_date_time": "2026-08-25T10:00:00"
}
```

Field validation errors (`@Valid`) return the list of invalid fields with their respective messages.

| Scenario | HTTP Status |
|---|---|
| Client/wallet not found | `404 Not Found` |
| Duplicate CPF | `400 Bad Request` |
| Deletion attempted with open expenses | `400 Bad Request` |
| Invalid/empty request body | `422 Unprocessable Entity` |
| Field validation error | `400 Bad Request` |

## ⚙️ How to run

### Prerequisites

- Java 11+
- Maven (or use the included wrapper `./mvnw`)
- Docker and Docker Compose (to run with PostgreSQL)

### Option 1 — Local with in-memory database (H2)

Doesn't require Docker. Uses `application-local.properties`, with the H2 console available at `/h2-console`.

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

The application starts at `http://localhost:8080`.

### Option 2 — With PostgreSQL via Docker Compose

1. Copy the database environment file:
   ```bash
   cp src/main/resources/database.env.example src/main/resources/database.env
   ```
2. Start the database and Adminer:
   ```bash
   docker-compose -f src/main/resources/docker-compose.yml up -d
   ```
3. Export the credentials used by the application (matching what's set in `database.env`):
   ```bash
   export DATABASE_USER=postgres
   export DATABASE_PASSWORD=password
   ```
4. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

Adminer is available at `http://localhost:9090` for inspecting the database.

## ✅ Tests

The project includes unit and integration tests for entities, services, and controllers, using an in-memory H2 database and the `test` profile.

```bash
./mvnw test
```

The coverage report is generated by JaCoCo during the build (`prepare-package`), excluding DTO, enum, handler, and API-wrapper packages.

## 🔭 Possible improvements

- Expose update and delete endpoints for wallets (`WalletService` already contains the logic, but it isn't mapped in `WalletController`)
- Add CRUD endpoints for expenses (currently they're only queryable through a wallet)
- Add authentication/authorization
- Add interactive API documentation via OpenAPI/Swagger

## 👤 Author

**Rafael Claumann**
