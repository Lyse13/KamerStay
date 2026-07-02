package com.kamerstay.app.data.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil

class BookingState {
    var checkInDate by mutableStateOf<LocalDate?>(null)
    var checkOutDate by mutableStateOf<LocalDate?>(null)
    var guestCount by mutableStateOf(1)
    var contactName by mutableStateOf(UserSession.fullName)
    var contactEmail by mutableStateOf(UserSession.email)
    var contactPhone by mutableStateOf(UserSession.phone)
    var specialRequests by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var showDatePicker by mutableStateOf(false)

    val nights: Int get() {
        val ci = checkInDate ?: return 0
        val co = checkOutDate ?: return 0
        return ci.daysUntil(co)
    }

    val checkInDisplay: String get() = checkInDate?.let { formatDate(it) } ?: "Select date"
    val checkOutDisplay: String get() = checkOutDate?.let { formatDate(it) } ?: "Select date"

    val dateRangeDisplay: String get() = when {
        checkInDate == null -> "Select check-in"
        checkOutDate == null -> "${formatDate(checkInDate!!)} → Select check-out"
        else -> "${formatDate(checkInDate!!)} — ${formatDate(checkOutDate!!)}"
    }

    private fun formatDate(date: LocalDate): String {
        val day = date.day
        val month = date.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
        val year = date.year
        return "$day $month $year"
    }
}

class FilterState {
    var minPrice by mutableStateOf(15000f)
    var maxPrice by mutableStateOf(450000f)
    var selectedStars by mutableStateOf(setOf<Int>())
    var selectedPropertyType by mutableStateOf("")
    var selectedAmenities by mutableStateOf(setOf<String>())
    var isVerifiedOnly by mutableStateOf(false)

    val hasActiveFilters get() =
        isVerifiedOnly ||
        selectedAmenities.isNotEmpty() ||
        selectedPropertyType.isNotEmpty() ||
        selectedStars.isNotEmpty() ||
        minPrice != 15000f ||
        maxPrice != 450000f

    fun clearAll() {
        isVerifiedOnly = false
        selectedStars = setOf()
        selectedPropertyType = ""
        selectedAmenities = setOf()
        minPrice = 15000f
        maxPrice = 450000f
    }

    fun matchesAmenity(filterAmenity: String, hotelAmenity: String): Boolean {
        val h = hotelAmenity.lowercase()
        return when (filterAmenity) {
            "Wi-Fi" -> h.contains("wi-fi") || h.contains("wifi") || h.contains("internet")
            "Pool"  -> h.contains("pool") || h.contains("piscine")
            "Gym"   -> h.contains("gym") || h.contains("fitness")
            "Spa"   -> h.contains("spa")
            "AC"    -> h.contains("clim") || h.contains("climatisation")
            else    -> h.contains(filterAmenity.lowercase())
        }
    }

    fun hotelMatches(
        price: Int,
        rating: Double,
        amenities: List<String>,
        isVerified: Boolean = true
    ): Boolean =
        price >= minPrice.toInt() &&
        price <= maxPrice.toInt() &&
        (selectedStars.isEmpty() || rating.toInt() in selectedStars) &&
        (selectedAmenities.isEmpty() || selectedAmenities.all { fa -> amenities.any { ha -> matchesAmenity(fa, ha) } }) &&
        (!isVerifiedOnly || isVerified)
}

class PaymentState {
    var selectedMethod by mutableStateOf("MTN")
    var phoneNumber by mutableStateOf("")
    var cardNumber by mutableStateOf("")
    var expiryDate by mutableStateOf("")
    var cvv by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var isPolling by mutableStateOf(false)
    var isSuccess by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var paymentReference by mutableStateOf<String?>(null)
    var statusMessage by mutableStateOf("")

    val isMobileMoney get() = selectedMethod == "MTN" || selectedMethod == "ORANGE"

    // Normalise "6XXXXXXXX" → "237XXXXXXXXX"
    val normalizedPhone: String get() {
        val digits = phoneNumber.filter { it.isDigit() }
        return when {
            digits.startsWith("237") -> digits
            digits.startsWith("6") && digits.length == 9 -> "237$digits"
            else -> digits
        }
    }

    val isPhoneValid: Boolean get() {
        val d = phoneNumber.filter { it.isDigit() }
        return d.length == 9 && d.startsWith("6")
    }
}

class SearchState {
    var query by mutableStateOf("")
    var city by mutableStateOf("")
    var viewMode by mutableStateOf("List")
    var destination by mutableStateOf("Douala, Cameroun")
    var minPrice by mutableStateOf(0)
    var maxPrice by mutableStateOf(500000)
    var guestCount by mutableStateOf(1)
    var isLoading by mutableStateOf(false)
    var selectedLandmark by mutableStateOf("")
}