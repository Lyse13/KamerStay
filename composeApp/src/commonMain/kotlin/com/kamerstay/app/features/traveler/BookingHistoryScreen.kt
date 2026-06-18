package com.kamerstay.app.features.traveler

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.components.BookingCardSkeleton
import com.kamerstay.app.core.components.EmptyBookingsCancelled
import com.kamerstay.app.core.components.EmptyBookingsPast
import com.kamerstay.app.core.components.EmptyBookingsUpcoming
import com.kamerstay.app.core.components.TravelerBottomNavBar
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.mock.BookingsMockData
import com.kamerstay.app.data.model.Booking
import com.kamerstay.app.data.model.BookingStatus
import com.kamerstay.app.viewmodel.TravelerViewModel
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BookingHistoryScreen(navController: NavController) {

    val viewModel = koinViewModel<TravelerViewModel>()
    var selectedTab by remember { mutableStateOf("Upcoming") }
    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        delay(1000L)
        isLoading = false
    }

    val tabs = listOf("Upcoming", "Past", "Cancelled")

    val currentBookings = when (selectedTab) {
        "Past" -> BookingsMockData.past
        "Cancelled" -> BookingsMockData.cancelled
        else -> BookingsMockData.upcoming
    }

    Scaffold(
        containerColor = LocalAppColors.current.background,
        bottomBar = {
            TravelerBottomNavBar(navController = navController, selectedTab = 2)
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
                        IconButton(onClick = { navController.navigate(Routes.TravelerProfile.route) }) {
                            Icon(
                                Icons.Outlined.Menu,
                                contentDescription = null,
                                tint = Secondary
                            )
                        }
                        Text(
                            text = "Bookings",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Secondary
                        )
                    }
                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .border(2.dp, Primary, CircleShape)
                            .background(Primary.copy(0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Outlined.Person,
                            contentDescription = null,
                            tint = Secondary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            // ── Tabs ──────────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    tabs.forEach { tab ->
                        val isSelected = selectedTab == tab
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    if (isSelected) Secondary else Color.White
                                )
                                .border(
                                    if (!isSelected) 1.dp else 0.dp,
                                    Divider,
                                    RoundedCornerShape(20.dp)
                                )
                                .clickable { selectedTab = tab }
                                .padding(horizontal = 18.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = tab,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isSelected) Color.White else LocalAppColors.current.textPrimary
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // ── Booking Cards ─────────────────────────
            if (isLoading) {
                items(3) {
                    BookingCardSkeleton()
                    Spacer(Modifier.height(16.dp))
                }
            } else if (currentBookings.isEmpty()) {
                item {
                    when (selectedTab) {
                        "Past" -> EmptyBookingsPast()
                        "Cancelled" -> EmptyBookingsCancelled()
                        else -> EmptyBookingsUpcoming(
                            onExplore = { navController.navigate(Routes.TravelerHome.route) }
                        )
                    }
                }
            } else {
                items(currentBookings) { booking ->
                    BookingCard(
                        booking = booking,
                        onClick = {
                            navController.navigate(
                                Routes.BookingDetails.createRoute(booking.id)
                            )
                        },
                        onTrackRefund = if (booking.status == BookingStatus.CANCELLED) {
                            { navController.navigate(Routes.RefundTracking.route) }
                        } else null
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

// ── Booking Card ──────────────────────────────────────────
@Composable
fun BookingCard(
    booking: Booking,
    onClick: () -> Unit,
    onTrackRefund: (() -> Unit)? = null
) {
    val statusLabel = when (booking.status) {
        BookingStatus.CONFIRMED -> "Confirmed"
        BookingStatus.UPCOMING -> "Upcoming"
        BookingStatus.PAST -> "Completed"
        BookingStatus.CANCELLED -> "Cancelled"
    }

    val statusBg = when (booking.status) {
        BookingStatus.CONFIRMED -> Primary
        BookingStatus.UPCOMING -> Primary.copy(0.8f)
        BookingStatus.PAST -> OnSurfaceSecondary.copy(0.7f)
        BookingStatus.CANCELLED -> ErrorColor.copy(0.8f)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(LocalAppColors.current.surface)
            .clickable { onClick() }
    ) {
        Column {
            // Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = booking.gradientColors
                        )
                    )
            ) {
                if (booking.imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = booking.imageUrl,
                        contentDescription = booking.hotelName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                // Status badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(statusBg)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = statusLabel,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                // Hotel name + location
                Text(
                    text = booking.hotelName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalAppColors.current.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Dates
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        Icons.Outlined.CalendarMonth,
                        contentDescription = null,
                        tint = OnSurfaceSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "${booking.checkIn} - ${booking.checkOut}",
                        fontSize = 13.sp,
                        color = OnSurfaceSecondary
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = Divider
                )

                // Price + Details
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "TOTAL PRICE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceSecondary,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = booking.totalPrice,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = LocalAppColors.current.textPrimary
                        )
                    }

                    if (onTrackRefund != null) {
                        Button(
                            onClick = onTrackRefund,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Primary),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Icon(
                                Icons.Outlined.TrackChanges,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Track Refund",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    } else {
                        Button(
                            onClick = onClick,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Secondary),
                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = "Details",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }
                }

                // Rating for past bookings
                if (booking.status == BookingStatus.PAST && booking.rating > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Outlined.Star,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "${booking.rating} Your rating",
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary
                        )
                    }
                }
            }
        }
    }
}