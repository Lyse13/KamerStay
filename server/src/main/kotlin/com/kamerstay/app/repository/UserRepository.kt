package com.kamerstay.app.repository

import com.kamerstay.app.config.DatabaseConfig
import com.kamerstay.app.model.User
import com.kamerstay.app.model.UserCredentials
import com.mongodb.client.model.Filters
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
}