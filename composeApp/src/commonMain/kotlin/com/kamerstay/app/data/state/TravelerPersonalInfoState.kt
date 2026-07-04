package com.kamerstay.app.data.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class TravelerPersonalInfoState {
    var fullName by mutableStateOf(UserSession.fullName.ifBlank { "Utilisateur KamerStay" })
    var email    by mutableStateOf(UserSession.email.ifBlank { "" })
    var phoneCode   by mutableStateOf("+237")
    var phoneNumber by mutableStateOf(UserSession.phone.ifBlank { "" })
    var city     by mutableStateOf("")
    var emailNotifications  by mutableStateOf(true)
    var isLoading           by mutableStateOf(false)
    var error               by mutableStateOf<String?>(null)
    var cityExpanded        by mutableStateOf(false)
    var profileImagePicked  by mutableStateOf(false)
}