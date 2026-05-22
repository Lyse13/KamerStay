package com.kamerstay.app.data.mock

import androidx.compose.ui.graphics.Color
import com.kamerstay.app.core.theme.DeepEmerald
import com.kamerstay.app.core.theme.StatusCleaning
import com.kamerstay.app.core.theme.StatusReserved
import com.kamerstay.app.core.theme.WarmAmber

data class StaffMember(
    val id: String,
    val name: String,
    val email: String,
    val role: String,
    val initials: String,
    val avatarColor: Color,
    val isActive: Boolean = true
)

val mockStaffMembers = listOf(
    StaffMember(
        id = "1", name = "Ekwalle Marc",
        email = "ekwalle.m@terroir.cm",
        role = "Receptionist",
        initials = "EM",
        avatarColor = DeepEmerald
    ),
    StaffMember(
        id = "2", name = "Salla Ngando",
        email = "salla.n@terroir.cm",
        role = "Housekeeping",
        initials = "SN",
        avatarColor = WarmAmber
    ),
    StaffMember(
        id = "3", name = "Marie Tchamba",
        email = "marie.t@terroir.cm",
        role = "Administrator",
        initials = "MT",
        avatarColor = StatusCleaning
    ),
    StaffMember(
        id = "4", name = "Paul Biya Jr.",
        email = "paul.b@terroir.cm",
        role = "Receptionist",
        initials = "PB",
        avatarColor = StatusReserved
    ),
)
