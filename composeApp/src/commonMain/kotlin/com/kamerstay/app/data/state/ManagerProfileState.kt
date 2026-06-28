package com.kamerstay.app.data.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kamerstay.app.data.model.AmenityItem
import com.kamerstay.app.data.mock.AmenitiesMockData

class VerificationState {
    var currentStep by mutableStateOf(1)
    var totalSteps by mutableStateOf(2)
    var frontIdUploaded by mutableStateOf(false)
    var backIdUploaded by mutableStateOf(false)
    var businessLicenseUploaded by mutableStateOf(false)
    var isSubmitting by mutableStateOf(false)
    var isSubmitted by mutableStateOf(false)
    val progress get() = when {
        frontIdUploaded && backIdUploaded && businessLicenseUploaded -> 1f
        frontIdUploaded && backIdUploaded -> 0.7f
        frontIdUploaded -> 0.4f
        else -> 0.1f
    }
}

class ManagerPersonalInfoState {
    var fullName by mutableStateOf(UserSession.fullName.ifBlank { "Gestionnaire d'Hôtel" })
    var email    by mutableStateOf(UserSession.email.ifBlank { "contact@akwa-palace.cm" })
    var phoneCode   by mutableStateOf("+237")
    var phoneNumber by mutableStateOf(UserSession.phone.ifBlank { "670 000 000" })
    var region by mutableStateOf("Douala, Littoral")
    var emailNotifications by mutableStateOf(true)
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var regionExpanded by mutableStateOf(false)
    var profileImagePicked by mutableStateOf(false)
}

class AmenitiesState {
    var amenities by mutableStateOf(AmenitiesMockData.amenities)
    var isLoading by mutableStateOf(false)
    var isSaved by mutableStateOf(false)

    fun toggleAmenity(id: String) {
        amenities = amenities.map { amenity ->
            if (amenity.id == id) amenity.copy(isEnabled = !amenity.isEnabled)
            else amenity
        }
    }
}