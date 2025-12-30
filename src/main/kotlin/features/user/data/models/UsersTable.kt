package com.piedpiper.features.user.data.models

import org.jetbrains.exposed.sql.Table

object UsersTable : Table("users") {
    val id = varchar("id", 64)
    val salt = varchar("salt", 255)
    val username = varchar("username", 255).uniqueIndex()
    val password = varchar("password", 255)
    val email = varchar("email", 255).uniqueIndex()
    val fullName = varchar("full_name", 255)
    val role = varchar("role", 50)
    val avatarUrl = varchar("avatar_url", 1024).nullable()
    val description = text("description").nullable()

    override val primaryKey = PrimaryKey(id)
}


