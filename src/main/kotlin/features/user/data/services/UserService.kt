package com.piedpiper.features.user.data.services
import com.piedpiper.common.SimpleResponse
import com.piedpiper.features.user.data.models.User
import com.piedpiper.features.user.data.repository.UserDataRepository
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class UserService(
    dataBase: CoroutineDatabase,
): UserDataRepository {

    private val userCollection = dataBase.getCollection<User>()

    override suspend fun getUserByUsername(username: String): SimpleResponse {
        return try {
            val user = userCollection.findOne(User::username eq username)
            if (user != null) {
                SimpleResponse(
                    status = HttpStatusCode.OK.value,                       // 200
                    message = "The user was received successfully",
                    data = Json.encodeToJsonElement(user)
                )
            } else {
                SimpleResponse(
                    status = HttpStatusCode.NotFound.value,                 // 404
                    message = "The user was not found",
                )
            }
        } catch (e: Exception) {
            SimpleResponse(
                status = HttpStatusCode.InternalServerError.value,         // 500
                message = "The user was not received successfully: $e"
            )
        }
    }

    override suspend fun getUserById(id: String): SimpleResponse {
        return try {
            val user = userCollection.findOne(User::id eq id)
            if (user != null) {
                SimpleResponse(
                    status = HttpStatusCode.OK.value,                       // 200
                    message = "The user was received successfully",
                    data = Json.encodeToJsonElement(user)
                )
            } else {
                SimpleResponse(
                    status = HttpStatusCode.NotFound.value,                 // 404
                    message = "The user was not found",
                )
            }
        } catch (e: Exception) {
            SimpleResponse(
                status = HttpStatusCode.InternalServerError.value,          // 500
                message = "The user was not received successfully: $e"
            )
        }
    }

    override suspend fun getAllUsers(): SimpleResponse {
        return try {
            val users = userCollection.find().toList()
            SimpleResponse(
                status = HttpStatusCode.OK.value,                           // 200
                message = "Users were received successfully",
                data = Json.encodeToJsonElement(users)
            )
        } catch (e: Exception) {
            SimpleResponse(
                status = HttpStatusCode.InternalServerError.value,          // 500
                message = "An error occurred while receiving users: $e",
            )
        }
    }

    override suspend fun insertUser(user: User): SimpleResponse {
        return try {
            val findUser = userCollection.findOne(User::username eq user.username, User::email eq user.email)
            if (findUser != null) {
                SimpleResponse(
                    status = HttpStatusCode.Conflict.value,                 // 409
                    message = "Such a user already exists"
                )
            } else {
                userCollection.insertOne(user)
                SimpleResponse(
                    status = HttpStatusCode.Created.value,                  // 201
                    message = "The user was created successfully",
                    data = Json.encodeToJsonElement(user)
                )
            }
        } catch (e: Exception) {
            SimpleResponse(
                status = HttpStatusCode.InternalServerError.value,          // 500
                message = "An error occurred during the creation: $e"
            )
        }
    }

    override suspend fun updateUser(user: User): SimpleResponse {
        return try {
            val isUserUpdate = userCollection.updateOne(User::id eq user.id, user).wasAcknowledged()
            if (isUserUpdate) {
                SimpleResponse(
                    status = HttpStatusCode.OK.value,                       // 200
                    message = "The user was updated successfully",
                    data = Json.encodeToJsonElement(user)
                )
            } else {
                SimpleResponse(
                    status = HttpStatusCode.BadRequest.value,               // 400
                    message = "The user was not updated successfully"
                )
            }
        } catch (e: Exception) {
            SimpleResponse(
                status = HttpStatusCode.InternalServerError.value,          // 500
                message = "An error occurred during the update: $e"
            )
        }
    }

    override suspend fun deleteUserById(id: String): SimpleResponse {
        return try {
            val isUserUpdate = userCollection.deleteOne(User::id eq id).wasAcknowledged()
            if (isUserUpdate) {
                SimpleResponse(
                    status = HttpStatusCode.OK.value,                       // 200
                    message = "The user was deleted successfully"
                )
            } else {
                SimpleResponse(
                    status = HttpStatusCode.NotFound.value,                 // 404
                    message = "The user was not deleted successfully"
                )
            }
        } catch (e: Exception) {
            SimpleResponse(
                status = HttpStatusCode.InternalServerError.value,          // 500
                message = "An error occurred during the delete: $e"
            )
        }
    }

}