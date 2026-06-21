package com.kamerstay.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kamerstay.app.data.mock.CheckInMockData
import com.kamerstay.app.data.mock.ManagerNotificationsMockData
import com.kamerstay.app.data.mock.ReservationMockData
import com.kamerstay.app.data.mock.RoomsMockData
import com.kamerstay.app.data.mock.StaffMockData
import com.kamerstay.app.data.model.CheckInGuest
import com.kamerstay.app.data.model.DepartureGuest
import com.kamerstay.app.data.model.ManagerNotification
import com.kamerstay.app.data.model.ManagerRoom
import com.kamerstay.app.data.model.Reservation
import com.kamerstay.app.data.model.StaffMember
import com.kamerstay.app.data.remote.BookingRemoteRepository
import com.kamerstay.app.data.state.AddEditStaffState
import com.kamerstay.app.data.state.AmenitiesState
import com.kamerstay.app.data.state.AnalyticsState
import com.kamerstay.app.data.state.CheckInState
import com.kamerstay.app.data.state.CheckOutState
import com.kamerstay.app.data.state.ManageHotelState
import com.kamerstay.app.data.state.ManagerPersonalInfoState
import com.kamerstay.app.data.state.ManagerSettingsState
import com.kamerstay.app.data.state.RegisterHotelState
import com.kamerstay.app.data.state.RevenueReportState
import com.kamerstay.app.data.state.RoomFormState
import com.kamerstay.app.data.state.SupportState
import com.kamerstay.app.data.state.UserSession
import com.kamerstay.app.data.state.VerificationState
import com.kamerstay.app.model.Booking
import kotlinx.coroutines.launch

class ManagerViewModel : ViewModel() {

    private val bookingRepository = BookingRemoteRepository()

    val roomFormState = RoomFormState()
    val checkInState = CheckInState()
    val checkOutState = CheckOutState()
    val addEditStaffState = AddEditStaffState()
    val registerHotelState = RegisterHotelState()
    val manageHotelState = ManageHotelState()
    val analyticsState = AnalyticsState()
    val verificationState = VerificationState()
    val managerPersonalInfoState = ManagerPersonalInfoState()
    val amenitiesState = AmenitiesState()
    val revenueReportState = RevenueReportState()
    val supportState = SupportState()
    val managerSettingsState = ManagerSettingsState()

    // ── Notifications ─────────────────────────
    var todayNotifications by mutableStateOf<List<ManagerNotification>>(ManagerNotificationsMockData.todayNotifications)
        private set

    var earlierNotifications by mutableStateOf<List<ManagerNotification>>(ManagerNotificationsMockData.earlierNotifications)
        private set

    fun markAllNotificationsRead() {
        todayNotifications = todayNotifications.map { it.copy(isRead = true) }
        earlierNotifications = earlierNotifications.map { it.copy(isRead = true) }
    }

    // ── Réservations depuis le vrai backend ───
    var bookings by mutableStateOf<List<Booking>>(emptyList())
        private set

    var isLoadingBookings by mutableStateOf(false)
        private set

    var bookingsError by mutableStateOf<String?>(null)
        private set

    private var allBookings: List<Booking> = emptyList()

    // ── Reservations mock (fallback) ──────────
    var reservations by mutableStateOf<List<Reservation>>(ReservationMockData.reservations)
        private set

    var rooms by mutableStateOf<List<ManagerRoom>>(RoomsMockData.rooms)
        private set

    var arrivals by mutableStateOf<List<CheckInGuest>>(CheckInMockData.arrivals)
        private set

    var departures by mutableStateOf<List<DepartureGuest>>(CheckInMockData.departures)
        private set

    var staffMembers by mutableStateOf<List<StaffMember>>(StaffMockData.staffMembers)
        private set

    init {
        loadBookings()
    }

    // ── Charger les réservations depuis MongoDB ─
    fun loadBookings(hotelId: String? = null) {
        viewModelScope.launch {
            isLoadingBookings = true
            bookingsError = null
            try {
                val result = if (hotelId != null) {
                    bookingRepository.getHotelBookings(hotelId)
                } else {
                    bookingRepository.getAllBookings()
                }
                allBookings = result
                bookings = result
            } catch (e: Exception) {
                bookingsError = "Impossible de charger les réservations."
                // Fallback mock si backend inaccessible
                bookings = emptyList()
            } finally {
                isLoadingBookings = false
            }
        }
    }

    // Statistiques calculées depuis les vraies réservations
    val totalBookings get() = bookings.size
    val confirmedBookings get() = bookings.count { it.bookingStatus.name == "CONFIRMED" }
    val pendingBookings get() = bookings.count { it.bookingStatus.name == "PENDING" }
    val totalRevenue get() = bookings.sumOf { it.totalAmount }

    // ── Fonctions mock (inchangées) ───────────
    fun filterReservations(status: String) {
        reservations = if (status == "All Bookings") {
            ReservationMockData.reservations
        } else {
            ReservationMockData.reservations.filter {
                it.status.equals(status, ignoreCase = true)
            }
        }
    }

    fun searchReservations(query: String) {
        reservations = if (query.isEmpty()) {
            ReservationMockData.reservations
        } else {
            ReservationMockData.reservations.filter {
                it.guestName.contains(query, ignoreCase = true) ||
                        it.roomType.contains(query, ignoreCase = true) ||
                        it.bookingId.contains(query, ignoreCase = true)
            }
        }
    }

    fun filterRooms(status: String) {
        rooms = if (status == "All Rooms") {
            RoomsMockData.rooms
        } else {
            RoomsMockData.rooms.filter {
                it.type.contains(status, ignoreCase = true)
            }
        }
    }

    fun searchArrivals(query: String) {
        arrivals = if (query.isEmpty()) {
            CheckInMockData.arrivals
        } else {
            CheckInMockData.arrivals.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.room.contains(query, ignoreCase = true) ||
                        it.bookingId.contains(query, ignoreCase = true)
            }
        }
    }

    fun searchDepartures(query: String) {
        departures = if (query.isEmpty()) {
            CheckInMockData.departures
        } else {
            CheckInMockData.departures.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.room.contains(query, ignoreCase = true)
            }
        }
    }

    fun searchStaff(query: String) {
        staffMembers = if (query.isEmpty()) {
            StaffMockData.staffMembers
        } else {
            StaffMockData.staffMembers.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.role.contains(query, ignoreCase = true)
            }
        }
    }
}