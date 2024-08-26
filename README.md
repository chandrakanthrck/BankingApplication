
# Spring Boot Banking Application

## Overview
This is a Spring Boot-based banking application that provides a range of features for managing user accounts, transactions, and authentication. The application includes capabilities such as account creation, login, debit, credit, funds transfer, and bank statement generation. JWT token-based authentication is implemented for secure user sessions. The application is documented using Swagger for easy API exploration and testing.

## Features
- **Account Management**
  - Create new user accounts
  - User login with JWT authentication
- **Transaction Management**
  - Credit and debit operations on user accounts
  - Funds transfer between accounts
- **Bank Statements**
  - Generate bank statements in PDF format for specific date ranges
- **Security**
  - JWT token-based authentication for secure access to the APIs
- **API Documentation**
  - Interactive API documentation with Swagger UI

## Technologies Used
- **Java 17**
- **Spring Boot 3.x**
- **Spring Security**
- **JWT (JSON Web Token)**
- **Hibernate/JPA**
- **MySQL**
- **Swagger/OpenAPI 3**
- **Lombok**

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL Server

### Setup

#### Clone the Repository
```bash
git clone https://github.com/your-username/banking-application.git
cd banking-application
```

#### Configure the Application

Update the database connection properties in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/banking
spring.datasource.username=root
spring.datasource.password=your_password
```

Update the JWT secret and other relevant properties as needed.

#### Run the Application

Use Maven to build and run the application:

```bash
mvn spring-boot:run
```

#### Access the Application

- The application will run on `http://localhost:8080`.
- The Swagger UI for API documentation is available at `http://localhost:8080/swagger-ui.html`.

## API Endpoints

### User Account Management
- `POST /api/user`: Create a new user account
- `POST /api/user/login`: Login and obtain JWT token

### Transaction Operations
- `POST /api/user/credit`: Credit an account
- `POST /api/user/debit`: Debit an account
- `POST /api/user/transfer`: Transfer funds between accounts

### Bank Statement
- `GET /bankstatement`: Generate a bank statement in PDF format

## Security

All sensitive operations require a valid JWT token to be passed in the `Authorization` header as a Bearer token.

## License

This project is licensed under the MIT License.
