# API Documentation - PiedPiper User Service

Complete documentation for all endpoints of the user data management microservice.

## Base URL

```
http://localhost:8081/PiedPiper/api/v1
```

## Response Format

All endpoints return a response in the standard format:

```json
{
    "status": 200,
    "message": "Operation result description",
    "data": { ... }  // Optional, contains response data
}
```

## Endpoints

### 1. Create User

Creates a new user in the system.

**Endpoint:** `POST /users`

**Request Body:**

```json
{
    "username": "john_doe",
    "password": "hashed_password",
    "email": "john.doe@example.com",
    "fullName": "John Doe",
    "role": "USER",
    "salt": "random_salt_string"
}
```

**Parameters:**
- `username` (String, required) - Unique username
- `password` (String, required) - Hashed password
- `email` (String, required) - Email address (must be unique)
- `fullName` (String, required) - User's full name
- `role` (UserRole, optional) - User role: `USER` or `ADMIN`. Default: `USER`
- `salt` (String, required) - Salt for password hashing

**Example Request:**

```bash
curl -X POST http://localhost:8081/PiedPiper/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "hashed_password_123",
    "email": "john.doe@example.com",
    "fullName": "John Doe",
    "role": "USER",
    "salt": "abc123salt"
  }'
```

**Success Response (201 Created):**

```json
{
    "status": 201,
    "message": "The user was created successfully",
    "data": {
        "id": "507f1f77bcf86cd799439011",
        "username": "john_doe",
        "password": "hashed_password_123",
        "email": "john.doe@example.com",
        "fullName": "John Doe",
        "role": "USER",
        "salt": "abc123salt",
        "avatarUrl": null,
        "description": null
    }
}
```

**Errors:**

- `400 Bad Request` - Invalid data format
  ```json
  {
      "status": 400,
      "message": "Invalid data format"
  }
  ```

- `409 Conflict` - User with such username or email already exists
  ```json
  {
      "status": 409,
      "message": "Such a user already exists"
  }
  ```

- `500 Internal Server Error` - Internal server error
  ```json
  {
      "status": 500,
      "message": "An error occurred during the creation: [error description]"
  }
  ```

---

### 2. Get All Users

Returns a list of all users in the system.

**Endpoint:** `GET /users`

**Query Parameters:** None

**Example Request:**

```bash
curl -X GET http://localhost:8081/PiedPiper/api/v1/users
```

**Success Response (200 OK):**

```json
{
    "status": 200,
    "message": "Users were received successfully",
    "data": [
        {
            "id": "507f1f77bcf86cd799439011",
            "username": "john_doe",
            "password": "hashed_password_123",
            "email": "john.doe@example.com",
            "fullName": "John Doe",
            "role": "USER",
            "salt": "abc123salt",
            "avatarUrl": null,
            "description": null
        },
        {
            "id": "507f1f77bcf86cd799439012",
            "username": "jane_smith",
            "password": "hashed_password_456",
            "email": "jane.smith@example.com",
            "fullName": "Jane Smith",
            "role": "ADMIN",
            "salt": "def456salt",
            "avatarUrl": "https://example.com/avatar.jpg",
            "description": "System administrator"
        }
    ]
}
```

**Errors:**

- `500 Internal Server Error` - Error retrieving data
  ```json
  {
      "status": 500,
      "message": "An error occurred while receiving users: [error description]"
  }
  ```

---

### 3. Find User by Username

Returns a user by their username.

**Endpoint:** `GET /users?username={username}`

**Query Parameters:**
- `username` (String, required) - Username to search for

**Example Request:**

```bash
curl -X GET "http://localhost:8081/PiedPiper/api/v1/users?username=john_doe"
```

**Success Response (200 OK):**

```json
{
    "status": 200,
    "message": "The user was received successfully",
    "data": {
        "id": "507f1f77bcf86cd799439011",
        "username": "john_doe",
        "password": "hashed_password_123",
        "email": "john.doe@example.com",
        "fullName": "John Doe",
        "role": "USER",
        "salt": "abc123salt",
        "avatarUrl": null,
        "description": null
    }
}
```

**Errors:**

- `404 Not Found` - User not found
  ```json
  {
      "status": 404,
      "message": "The user was not found"
  }
  ```

- `500 Internal Server Error` - Internal server error
  ```json
  {
      "status": 500,
      "message": "The user was not received successfully: [error description]"
  }
  ```

---

### 4. Get User by ID

Returns a user by their unique identifier.

**Endpoint:** `GET /users/{id}`

**Path Parameters:**
- `id` (String, required) - Unique user identifier (MongoDB ObjectId)

**Example Request:**

```bash
curl -X GET http://localhost:8081/PiedPiper/api/v1/users/507f1f77bcf86cd799439011
```

**Success Response (200 OK):**

```json
{
    "status": 200,
    "message": "The user was received successfully",
    "data": {
        "id": "507f1f77bcf86cd799439011",
        "username": "john_doe",
        "password": "hashed_password_123",
        "email": "john.doe@example.com",
        "fullName": "John Doe",
        "role": "USER",
        "salt": "abc123salt",
        "avatarUrl": null,
        "description": null
    }
}
```

**Errors:**

- `400 Bad Request` - Missing id parameter
  ```json
  {
      "status": 400,
      "message": "Missing id"
  }
  ```

- `404 Not Found` - User not found
  ```json
  {
      "status": 404,
      "message": "The user was not found"
  }
  ```

- `500 Internal Server Error` - Internal server error
  ```json
  {
      "status": 500,
      "message": "The user was not received successfully: [error description]"
  }
  ```

---

### 5. Update User

Updates data of an existing user.

**Endpoint:** `PUT /users/{id}`

**Path Parameters:**
- `id` (String, required) - Unique user identifier

**Request Body:**

```json
{
    "id": "507f1f77bcf86cd799439011",
    "username": "john_doe_updated",
    "password": "new_hashed_password",
    "email": "john.doe.new@example.com",
    "fullName": "John Doe Updated",
    "role": "ADMIN",
    "salt": "new_salt_string",
    "avatarUrl": "https://example.com/new-avatar.jpg",
    "description": "Updated description"
}
```

**Important:** 
- If `password` or `salt` are passed as empty strings, they will not be updated (old values will be preserved)
- The `id` field in the request body must match the `id` in the URL

**Example Request:**

```bash
curl -X PUT http://localhost:8081/PiedPiper/api/v1/users/507f1f77bcf86cd799439011 \
  -H "Content-Type: application/json" \
  -d '{
    "id": "507f1f77bcf86cd799439011",
    "username": "john_doe_updated",
    "password": "",
    "email": "john.doe.new@example.com",
    "fullName": "John Doe Updated",
    "role": "ADMIN",
    "salt": "",
    "avatarUrl": "https://example.com/new-avatar.jpg",
    "description": "Updated description"
  }'
```

**Success Response (200 OK):**

```json
{
    "status": 200,
    "message": "The user was updated successfully",
    "data": {
        "id": "507f1f77bcf86cd799439011",
        "username": "john_doe_updated",
        "password": "old_hashed_password",
        "email": "john.doe.new@example.com",
        "fullName": "John Doe Updated",
        "role": "ADMIN",
        "salt": "old_salt_string",
        "avatarUrl": "https://example.com/new-avatar.jpg",
        "description": "Updated description"
    }
}
```

**Errors:**

- `400 Bad Request` - Invalid data format or missing id
  ```json
  {
      "status": 400,
      "message": "Invalid data format"
  }
  ```
  or
  ```json
  {
      "status": 400,
      "message": "Missing id"
  }
  ```
  or
  ```json
  {
      "status": 400,
      "message": "The user was not updated successfully"
  }
  ```

- `404 Not Found` - User not found
  ```json
  {
      "status": 404,
      "message": "Couldn't find the user"
  }
  ```

- `500 Internal Server Error` - Internal server error
  ```json
  {
      "status": 500,
      "message": "An error occurred during the update: [error description]"
  }
  ```

---

### 6. Delete User

Deletes a user from the system by their ID.

**Endpoint:** `DELETE /users/{id}`

**Path Parameters:**
- `id` (String, required) - Unique user identifier

**Example Request:**

```bash
curl -X DELETE http://localhost:8081/PiedPiper/api/v1/users/507f1f77bcf86cd799439011
```

**Success Response (200 OK):**

```json
{
    "status": 200,
    "message": "The user was deleted successfully"
}
```

**Errors:**

- `400 Bad Request` - Missing id parameter
  ```json
  {
      "status": 400,
      "message": "Missing id"
  }
  ```

- `404 Not Found` - User not found
  ```json
  {
      "status": 404,
      "message": "The user was not deleted successfully"
  }
  ```

- `500 Internal Server Error` - Internal server error
  ```json
  {
      "status": 500,
      "message": "An error occurred during the delete: [error description]"
  }
  ```

---

## Usage Examples

### Creating and Managing a User

```bash
# 1. Create user
curl -X POST http://localhost:8081/PiedPiper/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "test_user",
    "password": "hashed_pass",
    "email": "test@example.com",
    "fullName": "Test User",
    "role": "USER",
    "salt": "salt123"
  }'

# 2. Get user by username
curl -X GET "http://localhost:8081/PiedPiper/api/v1/users?username=test_user"

# 3. Update user (preserving password)
curl -X PUT http://localhost:8081/PiedPiper/api/v1/users/{id} \
  -H "Content-Type: application/json" \
  -d '{
    "id": "{id}",
    "username": "test_user",
    "password": "",
    "email": "test@example.com",
    "fullName": "Test User Updated",
    "role": "USER",
    "salt": "",
    "avatarUrl": "https://example.com/avatar.jpg"
  }'

# 4. Delete user
curl -X DELETE http://localhost:8081/PiedPiper/api/v1/users/{id}
```

## Notes

1. **Password Security**: Passwords should be hashed on the client side before sending. The service expects an already hashed password and salt.

2. **Uniqueness**: The `username` and `email` fields must be unique in the system.

3. **Password Update**: When updating a user, if you don't want to change the password, pass an empty string in the `password` and `salt` fields.

4. **ID Format**: User ID is a MongoDB ObjectId in string format (24 characters).

5. **Roles**: Available roles: `USER` (default) and `ADMIN`.

## HTTP Status Codes

| Code | Description |
|------|-------------|
| 200  | Successful operation |
| 201  | Resource successfully created |
| 400  | Invalid request |
| 404  | Resource not found |
| 409  | Conflict (user already exists) |
| 500  | Internal server error |
