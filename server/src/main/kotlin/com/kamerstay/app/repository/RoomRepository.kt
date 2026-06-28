package com.kamerstay.app.repository

import com.kamerstay.app.config.DatabaseConfig
import com.kamerstay.app.model.Room
import com.kamerstay.app.model.enums.RoomStatus
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList

class RoomRepository {
    private val rooms = DatabaseConfig.roomsCollection

    suspend fun getRoomsByHotelId(hotelId: String): List<Room> {
        return rooms.find(Filters.eq("hotelId", hotelId)).toList()
    }

    suspend fun getRoomById(id: String): Room? {
        return rooms.find(Filters.eq("id", id)).firstOrNull()
    }

    suspend fun insertRoom(room: Room) {
        rooms.insertOne(room)
    }

    suspend fun countRoomsForHotel(hotelId: String): Long {
        return rooms.countDocuments(Filters.eq("hotelId", hotelId))
    }

    suspend fun updateRoom(room: Room): Boolean {
        val result = rooms.replaceOne(Filters.eq("id", room.id), room)
        return result.modifiedCount > 0
    }

    suspend fun updateRoomStatus(id: String, status: RoomStatus): Boolean {
        val result = rooms.updateOne(
            Filters.eq("id", id),
            Updates.set("status", status.name)
        )
        return result.modifiedCount > 0
    }

    suspend fun deleteRoom(id: String): Boolean {
        val result = rooms.deleteOne(Filters.eq("id", id))
        return result.deletedCount > 0
    }
}