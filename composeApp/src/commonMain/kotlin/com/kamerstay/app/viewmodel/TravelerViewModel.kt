package com.kamerstay.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.kamerstay.app.data.mock.MockData
import com.kamerstay.app.data.mock.NotificationsMockData
import com.kamerstay.app.data.model.AppNotification
import com.kamerstay.app.data.state.BookingState
import com.kamerstay.app.data.state.SearchState
import com.kamerstay.app.model.Hotel
import com.kamerstay.app.data.mock.BookingsMockData
import com.kamerstay.app.data.model.Booking
import com.kamerstay.app.data.state.BookingReviewState
import com.kamerstay.app.data.state.CancellationState
import com.kamerstay.app.data.state.FilterState
import com.kamerstay.app.data.state.MapState
import com.kamerstay.app.data.state.NoResultState
import com.kamerstay.app.data.state.PaymentFailedState
import com.kamerstay.app.data.state.PaymentMethodsState
import com.kamerstay.app.data.state.PaymentState
import com.kamerstay.app.data.state.ReviewState
import com.kamerstay.app.data.state.TravelerPaymentMethodsState
import com.kamerstay.app.data.state.TravelerPersonalInfoState
import com.kamerstay.app.data.state.TravelerSettingsState
import com.kamerstay.app.data.state.TravelerSupportState
import com.kamerstay.app.data.state.WishlistState
import com.kamerstay.app.data.state.WriteReviewState

class TravelerViewModel : ViewModel() {
    val searchState = SearchState()
    val bookingState = BookingState()
    val filterState = FilterState()
    val paymentState = PaymentState()
    val reviewState = ReviewState()
    val mapState = MapState()
    val wishlistState = WishlistState()
    val noResultState = NoResultState()
    val paymentMethodsState = PaymentMethodsState()
    val travelerPaymentMethodsState = TravelerPaymentMethodsState()
    val cancellationState = CancellationState()
    val writeReviewState = WriteReviewState()
    val paymentFailedState = PaymentFailedState()
    val travelerSupportState = TravelerSupportState()
    val bookingReviewState = BookingReviewState()
    val travelerPersonalInfoState = TravelerPersonalInfoState()
    val travelerSettingsState = TravelerSettingsState()

    // ── Notifications ─────────────────────────
    var todayNotifications by mutableStateOf<List<AppNotification>>(NotificationsMockData.todayNotifications)
        private set

    var earlierNotifications by mutableStateOf<List<AppNotification>>(NotificationsMockData.earlierNotifications)
        private set

    fun markAllNotificationsRead() {
        todayNotifications = todayNotifications.map { it.copy(isRead = true) }
        earlierNotifications = earlierNotifications.map { it.copy(isRead = true) }
    }

    val unreadNotificationCount get() =
        todayNotifications.count { !it.isRead } + earlierNotifications.count { !it.isRead }

    var hotels by mutableStateOf<List<Hotel>>(MockData.hotels)
        private set

    var priceSortAscending by mutableStateOf<Boolean?>(null)

    val displayedHotels get() = when (priceSortAscending) {
        true  -> hotels.sortedBy { it.pricePerNight }
        false -> hotels.sortedByDescending { it.pricePerNight }
        null  -> hotels
    }

    fun togglePriceSort() {
        priceSortAscending = when (priceSortAscending) {
            null  -> true
            true  -> false
            false -> null
        }
    }

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
                        it.city.contains(searchState.query, ignoreCase = true) ||
                        it.address.contains(searchState.query, ignoreCase = true)
            }
        }
    }
}