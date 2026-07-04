package com.kamerstay.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kamerstay.app.core.navigation.NavigationState
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
import com.kamerstay.app.data.state.SharedTravelerState
import com.kamerstay.app.data.state.TravelerPaymentMethodsState
import com.kamerstay.app.data.state.TravelerPersonalInfoState
import com.kamerstay.app.data.state.TravelerSettingsState
import com.kamerstay.app.data.state.TravelerSupportState
import com.kamerstay.app.data.state.WishlistState
import com.kamerstay.app.data.state.WriteReviewState
import com.kamerstay.app.model.Hotel
import kotlinx.coroutines.launch

// Mapping clé de recherche → nom canonique (pour normaliser les variantes sans accent)
private val CITY_NORMALIZER = mapOf(
    "douala"      to "Douala",
    "yaoundé"     to "Yaoundé",
    "yaounde"     to "Yaoundé",
    "kribi"       to "Kribi",
    "limbé"       to "Limbé",
    "limbe"       to "Limbé",
    "bafoussam"   to "Bafoussam",
    "garoua"      to "Garoua",
    "maroua"      to "Maroua",
    "ngaoundéré"  to "Ngaoundéré",
    "ngaoundere"  to "Ngaoundéré",
    "bertoua"     to "Bertoua",
    "ebolowa"     to "Ebolowa",
    "kumba"       to "Kumba",
    "bamenda"     to "Bamenda",
    "buea"        to "Buea"
)

private val CAMEROON_CITIES = CITY_NORMALIZER.keys.toList()

class TravelerViewModel(private val shared: SharedTravelerState) : ViewModel() {

    private val hotelRepository      = HotelRemoteRepository()
    private val bookingRepository    = BookingRemoteRepository()
    private val paymentRepository    = com.kamerstay.app.data.remote.PaymentRemoteRepository()
    private val reviewRepository     = com.kamerstay.app.data.remote.ReviewRemoteRepository()
    private val aiRepository         = AiRemoteRepository()
    private val authRepository       = com.kamerstay.app.data.remote.AuthRemoteRepository()
    private val landmarkRepository   = com.kamerstay.app.data.remote.LandmarkRemoteRepository()

    val aiConciergeState = AiConciergeState()

    // Shared across all screens via Koin singleton
    val searchState get() = shared.searchState
    val filterState get() = shared.filterState

    val bookingState = BookingState()
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

    // ── Hotels — délégués au singleton partagé ────────────────────────
    val hotels get() = shared.hotels
    val isLoadingHotels get() = shared.isLoadingHotels
    val hotelsError get() = shared.hotelsError
    val displayedHotels get() = shared.displayedHotels
    val priceSortAscending get() = shared.priceSortAscending

    fun togglePriceSort() = shared.togglePriceSort()

    var selectedHotel by mutableStateOf<Hotel?>(null)
        private set

    var hotelRooms by mutableStateOf<List<com.kamerstay.app.model.Room>>(emptyList())
        private set

    var isLoadingHotelDetail by mutableStateOf(false)
        private set

    var upcomingBookings by mutableStateOf<List<Booking>>(emptyList())
        private set

    var pastBookings by mutableStateOf<List<Booking>>(emptyList())
        private set

    var cancelledBookings by mutableStateOf<List<Booking>>(emptyList())
        private set

    var isLoadingBookings by mutableStateOf(false)
        private set

    var isChangingPassword by mutableStateOf(false)
        private set

    var changePasswordError by mutableStateOf<String?>(null)
        private set

    // Chargement initial
    init {
        loadHotels()
        loadUserBookings()
        loadChatHistory()
    }

    fun loadUserBookings() {
        viewModelScope.launch {
            isLoadingBookings = true
            try {
                val rawBookings = bookingRepository.getMyBookings()
                val gradientPalette = listOf(
                    listOf(androidx.compose.ui.graphics.Color(0xFF0D3A5C), androidx.compose.ui.graphics.Color(0xFF1A6B8A)),
                    listOf(androidx.compose.ui.graphics.Color(0xFF1A3A1A), androidx.compose.ui.graphics.Color(0xFF0D2A0D)),
                    listOf(androidx.compose.ui.graphics.Color(0xFF3A1A2A), androidx.compose.ui.graphics.Color(0xFF2A0D1A)),
                    listOf(androidx.compose.ui.graphics.Color(0xFF1A2A3A), androidx.compose.ui.graphics.Color(0xFF0D1A28)),
                )
                fun gradientFor(id: String) = gradientPalette[kotlin.math.abs(id.hashCode()) % gradientPalette.size]
                fun formatFcfa(amount: Double) =
                    "${amount.toLong().toString().reversed().chunked(3).joinToString(" ").reversed()} FCFA"

                val uiBookings = rawBookings.map { b ->
                    val hotelName = b.hotel?.name ?: "Hôtel"
                    val city      = b.hotel?.city ?: ""
                    val location  = if (city.isNotBlank()) "$city, Cameroun" else "Cameroun"
                    val uiStatus  = when (b.bookingStatus.name) {
                        "CONFIRMED"              -> BookingStatus.CONFIRMED
                        "CHECKED_IN"             -> BookingStatus.CONFIRMED
                        "CHECKED_OUT", "COMPLETED" -> BookingStatus.PAST
                        "CANCELLED"              -> BookingStatus.CANCELLED
                        else                     -> BookingStatus.UPCOMING
                    }
                    Booking(
                        id            = b.id,
                        hotelName     = hotelName,
                        location      = location,
                        checkIn       = b.checkInDate,
                        checkOut      = b.checkOutDate,
                        totalPrice    = formatFcfa(b.totalAmount),
                        status        = uiStatus,
                        gradientColors = gradientFor(b.id),
                        imageUrl      = b.hotel?.imageUrls?.firstOrNull() ?: ""
                    )
                }
                upcomingBookings  = uiBookings.filter { it.status in listOf(BookingStatus.CONFIRMED, BookingStatus.UPCOMING) }
                pastBookings      = uiBookings.filter { it.status == BookingStatus.PAST }
                cancelledBookings = uiBookings.filter { it.status == BookingStatus.CANCELLED }
            } catch (_: Exception) {
                // Garder les listes vides — l'UI affiche un état vide
            } finally {
                isLoadingBookings = false
            }
        }
    }

    fun changePassword(
        currentPassword: String,
        newPassword: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            isChangingPassword = true
            changePasswordError = null
            try {
                authRepository.changePassword(currentPassword, newPassword)
                onSuccess()
            } catch (e: Exception) {
                val msg = e.message?.let {
                    if (it.contains("incorrect", ignoreCase = true) || it.contains("actuel", ignoreCase = true))
                        "Mot de passe actuel incorrect."
                    else "Impossible de mettre à jour le mot de passe. Vérifiez votre connexion."
                } ?: "Impossible de mettre à jour le mot de passe."
                changePasswordError = msg
                onError(msg)
            } finally {
                isChangingPassword = false
            }
        }
    }

    fun loadHotels() {
        if (shared.allHotels.isNotEmpty()) return  // Déjà chargé dans le singleton, on ne recharge pas
        viewModelScope.launch {
            shared.isLoadingHotels = true
            shared.hotelsError = null
            try {
                val result = hotelRepository.getAllHotels()
                shared.allHotels = result
                // Re-apply active search query after load; otherwise the query gets overwritten
                if (shared.searchState.query.isNotEmpty()) {
                    shared.searchHotels()
                } else {
                    shared.hotels = result
                }
            } catch (e: Exception) {
                shared.hotelsError = "Impossible de charger les hôtels. Vérifiez votre connexion."
            } finally {
                shared.isLoadingHotels = false
            }
        }
    }

    fun selectHotel(hotelId: String) {
        selectedHotel = shared.allHotels.find { it.id == hotelId }
            ?: shared.hotels.find { it.id == hotelId }
    }

    fun loadHotelDetail(hotelId: String) {
        // Chemin rapide : hôtel déjà en mémoire
        val cached = shared.allHotels.find { it.id == hotelId } ?: shared.hotels.find { it.id == hotelId }
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

    fun searchHotels() = shared.searchHotels()

    var createdBooking by mutableStateOf<SharedBooking?>(null)
        private set

    var isCreatingBooking by mutableStateOf(false)
        private set

    var bookingError by mutableStateOf<String?>(null)
        private set

    // ── AI Concierge ──────────────────────────────────────────
    fun sendConciergeMessage(message: String) {
        // Capture l'historique AVANT d'ajouter le placeholder streaming
        val history      = aiConciergeState.historyForApi()
        val hotelsCtx    = buildHotelsContext(message)
        viewModelScope.launch {
            aiConciergeState.isTyping = true
            aiConciergeState.beginStreamingMessage()
            try {
                var firstChunk = true
                aiRepository.streamMessage(
                    ConciergeRequest(
                        message       = message,
                        history       = history,
                        userName      = UserSession.fullName.ifBlank { null },
                        userContext   = buildUserContext(),
                        hotelsContext = hotelsCtx
                    )
                ).collect { chunk ->
                    if (firstChunk) {
                        aiConciergeState.isTyping = false
                        firstChunk = false
                    }
                    aiConciergeState.appendStreamingChunk(chunk)
                }
                aiConciergeState.finalizeStreamingMessage()
                // Extraire les critères du dernier message de Kamsa (sans appel API)
                val lastKamsaMessage = aiConciergeState.messages
                    .lastOrNull { it.role == "assistant" && !it.isWelcome && !it.isError }
                    ?.content ?: ""
                extractCriteriaFromText(lastKamsaMessage)?.let {
                    aiConciergeState.extractedCriteria = it
                }
                saveChatHistory()
            } catch (_: Exception) {
                aiConciergeState.finalizeStreamingMessage()
                aiConciergeState.addAssistantMessage(
                    "Désolé, je ne peux pas vous répondre pour l'instant. Vérifiez votre connexion.",
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

    private fun buildHotelsContext(currentMessage: String): String? {
        val hotels = shared.allHotels.ifEmpty { return null }

        // Détecte la ville mentionnée dans le message ou les critères extraits
        val mentionedCity = aiConciergeState.extractedCriteria?.city?.lowercase()
            ?: CAMEROON_CITIES.firstOrNull { currentMessage.contains(it, ignoreCase = true) }

        val relevantHotels = if (mentionedCity != null) {
            hotels.filter { it.city.lowercase().contains(mentionedCity) }
                .ifEmpty { hotels } // fallback sur tous si aucun match
        } else {
            hotels
        }

        if (relevantHotels.isEmpty()) return null

        return buildString {
            appendLine("HÔTELS RÉELS DISPONIBLES SUR KAMERSTAY (données live, ne les invente pas) :")
            relevantHotels
                .sortedWith(compareBy({ it.city }, { -it.rating }))
                .groupBy { it.city }
                .forEach { (city, cityHotels) ->
                    appendLine("── $city ──")
                    cityHotels.take(8).forEach { h ->
                        val rating = if (h.rating > 0) {
                            val t = (h.rating * 10).toInt()
                            "★${t / 10}.${t % 10}"
                        } else "non noté"
                        val price = "${formatFcfa(h.pricePerNight)} FCFA/nuit"
                        val amenities = h.amenities.take(4).joinToString(", ")
                            .ifBlank { "équipements standard" }
                        appendLine("• [ID:${h.id}] ${h.name} | $rating | $price | ${h.address.ifBlank { city }} | $amenities")
                    }
                }
            append("Important : utilise les noms et prix exacts ci-dessus. Ne mentionne que des hôtels de cette liste.")
        }.trim()
    }

    // ── Extraction de critères côté client (après streaming) ──────────────────
    // Pas d'appel API supplémentaire — analyse le texte de Kamsa localement.

    private fun extractCriteriaFromText(text: String): com.kamerstay.app.data.model.SearchCriteria? {
        val lower = text.lowercase()

        // Ville
        val city = CITY_NORMALIZER.entries.firstOrNull { (key, _) -> lower.contains(key) }?.value

        // Budget — ex : "50 000 FCFA", "50.000 FCFA", "50000 FCFA", "50 000 XAF"
        val budget = extractBudget(text)

        // Type de voyage
        val travelType = when {
            lower.contains("famille") || lower.contains("family") || lower.contains("enfant") -> "family"
            lower.contains("business") || lower.contains("affaires") || lower.contains("professionnel") -> "business"
            lower.contains("couple") || lower.contains("romantique") || lower.contains("lune de miel") -> "couple"
            lower.contains(" solo") || lower.contains("seul ") || lower.contains("alone") -> "solo"
            else -> null
        }

        if (city == null && budget == null && travelType == null) return null

        // Fusionne avec les critères déjà extraits pour ne pas les effacer au fil de la conversation
        val existing = aiConciergeState.extractedCriteria
        return com.kamerstay.app.data.model.SearchCriteria(
            city       = city       ?: existing?.city,
            budgetFcfa = budget     ?: existing?.budgetFcfa,
            travelType = travelType ?: existing?.travelType,
            checkIn    = existing?.checkIn,
            checkOut   = existing?.checkOut,
            amenities  = existing?.amenities ?: emptyList()
        )
    }

    private fun extractBudget(text: String): Int? {
        // Cherche un nombre suivi de FCFA / XAF / CFA / francs (avec espaces et points comme séparateurs)
        val pattern = Regex("""(\d[\d\s.]{0,10}\d|\d)\s*(?:FCFA|XAF|CFA|francs?)""", RegexOption.IGNORE_CASE)
        val raw = pattern.find(text)?.groupValues?.get(1)
            ?.filter { it.isDigit() }
            ?: return null
        val value = raw.toLongOrNull() ?: return null
        // Garde seulement des montants plausibles (1 000 à 10 000 000 FCFA)
        return if (value in 1_000L..10_000_000L) value.toInt() else null
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

    fun reloadChatHistoryForUser() {
        aiConciergeState.reset()
        val json = ChatHistoryStore.load()
        if (!json.isNullOrBlank()) {
            aiConciergeState.restoreMessages(json)
        }
    }

    private fun saveChatHistory() {
        ChatHistoryStore.save(aiConciergeState.serializeMessages())
    }

    fun initiatePayment(
        amount: Double,
        description: String,
        bookingId: String = createdBooking?.id ?: "",
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
                // Mode simulation : succès immédiat sans polling
                if (response.reference.startsWith("SIM-")) {
                    state.isLoading = false
                    state.isSuccess = true
                    onSuccess()
                    return@launch
                }
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

    fun populateBookingReview() {
        val hotel = selectedHotel ?: shared.allHotels.find { it.id == NavigationState.selectedHotelId } ?: return
        val room  = hotelRooms.find { it.id == NavigationState.selectedRoomId } ?: hotelRooms.firstOrNull()
        val state = bookingState
        val rev   = bookingReviewState

        val nights  = state.nights.takeIf { it > 0 } ?: 1
        val price   = room?.pricePerNight ?: hotel.pricePerNight
        val roomTotal = price * nights
        val service = roomTotal * 0.05
        val taxes   = roomTotal * 0.085

        rev.fullName        = UserSession.fullName.ifBlank { state.contactName }
        rev.email           = UserSession.email.ifBlank { state.contactEmail }
        rev.phone           = UserSession.phone.ifBlank { state.contactPhone }
        rev.specialRequests = state.specialRequests

        rev.booking = com.kamerstay.app.data.model.BookingReviewData(
            hotelName    = hotel.name,
            location     = hotel.city,
            rating       = hotel.rating,
            amenities    = hotel.amenities.take(3),
            checkIn      = state.checkInDisplay,
            checkOut     = state.checkOutDisplay,
            nights       = nights,
            guests       = state.guestCount,
            rooms        = 1,
            roomType     = room?.type?.name?.lowercase()?.replaceFirstChar { it.uppercase() } ?: "Chambre standard",
            roomDetail   = room?.description?.take(60) ?: "",
            pricePerNight = price,
            serviceFee   = service,
            taxesFees    = taxes
        )
    }

    fun createBooking(onSuccess: () -> Unit, onError: (String) -> Unit = {}) {
        val hotel = selectedHotel
            ?: shared.allHotels.find { it.id == NavigationState.selectedHotelId }
            ?: run { onError("Aucun hôtel sélectionné."); return }
        // Ensure selectedHotel is set so PaymentScreen can access it
        if (selectedHotel == null) selectedHotel = hotel
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