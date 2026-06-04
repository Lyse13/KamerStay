package com.kamerstay.app.data.model

data class StaffMember(
    val id: String,
    val name: String,
    val role: String,
    val shift: String,
    val status: StaffStatus
)

data class StaffFormData(
    val fullName: String = "",
    val role: String = "Receptionist",
    val email: String = "",
    val phone: String = "",
    val permission: String = "View Only"
)

enum class StaffStatus {
    ACTIVE, AWAY, OFF_DUTY
}