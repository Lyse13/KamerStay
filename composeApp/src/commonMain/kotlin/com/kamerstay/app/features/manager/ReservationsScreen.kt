package com.kamerstay.app.features.manager

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
import androidx.compose.material.icons.filled.BookOnline
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
import com.kamerstay.app.data.mock.ReservationMockData
import com.kamerstay.app.data.model.Reservation
import com.kamerstay.app.viewmodel.ManagerViewModel
import org.koin.compose.viewmodel.koinViewModel
import com.kamerstay.app.core.components.EmptyReservationsSearch
import com.kamerstay.app.core.components.ManagerBottomNavBar
import com.kamerstay.app.core.components.ReservationCardSkeleton
import kotlinx.coroutines.delay

private const val MODE_LIST = "list"
private const val MODE_CALENDAR = "calendar"


@Composable
fun ReservationsScreen(navController: NavController) {

    val viewModel = koinViewModel<ManagerViewModel>()
    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        delay(1000L)
        isLoading = false
    }
    var viewMode by remember { mutableStateOf(MODE_LIST) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Filters") }
    var calendarMonth by remember { mutableStateOf(10) }
    var calendarYear by remember { mutableStateOf(2024) }
    var selectedCalendarDay by remember { mutableStateOf<Int?>(null) }

    val filters = listOf("Filters", "Today", "This Week")

    val reservations = ReservationMockData.reservations.filter { r ->
        searchQuery.isEmpty() ||
                r.guestName.contains(searchQuery, ignoreCase = true) ||
                r.roomType.contains(searchQuery, ignoreCase = true) ||
                r.bookingId.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        containerColor = LocalAppColors.current.background,
        bottomBar = {
            ManagerBottomNavBar(navController = navController, currentRoute = "reservations")
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
                        .padding(horizontal = 16.dp, vertical = 12.dp),
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
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Secondary
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.Notifications,
                            contentDescription = null,
                            tint = Secondary,
                            modifier = Modifier.size(22.dp)
                        )
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Primary.copy(0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "JD",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Secondary
                            )
                        }
                    }
                }
            }

            // ── Header ────────────────────────────────
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Reservations",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = LocalAppColors.current.textPrimary
                            )
                            Text(
                                text = "Manage upcoming guest stays",
                                fontSize = 13.sp,
                                color = OnSurfaceSecondary
                            )
                        }

                        // ── View toggle List / Calendar ───
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(LocalAppColors.current.surface)
                        ) {
                            listOf(
                                MODE_LIST to Icons.Outlined.FormatListBulleted,
                                MODE_CALENDAR to Icons.Outlined.CalendarViewMonth
                            ).forEach { (mode, icon) ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            if (viewMode == mode) Secondary
                                            else Color.Transparent
                                        )
                                        .clickable {
                                            viewMode = mode
                                            selectedCalendarDay = null
                                        }
                                        .padding(10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        icon,
                                        contentDescription = mode,
                                        tint = if (viewMode == mode) Color.White else OnSurfaceSecondary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Stats cards
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(LocalAppColors.current.surface)
                                .padding(14.dp)
                        ) {
                            Column {
                                Text("Total Bookings", fontSize = 12.sp, color = OnSurfaceSecondary)
                                Text(
                                    "124",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = LocalAppColors.current.textPrimary
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(LocalAppColors.current.surface)
                                .padding(14.dp)
                        ) {
                            Column {
                                Text("Pending Approval", fontSize = 12.sp, color = OnSurfaceSecondary)
                                Text(
                                    "12",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = LocalAppColors.current.textPrimary
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            if (viewMode == MODE_LIST) {
                // ── Search ────────────────────────────────
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(LocalAppColors.current.surface)
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
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(fontSize = 14.sp, color = LocalAppColors.current.textPrimary),
                            decorationBox = { inner ->
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        "Search guests, room types, or ID...",
                                        fontSize = 14.sp,
                                        color = OnSurfaceSecondary.copy(0.5f)
                                    )
                                }
                                inner()
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // ── Filter Chips ──────────────────────────
                item {
                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        filters.forEach { filter ->
                            val isSelected = selectedFilter == filter
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (isSelected) Secondary else Color.White)
                                    .border(if (!isSelected) 1.dp else 0.dp, Divider, RoundedCornerShape(20.dp))
                                    .clickable { selectedFilter = filter }
                                    .padding(horizontal = 16.dp, vertical = 9.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    if (filter == "Filters") {
                                        Icon(
                                            Icons.Outlined.FilterList,
                                            contentDescription = null,
                                            tint = if (isSelected) Color.White else OnSurfaceSecondary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                    Text(
                                        text = filter,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isSelected) Color.White else LocalAppColors.current.textPrimary
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // ── Reservation Cards ─────────────────────
                if (isLoading) {
                    items(3) {
                        ReservationCardSkeleton()
                        Spacer(Modifier.height(16.dp))
                    }
                } else if (reservations.isEmpty()) {
                    item {
                        EmptyReservationsSearch(
                            searchQuery = searchQuery,
                            onClearSearch = { searchQuery = "" }
                        )
                    }
                } else {
                    items(reservations) { reservation ->
                        ReservationCard(
                            reservation = reservation,
                            onDetails = {
                                navController.navigate(Routes.ReservationDetails.createRoute(reservation.id))
                            },
                            onApprove = { },
                            onMessage = {
                                navController.navigate(Routes.ReservationDetails.createRoute(reservation.id))
                            },
                            onMore = {
                                navController.navigate(Routes.ReservationDetails.createRoute(reservation.id))
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                // ── Load More ─────────────────────────────
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Outlined.KeyboardArrowDown,
                            contentDescription = null,
                            tint = OnSurfaceSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Load more reservations", fontSize = 13.sp, color = OnSurfaceSecondary)
                    }
                }

            } else {
                // ── Calendar view ─────────────────────────
                item {
                    ReservationCalendarView(
                        entries = ReservationMockData.calendarEntries,
                        totalRooms = ReservationMockData.totalRooms,
                        month = calendarMonth,
                        year = calendarYear,
                        selectedDay = selectedCalendarDay,
                        onDaySelected = { selectedCalendarDay = it },
                        onPrevMonth = {
                            if (calendarMonth == 1) {
                                calendarMonth = 12; calendarYear--
                            } else {
                                calendarMonth--
                            }
                            selectedCalendarDay = null
                        },
                        onNextMonth = {
                            if (calendarMonth == 12) {
                                calendarMonth = 1; calendarYear++
                            } else {
                                calendarMonth++
                            }
                            selectedCalendarDay = null
                        }
                    )
                }
            }
        }
    }
}

// ── Reservation Card ──────────────────────────────────────
@Composable
fun ReservationCard(
    reservation: Reservation,
    onDetails: () -> Unit,
    onApprove: () -> Unit,
    onMessage: () -> Unit = {},
    onMore: () -> Unit = {}
) {
    val isPending = reservation.status == "Pending"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(LocalAppColors.current.surface)
    ) {
        Column {
            // Room image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = reservation.gradientColors
                        )
                    )
            ) {
                // Room tag badge
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Primary.copy(0.85f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = reservation.roomTag,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OnPrimary
                    )
                }
            }

            // Content
            Column(modifier = Modifier.padding(16.dp)) {

                // Guest info
                Text(
                    text = "GUEST",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurfaceSecondary,
                    letterSpacing = 0.8.sp
                )
                Text(
                    text = reservation.guestName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalAppColors.current.textPrimary
                )
                Text(
                    text = reservation.bookingId,
                    fontSize = 12.sp,
                    color = OnSurfaceSecondary
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Stay Duration
                Text(
                    text = "STAY DURATION",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurfaceSecondary,
                    letterSpacing = 0.8.sp
                )
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
                        text = "${reservation.checkIn} - ${reservation.checkOut}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = LocalAppColors.current.textPrimary
                    )
                }
                Text(
                    text = "${reservation.nights} ${if (reservation.nights == 1) "Night" else "Nights"}",
                    fontSize = 12.sp,
                    color = OnSurfaceSecondary
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Status
                Text(
                    text = "STATUS",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurfaceSecondary,
                    letterSpacing = 0.8.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        if (isPending) Icons.Outlined.Schedule
                        else Icons.Outlined.CheckCircle,
                        contentDescription = null,
                        tint = if (isPending) OnSurfaceSecondary else Primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = reservation.status,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isPending) OnSurfaceSecondary else Primary
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Message icon
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .border(1.dp, Divider, CircleShape)
                                .background(LocalAppColors.current.surface)
                                .clickable { onMessage() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                if (isPending) Icons.Outlined.Edit
                                else Icons.Outlined.MailOutline,
                                contentDescription = null,
                                tint = OnSurfaceSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        if (isPending) {
                            // Three dots
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .border(1.dp, Divider, CircleShape)
                                    .background(LocalAppColors.current.surface)
                                    .clickable { onMore() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Outlined.MoreVert,
                                    contentDescription = null,
                                    tint = OnSurfaceSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    if (isPending) {
                        Button(
                            onClick = onApprove,
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Secondary
                            ),
                            contentPadding = PaddingValues(
                                horizontal = 20.dp,
                                vertical = 10.dp
                            )
                        ) {
                            Text(
                                text = "Approve",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    } else {
                        OutlinedButton(
                            onClick = onDetails,
                            shape = RoundedCornerShape(20.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp, Divider
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = LocalAppColors.current.textPrimary
                            ),
                            contentPadding = PaddingValues(
                                horizontal = 20.dp,
                                vertical = 10.dp
                            )
                        ) {
                            Text(
                                text = "Details",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = LocalAppColors.current.textPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}