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
import com.kamerstay.app.model.enums.BookingStatus as SharedBookingStatus
import com.kamerstay.app.model.enums.PaymentMethod
import androidx.compose.ui.graphics.Color
import kotlin.math.absoluteValue
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

    private val hotelRepository      = HotelRemoteRepository()
    private val bookingRepository    = BookingRemoteRepository()
    private val paymentRepository    = com.kamerstay.app.data.remote.PaymentRemoteRepository()
    private val reviewRepository     = com.kamerstay.app.data.remote.ReviewRemoteRepository()
    private val aiRepository         = AiRemoteRepository()
    private val authRepository       = com.kamerstay.app.data.remote.AuthRemoteRepository()
    private val landmarkRepository   = com.kamerstay.app.data.remote.LandmarkRemoteRepository()

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

    var hotelRooms by mutableStateOf<List<com.kamerstay.app.model.Room>>(emptyList())
        private set

    var isLoadingHotelDetail by mutableStateOf(false)
        private set

    var upcomingBookings by mutableStateOf<List<Booking>>(BookingsMockData.upcoming)
        private set

    var pastBookings by mutableStateOf<List<Booking>>(BookingsMockData.past)
        private set

    var cancelledBookings by mutableStateOf<List<Booking>>(BookingsMockData.cancelled)
        private set

    var isLoadingBookings by mutableStateOf(false)
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

    fun loadHotelDetail(hotelId: String) {
        // Chemin rapide : hôtel déjà en mémoire
        val cached = allHotels.find { it.id == hotelId } ?: hotels.find { it.id == hotelId }
        if (cached != null) selectedHotel = cached

        viewModelScope.launch {
            isLoadingHotelDetail = true
            try {
                // Si pas en cache, charger depuis l'API
                if (cached == null) {
                    selectedHotel = hotelRepository.getHotelById(hotelId)
                }
                // Toujours charger les chambres depuis l'API
                hotelRooms = hotelRepository.getRoomsForHotel(hotelId)
            } catch (_: Exception) {
                // Garder les données en cache si disponibles
            } finally {
                isLoadingHotelDetail = false
            }
        }
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

    fun initiatePayment(
        amount: Double,
        description: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val state = paymentState
        viewModelScope.launch {
            state.isLoading = true
            state.error = null
            try {
                val response = paymentRepository.initiatePayment(
                    com.kamerstay.app.data.model.PaymentInitRequest(
                        amount      = amount,
                        phone       = state.normalizedPhone,
                        operator    = state.selectedMethod,
                        description = description
                    )
                )
                if (response.reference.isBlank() || response.status == "FAILED" || response.status == "ERROR") {
                    state.isLoading = false
                    onFailure(response.message.ifBlank { "Paiement refusé. Vérifiez votre numéro." })
                    return@launch
                }
                state.paymentReference = response.reference
                state.isLoading  = false
                state.isPolling  = true
                state.statusMessage = "En attente de confirmation sur votre téléphone…"
                // Polling toutes les 5s pendant 5 minutes max
                repeat(60) { attempt ->
                    if (!state.isPolling) return@repeat
                    kotlinx.coroutines.delay(5_000)
                    try {
                        val statusResp = paymentRepository.getPaymentStatus(response.reference)
                        when (statusResp.status) {
                            "SUCCESSFUL" -> {
                                state.isPolling  = false
                                state.isSuccess  = true
                                onSuccess()
                                return@launch
                            }
                            "FAILED" -> {
                                state.isPolling = false
                                onFailure("Paiement échoué ou annulé.")
                                return@launch
                            }
                            else -> {
                                val elapsed = (attempt + 1) * 5
                                state.statusMessage = "En attente… (${elapsed}s)"
                            }
                        }
                    } catch (_: Exception) { /* ignorer les erreurs réseau transitoires */ }
                }
                // Timeout 5 minutes
                state.isPolling = false
                onFailure("Délai dépassé. Si vous avez confirmé le paiement, contactez le support.")
            } catch (e: Exception) {
                state.isLoading = false
                state.isPolling = false
                onFailure("Impossible de joindre le service de paiement. Vérifiez votre connexion.")
            }
        }
    }

    fun cancelPaymentPolling() {
        paymentState.isPolling = false
    }

    // ── Reviews ───────────────────────────────────────────────
    var hotelReviewsList by mutableStateOf<List<com.kamerstay.app.data.model.GuestReview>>(emptyList())
        private set

    var isLoadingReviews by mutableStateOf(false)
        private set

    fun loadReviewsForHotel(hotelId: String) {
        viewModelScope.launch {
            isLoadingReviews = true
            try {
                val reviews = reviewRepository.getReviewsForHotel(hotelId)
                hotelReviewsList = reviews.map { it.toGuestReview() }
            } catch (_: Exception) {
                // Garder la liste vide
            } finally {
                isLoadingReviews = false
            }
        }
    }

    fun submitReview(
        hotelId: String,
        rating: Int,
        comment: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (rating == 0) { onError("Veuillez sélectionner une note."); return }
        if (comment.isBlank()) { onError("Veuillez écrire un commentaire."); return }
        viewModelScope.launch {
            writeReviewState.isLoading = true
            try {
                reviewRepository.submitReview(
                    com.kamerstay.app.model.Review(
                        hotelId      = hotelId,
                        travelerName = UserSession.fullName.ifBlank { "Voyageur" },
                        rating       = rating.toDouble(),
                        comment      = comment
                    )
                )
                writeReviewState.isSubmitted = true
                onSuccess()
            } catch (_: Exception) {
                onError("Impossible d'envoyer l'avis. Vérifiez votre connexion.")
            } finally {
                writeReviewState.isLoading = false
            }
        }
    }

    private fun com.kamerstay.app.model.Review.toGuestReview(): com.kamerstay.app.data.model.GuestReview {
        val colors = listOf(
            androidx.compose.ui.graphics.Color(0xFF8B6914),
            androidx.compose.ui.graphics.Color(0xFF1A5276),
            androidx.compose.ui.graphics.Color(0xFF00D5E1),
            androidx.compose.ui.graphics.Color(0xFF1A3A2E),
            androidx.compose.ui.graphics.Color(0xFF4A235A),
        )
        val initials = travelerName.split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .joinToString("") { it.first().uppercase() }
            .ifBlank { "??" }
        return com.kamerstay.app.data.model.GuestReview(
            id          = this.id,
            name        = travelerName.ifBlank { "Voyageur" },
            initials    = initials,
            avatarColor = colors[kotlin.math.abs(this.id.hashCode()) % colors.size],
            hasPhoto    = false,
            stayType    = "Séjour vérifié",
            date        = createdAt.take(7).replace("-", "/").ifBlank { "" },
            rating      = this.rating,
            comment     = "\"${this.comment}\"",
            hasImages   = false
        )
    }

    // ── Annulation de réservation ─────────────────────────────
    fun loadCancellationData(bookingId: String) {
        val booking = upcomingBookings.find { it.id == bookingId }
            ?: pastBookings.find { it.id == bookingId }
            ?: return  // garder le mock si la réservation est introuvable localement

        val rawAmount = booking.totalPrice
            .replace(" FCFA", "").replace("FCFA", "")
            .replace(" ", "").replace(",", "").replace(".", "")
            .toLongOrNull() ?: 0L
        val depositPaid   = (rawAmount * 0.30).toLong()
        val cancelFee     = (depositPaid * 0.10).toLong()
        val refundAmount  = depositPaid - cancelFee

        cancellationState.summary = com.kamerstay.app.data.model.CancellationSummary(
            bookingId  = "#${bookingId.takeLast(8).uppercase()}",
            hotelName  = booking.hotelName,
            roomType   = "Chambre réservée",
            checkIn    = booking.checkIn,
            checkOut   = booking.checkOut,
            guests     = "Voir réservation",
            totalPaid  = booking.totalPrice
        )
        cancellationState.refund = com.kamerstay.app.data.model.RefundBreakdown(
            totalPaid       = fcfaStr(depositPaid),
            cancellationFee = "- ${fcfaStr(cancelFee)}",
            feeLabel        = "Frais d'annulation (10%)",
            estimatedRefund = fcfaStr(refundAmount),
            refundNote      = "Le remboursement sera traité sous 5 à 7 jours ouvrables sur votre moyen de paiement initial."
        )
    }

    fun cancelBooking(bookingId: String) {
        viewModelScope.launch {
            cancellationState.isLoading = true
            cancellationState.error = null
            try {
                bookingRepository.updateBookingStatus(bookingId, "CANCELLED")
                upcomingBookings = upcomingBookings.filter { it.id != bookingId }
                cancellationState.isConfirmed = true
            } catch (_: Exception) {
                cancellationState.error = "Annulation impossible pour l'instant. Vérifiez votre connexion et réessayez."
            } finally {
                cancellationState.isLoading = false
            }
        }
    }

    private fun fcfaStr(amount: Long): String =
        amount.toString().reversed().chunked(3).joinToString(" ").reversed() + " FCFA"

    fun loadMyBookings() {
        if (UserSession.token.isBlank()) return
        viewModelScope.launch {
            isLoadingBookings = true
            try {
                val all = bookingRepository.getMyBookings().map { it.toUiBooking() }
                upcomingBookings = all.filter {
                    it.status == BookingStatus.UPCOMING || it.status == BookingStatus.CONFIRMED
                }
                pastBookings = all.filter { it.status == BookingStatus.PAST }
                cancelledBookings = all.filter { it.status == BookingStatus.CANCELLED }
            } catch (_: Exception) {
                // Garder les données mock si le backend est inaccessible
            } finally {
                isLoadingBookings = false
            }
        }
    }

    private fun SharedBooking.toUiBooking(): Booking {
        val uiStatus = when (this.bookingStatus) {
            SharedBookingStatus.PENDING      -> BookingStatus.UPCOMING
            SharedBookingStatus.CONFIRMED    -> BookingStatus.CONFIRMED
            SharedBookingStatus.CHECKED_IN   -> BookingStatus.CONFIRMED
            SharedBookingStatus.CHECKED_OUT  -> BookingStatus.PAST
            SharedBookingStatus.COMPLETED    -> BookingStatus.PAST
            SharedBookingStatus.CANCELLED    -> BookingStatus.CANCELLED
        }
        val gradients = listOf(
            listOf(Color(0xFF0D3A5C), Color(0xFF1A2A3A)),
            listOf(Color(0xFF1A3A2E), Color(0xFF0D2218)),
            listOf(Color(0xFF2A1A3A), Color(0xFF1A0D28)),
            listOf(Color(0xFF3A2A1A), Color(0xFF281A0D)),
        )
        return Booking(
            id          = this.id,
            hotelName   = this.hotel?.name ?: "Hôtel",
            location    = this.hotel?.city ?: "",
            checkIn     = this.checkInDate,
            checkOut    = this.checkOutDate,
            totalPrice  = "${formatFcfa(this.totalAmount)} FCFA",
            status      = uiStatus,
            gradientColors = gradients[this.hotelId.hashCode().absoluteValue % gradients.size],
            imageUrl    = this.hotel?.imageUrls?.firstOrNull() ?: ""
        )
    }

    private fun formatFcfa(amount: Double): String =
        amount.toInt().toString().reversed().chunked(3).joinToString(" ").reversed()

    // ── Landmark Search ───────────────────────────────────────
    var landmarks by mutableStateOf<List<com.kamerstay.app.model.Landmark>>(emptyList())
        private set

    var isLoadingLandmarks by mutableStateOf(false)
        private set

    var selectedLandmark by mutableStateOf<com.kamerstay.app.model.Landmark?>(null)
        private set

    var hotelsNearLandmark by mutableStateOf<List<com.kamerstay.app.model.HotelWithDistance>>(emptyList())
        private set

    var isLoadingNearbyHotels by mutableStateOf(false)
        private set

    fun loadLandmarks() {
        if (landmarks.isNotEmpty()) return
        viewModelScope.launch {
            isLoadingLandmarks = true
            try {
                landmarks = landmarkRepository.getAllLandmarks()
            } catch (_: Exception) { /* garder liste vide */ }
            finally { isLoadingLandmarks = false }
        }
    }

    fun searchHotelsNearLandmark(landmark: com.kamerstay.app.model.Landmark) {
        selectedLandmark = landmark
        viewModelScope.launch {
            isLoadingNearbyHotels = true
            hotelsNearLandmark = emptyList()
            try {
                hotelsNearLandmark = landmarkRepository.getHotelsNearLandmark(landmark.id)
            } catch (_: Exception) { hotelsNearLandmark = emptyList() }
            finally { isLoadingNearbyHotels = false }
        }
    }

    fun updateProfile(onSuccess: () -> Unit) {
        val s = travelerPersonalInfoState
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

    fun createBooking(onSuccess: () -> Unit, onError: (String) -> Unit = {}) {
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
                val message = "Impossible de créer la réservation. Vérifiez votre connexion."
                bookingError = message
                onError(message)
            } finally {
                isCreatingBooking = false
            }
        }
    }
}