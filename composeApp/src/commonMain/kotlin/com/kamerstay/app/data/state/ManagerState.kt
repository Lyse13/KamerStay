package com.kamerstay.app.data.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kamerstay.app.data.model.PricingResponse

class RoomFormState {
    var editingRoomId by mutableStateOf<String?>(null)   // null = création, non-null = édition
    var roomNumber by mutableStateOf("")
    var category by mutableStateOf("Deluxe Suite")
    var pricePerNight by mutableStateOf("")
    var capacity by mutableStateOf("2")
    var floor by mutableStateOf("")
    var size by mutableStateOf("")
    var description by mutableStateOf("")
    var selectedFeatures by mutableStateOf(setOf<String>())
    var isLoading by mutableStateOf(false)
    var isSuccess by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var pricingSuggestion by mutableStateOf<PricingResponse?>(null)
    var isPricingLoading by mutableStateOf(false)

    fun reset() {
        editingRoomId = null; roomNumber = ""; category = "Deluxe Suite"
        pricePerNight = ""; capacity = "2"; floor = ""; size = ""
        description = ""; selectedFeatures = setOf()
        isLoading = false; isSuccess = false; error = null
        pricingSuggestion = null; isPricingLoading = false
    }
}

class CheckInState {
    var searchQuery by mutableStateOf("")
    var arrivalNotes by mutableStateOf("")
    var selectedRequests by mutableStateOf(setOf<String>())
    var isLoading by mutableStateOf(false)
    var isSuccess by mutableStateOf(false)
}

class CheckOutState {
    var roomStatusEnabled by mutableStateOf(true)
    var selectedPayment by mutableStateOf("CREDIT_CARD")
    var isLoading by mutableStateOf(false)
    var isSuccess by mutableStateOf(false)
}

class AddEditStaffState {
    var editingStaffId by mutableStateOf<String?>(null)
    var fullName by mutableStateOf("")
    var selectedRole by mutableStateOf("Receptionist")
    var email by mutableStateOf("")
    var phone by mutableStateOf("")
    var selectedPermission by mutableStateOf("View Only")
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    fun reset() {
        editingStaffId = null
        fullName = ""; email = ""; phone = ""
        selectedRole = "Receptionist"
        selectedPermission = "View Only"
        isLoading = false; error = null
    }
}

class RegisterHotelState {
    var hotelName by mutableStateOf("")
    var selectedCategory by mutableStateOf("")
    var city by mutableStateOf("")
    var address by mutableStateOf("")
    var description by mutableStateOf("")
    var pricePerNight by mutableStateOf("")
    var totalRooms by mutableStateOf("")
    var phoneNumber by mutableStateOf("")
    var emailContact by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var isSuccess by mutableStateOf(false)

    val isValid get() = hotelName.isNotBlank() && city.isNotBlank()
        && pricePerNight.toDoubleOrNull()?.let { it > 0 } == true
}

class ManageHotelState {
    var propertyName by mutableStateOf("La Résidence de Douala")
    var propertyType by mutableStateOf("Boutique Hotel")
    var streetAddress by mutableStateOf("1245 Boulevard de la Liberté, Akwa")
    var city by mutableStateOf("Douala")
    var region by mutableStateOf("Littoral")
    var postalCode by mutableStateOf("BP 450")
    var description by mutableStateOf(
        "Nestled in the heart of Douala's business district, La Résidence offers a unique blend of Cameroonian warmth and contemporary luxury."
    )
    var amenityChecked by mutableStateOf(
        mapOf(
            "High-speed Wi-Fi" to true,
            "Swimming Pool" to true,
            "Full-service Spa" to false,
            "24/7 Gym" to true,
            "In-house Dining" to true,
            "Secure Parking" to true
        )
    )
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
}