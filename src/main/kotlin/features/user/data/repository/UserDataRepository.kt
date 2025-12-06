package com.piedpiper.features.user.data.repository

import com.piedpiper.common.SimpleResponse
import com.piedpiper.features.user.data.models.User

interface UserDataRepository {
    suspend fun getUserById(id: String): SimpleResponse
    suspend fun getAllUsers(): SimpleResponse
    suspend fun insertUser(user: User): SimpleResponse
    suspend fun updateUser(user: User): SimpleResponse
    suspend fun deleteUserById(id: String): SimpleResponse

    suspend fun getUserByUsername(username: String): SimpleResponse
}