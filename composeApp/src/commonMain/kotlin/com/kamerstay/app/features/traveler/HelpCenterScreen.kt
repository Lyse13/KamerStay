package com.kamerstay.app.features.traveler

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.NavigationState
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.mock.FaqMockData
import com.kamerstay.app.data.model.FaqCategory
import com.kamerstay.app.data.model.FaqItem

@Composable
fun HelpCenterScreen(navController: NavController) {

    val role = NavigationState.helpCenterRole
    val isManager = role == "manager"

    val categories = if (isManager) FaqMockData.managerCategories else FaqMockData.travelerCategories
    val allArticles = if (isManager) FaqMockData.managerArticles else FaqMockData.travelerArticles

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf("all") }
    var expandedItemId by remember { mutableStateOf<String?>(null) }

    val filteredArticles = remember(searchQuery, selectedCategoryId, allArticles) {
        allArticles.filter { article ->
            val matchesCategory = selectedCategoryId == "all" || article.categoryId == selectedCategoryId
            val matchesSearch = searchQuery.isBlank() ||
                    article.question.contains(searchQuery, ignoreCase = true) ||
                    article.answer.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    // Reset expanded item when filter changes
    LaunchedEffect(searchQuery, selectedCategoryId) {
        expandedItemId = null
    }

    Scaffold(containerColor = LocalAppColors.current.background) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {

            // ── Top Bar ───────────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Secondary)
                        }
                        Text(
                            "Help Center",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = LocalAppColors.current.textPrimary
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Primary.copy(0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Outlined.HelpOutline, contentDescription = null, tint = Secondary, modifier = Modifier.size(20.dp))
                    }
                }
            }

            // ── Hero + Search ─────────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (isManager)
                                androidx.compose.ui.graphics.Brush.verticalGradient(listOf(Secondary, DeepBlue))
                            else
                                androidx.compose.ui.graphics.Brush.verticalGradient(listOf(Primary, DeepBlue))
                        )
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White.copy(0.2f))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (isManager) "HOST FAQ" else "TRAVELER FAQ",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    letterSpacing = 0.8.sp
                                )
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Frequently Asked\nQuestions",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            lineHeight = 30.sp
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "${allArticles.size} articles across ${categories.size - 1} categories",
                            fontSize = 13.sp,
                            color = Color.White.copy(0.75f)
                        )
                        Spacer(Modifier.height(16.dp))

                        // Search
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(LocalAppColors.current.surface)
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Outlined.Search, contentDescription = null, tint = OnSurfaceSecondary, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(10.dp))
                            BasicTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                modifier = Modifier.weight(1f),
                                textStyle = TextStyle(fontSize = 14.sp, color = LocalAppColors.current.textPrimary),
                                decorationBox = { inner ->
                                    if (searchQuery.isEmpty()) {
                                        Text(
                                            "Search questions...",
                                            fontSize = 14.sp,
                                            color = OnSurfaceSecondary.copy(0.5f)
                                        )
                                    }
                                    inner()
                                },
                                singleLine = true
                            )
                            if (searchQuery.isNotEmpty()) {
                                Icon(
                                    Icons.Outlined.Close,
                                    contentDescription = "Clear",
                                    tint = OnSurfaceSecondary,
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clickable { searchQuery = "" }
                                )
                            }
                        }
                    }
                }
            }

            // ── Category chips ────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { category ->
                        val isSelected = selectedCategoryId == category.id
                        val count = if (category.id == "all") allArticles.size
                        else allArticles.count { it.categoryId == category.id }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) Secondary else LocalAppColors.current.surface)
                                .border(
                                    1.dp,
                                    if (isSelected) Secondary else Divider,
                                    RoundedCornerShape(20.dp)
                                )
                                .clickable {
                                    selectedCategoryId = category.id
                                    expandedItemId = null
                                }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                val icon = faqCategoryIcon(category.icon)
                                Icon(icon, contentDescription = null, tint = if (isSelected) Color.White else OnSurfaceSecondary, modifier = Modifier.size(14.dp))
                                Text(
                                    category.label,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else LocalAppColors.current.textPrimary
                                )
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) Color.White.copy(0.25f) else Primary.copy(0.1f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "$count",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else Primary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ── Results count / search label ──────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val label = if (searchQuery.isNotBlank())
                        "${filteredArticles.size} result${if (filteredArticles.size != 1) "s" else ""} for \"$searchQuery\""
                    else {
                        val catLabel = categories.find { it.id == selectedCategoryId }?.label ?: "All"
                        "$catLabel — ${filteredArticles.size} article${if (filteredArticles.size != 1) "s" else ""}"
                    }
                    Text(label, fontSize = 13.sp, color = OnSurfaceSecondary)

                    if (expandedItemId != null) {
                        Text(
                            "Collapse all",
                            fontSize = 12.sp,
                            color = Secondary,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.clickable { expandedItemId = null }
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
            }

            // ── FAQ list ──────────────────────────────────
            if (filteredArticles.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(Primary.copy(0.08f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Outlined.SearchOff, contentDescription = null, tint = Primary.copy(0.5f), modifier = Modifier.size(32.dp))
                        }
                        Spacer(Modifier.height(16.dp))
                        Text(
                            "No articles found",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = LocalAppColors.current.textPrimary
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Try a different search term\nor select another category.",
                            fontSize = 13.sp,
                            color = OnSurfaceSecondary,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )
                    }
                }
            } else {
                items(filteredArticles, key = { it.id }) { article ->
                    FaqAccordionItem(
                        item = article,
                        isExpanded = expandedItemId == article.id,
                        searchQuery = searchQuery,
                        onClick = {
                            expandedItemId = if (expandedItemId == article.id) null else article.id
                        }
                    )
                }
            }

            // ── Contact support footer ────────────────────
            item {
                Spacer(Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Secondary.copy(0.07f))
                        .border(1.dp, Secondary.copy(0.2f), RoundedCornerShape(16.dp))
                        .padding(20.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Outlined.SupportAgent, contentDescription = null, tint = Secondary, modifier = Modifier.size(32.dp))
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Still need help?",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = LocalAppColors.current.textPrimary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Our team is available 24/7.",
                            fontSize = 13.sp,
                            color = OnSurfaceSecondary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(14.dp))
                        Button(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Secondary)
                        ) {
                            Icon(Icons.Outlined.Chat, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Contact Support", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

// ── FAQ Accordion Item ────────────────────────────────────────
@Composable
private fun FaqAccordionItem(
    item: FaqItem,
    isExpanded: Boolean,
    searchQuery: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(LocalAppColors.current.surface)
    ) {
        // Question row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(if (isExpanded) Secondary.copy(0.15f) else Primary.copy(0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Q",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isExpanded) Secondary else Primary
                    )
                }
                Text(
                    text = highlightText(item.question, searchQuery),
                    fontSize = 15.sp,
                    fontWeight = if (isExpanded) FontWeight.SemiBold else FontWeight.Normal,
                    color = LocalAppColors.current.textPrimary,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(top = 3.dp)
                )
            }
            Spacer(Modifier.width(8.dp))
            Icon(
                if (isExpanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                contentDescription = null,
                tint = if (isExpanded) Secondary else OnSurfaceSecondary,
                modifier = Modifier.size(20.dp)
            )
        }

        // Answer (animated expand)
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column {
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Divider)
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Secondary.copy(0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("A", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = Secondary)
                    }
                    Text(
                        text = highlightText(item.answer, searchQuery),
                        fontSize = 14.sp,
                        color = OnSurfaceSecondary,
                        lineHeight = 21.sp,
                        modifier = Modifier.weight(1f).padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

// ── Highlight matching text ───────────────────────────────────
@Composable
private fun highlightText(text: String, query: String): androidx.compose.ui.text.AnnotatedString {
    if (query.isBlank()) return androidx.compose.ui.text.AnnotatedString(text)
    return buildAnnotatedString {
        var startIndex = 0
        val lowerText = text.lowercase()
        val lowerQuery = query.lowercase()
        var matchIndex = lowerText.indexOf(lowerQuery, startIndex)
        while (matchIndex >= 0) {
            append(text.substring(startIndex, matchIndex))
            withStyle(SpanStyle(background = Primary.copy(0.25f), fontWeight = FontWeight.Bold)) {
                append(text.substring(matchIndex, matchIndex + query.length))
            }
            startIndex = matchIndex + query.length
            matchIndex = lowerText.indexOf(lowerQuery, startIndex)
        }
        append(text.substring(startIndex))
    }
}

// ── Category icon helper ──────────────────────────────────────
@Composable
fun faqCategoryIcon(icon: String): ImageVector = when (icon) {
    "booking"  -> Icons.Outlined.BookOnline
    "payments" -> Icons.Outlined.Payments
    "account"  -> Icons.Outlined.Person
    "travel"   -> Icons.Outlined.TravelExplore
    "settings" -> Icons.Outlined.Settings
    "tech"     -> Icons.Outlined.PhoneAndroid
    else       -> Icons.Outlined.GridView
}