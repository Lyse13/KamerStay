package com.kamerstay.app.features.traveler

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.NavigationState
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*

// ── Mock Booking Data ─────────────────────────────────────
data class MockBooking(
    val id: String,
    val hotelName: String,
    val location: String,
    val checkIn: String,
    val checkOut: String,
    val totalPrice: String,
    val status: String,
    val rating: Double = 0.0,
    val isUpcoming: Boolean = true,
    val gradientColors: List<Color> = listOf(Color(0xFF1A3A4A), Color(0xFF2D5016))
)

val mockUpcomingBookings = listOf(
    MockBooking(
        id = "1",
        hotelName = "The Douala Zenith",
        location = "Akwa, Douala",
        checkIn = "Oct 24, 2023",
        checkOut = "Oct 28, 2023",
        totalPrice = "450,000 CFA",
        status = "CONFIRMED",
        isUpcoming = true,
        gradientColors = listOf(Color(0xFF1A3A4A), Color(0xFF2D4A1E))
    ),
    MockBooking(
        id = "2",
        hotelName = "Kribi Sands Resort",
        location = "South Beach, Kribi",
        checkIn = "Nov 12, 2023",
        checkOut = "Nov 15, 2023",
        totalPrice = "280,000 CFA",
        status = "CONFIRMED",
        isUpcoming = true,
        gradientColors = listOf(Color(0xFF1A4A3A), Color(0xFF0D5A4A))
    ),
)

val mockPastBookings = listOf(
    MockBooking(
        id = "3",
        hotelName = "Yaoundé Grand Palace",
        location = "Sep 15 - Sep 18, 2023",
        checkIn = "Sep 15, 2023",
        checkOut = "Sep 18, 2023",
        totalPrice = "120,000 CFA",
        status = "COMPLETED",
        rating = 4.8,
        isUpcoming = false,
        gradientColors = listOf(Color(0xFF3A2A1A), Color(0xFF2A1A0D))
    ),
    MockBooking(
        id = "4",
        hotelName = "Limbe Ecolodge",
        location = "Aug 02 - Aug 05, 2023",
        checkIn = "Aug 02, 2023",
        checkOut = "Aug 05, 2023",
        totalPrice = "75,000 CFA",
        status = "CANCELLED",
        isUpcoming = false,
        gradientColors = listOf(Color(0xFF2A1A2A), Color(0xFF1A0D1A))
    ),
)

@Composable
fun BookingHistoryScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf("Upcoming") }

    Scaffold(
        containerColor = WarmIvory,
        bottomBar = {
            TravelerBottomNav(navController, currentRoute = "bookings")
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
                            Icon(
                                Icons.Filled.Menu,
                                contentDescription = "Menu",
                                tint = OnSurface
                            )
                        }
                        Text(
                            text = "Bookings",
                            fontSize = 20.sp,
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
                        Text(
                            text = "L",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepEmerald
                        )
                    }
                }
            }

            // ── Tabs ──────────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(SurfaceVariant)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    listOf("Upcoming", "Past").forEach { tab ->
                        val isSelected = selectedTab == tab
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    if (isSelected) DeepEmerald else Color.Transparent
                                )
                                .clickable { selectedTab = tab }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tab,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isSelected) Color.White else OnSurfaceVariant
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            if (selectedTab == "Upcoming") {
                // ── Upcoming Section ──────────────────
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Upcoming",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = OnSurface
                        )
                        Text(
                            text = "${mockUpcomingBookings.size} bookings",
                            fontSize = 13.sp,
                            color = OnSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                items(mockUpcomingBookings) { booking ->
                    UpcomingBookingCard(
                        booking = booking,
                        onClick = {
                            NavigationState.selectedBookingId = booking.id
                            navController.navigate(Routes.BookingDetails.route)
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

            } else {
                // ── Past Section ──────────────────────
                item {
                    Text(
                        text = "Past Bookings",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = OnSurface,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                items(mockPastBookings) { booking ->
                    PastBookingCard(
                        booking = booking,
                        onClick = {
                            NavigationState.selectedBookingId = booking.id
                            navController.navigate(Routes.BookingDetails.route)
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

// ── Upcoming Booking Card ─────────────────────────────────
@Composable
fun UpcomingBookingCard(booking: MockBooking, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            // Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(
                        brush = Brush.verticalGradient(colors = booking.gradientColors)
                    )
            ) {
                // Verified badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(DeepEmerald.copy(alpha = 0.85f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.Verified,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "Verified",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                // Hotel name + status
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = booking.hotelName,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    StatusChipBooking(status = booking.status)
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Place,
                        contentDescription = null,
                        tint = OnSurfaceVariant,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = booking.location,
                        fontSize = 12.sp,
                        color = OnSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "CHECK-IN",
                            fontSize = 10.sp,
                            color = OnSurfaceVariant,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = booking.checkIn,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )
                    }
                    Column {
                        Text(
                            text = "CHECK-OUT",
                            fontSize = 10.sp,
                            color = OnSurfaceVariant,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = booking.checkOut,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Spacer(modifier = Modifier.height(12.dp))

                HorizontalDivider(color = Divider)

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "TOTAL PRICE",
                            fontSize = 10.sp,
                            color = OnSurfaceVariant,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = booking.totalPrice,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = OnSurface
                        )
                    }
                    Button(
                        onClick = { onClick() },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = WarmAmber
                        ),
                        contentPadding = PaddingValues(
                            horizontal = 20.dp,
                            vertical = 10.dp
                        )
                    ) {
                        Text(
                            text = "Manage",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OnSurface
                        )
                    }
                }
            }
        }
    }
}

// ── Past Booking Card ─────────────────────────────────────
@Composable
fun PastBookingCard(booking: MockBooking, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        brush = Brush.verticalGradient(colors = booking.gradientColors)
                    )
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = booking.hotelName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OnSurface,
                        modifier = Modifier.weight(1f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    StatusChipBooking(status = booking.status)
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${booking.checkIn} - ${booking.checkOut}",
                    fontSize = 11.sp,
                    color = OnSurfaceVariant
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = booking.totalPrice,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface
                    )
                    if (booking.status == "COMPLETED" && booking.rating > 0) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Filled.Star,
                                contentDescription = null,
                                tint = StarRating,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = booking.rating.toString(),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = OnSurface
                            )
                        }
                    }
                    if (booking.status == "CANCELLED") {
                        Text(
                            text = "Refunded",
                            fontSize = 12.sp,
                            color = OnSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

// ── Status Chip ───────────────────────────────────────────
@Composable
fun StatusChipBooking(status: String) {
    val (bg, textColor) = when (status) {
        "CONFIRMED" -> StatusConfirmed.copy(alpha = 0.12f) to StatusConfirmed
        "COMPLETED" -> OnSurfaceVariant.copy(alpha = 0.12f) to OnSurfaceVariant
        "CANCELLED" -> StatusCancelled.copy(alpha = 0.12f) to StatusCancelled
        "PENDING" -> StatusPending.copy(alpha = 0.12f) to StatusPending
        else -> SurfaceVariant to OnSurfaceVariant
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(bg)
            .padding(horizontal = 6.dp, vertical = 3.dp)
    ) {
        Text(
            text = status,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}