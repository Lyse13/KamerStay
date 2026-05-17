package com.kamerstay.app.features.manager

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*

@Composable
fun CheckInScreen(
    navController: NavController,
    reservationId: String
) {
    var arrivalNotes by remember { mutableStateOf("") }
    var selectedRequests by remember { mutableStateOf(setOf<String>()) }
    var showSuccess by remember { mutableStateOf(false) }

    val quickRequests = listOf("Late Checkout", "Extra Pillow", "Quiet Room")

    // Success Dialog
    if (showSuccess) {
        AlertDialog(
            onDismissRequest = { },
            title = {
                Text("Check-In Complete!", fontWeight = FontWeight.Bold)
            },
            text = {
                Text("Jean-Claude Mbarga has been successfully checked in to Room 402.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccess = false
                        navController.navigate(Routes.ManagerDashboard.route) {
                            popUpTo(Routes.ManagerDashboard.route) { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepEmerald)
                ) {
                    Text("Done", color = Color.White)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }

    Scaffold(
        containerColor = WarmIvory,
        bottomBar = {
            // ── Manager Check-In Bottom Nav ───────────
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 0.dp
            ) {
                listOf(
                    Icons.Outlined.BookOnline to "Bookings",
                    Icons.Filled.Login to "Check-in",
                    Icons.Outlined.Logout to "Check-out",
                    Icons.Outlined.Hotel to "Rooms"
                ).forEachIndexed { index, (icon, label) ->
                    NavigationBarItem(
                        selected = index == 1,
                        onClick = {
                            when (index) {
                                0 -> navController.navigate(Routes.Reservations.route)
                                2 -> navController.navigate(Routes.CheckOut.route)
                                3 -> navController.navigate(Routes.RoomManagement.route)
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // ── Top Bar ───────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = OnSurface)
                    }
                    Text(
                        text = "Reservation Management",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface
                    )
                }
                IconButton(onClick = { }) {
                    Icon(Icons.Filled.MoreVert, contentDescription = null, tint = OnSurface)
                }
            }

            HorizontalDivider(color = Divider)

            Spacer(modifier = Modifier.height(20.dp))

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {

                // ── Header ────────────────────────────
                Text(
                    text = "ARRIVAL CHECK-IN",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DeepEmerald,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Guest Check-In",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = OnSurface,
                        modifier = Modifier.weight(1f)
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(WarmAmber.copy(alpha = 0.15f))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Outlined.Star,
                                contentDescription = null,
                                tint = WarmAmber,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Priority",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF8B6914)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Guest Card ────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(DeepEmerald)
                ) {
                    Column {
                        // Guest info
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Avatar
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "JC",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Jean-Claude Mbarga",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Booking #CMR-882190",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }
                            Icon(
                                Icons.Outlined.StarOutline,
                                contentDescription = null,
                                tint = WarmAmber,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        HorizontalDivider(color = Color.White.copy(alpha = 0.2f))

                        // Stay details
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "STAY DURATION",
                                    fontSize = 10.sp,
                                    color = Color.White.copy(0.6f),
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 0.5.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "4 Nights (Oct 24 –\nOct 28)",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    lineHeight = 18.sp
                                )
                            }
                            Column {
                                Text(
                                    text = "GUEST COUNT",
                                    fontSize = 10.sp,
                                    color = Color.White.copy(0.6f),
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 0.5.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "2 Adults, 1 Child",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        HorizontalDivider(color = Color.White.copy(alpha = 0.2f))

                        // Amenity chips
                        Row(
                            modifier = Modifier
                                .horizontalScroll(rememberScrollState())
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("High Speed", "Breakfast Incl.", "Parking").forEach { chip ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(Color.White.copy(alpha = 0.15f))
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = chip,
                                        fontSize = 12.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ── Room Assignment ───────────────────
                Text(
                    text = "Room Assignment",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Assigned Room
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(SurfaceVariant)
                        .border(1.5.dp, DeepEmerald, RoundedCornerShape(14.dp))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Row(verticalAlignment = Alignment.Top) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(DeepEmerald),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Outlined.Hotel,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Executive Suite",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = DeepEmerald
                                )
                                Text(
                                    text = "Room 402",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = OnSurface
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Outlined.Landscape,
                                        contentDescription = null,
                                        tint = OnSurfaceVariant,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Mt. Cameroon View",
                                        fontSize = 12.sp,
                                        color = OnSurfaceVariant
                                    )
                                }
                            }
                        }
                        Icon(
                            Icons.Outlined.CheckCircle,
                            contentDescription = null,
                            tint = DeepEmerald,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Change Room
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, OutlineVariant, RoundedCornerShape(14.dp))
                        .background(Color.White.copy(alpha = 0.5f))
                        .clickable { }
                        .padding(vertical = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Outlined.SwapHoriz,
                            contentDescription = null,
                            tint = OnSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Change Room",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = OnSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ── Special Requests ──────────────────
                Text(
                    text = "Special Requests & Notes",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "ARRIVAL COMMENTS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = OnSurfaceVariant,
                    letterSpacing = 0.8.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = arrivalNotes,
                    onValueChange = { arrivalNotes = it },
                    placeholder = {
                        Text(
                            "Enter guest preferences or specific arrival notes here...",
                            color = OnSurfaceVariant.copy(0.5f),
                            fontSize = 13.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DeepEmerald,
                        unfocusedBorderColor = OutlineVariant,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        cursorColor = DeepEmerald
                    ),
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Quick request chips
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    quickRequests.forEach { request ->
                        val isSelected = request in selectedRequests
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    if (isSelected) DeepEmerald.copy(0.1f) else Color.White
                                )
                                .border(
                                    if (isSelected) 1.5.dp else 1.dp,
                                    if (isSelected) DeepEmerald else OutlineVariant,
                                    RoundedCornerShape(20.dp)
                                )
                                .clickable {
                                    selectedRequests = if (request in selectedRequests)
                                        selectedRequests - request
                                    else selectedRequests + request
                                }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = request,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold
                                else FontWeight.Normal,
                                color = if (isSelected) DeepEmerald else OnSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ── Total Balance ─────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "TOTAL BALANCE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OnSurfaceVariant,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = "450,000 XAF",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = OnSurface
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(StatusConfirmed.copy(alpha = 0.12f))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "PREPAID",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = StatusConfirmed
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ── Complete Check-In Button ──────────
                Button(
                    onClick = { showSuccess = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DeepEmerald
                    )
                ) {
                    Icon(
                        Icons.Outlined.PersonAdd,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Complete Check-In",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}