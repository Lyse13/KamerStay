package com.kamerstay.app.data.model

data class StaffMember(
    val id: String,
    val name: String,
    val role: String,
    val shift: String,
    val status: StaffStatus
)

enum class StaffStatus {
    ACTIVE, AWAY, OFF_DUTY
}