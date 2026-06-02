package com.kamerstay.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.kamerstay.app.data.mock.MockData
import com.kamerstay.app.data.state.BookingState
import com.kamerstay.app.data.state.SearchState
import com.kamerstay.app.model.Hotel
import com.kamerstay.app.data.mock.BookingsMockData
import com.kamerstay.app.data.model.Booking
import com.kamerstay.app.data.state.FilterState
import com.kamerstay.app.data.state.PaymentState

class TravelerViewModel : ViewModel() {
    val searchState = SearchState()
    val bookingState = BookingState()
    val filterState = FilterState()
    val paymentState = PaymentState()

    var hotels by mutableStateOf<List<Hotel>>(MockData.hotels)
        private set

    var selectedHotel by mutableStateOf<Hotel?>(null)
        private set

    var upcomingBookings by mutableStateOf<List<Booking>>(BookingsMockData.upcoming)
        private set

    var pastBookings by mutableStateOf<List<Booking>>(BookingsMockData.past)
        private set

    fun selectHotel(hotelId: String) {
        selectedHotel = MockData.getHotelById(hotelId)
    }

    fun searchHotels() {
        hotels = if (searchState.query.isEmpty()) {
            MockData.hotels
        } else {
            MockData.hotels.filter {
                it.name.contains(searchState.query, ignoreCase = true) ||
                        it.city.contains(searchState.query, ignoreCase = true)
            }
        }
    }
}