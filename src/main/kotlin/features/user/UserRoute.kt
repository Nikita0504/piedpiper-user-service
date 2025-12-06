package com.piedpiper.features.user

import com.piedpiper.common.SimpleResponse
import com.piedpiper.features.user.data.models.CreateUserRequest
import com.piedpiper.features.user.data.models.User
import com.piedpiper.features.user.data.repository.UserDataRepository
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import org.koin.ktor.ext.get as koinGet

fun Route.userRoute() {
    val userDataSource: UserDataRepository = koinGet<UserDataRepository>()

    route("/users") {
        post("") {
            val request = call.receiveNullable<CreateUserRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(HttpStatusCode.BadRequest.value, "Invalid data format"))
                return@post
            }

            val user = User(
                username = request.username,
                fullName = request.fullName,
                password = request.password,
                salt = request.salt,
                role = request.role,
                email = request.email
            )

            val response = userDataSource.insertUser(user)

            if (response.status == HttpStatusCode.OK.value) {
                call.respond(HttpStatusCode.Created, response)
            } else {
                call.respond(HttpStatusCode.BadRequest, response)
            }
        }

        get("") {
            val username = call.request.queryParameters["username"]

            if (username.isNullOrBlank()) {
                // Возврат всех пользователей
                val response = userDataSource.getAllUsers()
                if (response.status == HttpStatusCode.OK.value) {
                    call.respond(HttpStatusCode.OK, response)
                } else {
                    call.respond(HttpStatusCode.InternalServerError, response)
                }
            } else {
                // Поиск пользователя по username
                val response = userDataSource.getUserByUsername(username)
                if (response.status == HttpStatusCode.OK.value) {
                    call.respond(HttpStatusCode.OK, response)
                } else {
                    call.respond(HttpStatusCode.NotFound, response)
                }
            }
        }

        route("/{id}") {
            get("") {
                val id = call.parameters["id"] ?: kotlin.run {
                    call.respond(HttpStatusCode.BadRequest, SimpleResponse(HttpStatusCode.BadRequest.value, "Missing id"))
                    return@get
                }

                val response = userDataSource.getUserById(id)
                if (response.status == HttpStatusCode.OK.value) {
                    call.respond(HttpStatusCode.OK, response)
                } else {
                    call.respond(HttpStatusCode.NotFound, response)
                }
            }

            // Update
            put {
                val id = call.parameters["id"] ?: kotlin.run {
                    call.respond(HttpStatusCode.BadRequest, SimpleResponse(HttpStatusCode.BadRequest.value, "Missing id"))
                    return@put
                }

                val request = call.receiveNullable<User>() ?: kotlin.run {
                    call.respond(HttpStatusCode.BadRequest, SimpleResponse(HttpStatusCode.BadRequest.value, "Invalid data format"))
                    return@put
                }

                // fetch existing to preserve password/salt if needed
                val existingResp = userDataSource.getUserById(id)
                val existingUser = existingResp.data?.let { Json.decodeFromJsonElement<User>(it) }

                if (existingUser == null) {
                    call.respond(HttpStatusCode.NotFound, SimpleResponse(HttpStatusCode.NotFound.value, "Couldn't find the user",))
                    return@put
                }

                val updatedUser = User(
                    id = id,
                    username = request.username,
                    fullName = request.fullName,
                    password = if (request.password.isNotEmpty()) request.password else existingUser.password,
                    salt = if (request.salt.isNotEmpty()) request.salt else existingUser.salt,
                    role = request.role,
                    email = request.email
                )

                val response = userDataSource.updateUser(updatedUser)
                if (response.status == HttpStatusCode.OK.value) {
                    call.respond(HttpStatusCode.OK, response)
                } else {
                    call.respond(HttpStatusCode.BadRequest, response)
                }
            }

            // Delete
            delete {
                val id = call.parameters["id"] ?: kotlin.run {
                    call.respond(HttpStatusCode.BadRequest, SimpleResponse(HttpStatusCode.BadRequest.value, "Missing id"))
                    return@delete
                }

                val response = userDataSource.deleteUserById(id)
                if (response.status == HttpStatusCode.OK.value) {
                    call.respond(HttpStatusCode.OK, response)
                } else {
                    call.respond(HttpStatusCode.BadRequest, response)
                }
            }
        }
    }

}