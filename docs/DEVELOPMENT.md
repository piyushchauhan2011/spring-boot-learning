# Development guide

## Prerequisites

- **Java 17+** (the project builds on Java 17; newer LTS versions such as 21 are fine).
- **Maven 3.9+**
- **Docker** (for Postgres, RabbitMQ, and MailHog via Compose)

## First-time setup

1. Start infrastructure:

   ```bash
   docker compose up -d
   ```

2. Install all modules into the local Maven repository (required so `api` and `worker` resolve `common` and `domain`):

   ```bash
   mvn install -DskipTests
   ```

3. Run the **API**:

   ```bash
   mvn -pl api spring-boot:run
   ```

4. Optionally run the **worker** (Rabbit consumer) in another terminal:

   ```bash
   mvn -pl worker spring-boot:run
   ```

## Useful URLs (local defaults)

| Service | URL |
|--------|-----|
| API | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| Static demo (`index.html`) | http://localhost:8080/index.html |
| MailHog | http://localhost:8025 |
| RabbitMQ management | http://localhost:15672 (guest / guest) |

## Configuration and secrets

- Default JDBC and Rabbit settings match [`docker-compose.yml`](../docker-compose.yml).
- **Do not commit** real secrets. Use `application-local.yml` (gitignored) or environment variables for overrides.
- JWT signing: set `app.jwt.secret` to a strong random value in non-dev environments.

## Tests

```bash
mvn test
```

Tests that use Testcontainers need a working Docker daemon; they are skipped when Docker is unavailable.

## Git workflow

- Use focused commits with clear messages.
- Run `mvn test` before pushing when you change Java code.

This repository was initialized with a **local** Git identity (`Developer` / `dev@local`) only so the first commit could be created. Set your own before pushing anywhere:

```bash
git config user.name "Your Name"
git config user.email "you@example.com"
```
