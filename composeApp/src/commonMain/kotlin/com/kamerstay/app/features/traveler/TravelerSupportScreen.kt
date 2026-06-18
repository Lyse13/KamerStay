package com.kamerstay.app.features.traveler

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.NavigationState
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.mock.TravelerSupportMockData
import com.kamerstay.app.viewmodel.TravelerViewModel
import org.koin.compose.viewmodel.koinViewModel
import com.kamerstay.app.core.components.TravelerBottomNavBar

@Composable
fun TravelerSupportScreen(navController: NavController) {

    val viewModel = koinViewModel<TravelerViewModel>()
    val state = viewModel.travelerSupportState
    var showChatDialog by remember { mutableStateOf(false) }

    if (showChatDialog) {
        AlertDialog(
            onDismissRequest = { showChatDialog = false },
            title = { Text("Contact Support", fontWeight = FontWeight.Bold, color = LocalAppColors.current.textPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Choose how you'd like to reach us:", color = OnSurfaceSecondary, fontSize = 14.sp)
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Outlined.Call, contentDescription = null, tint = Primary, modifier = Modifier.size(18.dp))
                        Text("+237 6 55 00 11 22", fontWeight = FontWeight.SemiBold, color = LocalAppColors.current.textPrimary)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Outlined.MailOutline, contentDescription = null, tint = Primary, modifier = Modifier.size(18.dp))
                        Text("support@kamerstay.cm", fontWeight = FontWeight.SemiBold, color = LocalAppColors.current.textPrimary)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showChatDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    shape = RoundedCornerShape(10.dp)
                ) { Text("Got it", color = OnPrimary) }
            },
            containerColor = LocalAppColors.current.surface,
            shape = RoundedCornerShape(16.dp)
        )
    }

    Scaffold(
        containerColor = LocalAppColors.current.background,
        bottomBar = {
            TravelerBottomNavBar(navController = navController, selectedTab = 3)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // ── Top Bar ───────────────────────────────
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
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Secondary
                            )
                        }
                        Text(
                            text = "KamerStay",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Secondary
                        )
                    }
                }
            }

            // ── Hero Section ──────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Secondary, DeepBlue)
                            )
                        )
                        .padding(horizontal = 20.dp, vertical = 28.dp)
                ) {
                    Column {
                        Text(
                            text = "How can we help you?",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            lineHeight = 32.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Search our knowledge base for instant answers to your questions.",
                            fontSize = 13.sp,
                            color = Color.White.copy(0.8f),
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // Search Bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(28.dp))
                                .background(LocalAppColors.current.surface)
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Outlined.Search,
                                contentDescription = null,
                                tint = OnSurfaceSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            BasicTextField(
                                value = state.searchQuery,
                                onValueChange = { state.searchQuery = it },
                                modifier = Modifier.weight(1f),
                                textStyle = TextStyle(
                                    fontSize = 14.sp,
                                    color = LocalAppColors.current.textPrimary
                                ),
                                decorationBox = { inner ->
                                    if (state.searchQuery.isEmpty()) {
                                        Text(
                                            "Search by booking ID, topic...",
                                            fontSize = 14.sp,
                                            color = OnSurfaceSecondary.copy(0.5f)
                                        )
                                    }
                                    inner()
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    NavigationState.helpCenterRole = "traveler"
                                    navController.navigate(Routes.HelpCenter.route)
                                },
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = "Search",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = OnPrimary
                                )
                            }
                        }
                    }
                }
            }

            // ── Categories ────────────────────────────
            items(TravelerSupportMockData.categories) { category ->
                val icon = travelerCategoryIcon(category.icon)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(LocalAppColors.current.surface)
                        .clickable {
                            NavigationState.helpCenterRole = "traveler"
                            navController.navigate(Routes.HelpCenter.route)
                        }
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Primary.copy(0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                icon,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = category.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = LocalAppColors.current.textPrimary
                            )
                            Text(
                                text = category.subtitle,
                                fontSize = 13.sp,
                                color = OnSurfaceSecondary,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }
            }

            // ── Popular Questions ─────────────────────
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Popular Questions",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = LocalAppColors.current.textPrimary
                    )
                    Text(
                        text = "View all FAQ",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Primary,
                        modifier = Modifier.clickable {
                            NavigationState.helpCenterRole = "traveler"
                            navController.navigate(Routes.HelpCenter.route)
                        }
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            items(TravelerSupportMockData.popularQuestions) { question ->
                val answer = TravelerSupportMockData.popularAnswers[question.id] ?: ""
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(LocalAppColors.current.surface)
                        .clickable {
                            NavigationState.helpCenterRole = "traveler"
                            navController.navigate(Routes.HelpCenter.route)
                        }
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            Icons.Outlined.HelpOutline,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = question.title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = LocalAppColors.current.textPrimary,
                                lineHeight = 20.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = answer,
                                fontSize = 13.sp,
                                color = OnSurfaceSecondary,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            // ── Still Need Help ───────────────────────
            item {
                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Primary.copy(0.08f))
                        .padding(20.dp)
                ) {
                    Column {
                        Text(
                            text = "Still need help?",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Secondary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Our travel concierge team is available 24/7 to assist you with any inquiries or issues you may encounter.",
                            fontSize = 13.sp,
                            color = OnSurfaceSecondary,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Live Chat
                        Button(
                            onClick = { showChatDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(28.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Secondary
                            )
                        ) {
                            Icon(
                                Icons.Outlined.Chat,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Start Live Chat",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Call Center
                        OutlinedButton(
                            onClick = { showChatDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(28.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp, Secondary
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Secondary
                            )
                        ) {
                            Icon(
                                Icons.Outlined.Call,
                                contentDescription = null,
                                tint = Secondary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Call Center",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Secondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ── Category Icon Helper ──────────────────────────────────
@Composable
fun travelerCategoryIcon(icon: String): ImageVector = when (icon) {
    "booking" -> Icons.Outlined.BookOnline
    "payments" -> Icons.Outlined.Payments
    "account" -> Icons.Outlined.Person
    "travel" -> Icons.Outlined.TravelExplore
    else -> Icons.Outlined.HelpOutline
}