package com.kamerstay.app.features.manager

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.NavigationState
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.mock.MockReservation
import com.kamerstay.app.data.mock.mockReservations

@Composable
fun ReservationsScreen(navController: NavController) {

    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf("All Bookings") }

    val tabs = listOf("All Bookings", "Pending", "Confirmed", "Completed")

    val filteredReservations = mockReservations.filter { r ->
        val matchesSearch = searchQuery.isEmpty() ||
                r.guestName.contains(searchQuery, ignoreCase = true) ||
                r.roomInfo.contains(searchQuery, ignoreCase = true)
        val matchesTab = selectedTab == "All Bookings" ||
                r.status.equals(selectedTab, ignoreCase = true)
        matchesSearch && matchesTab
    }

    Scaffold(
        containerColor = WarmIvory,
        bottomBar = { ManagerBottomNav(navController, currentRoute = "reservations") },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                containerColor = WarmAmber,
                contentColor = OnSurface,
                shape = CircleShape
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 80.dp)
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
                            text = "Hotel Manager",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepEmerald
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(PrimaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Outlined.Person,
                            contentDescription = null,
                            tint = DeepEmerald,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            // ── Title ─────────────────────────────────
            item {
                Text(
                    text = "All Reservations",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = OnSurface,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
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
                                    "Find specific reservations...",
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
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    if (isSelected) DeepEmerald else Color.White
                                )
                                .clickable { selectedTab = tab }
                                .padding(horizontal = 18.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = tab,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isSelected) Color.White else OnSurface
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // ── Reservation Cards ─────────────────────
            items(filteredReservations) { reservation ->
                ReservationCard(
                    reservation = reservation,
                    onClick = {
                        NavigationState.selectedBookingId = reservation.id
                        navController.navigate(Routes.ReservationDetails.route)
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

// ── Reservation Card ──────────────────────────────────────
@Composable
fun ReservationCard(
    reservation: MockReservation,
    onClick: () -> Unit
) {
    val (statusBg, statusTextColor) = when (reservation.status) {
        "Confirmed" -> PrimaryContainer to DeepEmerald
        "Pending" -> WarmAmber.copy(alpha = 0.15f) to WarmAmber
        "Completed" -> PrimaryContainer.copy(alpha = 0.5f) to DeepEmerald.copy(alpha = 0.7f)
        "Cancelled" -> ErrorColor.copy(alpha = 0.12f) to ErrorColor
        else -> SurfaceVariant to OnSurfaceVariant
    }

    val actionLabel = when (reservation.status) {
        "Confirmed" -> "View Details"
        "Pending" -> "Review Now"
        "Completed" -> "View Invoice"
        "Cancelled" -> "Rebook"
        else -> "View"
    }

    val actionColor = when (reservation.status) {
        "Cancelled" -> DeepEmerald
        else -> OnSurface
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(reservation.avatarColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = reservation.guestName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface
                    )
                    Text(
                        text = reservation.roomInfo,
                        fontSize = 13.sp,
                        color = OnSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.CalendarMonth,
                            contentDescription = null,
                            tint = OnSurfaceVariant,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = reservation.dates,
                            fontSize = 12.sp,
                            color = OnSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Status badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(statusBg)
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = reservation.status,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = statusTextColor
                    )
                }

                // Action
                Text(
                    text = actionLabel,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = actionColor,
                    modifier = Modifier.clickable { onClick() }
                )
            }
        }
    }
}