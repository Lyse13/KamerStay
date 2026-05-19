package com.kamerstay.app.features.traveler

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
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.mock.MockNotification
import com.kamerstay.app.data.mock.mockNotifications

@Composable
fun NotificationsScreen(navController: NavController) {

    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf("All") }

    val tabs = listOf("All", "Bookings", "Payments", "Promos")

    val filteredNotifications = mockNotifications.filter { n ->
        val matchesSearch = searchQuery.isEmpty() ||
                n.title.contains(searchQuery, ignoreCase = true) ||
                n.message.contains(searchQuery, ignoreCase = true)
        val matchesTab = selectedTab == "All" ||
                (selectedTab == "Bookings" && n.type == "BOOKING") ||
                (selectedTab == "Payments" && n.type == "PAYMENT") ||
                (selectedTab == "Promos" && n.type == "PROMO")
        matchesSearch && matchesTab
    }

    Scaffold(
        containerColor = WarmIvory,
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 0.dp
            ) {
                listOf(
                    Icons.Outlined.Search to "Explore",
                    Icons.Outlined.BookOnline to "Bookings",
                    Icons.Filled.Notifications to "Alerts",
                    Icons.Outlined.Settings to "Settings"
                ).forEachIndexed { index, (icon, label) ->
                    NavigationBarItem(
                        selected = index == 2,
                        onClick = {
                            when (index) {
                                0 -> navController.navigate(Routes.HotelSearch.route)
                                1 -> navController.navigate(Routes.BookingHistory.route)
                                3 -> navController.navigate(Routes.Settings.route)
                            }
                        },
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label, fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DeepEmerald,
                            selectedTextColor = DeepEmerald,
                            indicatorColor = PrimaryContainer,
                            unselectedIconColor = OnSurfaceVariant,
                            unselectedTextColor = OnSurfaceVariant
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // ── Top Bar ───────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { }) {
                            Icon(Icons.Filled.Menu, contentDescription = null, tint = OnSurface)
                        }
                        Text(
                            text = "KamerStay",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepEmerald
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Mark all as read",
                            fontSize = 13.sp,
                            color = OnSurfaceVariant,
                            modifier = Modifier.clickable { }
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(PrimaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "L",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepEmerald
                        )
                    }
                }
            }

            // ── Search Bar ────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .padding(horizontal = 14.dp, vertical = 13.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.Search,
                        contentDescription = null,
                        tint = OnSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(fontSize = 14.sp, color = OnSurface),
                        decorationBox = { inner ->
                            if (searchQuery.isEmpty()) {
                                Text(
                                    "Search alerts...",
                                    fontSize = 14.sp,
                                    color = OnSurfaceVariant.copy(0.5f)
                                )
                            }
                            inner()
                        }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // ── Filter Tabs ───────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    tabs.forEach { tab ->
                        val isSelected = selectedTab == tab
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) DeepEmerald else Color.White
                                )
                                .border(
                                    if (!isSelected) 1.dp else 0.dp,
                                    OutlineVariant,
                                    CircleShape
                                )
                                .clickable { selectedTab = tab }
                                .padding(horizontal = 20.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = tab,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (isSelected) Color.White else OnSurface
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // ── Notifications ─────────────────────────
            items(filteredNotifications) { notification ->
                NotificationCard(notification = notification)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

// ── Notification Card ─────────────────────────────────────
@Composable
fun NotificationCard(notification: MockNotification) {
    val (iconBg, iconTint, icon) = when (notification.type) {
        "BOOKING" -> Triple(
            PrimaryContainer,
            DeepEmerald,
            Icons.Outlined.CalendarMonth
        )
        "PAYMENT" -> Triple(
            WarmAmber.copy(alpha = 0.15f),
            WarmAmber,
            Icons.Outlined.Wallet
        )
        "PROMO" -> Triple(
            ErrorColor.copy(alpha = 0.1f),
            ErrorColor,
            Icons.Outlined.Tag
        )
        else -> Triple(
            SurfaceVariant,
            OnSurfaceVariant,
            Icons.Outlined.Notifications
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .then(
                if (!notification.isRead) Modifier.border(
                    1.dp, DeepEmerald.copy(alpha = 0.3f), RoundedCornerShape(16.dp)
                ) else Modifier
            )
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Icon
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = notification.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = notification.time,
                                fontSize = 11.sp,
                                color = OnSurfaceVariant
                            )
                            if (!notification.isRead) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(DeepEmerald)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Message with highlight
                    if (notification.highlightText.isNotEmpty()) {
                        Text(
                            text = buildAnnotatedString {
                                val msg = notification.message
                                val idx = msg.indexOf(notification.highlightText)
                                if (idx >= 0) {
                                    append(msg.substring(0, idx))
                                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = OnSurface)) {
                                        append(notification.highlightText)
                                    }
                                    append(msg.substring(idx + notification.highlightText.length))
                                } else {
                                    append(msg)
                                }
                            },
                            fontSize = 13.sp,
                            color = OnSurfaceVariant,
                            lineHeight = 18.sp
                        )
                    } else {
                        Text(
                            text = notification.message,
                            fontSize = 13.sp,
                            color = OnSurfaceVariant,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // Promo image
            if (notification.hasImage) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF2A1A0D),
                                    Color(0xFF1A0D06)
                                )
                            )
                        )
                )
            }
        }
    }
}