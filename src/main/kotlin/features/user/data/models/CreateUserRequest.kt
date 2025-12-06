package com.piedpiper.features.user.data.models

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserRequest(
    val username: String,
    val password: String,
    val email: String,
    val fullName: String,
    val role: UserRole = UserRole.USER,
    val salt: String
)
