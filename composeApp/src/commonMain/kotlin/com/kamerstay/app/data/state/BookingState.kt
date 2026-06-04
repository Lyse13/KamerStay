package com.kamerstay.app.data.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class BookingState {
    var checkInDate by mutableStateOf("")
    var checkOutDate by mutableStateOf("")
    var guestCount by mutableStateOf(1)
    var specialRequests by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
}

class FilterState {
    var minPrice by mutableStateOf(15000f)
    var maxPrice by mutableStateOf(450000f)
    var selectedStars by mutableStateOf(setOf<Int>())
    var selectedPropertyType by mutableStateOf("")
    var selectedAmenities by mutableStateOf(setOf<String>())
    var selectedLandmark by mutableStateOf("")
}

class PaymentState {
    var selectedMethod by mutableStateOf("MTN")
    var cardNumber by mutableStateOf("")
    var expiryDate by mutableStateOf("")
    var cvv by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var isSuccess by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
}

class SearchState {
    var query by mutableStateOf("")
    var city by mutableStateOf("")
    var viewMode by mutableStateOf("List")
    var destination by mutableStateOf("Abidjan, Ivory Coast")
    var minPrice by mutableStateOf(0)
    var maxPrice by mutableStateOf(500000)
    var guestCount by mutableStateOf(1)
    var isLoading by mutableStateOf(false)
    var selectedLandmark by mutableStateOf("")
}