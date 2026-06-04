package com.kamerstay.app.viewmodel

import androidx.lifecycle.ViewModel
import com.kamerstay.app.data.state.CheckInState
import com.kamerstay.app.data.state.CheckOutState
import com.kamerstay.app.data.state.RoomFormState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kamerstay.app.data.mock.CheckInMockData
import com.kamerstay.app.data.mock.ReservationMockData
import com.kamerstay.app.data.mock.RoomsMockData
import com.kamerstay.app.data.mock.StaffMockData
import com.kamerstay.app.data.mock.mockReservations
import com.kamerstay.app.data.model.CheckInGuest
import com.kamerstay.app.data.model.DepartureGuest
import com.kamerstay.app.data.model.ManagerRoom
import com.kamerstay.app.data.model.Reservation
import com.kamerstay.app.data.model.StaffMember
import com.kamerstay.app.data.state.AddEditStaffState
import com.kamerstay.app.data.state.AmenitiesState
import com.kamerstay.app.data.state.AnalyticsState
import com.kamerstay.app.data.state.ManageHotelState
import com.kamerstay.app.data.state.ManagerPersonalInfoState
import com.kamerstay.app.data.state.RegisterHotelState
import com.kamerstay.app.data.state.ManagerSettingsState
import com.kamerstay.app.data.state.RevenueReportState
import com.kamerstay.app.data.state.SupportState
import com.kamerstay.app.data.state.VerificationState

class ManagerViewModel : ViewModel() {
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

    // ── Reservations ──────────────────────────
    var reservations by mutableStateOf<List<Reservation>>(ReservationMockData.reservations)
        private set

    var rooms by mutableStateOf<List<ManagerRoom>>(RoomsMockData.rooms)
        private set

    // ── Check-In ──────────────────────────────
    var arrivals by mutableStateOf<List<CheckInGuest>>(CheckInMockData.arrivals)
        private set

    // ── Check-Out ─────────────────────────────
    var departures by mutableStateOf<List<DepartureGuest>>(CheckInMockData.departures)
        private set

    // ── Staff ─────────────────────────────────
    var staffMembers by mutableStateOf<List<StaffMember>>(StaffMockData.staffMembers)
        private set

    // ── Functions ─────────────────────────────

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

    fun searchArrivals(query: String) {    // ← au niveau de la classe ✅
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

    fun searchDepartures(query: String) {  // ← au niveau de la classe ✅
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