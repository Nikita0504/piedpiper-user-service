package com.piedpiper.features.user.data.services
import com.piedpiper.common.SimpleResponse
import com.piedpiper.features.user.data.models.User
import com.piedpiper.features.user.data.models.UserRole
import com.piedpiper.features.user.data.models.UsersTable
import com.piedpiper.features.user.data.repository.UserDataRepository
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class UserService(
    private val database: Database,
): UserDataRepository {

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO, database) { block() }

    private fun ResultRow.toUser(): User =
        User(
            id = this[UsersTable.id],
            salt = this[UsersTable.salt],
            username = this[UsersTable.username],
            password = this[UsersTable.password],
            email = this[UsersTable.email],
            fullName = this[UsersTable.fullName],
            role = UserRole.valueOf(this[UsersTable.role]),
            avatarUrl = this[UsersTable.avatarUrl],
            description = this[UsersTable.description],
        )

    override suspend fun getUserByUsername(username: String): SimpleResponse {
        return try {
            val user = dbQuery {
                UsersTable
                    .selectAll().where(UsersTable.username eq username)
                    .singleOrNull()
                    ?.toUser()
            }
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
            val user = dbQuery {
                UsersTable
                    .selectAll().where(UsersTable.id eq id)
                    .singleOrNull()
                    ?.toUser()
            }
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
            val users = dbQuery {
                UsersTable
                    .selectAll()
                    .map { it.toUser() }
            }
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
            val findUser = dbQuery {
                UsersTable
                    .selectAll().where(
                        (UsersTable.username eq user.username) and
                                (UsersTable.email eq user.email)
                    )
                    .singleOrNull()
                    ?.toUser()
            }
            if (findUser != null) {
                SimpleResponse(
                    status = HttpStatusCode.Conflict.value,                 // 409
                    message = "Such a user already exists"
                )
            } else {
                dbQuery {
                    UsersTable.insert { row ->
                        row[id] = user.id
                        row[salt] = user.salt
                        row[username] = user.username
                        row[password] = user.password
                        row[email] = user.email
                        row[fullName] = user.fullName
                        row[role] = user.role.name
                        row[avatarUrl] = user.avatarUrl
                        row[description] = user.description
                    }
                }
                SimpleResponse(
                    status = HttpStatusCode.OK.value,                  // 201
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
            val updatedRows = dbQuery {
                UsersTable.update({ UsersTable.id eq user.id }) { row ->
                    row[salt] = user.salt
                    row[username] = user.username
                    row[password] = user.password
                    row[email] = user.email
                    row[fullName] = user.fullName
                    row[role] = user.role.name
                    row[avatarUrl] = user.avatarUrl
                    row[description] = user.description
                }
            }
            val isUserUpdate = updatedRows > 0
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
            val deletedRows = dbQuery {
                UsersTable.deleteWhere { UsersTable.id eq id }
            }
            if (deletedRows > 0) {
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