package com.kamerstay.app.features.traveler

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.kamerstay.app.data.state.UiChatMessage
import com.kamerstay.app.viewmodel.TravelerViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

private val AiGradient = Brush.linearGradient(colors = listOf(Color(0xFF4A00E0), Color(0xFF8E2DE2)))

@Composable
fun AIConciergeScreen(navController: NavController) {
    val viewModel = koinViewModel<TravelerViewModel>()
    val state = viewModel.aiConciergeState
    val messages = state.messages
    val isTyping = state.isTyping
    val criteria = state.extractedCriteria

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

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
                items(messages, key = { it.id }) { message ->
                    MessageBubble(message)
                }
                if (isTyping) {
                    item(key = "typing") { TypingIndicator() }
                }
            }

            // ── Criteria Card ─────────────────────────────────────
            if (criteria != null && criteria.hasContent()) {
                CriteriaCard(
                    criteria = criteria,
                    onClick = {
                        viewModel.searchState.query = criteria.city ?: ""
                        criteria.budgetFcfa?.let { viewModel.filterState.maxPrice = it.toFloat() }
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

@Composable
private fun MessageBubble(message: UiChatMessage) {
    val isUser = message.role == "user"

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
            message.isWelcome -> Color.White
            else -> LocalAppColors.current.textPrimary
        }
        val solidColor = when {
            isUser -> Primary
            message.isError -> Color(0xFFFFEEEE)
            else -> LocalAppColors.current.surface
        }

        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(bubbleShape)
                .then(
                    if (message.isWelcome)
                        Modifier.background(AiGradient)
                    else
                        Modifier.background(solidColor)
                )
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Text(
                text = message.content,
                fontSize = 14.sp,
                color = textColor,
                lineHeight = 20.sp
            )
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

// ── Criteria Card ─────────────────────────────────────────────────────────────

@Composable
private fun CriteriaCard(criteria: SearchCriteria, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Primary.copy(0.08f))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    Icons.Outlined.Hotel,
                    contentDescription = null,
                    tint = Secondary,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    "Critères détectés",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Secondary
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                criteria.city?.let { CriteriaChip(Icons.Outlined.Place, it) }
                criteria.budgetFcfa?.let { CriteriaChip(Icons.Outlined.Payments, "${it.toFcfa()} FCFA") }
                criteria.travelType?.let { CriteriaChip(Icons.Outlined.Group, it.capitalize()) }
            }
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Secondary),
                contentPadding = PaddingValues(vertical = 10.dp)
            ) {
                Icon(
                    Icons.Outlined.Search,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    "Rechercher ces hôtels",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
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