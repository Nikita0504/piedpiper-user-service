# PiedPiper User Service

Microservice for managing user data in the PiedPiper Services ecosystem.

## Description

PiedPiper User Service is a RESTful microservice developed in Kotlin using the Ktor framework. The service provides a complete set of operations for managing user data: creating, reading, updating, and deleting users.

## Technology Stack

- **Language**: Kotlin
- **Framework**: Ktor
- **Database**: MongoDB
- **Dependency Injection**: Koin
- **Serialization**: Kotlinx Serialization (JSON)
- **Server**: Netty Engine

## Key Features

- ✅ Create new users
- ✅ Get user by ID
- ✅ Get user by username
- ✅ Get list of all users
- ✅ Update user data
- ✅ Delete user

## Project Structure

```
src/main/kotlin/
├── Application.kt                    # Application entry point
├── common/                          # Common models and utilities
│   ├── SimpleResponse.kt           # Standard API response format
│   └── Parameters.kt
├── features/
│   ├── database/
│   │   └── di/
│   │       └── dataBaseModule.kt   # Database connection module
│   └── user/
│       ├── data/
│       │   ├── models/             # Data models
│       │   │   ├── User.kt         # User model
│       │   │   └── CreateUserRequest.kt
│       │   ├── repository/         # Repository interfaces
│       │   │   └── UserDataRepository.kt
│       │   └── services/           # Business logic implementation
│       │       └── UserService.kt
│       ├── di/
│       │   └── userModule.kt       # DI module for users
│       └── UserRoute.kt            # API routes
└── plugins/                         # Plugin configuration
    ├── Frameworks.kt
    ├── Routing.kt
    └── Serialization.kt
```

## Data Model

### User

System user with the following fields:

- `id` (String) - Unique user identifier (MongoDB ObjectId)
- `username` (String) - Username (unique)
- `password` (String) - Hashed password
- `salt` (String) - Salt for password hashing
- `email` (String) - Email address (unique)
- `fullName` (String) - User's full name
- `role` (UserRole) - User role (USER or ADMIN)
- `avatarUrl` (String?) - Avatar URL (optional)
- `description` (String?) - User description/biography (optional)

### UserRole

User roles:
- `USER` - Regular user
- `ADMIN` - Administrator

## Configuration

The service uses the `application.yaml` file for configuration:

```yaml
ktor:
    application:
        modules:
            - com.piedpiper.ApplicationKt.module
    deployment:
        port: 8081
```

By default, the service runs on port **8081**.

## Running

### Requirements

- JDK 11 or higher
- MongoDB (locally or remotely)
- Gradle

### Building the Project

```bash
./gradlew build
```

### Running the Service

```bash
./gradlew run
```

Or using the built JAR:

```bash
java -jar build/libs/user-all.jar
```

## API Documentation

Complete documentation for all available endpoints and request examples can be found in [API.md](./API.md).

## Response Format

All endpoints return a response in the standard `SimpleResponse` format:

```json
{
    "status": 200,
    "message": "Operation result description",
    "data": { ... }  // Optional, contains response data
}
```

## HTTP Status Codes

- `200 OK` - Successful operation
- `201 Created` - Resource successfully created
- `400 Bad Request` - Invalid request
- `404 Not Found` - Resource not found
- `409 Conflict` - Conflict (e.g., user already exists)
- `500 Internal Server Error` - Internal server error

## Security

⚠️ **Important**: In the current implementation, passwords are stored using salt, but additional protection is recommended:
- Use HTTPS for all requests
- Input data validation
- Rate limiting
- Authentication and authorization (JWT tokens)

## Development

### Running Tests

```bash
./gradlew test
```

### Module Structure

The project uses a modular architecture with layer separation:
- **Data Layer** - Database operations (models, repositories, services)
- **Presentation Layer** - HTTP routes and request handling
- **Dependency Injection** - Dependency management via Koin
