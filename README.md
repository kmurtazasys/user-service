# User Service

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
- `POST http://localhost:8081/api/v1/auth/login` - Get OAuth2 token
- `GET localhost:8081/api/v1/users/username/{username}` - Get user details (cached)
- `POST localhost:8081/api/v1/users/register` - Register User
