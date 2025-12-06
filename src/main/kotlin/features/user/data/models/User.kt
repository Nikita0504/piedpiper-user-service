package com.piedpiper.features.user.data.models

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class User(
    @BsonId
    val id: String = ObjectId().toString(),
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
