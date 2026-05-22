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
import com.kamerstay.app.data.mock.mockStaffMembers

@Composable
fun StaffManagementScreen(navController: NavController) {

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All Staff") }
    var selectedStaff by remember { mutableStateOf(mockStaffMembers.first()) }
    var selectedAccessLevel by remember { mutableStateOf("Standard Staff") }
    var reservationsEnabled by remember { mutableStateOf(true) }
    var roomManagementEnabled by remember { mutableStateOf(true) }
    var financeEnabled by remember { mutableStateOf(false) }
    var currentPage by remember { mutableStateOf(1) }

    val filters = listOf("All Staff", "Active", "Receptionist")

    val filteredStaff = mockStaffMembers.filter { s ->
        val matchesSearch = searchQuery.isEmpty() ||
                s.name.contains(searchQuery, ignoreCase = true) ||
                s.email.contains(searchQuery, ignoreCase = true)
        val matchesFilter = selectedFilter == "All Staff" ||
                (selectedFilter == "Active" && s.isActive) ||
                s.role.equals(selectedFilter, ignoreCase = true)
        matchesSearch && matchesFilter
    }

    Scaffold(
        containerColor = WarmIvory,
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 0.dp
            ) {
                listOf(
                    Icons.Outlined.Dashboard to "Overview",
                    Icons.Outlined.BookOnline to "Bookings",
                    Icons.Filled.People to "Staff",
                    Icons.Outlined.Person to "Profile"
                ).forEachIndexed { index, (icon, label) ->
                    NavigationBarItem(
                        selected = index == 2,
                        onClick = {
                            when (index) {
                                0 -> navController.navigate(Routes.ManagerDashboard.route)
                                1 -> navController.navigate(Routes.Reservations.route)
                                3 -> navController.navigate(Routes.ManagerProfile.route)
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                containerColor = WarmAmber,
                contentColor = OnSurface,
                shape = CircleShape
            ) {
                Icon(Icons.Outlined.PersonAdd, contentDescription = "Add Staff")
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

                // ── Header ────────────────────────────
                Text(
                    text = "Staff Access\nManagement",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = OnSurface,
                    lineHeight = 34.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Control organizational permissions and manage your Douala Central Branch team member roles and active modules.",
                    fontSize = 13.sp,
                    color = OnSurfaceVariant,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Total staff badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(SurfaceVariant)
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.People,
                            contentDescription = null,
                            tint = OnSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "TOTAL STAFF",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = OnSurfaceVariant,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "24 Members",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = OnSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ── Staff List Card ───────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Column {
                        // Search
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(SurfaceVariant)
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Outlined.Search,
                                contentDescription = null,
                                tint = OnSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            BasicTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = TextStyle(fontSize = 13.sp, color = OnSurface),
                                decorationBox = { inner ->
                                    if (searchQuery.isEmpty()) {
                                        Text(
                                            "Search staff by name or email...",
                                            fontSize = 13.sp,
                                            color = OnSurfaceVariant.copy(0.5f)
                                        )
                                    }
                                    inner()
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Filter chips
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            filters.forEach { filter ->
                                val isSelected = selectedFilter == filter
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(
                                            if (isSelected) DeepEmerald else SurfaceVariant
                                        )
                                        .clickable { selectedFilter = filter }
                                        .padding(horizontal = 14.dp, vertical = 7.dp)
                                ) {
                                    Text(
                                        text = filter,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (isSelected) Color.White else OnSurface
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Table header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "MEMBER",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceVariant,
                                letterSpacing = 0.8.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "ROLE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceVariant,
                                letterSpacing = 0.8.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        HorizontalDivider(color = Divider)

                        // Staff rows
                        filteredStaff.take(2).forEach { staff ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedStaff = staff }
                                    .padding(vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(staff.avatarColor),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = staff.initials,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = staff.name,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = OnSurface
                                        )
                                        Text(
                                            text = staff.email,
                                            fontSize = 11.sp,
                                            color = OnSurfaceVariant
                                        )
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .border(1.dp, OutlineVariant, RoundedCornerShape(6.dp))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "${staff.role}  ∨",
                                        fontSize = 12.sp,
                                        color = OnSurface
                                    )
                                }
                            }
                            HorizontalDivider(color = Divider)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Pagination
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Showing 1–10 of 24 staff",
                                fontSize = 12.sp,
                                color = OnSurfaceVariant
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .border(1.dp, OutlineVariant, RoundedCornerShape(6.dp))
                                        .clickable { if (currentPage > 1) currentPage-- },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Filled.ChevronLeft,
                                        contentDescription = null,
                                        tint = OnSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .border(1.dp, OutlineVariant, RoundedCornerShape(6.dp))
                                        .clickable { currentPage++ },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Filled.ChevronRight,
                                        contentDescription = null,
                                        tint = OnSurface,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Staff Detail Card ─────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(20.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Avatar
                        Box(modifier = Modifier.size(72.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(selectedStaff.avatarColor)
                                    .align(Alignment.Center),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = selectedStaff.initials,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            }
                            // Online indicator
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(WarmAmber)
                                    .align(Alignment.BottomEnd)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = selectedStaff.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = OnSurface
                        )
                        Text(
                            text = "Lead ${selectedStaff.role} • Douala Central",
                            fontSize = 13.sp,
                            color = OnSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Access Level
                        Text(
                            text = "ACCESS LEVEL",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceVariant,
                            letterSpacing = 0.8.sp,
                            modifier = Modifier.align(Alignment.Start)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Standard Staff option
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .border(
                                    if (selectedAccessLevel == "Standard Staff") 1.5.dp
                                    else 1.dp,
                                    if (selectedAccessLevel == "Standard Staff") DeepEmerald
                                    else OutlineVariant,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { selectedAccessLevel = "Standard Staff" }
                                .padding(horizontal = 14.dp, vertical = 12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Outlined.Person,
                                        contentDescription = null,
                                        tint = OnSurfaceVariant,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "Standard Staff",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = OnSurface
                                    )
                                }
                                if (selectedAccessLevel == "Standard Staff") {
                                    Icon(
                                        Icons.Outlined.CheckCircle,
                                        contentDescription = null,
                                        tint = DeepEmerald,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Administrator option
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .border(
                                    if (selectedAccessLevel == "Administrator") 1.5.dp
                                    else 1.dp,
                                    if (selectedAccessLevel == "Administrator") DeepEmerald
                                    else OutlineVariant,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { selectedAccessLevel = "Administrator" }
                                .padding(horizontal = 14.dp, vertical = 12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Outlined.AdminPanelSettings,
                                    contentDescription = null,
                                    tint = OnSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Administrator",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = OnSurface
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Module Permissions
                        Text(
                            text = "MODULE PERMISSIONS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceVariant,
                            letterSpacing = 0.8.sp,
                            modifier = Modifier.align(Alignment.Start)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Permissions list
                        PermissionToggle(
                            icon = Icons.Outlined.CalendarMonth,
                            label = "Reservations",
                            enabled = reservationsEnabled,
                            onToggle = { reservationsEnabled = it }
                        )
                        HorizontalDivider(color = Divider)
                        PermissionToggle(
                            icon = Icons.Outlined.Hotel,
                            label = "Room Management",
                            enabled = roomManagementEnabled,
                            onToggle = { roomManagementEnabled = it }
                        )
                        HorizontalDivider(color = Divider)
                        PermissionToggle(
                            icon = Icons.Outlined.Receipt,
                            label = "Finance & Billing",
                            enabled = financeEnabled,
                            onToggle = { financeEnabled = it }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Update button
                        Button(
                            onClick = { },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DeepEmerald
                            )
                        ) {
                            Text(
                                text = "Update Permissions",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── System Health Card ────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF1A3A2E),
                                    Color(0xFF0D2218)
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "System Health",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Icon(
                                Icons.Filled.ElectricBolt,
                                contentDescription = null,
                                tint = WarmAmber,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Staff login activity is 12% higher than last week. Security protocols are optimal.",
                            fontSize = 13.sp,
                            color = Color.White.copy(0.7f),
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color.White.copy(0.15f))
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = "Last Admin Audit",
                                    fontSize = 11.sp,
                                    color = Color.White.copy(0.7f)
                                )
                            }
                            Text(
                                text = "2h ago",
                                fontSize = 12.sp,
                                color = Color.White.copy(0.5f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// ── Permission Toggle Row ─────────────────────────────────
@Composable
fun PermissionToggle(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    enabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                icon,
                contentDescription = null,
                tint = OnSurfaceVariant,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = label, fontSize = 14.sp, color = OnSurface)
        }
        Switch(
            checked = enabled,
            onCheckedChange = { onToggle(it) },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = DeepEmerald,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = OutlineVariant
            )
        )
    }
}