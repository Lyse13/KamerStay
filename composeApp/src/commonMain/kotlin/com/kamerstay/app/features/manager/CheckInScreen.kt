package com.kamerstay.app.features.manager

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
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
import com.kamerstay.app.data.model.CheckInGuest
import com.kamerstay.app.viewmodel.ManagerViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CheckInScreen(
    navController: NavController,
    reservationId: String
) {
    val viewModel = koinViewModel<ManagerViewModel>()
    val guests = viewModel.arrivals
    val state = viewModel.checkInState

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // ── Top Bar ───────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Secondary
                        )
                    }
                    Text(
                        text = "MyStays",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Secondary
                    )
                }
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.Person,
                        contentDescription = null,
                        tint = OnPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {

                // ── Header ────────────────────────────
                Text(
                    text = "Daily Arrivals",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextDark
                )
                Text(
                    text = "Review and check-in guests for Oct 24, 2023",
                    fontSize = 13.sp,
                    color = OnSurfaceSecondary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ── Search ────────────────────────────
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White)
                        .border(1.dp, Divider, RoundedCornerShape(24.dp))
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
                        onValueChange = {
                            state.searchQuery = it
                            viewModel.searchArrivals(it)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(
                            fontSize = 14.sp,
                            color = TextDark
                        ),
                        decorationBox = { inner ->
                            if (state.arrivalNotes.isEmpty()) {
                                Text(
                                    "Search guest name, room, or ID...",
                                    fontSize = 14.sp,
                                    color = OnSurfaceSecondary.copy(0.5f)
                                )
                            }
                            inner()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Stats Cards ───────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Total Arrivals
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(ElectricBlue.copy(0.2f))
                            .padding(16.dp)
                    ) {
                        Column {
                            Text(
                                text = "Total Arrivals",
                                fontSize = 12.sp,
                                color = Secondary
                            )
                            Text(
                                text = "24",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Secondary
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Pending Check-In
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color.White)
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = "Pending Check-In",
                                    fontSize = 11.sp,
                                    color = OnSurfaceSecondary
                                )
                                Text(
                                    text = "09",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextDark
                                )
                            }
                        }

                        // Rooms Ready
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Primary)
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = "Rooms Ready",
                                    fontSize = 11.sp,
                                    color = OnPrimary.copy(0.7f)
                                )
                                Text(
                                    text = "15",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = OnPrimary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ── Guest Cards ───────────────────────
                guests.forEach { guest ->
                    CheckInGuestCard(
                        guest = guest,
                        onCheckIn = {
                            navController.navigate(Routes.CheckIn.createRoute(guest.id))
                        },
                        onDetails = {
                            navController.navigate(Routes.ReservationDetails.route)
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // ── Bottom Nav ────────────────────────────
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            CheckInBottomNav(navController)
        }
    }
}

// ── Guest Card ────────────────────────────────────────────
@Composable
fun CheckInGuestCard(
    guest: CheckInGuest,
    onCheckIn: () -> Unit,
    onDetails: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(
                        if (guest.isCheckedIn) OnSurfaceSecondary.copy(0.2f)
                        else Primary.copy(0.15f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = guest.name.split(" ")
                        .take(2)
                        .joinToString("") { it.first().toString() },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (guest.isCheckedIn) OnSurfaceSecondary else Secondary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = guest.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (guest.isCheckedIn) OnSurfaceSecondary else TextDark
            )

            Text(
                text = "${guest.bookingId} • ${guest.room}",
                fontSize = 12.sp,
                color = OnSurfaceSecondary,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tags row
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                guest.tag?.let { tag ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(guest.tagColor.copy(0.12f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = tag,
                            fontSize = 11.sp,
                            color = guest.tagColor,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                guest.arrivalTime?.let { time ->
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(OnSurfaceSecondary.copy(0.1f))
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.Schedule,
                            contentDescription = null,
                            tint = OnSurfaceSecondary,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = time,
                            fontSize = 11.sp,
                            color = OnSurfaceSecondary
                        )
                    }
                }

                if (guest.paymentPending) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(ErrorColor.copy(0.1f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Payment Pending",
                            fontSize = 11.sp,
                            color = ErrorColor,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (guest.isCheckedIn) {
                Text(
                    text = guest.checkedInTime ?: "",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Primary
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDetails,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(22.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp, Divider
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = TextDark
                        )
                    ) {
                        Text(
                            text = "Details",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextDark
                        )
                    }

                    Button(
                        onClick = onCheckIn,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(22.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Secondary
                        )
                    ) {
                        Text(
                            text = "Check-In",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

// ── Bottom Nav ────────────────────────────────────────────
@Composable
fun CheckInBottomNav(navController: NavController) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp
    ) {
        listOf(
            Icons.Outlined.Home to "Home",
            Icons.Outlined.Explore to "Explore",
            Icons.Outlined.BookOnline to "Bookings",
            Icons.Outlined.Person to "Profile"
        ).forEachIndexed { index, (icon, label) ->
            NavigationBarItem(
                selected = index == 1,
                onClick = {
                    when (index) {
                        0 -> navController.navigate(Routes.ManagerDashboard.route)
                        2 -> navController.navigate(Routes.Reservations.route)
                    }
                },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label, fontSize = 11.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Secondary,
                    selectedTextColor = Secondary,
                    indicatorColor = Primary.copy(0.15f),
                    unselectedIconColor = OnSurfaceSecondary,
                    unselectedTextColor = OnSurfaceSecondary
                )
            )
        }
    }
}