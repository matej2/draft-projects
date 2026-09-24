# Outlay

A personal budgeting API built with Spring Boot. Beyond basic expense CRUD, it tracks budgets and savings goals in relation with their frequencies.

> Originally built as a learning project based on [roadmap.sh's Expense Tracker API](https://roadmap.sh/projects/expense-tracker-api), then extended into a real budgeting tool.

## Data

Each expense should include expense cost, frequency, note and category. Expense cost should include tax and shipment cost and be normalized to be in single currency. Note should include a text that identifies that event - item name(s), location or event note.

Frequency includes a name and number of appearances in year. Although most business cases are focused on monthly frequency, it is important to notice that some edge cases include yearly frequency. Predefined categories are as follows:

- Yearly: 1
- Monthly: 12
- Daily: 365
- Weekly (average): 52

Expense can fall into one or more categories. Category includes name and descriptions. According to business use case it would be beneficial that app should provide a way to optimize costs. Therefore, category should also include data flag if this cost is leisure / one time cost. Predefined categories are as follows:

- Groceries
- Leisure
- Electronics
- Utilities
- Clothing
- Health
- Others

Result of this application would be to provide an insight in recurring and non-recurring costs and provide future cost predictions with a certain percentage probability. Besides that it should also provide a way to optimize costs by restructuring them or removing unnecessary purchases.

## Features

- Sign up / log in with JWT (RSA-signed, stateless sessions)
- Full CRUD on expenses
- Filter expenses by past week, past month, last 3 months, or a custom date range
- Recurring expense frequencies (daily / weekly / monthly / yearly)
- Budgets per category, with a status endpoint showing spend vs. limit
- Savings goals
- Weekly job that flags anomalous spending per category (statistical: rolling average + standard deviation) and generates a plain-language explanation with concrete suggestions using an LLM
- CSV import for bulk-adding historical expenses
- API docs via springdoc-openapi (Swagger UI)
- Metrics via Actuator + Prometheus

## Tech stack

- Java, Spring Boot (Web, Data JPA, Security, Actuator)
- PostgreSQL, Flyway for schema migrations
- Docker / Docker Compose for local development

## Architecture notes

- Authentication uses a self-issued JWT (RSA keypair), not a third-party OAuth2 provider.
- Anomaly detection is deterministic (statistics only); the LLM is used **only** to turn already-computed numbers into a human-readable explanation — it never decides on its own what counts as "unusual." See `InsightsCalculatorJob` and `InsightExplanationService`.
- Database access follows least-privilege: the app connects as a dedicated `app_user`, not the Postgres superuser. Schema is created via Flyway.

## Getting started

### Prerequisites

- Java 21+
- Docker & Docker Compose
- An Anthropic API key (for the insight-explanation feature — the rest of the app works without it)

### 1. Configure environment variables

Copy the example file and fill in real values:

```bash
cp .env.example .env
```

| Variable | Description |
|---|---|
| `POSTGRES_PASSWORD` | Password for the Postgres superuser (used only to create the DB and app role) |
| `APP_PASSWORD` | Password for the least-privilege `app_user` the application connects as |
| `ANTHROPIC_API_KEY` | Used for AI-generated spending insights |

**Never commit `.env` or the RSA keypair** — see [Authentication](#authentication) below for how to generate your own keys locally.

### 2. Generate your own JWT signing keys

The repository does not ship with real keys. Generate your own:

```bash
openssl genrsa -out keypair.pem 2048
openssl rsa -in keypair.pem -pubout -out src/main/resources/certs/public.pem
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out src/main/resources/certs/private.pem
rm keypair.pem
```

### 3. Run

```bash
docker compose up
```

This starts Postgres, the app, and (for local inspection only) `pgweb` on `localhost:8081`. **`pgweb` has no authentication — do not expose this compose file outside your local machine.**

The API is available at `http://localhost:8080`. Interactive API docs: `http://localhost:8080/swagger-ui.html`.

## Running tests

```bash
./mvnw test
```

## Roadmap / known limitations

- Test coverage is currently thin — service-layer and repository integration tests are in progress.
- The server-rendered web UI (Thymeleaf) is a secondary, less-maintained interface; the REST API is the primary surface.
- No CI pipeline yet.

## License

MIT
