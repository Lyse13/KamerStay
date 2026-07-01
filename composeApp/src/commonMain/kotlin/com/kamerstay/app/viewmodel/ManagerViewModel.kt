package com.kamerstay.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kamerstay.app.data.mock.ManagerNotificationsMockData
import com.kamerstay.app.data.model.CheckInGuest
import com.kamerstay.app.data.model.DepartureGuest
import com.kamerstay.app.data.model.ManagerNotification
import com.kamerstay.app.data.model.ManagerRoom
import com.kamerstay.app.data.model.Reservation
import com.kamerstay.app.data.model.StaffMember
import com.kamerstay.app.data.model.StaffStatus
import com.kamerstay.app.data.model.PricingRequest
import com.kamerstay.app.data.remote.AiRemoteRepository
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
    private val hotelRepository   = com.kamerstay.app.data.remote.HotelRemoteRepository()
    private val staffRepository   = com.kamerstay.app.data.remote.StaffRemoteRepository()
    private val aiRepository      = AiRemoteRepository()
    private val authRepository    = com.kamerstay.app.data.remote.AuthRemoteRepository()

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

    var reservations by mutableStateOf<List<Reservation>>(emptyList())
        private set

    var rooms by mutableStateOf<List<ManagerRoom>>(emptyList())
        private set

    var managerRooms by mutableStateOf<List<com.kamerstay.app.model.Room>>(emptyList())
        private set

    var isLoadingManagerRooms by mutableStateOf(false)
        private set

    var arrivals by mutableStateOf<List<CheckInGuest>>(emptyList())
        private set

    var departures by mutableStateOf<List<DepartureGuest>>(emptyList())
        private set

    var managedHotelId by mutableStateOf("")
        private set

    var managedHotel by mutableStateOf<com.kamerstay.app.model.Hotel?>(null)
        private set

    var isLoadingHotel by mutableStateOf(false)
        private set

    var staffMembers by mutableStateOf<List<StaffMember>>(emptyList())
        private set

    var isLoadingStaff by mutableStateOf(false)
        private set

    private var rawStaffList: List<com.kamerstay.app.model.Staff> = emptyList()

    init {
        loadMyHotel()
    }

    fun loadMyHotel() {
        viewModelScope.launch {
            isLoadingHotel = true
            try {
                val hotel = hotelRepository.getMyHotel()
                if (hotel != null) {
                    managedHotel = hotel
                    managedHotelId = hotel.id
                    manageHotelState.propertyName = hotel.name
                    manageHotelState.city = hotel.city
                    manageHotelState.streetAddress = hotel.address
                    manageHotelState.description = hotel.description
                    loadBookings(hotel.id)
                    loadManagerRooms(hotel.id)
                    loadStaff()
                } else {
                    loadBookings()
                }
            } catch (_: Exception) {
                loadBookings()
            } finally {
                isLoadingHotel = false
            }
        }
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
                updateCheckInOutLists(result)
                result.firstOrNull()?.hotelId?.let { if (it.isNotBlank()) managedHotelId = it }
            } catch (e: Exception) {
                bookingsError = "Impossible de charger les réservations."
                bookings = emptyList()
            } finally {
                isLoadingBookings = false
            }
        }
    }

    // ── Réservations UI ───────────────────────
    var selectedReservationDetail by mutableStateOf<com.kamerstay.app.data.model.ReservationDetail?>(null)
        private set

    val reservationsList: List<com.kamerstay.app.data.model.Reservation>
        get() = bookings.map { it.toReservation() }

    fun selectReservation(bookingId: String) {
        selectedReservationDetail = bookings.find { it.id == bookingId }?.toReservationDetail()
    }

    fun cancelReservation(bookingId: String, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                bookingRepository.updateBookingStatus(bookingId, "CANCELLED")
                bookings = bookings.map {
                    if (it.id == bookingId) it.copy(bookingStatus = com.kamerstay.app.model.enums.BookingStatus.CANCELLED) else it
                }
                selectedReservationDetail = selectedReservationDetail?.copy(status = "Annulé")
                onDone()
            } catch (_: Exception) { /* ignorer */ }
        }
    }

    fun approveBooking(bookingId: String, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                bookingRepository.updateBookingStatus(bookingId, "CONFIRMED")
                bookings = bookings.map {
                    if (it.id == bookingId) it.copy(bookingStatus = com.kamerstay.app.model.enums.BookingStatus.CONFIRMED) else it
                }
                selectedReservationDetail = selectedReservationDetail?.copy(status = "Confirmé")
                onDone()
            } catch (_: Exception) { /* ignorer */ }
        }
    }

    private fun Booking.toReservation(): com.kamerstay.app.data.model.Reservation {
        val colors = listOf(
            listOf(androidx.compose.ui.graphics.Color(0xFF1A2A3A), androidx.compose.ui.graphics.Color(0xFF0D1A28)),
            listOf(androidx.compose.ui.graphics.Color(0xFF1A3A2E), androidx.compose.ui.graphics.Color(0xFF0D2218)),
            listOf(androidx.compose.ui.graphics.Color(0xFF2A1A3A), androidx.compose.ui.graphics.Color(0xFF1A0D28)),
            listOf(androidx.compose.ui.graphics.Color(0xFF3A2A1A), androidx.compose.ui.graphics.Color(0xFF281A0D)),
        )
        val ref = bookingReference.ifBlank { id.take(8) }
        val uiStatus = mapBookingStatus()
        return com.kamerstay.app.data.model.Reservation(
            id             = this.id,
            guestName      = hotel?.name ?: "Hôtel",
            guestInitials  = ref.take(2).uppercase(),
            bookingId      = "#$ref",
            roomType       = room?.type?.name?.lowercase()?.replaceFirstChar { it.uppercase() } ?: "Chambre",
            roomTag        = room?.roomNumber ?: "",
            checkIn        = checkInDate,
            checkOut       = checkOutDate,
            nights         = numberOfNights,
            status         = uiStatus,
            gradientColors = colors[kotlin.math.abs(id.hashCode()) % colors.size]
        )
    }

    private fun Booking.toReservationDetail(): com.kamerstay.app.data.model.ReservationDetail {
        val ref = bookingReference.ifBlank { id.take(8).uppercase() }
        val charge      = totalAmount * 0.932
        val fees        = totalAmount * 0.068
        val payUiStatus = when (paymentStatus.name) {
            "FULLY_PAID"   -> "Payé intégralement"
            "DEPOSIT_PAID" -> "Acompte versé"
            else           -> "En attente de paiement"
        }
        return com.kamerstay.app.data.model.ReservationDetail(
            id              = this.id,
            guestName       = hotel?.name ?: "Hôtel",
            guestInitials   = ref.take(2).uppercase(),
            reservationId   = ref,
            status          = mapBookingStatus(),
            checkIn         = checkInDate,
            checkOut        = checkOutDate,
            nights          = numberOfNights,
            roomName        = hotel?.name ?: "Hôtel",
            roomDetails     = room?.let { "${it.type.name.lowercase().replaceFirstChar { c -> c.uppercase() }} • Chambre ${it.roomNumber}" } ?: "Chambre réservée",
            phoneNumber     = hotel?.phoneNumber ?: "",
            email           = hotel?.email ?: "",
            membershipTier  = "Standard",
            specialRequests = specialRequests.ifBlank { "Aucune demande spéciale." },
            requestTags     = emptyList(),
            roomCharge      = "${fcfaFmt(charge)} FCFA",
            serviceFees     = "${fcfaFmt(fees)} FCFA",
            amenitiesAddOn  = "0 FCFA",
            totalAmount     = "${fcfaFmt(totalAmount)} FCFA",
            paymentStatus   = payUiStatus,
            cardInfo        = "Mobile Money"
        )
    }

    private fun Booking.mapBookingStatus(): String = when (bookingStatus.name) {
        "CONFIRMED"   -> "Confirmé"
        "CHECKED_IN"  -> "En cours"
        "CHECKED_OUT", "COMPLETED" -> "Terminé"
        "CANCELLED"   -> "Annulé"
        else          -> "En attente"
    }

    private fun fcfaFmt(amount: Double): String =
        amount.toLong().toString().reversed().chunked(3).joinToString(" ").reversed()

    private fun updateCheckInOutLists(all: List<Booking>) {
        allArrivals = all
            .filter { it.bookingStatus.name in listOf("CONFIRMED", "PENDING") }
            .map { it.toCheckInGuest() }
        arrivals = allArrivals

        allDepartures = all
            .filter { it.bookingStatus.name == "CHECKED_IN" }
            .map { it.toDepartureGuest() }
        departures = allDepartures
    }

    private fun Booking.toCheckInGuest(): CheckInGuest {
        val roomInfo = room?.let {
            "${it.type.name.lowercase().replaceFirstChar { c -> c.uppercase() }} • Chambre ${it.roomNumber}"
        } ?: "Chambre"
        val (tag, tagColor) = when (paymentStatus.name) {
            "PENDING"      -> "Paiement en attente" to androidx.compose.ui.graphics.Color(0xFFE53935)
            "DEPOSIT_PAID" -> "Acompte versé" to androidx.compose.ui.graphics.Color(0xFFFF9800)
            "FULLY_PAID"   -> "Payé intégralement" to androidx.compose.ui.graphics.Color(0xFF00D5E1)
            else           -> null to androidx.compose.ui.graphics.Color.Transparent
        }
        return CheckInGuest(
            id            = this.id,
            name          = "Rés. ${bookingReference.ifBlank { id.take(8).uppercase() }}",
            bookingId     = "#${bookingReference}",
            room          = roomInfo,
            tag           = tag,
            tagColor      = tagColor,
            arrivalTime   = checkInDate.ifBlank { null },
            paymentPending = paymentStatus.name == "PENDING",
            isCheckedIn   = bookingStatus.name == "CHECKED_IN"
        )
    }

    private fun Booking.toDepartureGuest(): DepartureGuest {
        val ref = bookingReference.takeLast(4).uppercase().ifBlank { id.take(4).uppercase() }
        val isPaid = paymentStatus.name == "FULLY_PAID"
        return DepartureGuest(
            initials     = ref.take(2),
            name         = "Rés. #${bookingReference.ifBlank { id.take(8) }}",
            room         = room?.let { "Chambre ${it.roomNumber} • ${it.type.name}" } ?: "Chambre",
            nights       = "$numberOfNights ${if (numberOfNights > 1) "nuits" else "nuit"}",
            dates        = "$checkInDate → $checkOutDate",
            balance      = "${remainingAmount.toInt()} FCFA",
            balanceLabel = if (isPaid) "Soldé" else "Solde dû",
            isPaid       = isPaid,
            bookingId    = this.id
        )
    }

    // ── Check-In ──────────────────────────────
    fun performCheckIn(bookingId: String, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            checkInState.isLoading = true
            try {
                bookingRepository.updateBookingStatus(bookingId, "CHECKED_IN")
                // Mise à jour optimiste locale
                arrivals = arrivals.map {
                    if (it.id == bookingId)
                        it.copy(isCheckedIn = true, checkedInTime = "Arrivée confirmée")
                    else it
                }
                onDone()
            } catch (_: Exception) {
                // Si erreur réseau, on laisse l'état actuel
            } finally {
                checkInState.isLoading = false
            }
        }
    }

    // ── Check-Out ─────────────────────────────
    fun performCheckOut(bookingId: String, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            checkOutState.isLoading = true
            try {
                bookingRepository.updateBookingStatus(bookingId, "CHECKED_OUT")
                // Retirer de la liste des départs
                departures = departures.filter { it.bookingId != bookingId }
                onDone()
            } catch (_: Exception) {
                // Si erreur réseau, on laisse l'état actuel
            } finally {
                checkOutState.isLoading = false
            }
        }
    }

    // Statistiques calculées depuis les vraies réservations
    val totalBookings get() = bookings.size
    val confirmedBookings get() = bookings.count { it.bookingStatus.name == "CONFIRMED" }
    val pendingBookings get() = bookings.count { it.bookingStatus.name == "PENDING" }
    val totalRevenue get() = bookings.sumOf { it.totalAmount }

    // ── Gestion des chambres ─────────────────
    fun loadManagerRooms(hotelId: String) {
        viewModelScope.launch {
            isLoadingManagerRooms = true
            try {
                managerRooms = hotelRepository.getRoomsForHotel(hotelId)
            } catch (_: Exception) { /* garder l'état actuel */ }
            finally { isLoadingManagerRooms = false }
        }
    }

    fun loadRoomForEdit(roomId: String) {
        val room = managerRooms.find { it.id == roomId } ?: return
        with(roomFormState) {
            editingRoomId = room.id
            roomNumber    = room.roomNumber
            category      = room.type.name.lowercase().replaceFirstChar { it.uppercase() }
            pricePerNight = room.pricePerNight.toInt().toString()
            capacity      = room.capacity.toString()
            floor         = room.floor.toString()
            size          = room.size.toInt().toString()
            description   = room.description
            selectedFeatures = room.features.toSet()
        }
    }

    fun saveRoom(hotelId: String, onSuccess: () -> Unit) {
        val s = roomFormState
        if (s.roomNumber.isBlank()) { s.error = "Numéro de chambre requis."; return }
        val price = s.pricePerNight.replace(",", ".").toDoubleOrNull()
        if (price == null || price <= 0) { s.error = "Prix invalide."; return }

        viewModelScope.launch {
            s.isLoading = true; s.error = null
            try {
                val roomType = categoryToRoomType(s.category)
                val room = com.kamerstay.app.model.Room(
                    id           = s.editingRoomId ?: "",
                    hotelId      = hotelId,
                    roomNumber   = s.roomNumber.trim(),
                    type         = roomType,
                    status       = com.kamerstay.app.model.enums.RoomStatus.AVAILABLE,
                    pricePerNight = price,
                    capacity     = s.capacity.toIntOrNull() ?: 2,
                    floor        = s.floor.toIntOrNull() ?: 1,
                    size         = s.size.toDoubleOrNull() ?: 0.0,
                    description  = s.description.trim(),
                    features     = s.selectedFeatures.toList()
                )
                if (s.editingRoomId != null) {
                    hotelRepository.updateRoom(hotelId, room)
                    managerRooms = managerRooms.map { if (it.id == room.id) room else it }
                } else {
                    val created = hotelRepository.createRoomForHotel(hotelId, room)
                    managerRooms = managerRooms + created
                }
                s.isSuccess = true
                s.reset()
                onSuccess()
            } catch (_: Exception) {
                s.error = "Impossible d'enregistrer la chambre. Vérifiez votre connexion."
            } finally {
                s.isLoading = false
            }
        }
    }

    fun deleteRoom(hotelId: String, roomId: String) {
        viewModelScope.launch {
            try {
                hotelRepository.deleteRoom(hotelId, roomId)
                managerRooms = managerRooms.filter { it.id != roomId }
            } catch (_: Exception) { /* ignorer */ }
        }
    }

    private fun categoryToRoomType(category: String): com.kamerstay.app.model.enums.RoomType =
        when (category.lowercase()) {
            "suite", "executive suite", "presidential suite", "deluxe suite" -> com.kamerstay.app.model.enums.RoomType.SUITE
            "deluxe" -> com.kamerstay.app.model.enums.RoomType.DELUXE
            "family room" -> com.kamerstay.app.model.enums.RoomType.FAMILY
            "twin" -> com.kamerstay.app.model.enums.RoomType.TWIN
            "double" -> com.kamerstay.app.model.enums.RoomType.DOUBLE
            else -> com.kamerstay.app.model.enums.RoomType.SINGLE
        }

    private fun com.kamerstay.app.model.Room.toManagerRoom(): ManagerRoom {
        val colors = listOf(
            listOf(androidx.compose.ui.graphics.Color(0xFF1A2A3A), androidx.compose.ui.graphics.Color(0xFF0D1A28)),
            listOf(androidx.compose.ui.graphics.Color(0xFF1A3A2E), androidx.compose.ui.graphics.Color(0xFF0D2218)),
            listOf(androidx.compose.ui.graphics.Color(0xFF2A2A3A), androidx.compose.ui.graphics.Color(0xFF1A1A28)),
            listOf(androidx.compose.ui.graphics.Color(0xFF1A2E3A), androidx.compose.ui.graphics.Color(0xFF0D1E28)),
        )
        return ManagerRoom(
            id             = this.id,
            number         = this.roomNumber,
            type           = this.type.name.lowercase().replaceFirstChar { it.uppercase() },
            description    = "${this.description.take(40)}${if (this.description.length > 40) "…" else ""}",
            price          = this.pricePerNight.toInt(),
            status         = this.status,
            gradientColors = colors[kotlin.math.abs(this.id.hashCode()) % colors.size]
        )
    }

    val managerRoomsUi: List<ManagerRoom>
        get() = managerRooms.map { it.toManagerRoom() }

    // ── Enregistrement hôtel ─────────────────
    fun registerHotel(onSuccess: () -> Unit) {
        val s = registerHotelState
        if (!s.isValid) {
            s.error = "Nom, ville et prix par nuit sont requis."
            return
        }
        viewModelScope.launch {
            s.isLoading = true
            s.error = null
            try {
                val hotel = com.kamerstay.app.model.Hotel(
                    name          = s.hotelName.trim(),
                    description   = s.description.trim(),
                    address       = s.address.trim(),
                    city          = s.city.trim(),
                    phoneNumber   = s.phoneNumber.ifBlank { UserSession.phone },
                    email         = s.emailContact.ifBlank { UserSession.email },
                    pricePerNight = s.pricePerNight.replace(",", ".").toDoubleOrNull() ?: 0.0,
                    totalRooms    = s.totalRooms.toIntOrNull() ?: 0,
                    amenities     = emptyList(),
                    imageUrls     = emptyList()
                )
                val created = hotelRepository.createHotel(hotel)
                managedHotel = created
                managedHotelId = created.id
                s.isSuccess = true
                onSuccess()
            } catch (_: Exception) {
                s.error = "Impossible d'enregistrer l'hôtel. Vérifiez votre connexion."
            } finally {
                s.isLoading = false
            }
        }
    }

    // ── Staff ────────────────────────────────
    fun loadStaff() {
        viewModelScope.launch {
            isLoadingStaff = true
            try {
                val list = staffRepository.getStaff(managedHotelId)
                rawStaffList = list
                staffMembers = list.map { it.toStaffMember() }
            } catch (_: Exception) { /* garder le mock */ }
            finally { isLoadingStaff = false }
        }
    }

    fun loadStaffForEdit(staffId: String) {
        if (staffId.isBlank()) {
            addEditStaffState.reset()
            return
        }
        val staff = rawStaffList.find { it.id == staffId } ?: return
        with(addEditStaffState) {
            editingStaffId  = staff.id
            fullName        = staff.fullName
            selectedRole    = staff.role.replaceFirstChar { it.uppercase() }
            email           = staff.email
            phone           = staff.phone
            selectedPermission = staff.permission
        }
    }

    fun saveStaff(onSuccess: () -> Unit) {
        val s = addEditStaffState
        if (s.fullName.isBlank() || s.email.isBlank()) {
            s.error = "Nom et email requis."
            return
        }
        viewModelScope.launch {
            s.isLoading = true; s.error = null
            try {
                val staff = com.kamerstay.app.model.Staff(
                    id         = s.editingStaffId ?: "",
                    hotelId    = managedHotelId,
                    fullName   = s.fullName.trim(),
                    role       = s.selectedRole,
                    email      = s.email.trim(),
                    phone      = s.phone.trim(),
                    permission = s.selectedPermission,
                    status     = "ACTIVE",
                    shift      = ""
                )
                if (s.editingStaffId != null) {
                    val updated = staffRepository.updateStaff(staff)
                    rawStaffList = rawStaffList.map { if (it.id == updated.id) updated else it }
                    staffMembers = staffMembers.map { if (it.id == updated.id) updated.toStaffMember() else it }
                } else {
                    val created = staffRepository.createStaff(staff)
                    rawStaffList = rawStaffList + created
                    staffMembers = staffMembers + created.toStaffMember()
                }
                s.reset()
                onSuccess()
            } catch (_: Exception) {
                s.error = if (s.editingStaffId != null)
                    "Impossible de modifier le membre. Vérifiez votre connexion."
                else
                    "Impossible d'ajouter le membre. Vérifiez votre connexion."
            } finally {
                s.isLoading = false
            }
        }
    }

    fun deleteStaffMember(staffId: String) {
        viewModelScope.launch {
            try {
                staffRepository.deleteStaff(staffId)
                staffMembers = staffMembers.filter { it.id != staffId }
            } catch (_: Exception) { /* ignorer */ }
        }
    }

    private fun com.kamerstay.app.model.Staff.toStaffMember(): StaffMember {
        val uiStatus = when (status.uppercase()) {
            "AWAY"     -> StaffStatus.AWAY
            "OFF_DUTY" -> StaffStatus.OFF_DUTY
            else       -> StaffStatus.ACTIVE
        }
        return StaffMember(
            id     = this.id,
            name   = this.fullName,
            role   = this.role.uppercase(),
            shift  = this.shift.ifBlank { "Horaire non défini" },
            status = uiStatus
        )
    }

    // Stats staff dérivées
    val totalStaff get() = staffMembers.size
    val onDutyStaff get() = staffMembers.count { it.status == StaffStatus.ACTIVE }
    val awayStaff get() = staffMembers.count { it.status == StaffStatus.AWAY }

    // ── AI Pricing ────────────────────────────
    fun suggestPricing() {
        viewModelScope.launch {
            roomFormState.isPricingLoading = true
            roomFormState.pricingSuggestion = null
            try {
                val priceInt = roomFormState.pricePerNight.replace(",", "").toDoubleOrNull()?.toInt() ?: 0
                val response = aiRepository.suggestPricing(
                    PricingRequest(
                        hotelName = manageHotelState.propertyName,
                        city = manageHotelState.city,
                        currentOccupancyPercent = 65,
                        currentPricePerNight = priceInt,
                        roomType = roomFormState.category
                    )
                )
                roomFormState.pricingSuggestion = response
            } catch (_: Exception) {
                // Keep existing price, fail silently
            } finally {
                roomFormState.isPricingLoading = false
            }
        }
    }

    // ── Filtrage / recherche sur données réelles ──────────────
    private var allArrivals:   List<CheckInGuest>   = emptyList()
    private var allDepartures: List<DepartureGuest> = emptyList()

    fun searchArrivals(query: String) {
        arrivals = if (query.isEmpty()) allArrivals
        else allArrivals.filter {
            it.name.contains(query, ignoreCase = true) ||
            it.room.contains(query, ignoreCase = true) ||
            it.bookingId.contains(query, ignoreCase = true)
        }
    }

    fun searchDepartures(query: String) {
        departures = if (query.isEmpty()) allDepartures
        else allDepartures.filter {
            it.name.contains(query, ignoreCase = true) ||
            it.room.contains(query, ignoreCase = true)
        }
    }

    fun searchStaff(query: String) {
        staffMembers = if (query.isEmpty()) rawStaffList.map { it.toStaffMember() }
        else rawStaffList.filter {
            it.fullName.contains(query, ignoreCase = true) ||
            it.role.contains(query, ignoreCase = true)
        }.map { it.toStaffMember() }
    }

    fun updateProfile(onSuccess: () -> Unit) {
        val s = managerPersonalInfoState
        if (s.fullName.isBlank()) { s.error = "Le nom ne peut pas être vide."; return }
        viewModelScope.launch {
            s.isLoading = true
            s.error = null
            try {
                authRepository.updateProfile(s.fullName.trim(), s.phoneNumber.trim())
                UserSession.login(
                    name  = s.fullName.trim(),
                    email = UserSession.email,
                    phone = s.phoneNumber.trim(),
                    role  = UserSession.role,
                    token = UserSession.token
                )
                onSuccess()
            } catch (_: Exception) {
                s.error = "Impossible de sauvegarder. Vérifiez votre connexion."
            } finally {
                s.isLoading = false
            }
        }
    }
}