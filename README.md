# Spring Boot learning monorepo

Multi-module Maven project that follows a typical backend layout: **`common`** (DTOs and shared contracts), **`domain`** (JPA + Flyway migrations), **`api`** (HTTP, JWT security, OpenAPI, messaging producer, mail, observability hooks), and **`worker`** (RabbitMQ consumer with DB-backed deduplication and mail side-effects).

More detailed setup, URLs, and workflow notes: [docs/DEVELOPMENT.md](docs/DEVELOPMENT.md).

## Prerequisites

- Java **17+**
- **Docker** (for `docker compose` and optional Testcontainers-powered tests)
- **Maven 3.9+**

## Quick start

1. Start infrastructure:

   ```bash
   docker compose up -d
   ```

2. Build and install modules (needed so `api` / `worker` resolve `common` and `domain`):

   ```bash
   mvn install -DskipTests
   ```

3. Run the API (profile **`dev`** seeds an `admin` user — password `adminadmin` — for local use only):

   ```bash
   mvn -pl api spring-boot:run
   ```

4. Run the worker (consumes `task.created` and sends notification mail to [Mailpit](https://github.com/axllent/mailpit)):

   ```bash
   mvn -pl worker spring-boot:run
   ```

5. Open **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  
   **Mailpit UI** (captured mail): [http://localhost:8025](http://localhost:8025)  
   **RabbitMQ UI**: [http://localhost:15672](http://localhost:15672) (guest / guest)

6. Static **jQuery** demo page (calls the public i18n endpoint): [http://localhost:8080/index.html](http://localhost:8080/index.html)

## See it working

- **Kahan summation (via API):** open [http://localhost:8080/api/v1/demo/kahan](http://localhost:8080/api/v1/demo/kahan) or run `curl -s http://localhost:8080/api/v1/demo/kahan | jq`. You should see `naiveMatchesExpected: false` and `kahanMatchesExpected: true` for the built-in ill-conditioned series. The same logic is covered by `KahanSummationTest` in the `common` module.
- **Mailpit:** with the API running, register a user (`POST /api/v1/auth/register`) or create a task (after login); welcome mail and worker notifications go to SMTP `localhost:1025` and appear in the [Mailpit UI](http://localhost:8025).
- **RabbitMQ:** creating a task publishes to exchange `task.events` with routing key `task.created`. In the [management UI](http://localhost:15672) (guest / guest), open **Queues** and inspect `task.created`; with the **worker** running, messages are consumed and the queue should stay near zero under light load.

## Configuration notes

- **JWT**: stateless bearer tokens; configure `app.jwt.secret` (≥ 32-byte secret for HS256) and treat the sample YAML as **dev-only**.
- **CORS**: permissive for `localhost` in `SecurityConfig` — tighten `allowedOrigins` for production.
- **CSRF**: disabled for this stateless JWT API. Server-rendered cookie sessions would require CSRF protection; this setup is **not** that model.
- **Postgres**: UTF-8 text (`TEXT` / `VARCHAR`) for emoji and multi-language content; see `TaskUnicodePersistenceTest` (runs when Docker is available).
- **Messaging**: `task.created` uses a **dead-letter** queue; consumers use `processed_events` for **at-least-once** deduplication by `messageId` (not a full outbox — documented tradeoff for learning).
- **Resilience**: `GET /api/v1/demo/resilience` exercises a circuit breaker + fallback against `https://httpbin.org/status/500` (requires outbound network from the API process).
- **Caching**: in-memory Spring Cache for `tasksById`; Redis can be added later with `RedisCacheManager` if you introduce `spring-boot-starter-data-redis`.
- **Observability**: JSON-friendly timestamps; MDC `correlationId` from `X-Correlation-Id`; Actuator exposes `health`, `info`, and `prometheus` (secure endpoints in real deployments).

## Interview-friendly case studies (what to say)

1. **Payments-style correctness**: Use `java.math.BigDecimal` with explicit scale/rounding for money; use `double` only with awareness of error — see `KahanSummation` in `common` for compensated summation as a contrast.
2. **Messaging reliability**: Producers publish with a unique `messageId`; consumers check `processed_events` before side effects; failures can route to DLQ for replay — discuss poison messages and idempotency keys.
3. **i18n**: `MessageSource` + `Accept-Language` on `/api/v1/locale/greeting`; extend with `messages_xx.properties` and locale resolvers as needed.

## Build

```bash
mvn test
```

Tests that use Testcontainers are skipped automatically when Docker is not available (`disabledWithoutDocker = true` on container tests).

## Module dependency direction

`api` → `domain`, `common`  
`worker` → `domain`, `common`  
`domain` → (no dependency on `api` / `worker`)  

This keeps domain rules and schema migrations reusable and avoids circular coupling.
