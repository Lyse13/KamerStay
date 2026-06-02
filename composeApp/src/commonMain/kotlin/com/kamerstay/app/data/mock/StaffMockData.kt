package com.kamerstay.app.data.mock

import com.kamerstay.app.data.model.StaffMember
import com.kamerstay.app.data.model.StaffStatus

object StaffMockData {

    val staffMembers = listOf(
        StaffMember(
            id = "1",
            name = "Elena Rodriguez",
            role = "RECEPTIONIST",
            shift = "Shift: 08:00 - 16:00",
            status = StaffStatus.ACTIVE
        ),
        StaffMember(
            id = "2",
            name = "Marcus Chen",
            role = "HOUSEKEEPING",
            shift = "On Break: 12:30 - 13:30",
            status = StaffStatus.AWAY
        ),
        StaffMember(
            id = "3",
            name = "Sarah Jenkins",
            role = "SECURITY",
            shift = "Shift: 14:00 - 22:00",
            status = StaffStatus.ACTIVE
        ),
        StaffMember(
            id = "4",
            name = "James Wilson",
            role = "MANAGEMENT",
            shift = "Shift: 09:00 - 18:00",
            status = StaffStatus.ACTIVE
        ),
    )

    val totalStaff = 42
    val onDuty = 28
    val away = 14
    val newRecruits = 3
}