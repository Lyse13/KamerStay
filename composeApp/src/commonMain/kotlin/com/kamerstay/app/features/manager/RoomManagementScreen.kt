package com.kamerstay.app.features.manager

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.NavigationState
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.model.enums.RoomStatus

// ── Mock Room Data ────────────────────────────────────────
data class ManagerRoom(
    val id: String,
    val number: String,
    val type: String,
    val description: String,
    val price: Int,
    val status: RoomStatus,
    val gradientColors: List<Color>
)

val mockManagerRooms = listOf(
    ManagerRoom(
        id = "1", number = "101",
        type = "Executive Suite", description = "King Bed",
        price = 85000, status = RoomStatus.AVAILABLE,
        gradientColors = listOf(Color(0xFF3D2E1A), Color(0xFF1A1208))
    ),
    ManagerRoom(
        id = "2", number = "204",
        type = "Standard Twin", description = "City View",
        price = 45000, status = RoomStatus.OCCUPIED,
        gradientColors = listOf(Color(0xFF1A3A2E), Color(0xFF0D2218))
    ),
    ManagerRoom(
        id = "3", number = "302",
        type = "Deluxe Queen", description = "Garden Access",
        price = 60000, status = RoomStatus.CLEANING,
        gradientColors = listOf(Color(0xFF2E1A3A), Color(0xFF180D22))
    ),
    ManagerRoom(
        id = "4", number = "105",
        type = "Junior Suite", description = "Balcony",
        price = 72000, status = RoomStatus.RESERVED,
        gradientColors = listOf(Color(0xFF3A1A0D), Color(0xFF220D06))
    ),
)

@Composable
fun RoomManagementScreen(navController: NavController) {

    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf("All Status") }

    val statusFilters = listOf("All Status", "Available", "Maintenance")

    val filteredRooms = mockManagerRooms.filter { room ->
        val matchesSearch = searchQuery.isEmpty() ||
                room.number.contains(searchQuery, ignoreCase = true) ||
                room.type.contains(searchQuery, ignoreCase = true)
        val matchesStatus = selectedStatus == "All Status" ||
                room.status.name.equals(selectedStatus, ignoreCase = true)
        matchesSearch && matchesStatus
    }

    Scaffold(
        containerColor = WarmIvory,
        bottomBar = { ManagerBottomNav(navController, currentRoute = "rooms") },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Routes.AddEditRoom.route) },
                containerColor = WarmAmber,
                contentColor = OnSurface,
                shape = CircleShape
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Room")
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

            // ── Header ────────────────────────────────
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = "INVENTORY MANAGEMENT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DeepEmerald,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Room Directory",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = OnSurface
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Add Room Button
                    Button(
                        onClick = { navController.navigate(Routes.AddEditRoom.route) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DeepEmerald
                        )
                    ) {
                        Icon(
                            Icons.Outlined.AddHome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Add Room",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Search Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White)
                            .padding(horizontal = 14.dp, vertical = 12.dp),
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
                            textStyle = TextStyle(
                                fontSize = 14.sp,
                                color = OnSurface
                            ),
                            decorationBox = { inner ->
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        "Search room name or number...",
                                        fontSize = 14.sp,
                                        color = OnSurfaceVariant.copy(0.5f)
                                    )
                                }
                                inner()
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Status Filter Chips
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        statusFilters.forEach { filter ->
                            val isSelected = selectedStatus == filter
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(
                                        if (isSelected) DeepEmerald else Color.White
                                    )
                                    .border(
                                        if (!isSelected) 1.dp else 0.dp,
                                        OutlineVariant,
                                        RoundedCornerShape(20.dp)
                                    )
                                    .clickable { selectedStatus = filter }
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = filter,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (isSelected) Color.White else OnSurface
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // ── Room Cards ────────────────────────────
            items(filteredRooms) { room ->
                ManagerRoomCard(
                    room = room,
                    onEdit = {
                        NavigationState.selectedRoomId = room.id
                        navController.navigate(Routes.AddEditRoom.route)
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // ── Create New Room Card ──────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            1.5.dp,
                            OutlineVariant,
                            RoundedCornerShape(16.dp)
                        )
                        .background(Color.White.copy(alpha = 0.5f))
                        .clickable { navController.navigate(Routes.AddEditRoom.route) }
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .border(1.5.dp, OutlineVariant, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Outlined.Add,
                                contentDescription = null,
                                tint = OnSurfaceVariant,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Create New Room",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OnSurface
                        )
                        Text(
                            text = "Add to your hotel inventory",
                            fontSize = 12.sp,
                            color = OnSurfaceVariant
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ── Manager Room Card ─────────────────────────────────────
@Composable
fun ManagerRoomCard(room: ManagerRoom, onEdit: () -> Unit) {
    val (statusBg, statusText, statusLabel) = when (room.status) {
        RoomStatus.AVAILABLE -> Triple(
            StatusAvailable.copy(0.12f), StatusAvailable, "AVAILABLE"
        )
        RoomStatus.OCCUPIED -> Triple(
            StatusOccupied.copy(0.12f), StatusOccupied, "OCCUPIED"
        )
        RoomStatus.CLEANING -> Triple(
            StatusCleaning.copy(0.12f), StatusCleaning, "CLEANING"
        )
        RoomStatus.RESERVED -> Triple(
            Color(0xFF6B3A00).copy(0.12f), Color(0xFF6B3A00), "MAINTENANCE"
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
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
                        brush = Brush.verticalGradient(colors = room.gradientColors)
                    )
            ) {
                if (room.status == RoomStatus.AVAILABLE) {
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
                                modifier = Modifier.size(10.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "VERIFIED",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // Info
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Room ${room.number}",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface
                    )
                    // Status badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(statusBg)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = statusLabel,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = statusText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${room.type} • ${room.description}",
                    fontSize = 13.sp,
                    color = OnSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "${room.price.toString().dropLast(3)},000",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = OnSurface
                        )
                        Text(
                            text = " XAF / NIGHT",
                            fontSize = 11.sp,
                            color = OnSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    IconButton(onClick = onEdit) {
                        Icon(
                            Icons.Outlined.Edit,
                            contentDescription = "Edit",
                            tint = OnSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}