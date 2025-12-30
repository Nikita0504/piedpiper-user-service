package com.piedpiper.features.user.data.models

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class User(
    val id: String = UUID.randomUUID().toString(),
    val salt: String,
    val username: String,
    val password: String,
    val email: String,
    val fullName: String,
    val role: UserRole,
    val avatarUrl: String? = null,
    val description: String? = null,
)

@Serializable
enum class UserRole {
    USER,
    ADMIN
}
