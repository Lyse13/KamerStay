package com.kamerstay.app.repository

import com.kamerstay.app.config.DatabaseConfig
import com.kamerstay.app.model.User
import com.kamerstay.app.model.UserCredentials
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import kotlinx.coroutines.flow.firstOrNull

class UserRepository {
    private val users = DatabaseConfig.usersCollection
    private val credentials = DatabaseConfig.userCredentialsCollection

    suspend fun findUserByEmail(email: String): User? {
        return users.find(Filters.eq("email", email)).firstOrNull()
    }

    suspend fun findCredentialsByEmail(email: String): UserCredentials? {
        return credentials.find(Filters.eq("email", email)).firstOrNull()
    }

    suspend fun findUserById(id: String): User? {
        return users.find(Filters.eq("id", id)).firstOrNull()
    }

    suspend fun createUser(user: User, passwordHash: String) {
        users.insertOne(user)
        credentials.insertOne(
            UserCredentials(
                id = user.id,
                userId = user.id,
                email = user.email,
                passwordHash = passwordHash
            )
        )
    }

    suspend fun emailExists(email: String): Boolean {
        return findUserByEmail(email) != null
    }

    suspend fun updatePassword(email: String, newPasswordHash: String) {
        credentials.updateOne(
            Filters.eq("email", email),
            Updates.set("passwordHash", newPasswordHash)
        )
    }

    suspend fun updateUser(userId: String, fullName: String, phoneNumber: String): Boolean {
        val result = users.updateOne(
            Filters.eq("id", userId),
            Updates.combine(
                Updates.set("fullName", fullName),
                Updates.set("phoneNumber", phoneNumber)
            )
        )
        return result.modifiedCount > 0
    }
}