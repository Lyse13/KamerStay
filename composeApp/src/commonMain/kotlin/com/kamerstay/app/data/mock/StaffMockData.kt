package com.kamerstay.app.data.mock

import com.kamerstay.app.data.model.StaffMember
import com.kamerstay.app.data.model.StaffStatus

object StaffMockData {

    val staffMembers = listOf(
        StaffMember(
            id = "1",
            name = "Brigitte Owona",
            role = "RÉCEPTIONNISTE",
            shift = "Équipe : 08:00 - 16:00",
            status = StaffStatus.ACTIVE
        ),
        StaffMember(
            id = "2",
            name = "Boris Tankeu",
            role = "ENTRETIEN",
            shift = "Pause : 12:30 - 13:30",
            status = StaffStatus.AWAY
        ),
        StaffMember(
            id = "3",
            name = "Sylvie Fonkam",
            role = "SÉCURITÉ",
            shift = "Équipe : 14:00 - 22:00",
            status = StaffStatus.ACTIVE
        ),
        StaffMember(
            id = "4",
            name = "Jean-Claude Wamba",
            role = "DIRECTION",
            shift = "Équipe : 09:00 - 18:00",
            status = StaffStatus.ACTIVE
        ),
    )

    val totalStaff = 42
    val onDuty = 28
    val away = 14
    val newRecruits = 3
}