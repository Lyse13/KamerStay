package com.kamerstay.app.features.manager

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.theme.*

@Composable
fun AddEditRoomScreen(navController: NavController) {

    var roomNumber by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Standard") }
    var pricePerNight by remember { mutableStateOf("45000") }
    var capacity by remember { mutableStateOf("2") }
    var description by remember { mutableStateOf("") }
    var selectedFeatures by remember { mutableStateOf(setOf<String>()) }
    var showCategoryDropdown by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val categories = listOf("Standard", "Deluxe", "Suite", "Executive Suite",
        "Presidential Suite", "Family Room")

    val features = listOf(
        "WiFi", "Climatisation", "King Bed", "Twin Bed",
        "City View", "Garden View", "Mini-bar", "Balcon",
        "Bureau de travail", "Baignoire"
    )

    Scaffold(
        containerColor = WarmIvory,
        bottomBar = { ManagerBottomNav(navController, currentRoute = "rooms") }
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

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {

                // ── Breadcrumb ────────────────────────
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "ROOMS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OnSurfaceVariant,
                        letterSpacing = 0.8.sp
                    )
                    Icon(
                        Icons.Filled.ChevronRight,
                        contentDescription = null,
                        tint = OnSurfaceVariant,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "ADD NEW ROOM",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface,
                        letterSpacing = 0.8.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Room Configuration",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = OnSurface
                )
                Text(
                    text = "Configure room details for Akwa Palace guest inventory.",
                    fontSize = 13.sp,
                    color = OnSurfaceVariant,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // ── Form Card ─────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Column {
                        // Room Number
                        AddRoomLabel(text = "Room Number")
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = roomNumber,
                            onValueChange = { roomNumber = it },
                            placeholder = {
                                Text("e.g. 304", color = OnSurfaceVariant.copy(0.5f))
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Tag,
                                    contentDescription = null,
                                    tint = OnSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = addRoomTextFieldColors(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Room Category
                        AddRoomLabel(text = "Room Category")
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = selectedCategory,
                                onValueChange = { },
                                readOnly = true,
                                leadingIcon = {
                                    Icon(
                                        Icons.Outlined.Category,
                                        contentDescription = null,
                                        tint = OnSurfaceVariant,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                trailingIcon = {
                                    IconButton(onClick = { showCategoryDropdown = true }) {
                                        Icon(
                                            Icons.Filled.KeyboardArrowDown,
                                            contentDescription = null,
                                            tint = OnSurfaceVariant
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showCategoryDropdown = true },
                                shape = RoundedCornerShape(10.dp),
                                colors = addRoomTextFieldColors(),
                                singleLine = true
                            )
                            DropdownMenu(
                                expanded = showCategoryDropdown,
                                onDismissRequest = { showCategoryDropdown = false },
                                modifier = Modifier.background(Color.White)
                            ) {
                                categories.forEach { category ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = category,
                                                color = if (selectedCategory == category)
                                                    DeepEmerald else OnSurface
                                            )
                                        },
                                        onClick = {
                                            selectedCategory = category
                                            showCategoryDropdown = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Price per night
                        AddRoomLabel(text = "Price per night (FCFA)")
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = pricePerNight,
                            onValueChange = { pricePerNight = it },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Payments,
                                    contentDescription = null,
                                    tint = OnSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = addRoomTextFieldColors(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Capacity
                        AddRoomLabel(text = "Guest Capacity")
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            listOf("1", "2", "3", "4").forEach { cap ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            if (capacity == cap) DeepEmerald
                                            else SurfaceVariant
                                        )
                                        .clickable { capacity = cap }
                                        .padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = cap,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (capacity == cap) Color.White
                                        else OnSurface
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Description
                        AddRoomLabel(text = "Room Description")
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            placeholder = {
                                Text(
                                    "Describe the room...",
                                    color = OnSurfaceVariant.copy(0.5f)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = addRoomTextFieldColors(),
                            maxLines = 4
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Room Photography ──────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = "Room Photography",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Upload box
                            Box(
                                modifier = Modifier
                                    .size(110.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(
                                        1.5.dp,
                                        OutlineVariant,
                                        RoundedCornerShape(12.dp)
                                    )
                                    .background(SurfaceVariant)
                                    .clickable { },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        Icons.Outlined.CameraAlt,
                                        contentDescription = null,
                                        tint = OnSurfaceVariant,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Upload",
                                        fontSize = 12.sp,
                                        color = OnSurfaceVariant
                                    )
                                }
                            }

                            // Preview image placeholder
                            Box(
                                modifier = Modifier
                                    .size(110.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF1A3A2E)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Outlined.Hotel,
                                    contentDescription = null,
                                    tint = Color.White.copy(0.5f),
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Room Features ─────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = "Room Features",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        androidx.compose.foundation.layout.FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            features.forEach { feature ->
                                val isSelected = feature in selectedFeatures
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(
                                            if (isSelected) DeepEmerald.copy(0.1f)
                                            else SurfaceVariant
                                        )
                                        .border(
                                            if (isSelected) 1.5.dp else 0.dp,
                                            if (isSelected) DeepEmerald else Color.Transparent,
                                            RoundedCornerShape(20.dp)
                                        )
                                        .clickable {
                                            selectedFeatures = if (feature in selectedFeatures)
                                                selectedFeatures - feature
                                            else selectedFeatures + feature
                                        }
                                        .padding(horizontal = 14.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = feature,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.SemiBold
                                        else FontWeight.Normal,
                                        color = if (isSelected) DeepEmerald else OnSurface
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ── Save Button ───────────────────────
                Button(
                    onClick = {
                        isLoading = true
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DeepEmerald
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            Icons.Outlined.Save,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Save Room",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ── Helpers ───────────────────────────────────────────────
@Composable
fun AddRoomLabel(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = OnSurface
    )
}

@Composable
fun addRoomTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = DeepEmerald,
    unfocusedBorderColor = OutlineVariant,
    focusedContainerColor = SurfaceVariant,
    unfocusedContainerColor = SurfaceVariant,
    cursorColor = DeepEmerald,
    focusedLabelColor = DeepEmerald
)