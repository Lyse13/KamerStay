package com.kamerstay.app.data.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class RoomFormState {
    var roomNumber by mutableStateOf("")
    var category by mutableStateOf("Deluxe Suite")
    var pricePerNight by mutableStateOf("285.00")
    var capacity by mutableStateOf("2")
    var description by mutableStateOf("")
    var selectedFeatures by mutableStateOf(setOf<String>())
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
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