# Kamil Auth Service

OAuth2 Authorization Server for the microservices architecture.

## Port
8081

## Features
- OAuth2 Authorization Server
- JWT token generation
- User & Role management
- BCrypt password encoding
- Virtual Threads enabled
- Caffeine Caching

## Tech Stack
- Spring Boot 3.5
- Java 21
- PostgreSQL
- Liquibase
- Spring Security OAuth2

## Database
```sql
CREATE DATABASE auth_db;
```

## Running
```bash
mvn spring-boot:run
```

## Testing
```bash
mvn test
```

## API Endpoints
- `POST /oauth2/token` - Get OAuth2 token
- `GET /api/users/{username}` - Get user details (cached)

## OAuth2 Token Example
```bash
curl -X POST http://localhost:8081/oauth2/token \
  -u client:secret \
  -d "grant_type=client_credentials&scope=read write"
```
