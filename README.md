# User and Identity Service

A Spring Boot microservice responsible for user registration, authentication (JWT), role-based authorization, and user profile management. This service is part of the StudyBuddy ecosystem.

## 🚀 Features

*   **User Registration**: Sign up new users with default `STUDENT` role.
*   **Authentication**: Secure login using Email and Password to receive a JWT (JSON Web Token).
*   **Role-Based Access Control (RBAC)**: Support for `STUDENT` and `ADMIN` roles.
*   **JWT Verification**: Dedicated endpoint to validate tokens and extract user details.
*   **Admin Management**: Secure endpoint to create new administrators (requires Admin privileges).
*   **User Profile**: Public endpoint to retrieve user details by ID.
*   **Database Seeding**: Automatic creation of a Super Admin on startup if configured.
*   **Custom Security**: Implements Spring Security with a custom JWT filter and JDBC-based user repository.

## 🛠 Tech Stack

*   **Java 17+**
*   **Spring Boot 3.x** (Web, Security, JDBC)
*   **MySQL** (Database)
*   **Flyway** (Database Migration)
*   **JJWT** (JWT Library)
*   **Lombok**
*   **Maven**

## ⚙️ Configuration

The application is configured via `src/main/resources/application.yml`.

### Database
Ensure your MySQL instance is running on port `3307` (or update the config) and the database `user_identity` exists.
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3307/user_identity
    username: root
    password: <YOUR_PASSWORD>
```

### JWT Settings
Configure the secret key and expiration time.
```yaml
app:
  jwtSecret: <YOUR_SECRET_KEY>
  jwtExpirationMs: 86400000 # 24 hours
```

### Initial Admin Seeding
Credentials for the initial super admin created on startup.
```yaml
app:
  admin:
    email: admin@studybuddy.com
    password: admin123
```

## 🏃‍♂️ How to Run

1.  **Clone the repository**.
2.  **Configure Database**: Update `application.yml` with your MySQL credentials.
3.  **Build the project**:
    ```bash
    mvn clean install
    ```
4.  **Run the application**:
    ```bash
    mvn spring-boot:run
    ```
    The server will start on port **8081**.

## 📡 API Endpoints

### Authentication

| Method | Endpoint | Description | Auth Required |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/auth/register` | Register a new user (Student) | No |
| `POST` | `/api/v1/auth/login` | Login to receive JWT | No |
| `POST` | `/v1/auth/verify` | Verify validity of a JWT | No |
| `POST` | `/api/v1/auth/admin/create-admin` | Create a new Admin user | Yes (Role: ADMIN) |

### User Management

| Method | Endpoint | Description | Auth Required |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/users/{id}` | Get public user profile by UUID | Yes |

## 📝 Request Examples

### 1. Register User
**POST** `/api/v1/auth/register`
```json
{
  "firstname": "John",
  "lastname": "Doe",
  "email": "john.doe@example.com",
  "password": "password123"
}
```

### 2. Login
**POST** `/api/v1/auth/login`
```json
{
  "email": "john.doe@example.com",
  "password": "password123"
}
```

### 3. Verify Token
**POST** `/v1/auth/verify`
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

## 🔒 Security Architecture

1.  **Login**: User submits email/password. `UserDetailsServiceImpl` loads user by **Email**.
2.  **Token Generation**: On success, a JWT is generated with the user's **UUID** as the Subject.
3.  **Authentication Filter**: For protected requests, `JWTAuthenticationFilter` extracts the UUID from the token.
4.  **Validation**: `UserDetailsServiceImpl` loads the user by **UUID** to verify existence and roles.
