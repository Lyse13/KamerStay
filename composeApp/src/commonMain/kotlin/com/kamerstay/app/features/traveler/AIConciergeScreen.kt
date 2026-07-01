package com.kamerstay.app.features.traveler

import androidx.compose.animation.core.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import com.kamerstay.app.core.components.MarkdownText
import com.kamerstay.app.data.state.UserSession
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.model.SearchCriteria
import com.kamerstay.app.data.state.getQuickReplies
import com.kamerstay.app.data.state.UiChatMessage
import com.kamerstay.app.viewmodel.TravelerViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.koin.compose.viewmodel.koinViewModel

private val AiGradient = Brush.linearGradient(colors = listOf(Color(0xFF4A00E0), Color(0xFF8E2DE2)))

@Composable
fun AIConciergeScreen(navController: NavController) {
    val viewModel = koinViewModel<TravelerViewModel>()
    val state = viewModel.aiConciergeState
    val messages = state.messages
    val isTyping = state.isTyping
    val criteria = state.extractedCriteria

    // Nombre de messages réels (hors bienvenue/erreurs) — pour détecter le dépassement de fenêtre
    val realMessageCount = messages.count { !it.isWelcome && !it.isError && !it.isStreaming }
    val isContextTruncated = realMessageCount > 21  // > MAX_HISTORY_FOR_API + 1 (message courant)

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Message proactif au premier affichage
    LaunchedEffect(Unit) {
        viewModel.checkProactiveMessage()
    }

    // Scroll to bottom when messages change or typing starts
    LaunchedEffect(messages.size, isTyping) {
        val target = messages.size + if (isTyping) 1 else 0
        if (target > 0) {
            listState.animateScrollToItem(maxOf(0, target - 1))
        }
    }

    Scaffold(
        containerColor = LocalAppColors.current.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // ── Header ────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AiGradient)
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retour",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Outlined.AutoAwesome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Kamsa",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Concierge IA · KamerStay",
                            fontSize = 12.sp,
                            color = Color.White.copy(0.75f)
                        )
                    }
                    IconButton(
                        onClick = { state.reset() }
                    ) {
                        Icon(
                            Icons.Outlined.Refresh,
                            contentDescription = "Nouvelle conversation",
                            tint = Color.White.copy(0.8f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // ── Messages ──────────────────────────────────────────
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                state = listState,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Bandeau affiché quand les anciens messages ne font plus partie du contexte envoyé à Claude
                if (isContextTruncated) {
                    item(key = "context_limit_banner") {
                        ContextLimitBanner()
                    }
                }
                itemsIndexed(messages, key = { _, msg -> msg.id }) { _, message ->
                    // Masque le placeholder streaming tant qu'il est encore vide
                    if (message.isStreaming && message.content.isEmpty()) return@itemsIndexed
                    MessageBubble(message)
                }
                // Quick replies — visibles seulement quand la conversation est vide
                if (state.hasOnlyWelcome && !isTyping) {
                    item(key = "quick_replies") {
                        QuickRepliesRow(getQuickReplies()) { reply ->
                            state.addUserMessage(reply)
                            viewModel.sendConciergeMessage(reply)
                            coroutineScope.launch {
                                listState.animateScrollToItem(maxOf(0, messages.size - 1))
                            }
                        }
                    }
                }
                if (isTyping) {
                    item(key = "typing") { TypingIndicator() }
                }
            }

            // ── Criteria Card ─────────────────────────────────────
            if (criteria != null && criteria.hasContent()) {
                CriteriaCard(
                    criteria = criteria,
                    onSearch = {
                        viewModel.searchState.query = criteria.city ?: ""
                        criteria.budgetFcfa?.let { viewModel.filterState.maxPrice = it.toFloat() }
                        navController.navigate(Routes.HotelSearch.route)
                    },
                    onBook = {
                        viewModel.searchState.query = criteria.city ?: ""
                        criteria.budgetFcfa?.let { viewModel.filterState.maxPrice = it.toFloat() }
                        try {
                            criteria.checkIn?.let { viewModel.bookingState.checkInDate = LocalDate.parse(it) }
                            criteria.checkOut?.let { viewModel.bookingState.checkOutDate = LocalDate.parse(it) }
                        } catch (_: Exception) { }
                        navController.navigate(Routes.HotelSearch.route)
                    }
                )
            }

            // ── Input Bar ─────────────────────────────────────────
            InputBar(
                text = state.inputText,
                onTextChange = { state.inputText = it },
                enabled = !isTyping,
                onSend = {
                    val msg = state.inputText.trim()
                    if (msg.isNotBlank()) {
                        state.addUserMessage(msg)
                        state.inputText = ""
                        viewModel.sendConciergeMessage(msg)
                        coroutineScope.launch {
                            listState.animateScrollToItem(maxOf(0, messages.size - 1))
                        }
                    }
                }
            )
        }
    }
}

// ── Message Bubble ────────────────────────────────────────────────────────────

// ── Bandeau limite de contexte ────────────────────────────────

@Composable
private fun ContextLimitBanner() {
    val isEn = UserSession.language == "en"
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(OnSurfaceSecondary.copy(0.07f))
            .padding(horizontal = 12.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Outlined.HistoryToggleOff,
            contentDescription = null,
            tint = OnSurfaceSecondary,
            modifier = Modifier.size(13.dp)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = if (isEn)
                "Kamsa only remembers the last 10 exchanges"
            else
                "Kamsa ne mémorise que les 10 derniers échanges",
            fontSize = 11.sp,
            color = OnSurfaceSecondary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun MessageBubble(message: UiChatMessage) {
    val isUser = message.role == "user"

    // Curseur clignotant pour les messages en cours de streaming
    val cursorAlpha by if (message.isStreaming) {
        rememberInfiniteTransition(label = "cursor").animateFloat(
            initialValue = 1f, targetValue = 0f,
            animationSpec = infiniteRepeatable(tween(500), RepeatMode.Reverse),
            label = "cursor_alpha"
        )
    } else {
        remember { mutableStateOf(0f) }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(AiGradient),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.AutoAwesome,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        val bubbleShape = RoundedCornerShape(
            topStart = if (isUser) 18.dp else 4.dp,
            topEnd = if (isUser) 4.dp else 18.dp,
            bottomStart = 18.dp,
            bottomEnd = 18.dp
        )
        val textColor = when {
            isUser -> OnPrimary
            message.isError -> Color(0xFFB00020)
            message.isWelcome || message.isProactive -> Color.White
            else -> LocalAppColors.current.textPrimary
        }
        val solidColor = when {
            isUser -> Primary
            message.isError -> Color(0xFFFFEEEE)
            else -> LocalAppColors.current.surface
        }
        val proactiveGradient = Brush.linearGradient(colors = listOf(Color(0xFFE65100), Color(0xFFFF8F00)))

        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(bubbleShape)
                .then(
                    when {
                        message.isWelcome -> Modifier.background(AiGradient)
                        message.isProactive -> Modifier.background(proactiveGradient)
                        else -> Modifier.background(solidColor)
                    }
                )
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                if (message.isProactive) {
                    Text(
                        text = "Rappel",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(0.8f),
                        letterSpacing = 1.sp
                    )
                }
                if (message.isStreaming) {
                    // Pendant le streaming : texte brut + curseur clignotant
                    // (évite les artefacts de Markdown incomplet genre **texte sans fermeture)
                    Text(
                        text = message.content +
                            if (cursorAlpha > 0.5f) "▋" else " ",
                        fontSize   = 14.sp,
                        color      = textColor,
                        lineHeight = 20.sp
                    )
                } else {
                    // Message complet : rendu Markdown
                    MarkdownText(
                        text      = message.content,
                        baseColor = textColor,
                        fontSize  = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Primary.copy(0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.Person,
                    contentDescription = null,
                    tint = Secondary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

// ── Typing Indicator ──────────────────────────────────────────────────────────

@Composable
private fun TypingIndicator() {
    var activeIndex by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(350)
            activeIndex = (activeIndex + 1) % 4
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(AiGradient),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 18.dp, bottomEnd = 18.dp, bottomStart = 18.dp))
                .background(LocalAppColors.current.surface)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(
                                if (index == activeIndex % 3) Secondary
                                else OnSurfaceSecondary.copy(0.3f)
                            )
                    )
                }
            }
        }
    }
}

// ── Quick Replies ─────────────────────────────────────────────────────────────

@Composable
private fun QuickRepliesRow(replies: List<String>, onReply: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Suggestions",
            fontSize = 12.sp,
            color = OnSurfaceSecondary.copy(0.6f),
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 4.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
            items(replies) { reply ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(AiGradient)
                        .clickable { onReply(reply) }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = reply,
                        fontSize = 13.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

// ── Criteria Card ─────────────────────────────────────────────────────────────

@Composable
private fun CriteriaCard(criteria: SearchCriteria, onSearch: () -> Unit, onBook: () -> Unit) {
    val readyToBook = criteria.hasReadyToBook()
    val isEn = UserSession.language == "en"

    val labelDetected   = if (isEn) "Kamsa understood" else "Kamsa a compris"
    val btnSeeHotels    = if (isEn) "See matching hotels" else "Voir les hôtels correspondants"
    val btnSeeShort     = if (isEn) "See" else "Voir"
    val btnBook         = if (isEn) "Book now" else "Réserver maintenant"

    // Entrée animée pour attirer l'attention
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 300f),
        label = "criteria_scale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.linearGradient(
                    listOf(Secondary.copy(0.12f), Primary.copy(0.06f))
                )
            )
            .border(1.dp, Secondary.copy(0.25f), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

            // En-tête
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    Icons.Outlined.AutoAwesome,
                    contentDescription = null,
                    tint = Secondary,
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    labelDetected,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Secondary,
                    letterSpacing = 0.3.sp
                )
            }

            // Chips des critères
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement   = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                criteria.city?.let       { CriteriaChip(Icons.Outlined.Place,         it) }
                criteria.budgetFcfa?.let { CriteriaChip(Icons.Outlined.Payments,      "max ${it.toFcfa()} FCFA") }
                criteria.checkIn?.let    { CriteriaChip(Icons.Outlined.CalendarMonth,  it) }
                criteria.checkOut?.let   { CriteriaChip(Icons.Outlined.CalendarMonth,  it) }
                criteria.travelType?.let { CriteriaChip(Icons.Outlined.Group,          it.replaceFirstChar { c -> c.uppercase() }) }
            }

            // Bouton(s) d'action
            if (readyToBook) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onSearch,
                        modifier = Modifier.weight(1f),
                        shape  = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, Secondary),
                        contentPadding = PaddingValues(vertical = 10.dp)
                    ) {
                        Icon(Icons.Outlined.Search, null, modifier = Modifier.size(14.dp), tint = Secondary)
                        Spacer(Modifier.width(4.dp))
                        Text(btnSeeShort, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Secondary)
                    }
                    Button(
                        onClick = onBook,
                        modifier = Modifier.weight(2f),
                        shape  = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Secondary),
                        contentPadding = PaddingValues(vertical = 10.dp)
                    ) {
                        Icon(Icons.Outlined.Hotel, null, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(btnBook, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            } else {
                // Bouton principal — pleine largeur, bien visible
                Button(
                    onClick = onSearch,
                    modifier = Modifier.fillMaxWidth(),
                    shape  = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Secondary),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    Icon(Icons.Outlined.Search, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(btnSeeHotels, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun CriteriaChip(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Secondary.copy(0.12f))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(icon, contentDescription = null, tint = Secondary, modifier = Modifier.size(12.dp))
        Text(label, fontSize = 12.sp, color = Secondary, fontWeight = FontWeight.Medium)
    }
}

// ── Input Bar ─────────────────────────────────────────────────────────────────

@Composable
private fun InputBar(
    text: String,
    onTextChange: (String) -> Unit,
    enabled: Boolean,
    onSend: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LocalAppColors.current.surface)
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(24.dp))
                .background(LocalAppColors.current.background)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            BasicTextField(
                value = text,
                onValueChange = onTextChange,
                enabled = enabled,
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    color = LocalAppColors.current.textPrimary
                ),
                maxLines = 4,
                decorationBox = { inner ->
                    if (text.isEmpty()) {
                        Text(
                            "Posez votre question à Kamsa...",
                            fontSize = 14.sp,
                            color = OnSurfaceSecondary.copy(0.5f)
                        )
                    }
                    inner()
                }
            )
        }
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(if (text.isNotBlank() && enabled) AiGradient else Brush.linearGradient(listOf(OnSurfaceSecondary.copy(0.3f), OnSurfaceSecondary.copy(0.3f))))
                .clickable(enabled = text.isNotBlank() && enabled) { onSend() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.AutoMirrored.Filled.Send,
                contentDescription = "Envoyer",
                tint = if (text.isNotBlank() && enabled) Color.White else OnSurfaceSecondary.copy(0.5f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// ── Helpers ───────────────────────────────────────────────────────────────────

private fun Int.toFcfa(): String {
    val s = this.toString()
    return s.reversed().chunked(3).joinToString(".").reversed()
}

private fun String.capitalize(): String = replaceFirstChar { it.uppercase() }