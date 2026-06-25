package com.kamerstay.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kamerstay.app.core.navigation.NavigationState
import com.kamerstay.app.data.mock.BookingsMockData
import com.kamerstay.app.data.mock.MockData
import com.kamerstay.app.data.mock.NotificationsMockData
import com.kamerstay.app.data.model.AppNotification
import com.kamerstay.app.data.model.Booking
import com.kamerstay.app.data.model.ConciergeRequest
import com.kamerstay.app.data.model.BookingStatus
import com.kamerstay.app.data.remote.AiRemoteRepository
import com.kamerstay.app.data.remote.BookingRemoteRepository
import com.kamerstay.app.data.store.ChatHistoryStore
import com.kamerstay.app.model.Booking as SharedBooking
import com.kamerstay.app.model.enums.PaymentMethod
import com.kamerstay.app.data.remote.HotelRemoteRepository
import com.kamerstay.app.data.state.AiConciergeState
import com.kamerstay.app.data.state.UserSession
import com.kamerstay.app.data.state.BookingReviewState
import com.kamerstay.app.data.state.BookingState
import com.kamerstay.app.data.state.CancellationState
import com.kamerstay.app.data.state.FilterState
import com.kamerstay.app.data.state.MapState
import com.kamerstay.app.data.state.NoResultState
import com.kamerstay.app.data.state.PaymentFailedState
import com.kamerstay.app.data.state.PaymentMethodsState
import com.kamerstay.app.data.state.PaymentState
import com.kamerstay.app.data.state.ReviewState
import com.kamerstay.app.data.state.SearchState
import com.kamerstay.app.data.state.TravelerPaymentMethodsState
import com.kamerstay.app.data.state.TravelerPersonalInfoState
import com.kamerstay.app.data.state.TravelerSettingsState
import com.kamerstay.app.data.state.TravelerSupportState
import com.kamerstay.app.data.state.WishlistState
import com.kamerstay.app.data.state.WriteReviewState
import com.kamerstay.app.model.Hotel
import kotlinx.coroutines.launch

class TravelerViewModel : ViewModel() {

    private val hotelRepository = HotelRemoteRepository()
    private val bookingRepository = BookingRemoteRepository()
    private val aiRepository = AiRemoteRepository()

    val aiConciergeState = AiConciergeState()

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

    // ── Hotels (chargés depuis le vrai backend) ────────────────────────
    var hotels by mutableStateOf<List<Hotel>>(emptyList())
        private set

    var isLoadingHotels by mutableStateOf(false)
        private set

    var hotelsError by mutableStateOf<String?>(null)
        private set

    // Cache local pour la recherche (tous les hôtels chargés)
    private var allHotels: List<Hotel> = emptyList()

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

    // Chargement initial des hôtels au démarrage du ViewModel
    init {
        loadHotels()
        loadChatHistory()
    }

    fun loadHotels() {
        viewModelScope.launch {
            isLoadingHotels = true
            hotelsError = null
            try {
                val result = hotelRepository.getAllHotels()
                allHotels = result
                hotels = result
            } catch (e: Exception) {
                hotelsError = "Impossible de charger les hôtels. Vérifiez votre connexion."
                // Fallback sur MockData si le backend est inaccessible
                // hotels = MockData.hotels
            } finally {
                isLoadingHotels = false
            }
        }
    }

    fun selectHotel(hotelId: String) {
        selectedHotel = allHotels.find { it.id == hotelId }
            ?: hotels.find { it.id == hotelId }
    }

    fun searchHotels() {
        hotels = if (searchState.query.isEmpty()) {
            allHotels
        } else {
            allHotels.filter {
                it.name.contains(searchState.query, ignoreCase = true) ||
                        it.city.contains(searchState.query, ignoreCase = true) ||
                        it.address.contains(searchState.query, ignoreCase = true)
            }
        }
    }

    var createdBooking by mutableStateOf<SharedBooking?>(null)
        private set

    var isCreatingBooking by mutableStateOf(false)
        private set

    var bookingError by mutableStateOf<String?>(null)
        private set

    // ── AI Concierge ──────────────────────────────────────────
    fun sendConciergeMessage(message: String) {
        viewModelScope.launch {
            aiConciergeState.isTyping = true
            try {
                val response = aiRepository.sendMessage(
                    ConciergeRequest(
                        message = message,
                        history = aiConciergeState.historyForApi(),
                        userName = UserSession.fullName.ifBlank { null },
                        userContext = buildUserContext()
                    )
                )
                aiConciergeState.addAssistantMessage(response.message)
                response.criteria?.let { criteria ->
                    if (criteria.hasContent()) aiConciergeState.extractedCriteria = criteria
                }
                saveChatHistory()
            } catch (_: Exception) {
                aiConciergeState.addAssistantMessage(
                    "Désolé, je ne peux pas vous répondre pour l'instant. Vérifiez votre connexion et réessayez.",
                    isError = true
                )
            } finally {
                aiConciergeState.isTyping = false
            }
        }
    }

    private fun buildUserContext(): String {
        val upcoming = upcomingBookings.filter { it.status == BookingStatus.UPCOMING || it.status == BookingStatus.CONFIRMED }
        val past = pastBookings.filter { it.status == BookingStatus.PAST }
        if (upcoming.isEmpty() && past.isEmpty()) return ""
        return buildString {
            if (upcoming.isNotEmpty()) {
                appendLine("Réservations à venir :")
                upcoming.forEach { b ->
                    appendLine("- ${b.hotelName} (${b.location}) — check-in : ${b.checkIn}, check-out : ${b.checkOut} [${b.status.name}]")
                }
            }
            if (past.isNotEmpty()) {
                appendLine("Séjours passés :")
                past.take(3).forEach { b ->
                    appendLine("- ${b.hotelName} (${b.location}) — check-in : ${b.checkIn} [PASSÉ]")
                }
            }
        }.trim()
    }

    fun checkProactiveMessage() {
        if (aiConciergeState.hasShownProactive) return
        val nextBooking = upcomingBookings.firstOrNull {
            it.status == BookingStatus.UPCOMING || it.status == BookingStatus.CONFIRMED
        } ?: return
        aiConciergeState.addProactiveMessage(
            "Rappel : vous avez une réservation à ${nextBooking.hotelName} du ${nextBooking.checkIn} au ${nextBooking.checkOut}. Besoin d'infos sur le quartier, les transports ou les activités à proximité ?"
        )
    }

    private fun loadChatHistory() {
        val json = ChatHistoryStore.load() ?: return
        aiConciergeState.restoreMessages(json)
    }

    private fun saveChatHistory() {
        ChatHistoryStore.save(aiConciergeState.serializeMessages())
    }

    fun createBooking(onSuccess: () -> Unit) {
        val hotel = selectedHotel
            ?: MockData.hotels.find { it.id == NavigationState.selectedHotelId }
            ?: MockData.hotels.first()
        val state = bookingState

        viewModelScope.launch {
            isCreatingBooking = true
            bookingError = null
            try {
                val booking = SharedBooking(
                    hotelId = hotel.id,
                    checkInDate = state.checkInDate?.toString() ?: "",
                    checkOutDate = state.checkOutDate?.toString() ?: "",
                    numberOfNights = state.nights.takeIf { it > 0 } ?: 1,
                    totalAmount = (hotel.pricePerNight * (state.nights.takeIf { it > 0 } ?: 1)),
                    depositAmount = (hotel.pricePerNight * (state.nights.takeIf { it > 0 } ?: 1)) * 0.20,
                    remainingAmount = (hotel.pricePerNight * (state.nights.takeIf { it > 0 } ?: 1)) * 0.80,
                    specialRequests = state.specialRequests
                )
                val result = bookingRepository.createBooking(booking)
                createdBooking = result
                onSuccess()
            } catch (e: Exception) {
                onSuccess()
            } finally {
                isCreatingBooking = false
            }
        }
    }
}