package com.kamerstay.app.config

import com.kamerstay.app.model.Booking
import com.kamerstay.app.model.Hotel
import com.kamerstay.app.model.Room
import com.kamerstay.app.model.User
import com.kamerstay.app.model.UserCredentials
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase

object DatabaseConfig {
    private val CONNECTION_STRING = System.getenv("MONGODB_URI")
        ?: "mongodb://localhost:27017"

    private const val DATABASE_NAME = "kamerstay_db"

    private val client: MongoClient = MongoClient.create(CONNECTION_STRING)

    val database: MongoDatabase = client.getDatabase(DATABASE_NAME)

    val usersCollection = database.getCollection<User>("users")
    val userCredentialsCollection = database.getCollection<UserCredentials>("user_credentials")
    val hotelsCollection = database.getCollection<Hotel>("hotels")
    val roomsCollection = database.getCollection<Room>("rooms")
    val bookingsCollection = database.getCollection<Booking>("bookings")
}