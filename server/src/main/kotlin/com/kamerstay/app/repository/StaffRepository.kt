package com.kamerstay.app.repository

import com.kamerstay.app.config.DatabaseConfig
import com.kamerstay.app.model.Staff
import com.mongodb.client.model.Filters
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList

class StaffRepository {
    private val staff = DatabaseConfig.staffCollection

    suspend fun getStaffByHotel(hotelId: String): List<Staff> {
        return staff.find(Filters.eq("hotelId", hotelId)).toList()
    }

    suspend fun getById(id: String): Staff? {
        return staff.find(Filters.eq("id", id)).firstOrNull()
    }

    suspend fun create(member: Staff) {
        staff.insertOne(member)
    }

    suspend fun update(member: Staff): Boolean {
        val result = staff.replaceOne(Filters.eq("id", member.id), member)
        return result.modifiedCount > 0
    }

    suspend fun delete(id: String): Boolean {
        val result = staff.deleteOne(Filters.eq("id", id))
        return result.deletedCount > 0
    }
}