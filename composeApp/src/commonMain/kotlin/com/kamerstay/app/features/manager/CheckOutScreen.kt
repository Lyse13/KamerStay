package com.kamerstay.app.features.manager

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.model.DepartureGuest
import com.kamerstay.app.viewmodel.ManagerViewModel
import org.koin.compose.viewmodel.koinViewModel
import com.kamerstay.app.core.components.ManagerBottomNavBar

@Composable
fun CheckOutScreen(
    navController: NavController,
    reservationId: String
) {
    val viewModel = koinViewModel<ManagerViewModel>()
    val departures = viewModel.departures

    var searchQuery by remember { mutableStateOf("") }
    var currentPage by remember { mutableStateOf(1) }
    val totalPages = 3

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalAppColors.current.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 80.dp)
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
                        text = "KamerStay",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Secondary
                    )
                }
                Icon(
                    Icons.Outlined.Notifications,
                    contentDescription = null,
                    tint = Secondary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {

                // ── Header ────────────────────────────
                Text(
                    text = "Today's Departures",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = LocalAppColors.current.textPrimary
                )
                Text(
                    text = "Review pending check-outs and finalize guest balances.",
                    fontSize = 13.sp,
                    color = OnSurfaceSecondary,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Date badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, Divider, RoundedCornerShape(20.dp))
                        .background(LocalAppColors.current.surface)
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.CalendarMonth,
                            contentDescription = null,
                            tint = OnSurfaceSecondary,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Oct 24, 2023",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = LocalAppColors.current.textPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Stats Cards ───────────────────────
                // Total Departures
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(LocalAppColors.current.surface)
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = "TOTAL DEPARTURES",
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary,
                            letterSpacing = 0.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "24",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = LocalAppColors.current.textPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Guests",
                                fontSize = 14.sp,
                                color = OnSurfaceSecondary,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Completed
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(LocalAppColors.current.surface)
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = "COMPLETED",
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary,
                            letterSpacing = 0.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "16",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = LocalAppColors.current.textPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Checked Out",
                                fontSize = 14.sp,
                                color = OnSurfaceSecondary,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Pending Balance
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(LocalAppColors.current.surface)
                        .drawBehind {
                            drawRect(
                                color = ErrorColor,
                                size = Size(4.dp.toPx(), size.height)
                            )
                        }
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = "PENDING BALANCE",
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary,
                            letterSpacing = 0.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "\$1,240",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = ErrorColor
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "to collect",
                                fontSize = 14.sp,
                                color = OnSurfaceSecondary,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Room Readiness
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Primary)
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = "ROOM READINESS",
                            fontSize = 12.sp,
                            color = OnPrimary.copy(0.7f),
                            letterSpacing = 0.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "65%",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = OnPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Turnover",
                                fontSize = 14.sp,
                                color = OnPrimary.copy(0.7f),
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ── Search + Filter ───────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(24.dp))
                            .background(LocalAppColors.current.surface)
                            .border(1.dp, Divider, RoundedCornerShape(24.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.Search,
                            contentDescription = null,
                            tint = OnSurfaceSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = {
                                searchQuery = it
                                viewModel.searchDepartures(it)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(fontSize = 13.sp, color = LocalAppColors.current.textPrimary),
                            decorationBox = { inner ->
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        "Search guest or room...",
                                        fontSize = 13.sp,
                                        color = OnSurfaceSecondary.copy(0.5f)
                                    )
                                }
                                inner()
                            }
                        )
                    }

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .background(LocalAppColors.current.surface)
                            .border(1.dp, Divider, RoundedCornerShape(24.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                            .clickable { navController.navigate(Routes.Filter.route) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.FilterList,
                            contentDescription = null,
                            tint = OnSurfaceSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Filter",
                            fontSize = 13.sp,
                            color = LocalAppColors.current.textPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Departures Table ──────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(LocalAppColors.current.surface)
                ) {
                    Column {
                        // Table header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = "Guest & Room",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = OnSurfaceSecondary,
                                modifier = Modifier.weight(2f)
                            )
                            Text(
                                text = "Stay\nDuration",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = OnSurfaceSecondary,
                                modifier = Modifier.weight(1.2f)
                            )
                            Text(
                                text = "Balance\nDetails",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = OnSurfaceSecondary,
                                modifier = Modifier.weight(1f),
                                textAlign = androidx.compose.ui.text.style.TextAlign.End
                            )
                        }

                        HorizontalDivider(color = Divider)

                        // Departure rows
                        departures.forEach { guest ->
                            DepartureRow(guest = guest)
                            HorizontalDivider(color = Divider.copy(0.5f))
                        }

                        // Pagination
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Showing 4 of 24 departures for today",
                                fontSize = 12.sp,
                                color = OnSurfaceSecondary
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Box(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .border(1.dp, Divider, RoundedCornerShape(6.dp))
                                        .clickable { if (currentPage > 1) currentPage-- },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Outlined.ChevronLeft,
                                        contentDescription = null,
                                        tint = if (currentPage > 1) OnSurfaceSecondary else OnSurfaceSecondary.copy(0.3f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .border(1.dp, Divider, RoundedCornerShape(6.dp))
                                        .clickable { if (currentPage < totalPages) currentPage++ },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Outlined.ChevronRight,
                                        contentDescription = null,
                                        tint = LocalAppColors.current.textPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
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
            ManagerBottomNavBar(navController = navController, currentRoute = "reservations")
        }
    }
}

// ── Departure Row ─────────────────────────────────────────
@Composable
fun DepartureRow(guest: DepartureGuest) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Avatar + Info
        Row(
            modifier = Modifier.weight(2f),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        if (guest.isPaid) OnSurfaceSecondary.copy(0.15f)
                        else ErrorColor.copy(0.1f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = guest.initials,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (guest.isPaid) OnSurfaceSecondary else ErrorColor
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = guest.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LocalAppColors.current.textPrimary
                )
                Text(
                    text = guest.room,
                    fontSize = 12.sp,
                    color = OnSurfaceSecondary,
                    lineHeight = 15.sp
                )
            }
        }

        // Stay Duration
        Column(modifier = Modifier.weight(1.2f)) {
            Text(
                text = guest.nights,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = LocalAppColors.current.textPrimary
            )
            Text(
                text = guest.dates,
                fontSize = 12.sp,
                color = OnSurfaceSecondary,
                lineHeight = 15.sp
            )
        }

        // Balance
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = guest.balance,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (guest.isPaid) LocalAppColors.current.textPrimary else ErrorColor
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    if (guest.isPaid) Icons.Outlined.CheckCircle
                    else Icons.Outlined.Warning,
                    contentDescription = null,
                    tint = if (guest.isPaid) Primary else ErrorColor,
                    modifier = Modifier.size(10.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = guest.balanceLabel,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (guest.isPaid) Primary else ErrorColor,
                    lineHeight = 11.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.End
                )
            }
        }
    }
}