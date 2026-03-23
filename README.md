# 🏦 Bank Card Management System

![Java](https://img.shields.io/badge/Java-17-blue) ![Spring
Boot](https://img.shields.io/badge/SpringBoot-3-green)
![Security](https://img.shields.io/badge/SpringSecurity-JWT-orange)
![Database](https://img.shields.io/badge/PostgreSQL-17-blue)
![Build](https://img.shields.io/badge/Maven-Build-red)

Backend REST API for managing bank cards with authentication,
authorization, and secure data handling.

The project demonstrates modern **Java backend development practices**
using Spring Boot, JWT authentication, and PostgreSQL.

------------------------------------------------------------------------

# 📌 Project Overview

The **Bank Card Management System** is a backend service that allows
users to:

-   Register and authenticate using JWT
-   Manage bank cards
-   Perform secure operations with role-based authorization
-   Transfer funds between cards (planned)
-   Store sensitive data securely

This project is designed as a **portfolio-quality backend system**
demonstrating real-world architecture and security practices.

------------------------------------------------------------------------

# 🧱 Architecture

The application follows a **layered architecture**:

Controller → Service → Repository → Database

Principles used:

-   Separation of concerns
-   DTO pattern
-   Validation on input layer
-   Centralized exception handling
-   Secure authentication flow

Project layers:

  Layer        Responsibility
  ------------ --------------------------------
  Controller   REST API endpoints
  Service      Business logic
  Repository   Database interaction
  Model        Entities
  Security     Authentication & Authorization
  DTO          Request / Response models

------------------------------------------------------------------------

# 🛠 Technology Stack

  Technology          Description
  ------------------- --------------------------------
  Java 17             Core programming language
  Spring Boot 3       Backend framework
  Spring Security 6   Authentication & authorization
  JWT                 Stateless authentication
  Spring Data JPA     ORM framework
  PostgreSQL          Relational database
  Maven               Build tool
  Hibernate           ORM implementation

------------------------------------------------------------------------

# 📁 Project Structure

    src/
     └ main/
        └ java/com/andrej911/bankcardsystem/

           controller/
              AuthController.java

           dto/
              LoginRequest.java
              LoginResponse.java
              SignupRequest.java

           exception/
              GlobalExceptionHandler.java

           model/
              User.java
              Role.java

           repository/
              UserRepository.java

           security/
              AuthTokenFilter.java
              JwtUtils.java
              UserDetailsServiceImpl.java
              WebSecurityConfig.java

           service/
              AuthService.java

           BankCardSystemApplication.java

    resources/
       application.yml

------------------------------------------------------------------------

# 🔐 Authentication Flow

1.  User sends **signup request**
2.  Password is **hashed using Spring Security**
3.  User logs in with credentials
4.  Server generates **JWT token**
5.  Client sends token in header

```{=html}
<!-- -->
```
    Authorization: Bearer <JWT_TOKEN>

6.  Security filter validates token for each request

------------------------------------------------------------------------

# 🔑 API Endpoints

## Authentication

### Register user

POST `/api/auth/signup`

Request

``` json
{
  "username": "testuser",
  "email": "test@example.com",
  "password": "password123"
}
```

Response

    201 Created

------------------------------------------------------------------------

### Login

POST `/api/auth/signin`

Request

``` json
{
  "username": "testuser",
  "password": "password123"
}
```

Response

``` json
{
  "token": "jwt_token_here",
  "type": "Bearer",
  "username": "testuser",
  "role": "ROLE_USER"
}
```

------------------------------------------------------------------------

# 🔒 Security

Role-based authorization:

  Role         Permissions
  ------------ ---------------------------
  ROLE_USER    Access personal resources
  ROLE_ADMIN   Manage users and system

Protected endpoints:

    /api/admin/**  -> ADMIN only
    /api/user/**   -> Authenticated users
    /api/**        -> Authenticated users

Public endpoints:

    /api/auth/**

------------------------------------------------------------------------

# 🗄 Database

Database: **PostgreSQL**

Create database:

``` sql
CREATE DATABASE bank_card_db;
```

Configuration example:

``` yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/bank_card_db
    username: postgres
    password: postgres

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

app:
  jwt:
    secret: your_jwt_secret_key
    expiration:
      ms: 86400000
```

------------------------------------------------------------------------

# ▶ Running the Application

### Requirements

-   Java 17+
-   PostgreSQL
-   Maven

Run application:

    ./mvnw spring-boot:run

or

    mvn spring-boot:run

Server starts at:

    http://localhost:8080

------------------------------------------------------------------------

# 🧪 Testing with curl

Register:

    curl -X POST http://localhost:8080/api/auth/signup \
    -H "Content-Type: application/json" \
    -d '{"username":"testuser","email":"test@example.com","password":"password123"}'

Login:

    curl -X POST http://localhost:8080/api/auth/signin \
    -H "Content-Type: application/json" \
    -d '{"username":"testuser","password":"password123"}'

Authenticated request:

    curl -H "Authorization: Bearer YOUR_TOKEN" \
    http://localhost:8080/api/user/profile

------------------------------------------------------------------------

# 🗺 Project Roadmap

Planned features:

-   BankCard entity
-   CRUD operations for cards
-   Card number masking
-   Balance management
-   Transfers between cards
-   Transaction history
-   Encryption of sensitive data
-   Docker support
-   Swagger / OpenAPI documentation
-   Integration tests
-   CI/CD pipeline

------------------------------------------------------------------------

# 📸 Screenshots

    docs/screenshots/

Examples:

-   Signup success
-   Login success
-   Validation errors
-   Duplicate user error

------------------------------------------------------------------------

# 💡 Engineering Practices

The project follows modern backend development practices:

-   Clean architecture
-   DTO usage
-   Global exception handling
-   Secure password storage
-   Stateless authentication
-   Input validation
-   Principle of least privilege

------------------------------------------------------------------------

# 🤝 Contributing

1.  Fork repository
2.  Create feature branch

```{=html}
<!-- -->
```
    git checkout -b feature/new-feature

3.  Commit changes

```{=html}
<!-- -->
```
    git commit -m "Add new feature"

4.  Push branch

```{=html}
<!-- -->
```
    git push origin feature/new-feature

5.  Open Pull Request

------------------------------------------------------------------------

# 📄 License

MIT License

------------------------------------------------------------------------

# 👨‍💻 Author

Andrey

Backend Java Developer

If you like the project, ⭐ star the repository.
