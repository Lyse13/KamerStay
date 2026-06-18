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
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.mock.StaffMockData
import com.kamerstay.app.data.model.StaffMember
import com.kamerstay.app.data.model.StaffStatus
import com.kamerstay.app.viewmodel.ManagerViewModel
import org.koin.compose.viewmodel.koinViewModel
import com.kamerstay.app.core.components.EmptyStaffList
import com.kamerstay.app.core.components.ManagerBottomNavBar
@Composable
fun StaffManagementScreen(navController: NavController) {

    val viewModel = koinViewModel<ManagerViewModel>()
    var searchQuery by remember { mutableStateOf("") }
    var staffToDelete by remember { mutableStateOf<StaffMember?>(null) }

    if (staffToDelete != null) {
        AlertDialog(
            onDismissRequest = { staffToDelete = null },
            title = {
                Text(
                    "Remove staff member?",
                    fontWeight = FontWeight.Bold,
                    color = LocalAppColors.current.textPrimary
                )
            },
            text = {
                Text(
                    "${staffToDelete!!.name} will be permanently removed from the team.",
                    color = OnSurfaceSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = { staffToDelete = null },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorColor),
                    shape = RoundedCornerShape(10.dp)
                ) { Text("Remove", color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { staffToDelete = null }) {
                    Text("Cancel", color = OnSurfaceSecondary)
                }
            },
            containerColor = LocalAppColors.current.surface,
            shape = RoundedCornerShape(16.dp)
        )
    }

    val staff = StaffMockData.staffMembers.filter { member ->
        searchQuery.isEmpty() ||
                member.name.contains(searchQuery, ignoreCase = true) ||
                member.role.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        containerColor = LocalAppColors.current.background,
        bottomBar = {
            ManagerBottomNavBar(navController = navController, currentRoute = "profile")
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Routes.AddEditStaff.createRoute()) },
                containerColor = Secondary,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Outlined.Add, contentDescription = "Add Staff")
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
                        .padding(horizontal = 8.dp, vertical = 8.dp),
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
                            text = "Reservation Management",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = LocalAppColors.current.textPrimary
                        )
                    }
                    IconButton(onClick = { navController.navigate(Routes.ManagerProfile.route) }) {
                        Icon(
                            Icons.Outlined.MoreVert,
                            contentDescription = null,
                            tint = LocalAppColors.current.textPrimary
                        )
                    }
                }
            }

            // ── Header ────────────────────────────────
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = "Staff Roster",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Secondary
                    )
                    Text(
                        text = "Manage and monitor your hotel's operational team.",
                        fontSize = 13.sp,
                        color = OnSurfaceSecondary,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Search
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
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
                                        "Search staff...",
                                        fontSize = 14.sp,
                                        color = OnSurfaceSecondary.copy(0.5f)
                                    )
                                }
                                inner()
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Stats Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StaffStatCard(
                            label = "TOTAL STAFF",
                            value = StaffMockData.totalStaff.toString(),
                            dotColor = null,
                            modifier = Modifier.weight(1f)
                        )
                        StaffStatCard(
                            label = "ON DUTY",
                            value = StaffMockData.onDuty.toString(),
                            dotColor = Primary,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StaffStatCard(
                            label = "AWAY",
                            value = StaffMockData.away.toString(),
                            dotColor = Color(0xFFFFA500),
                            modifier = Modifier.weight(1f)
                        )
                        StaffStatCard(
                            label = "NEW RECRUITS",
                            value = StaffMockData.newRecruits.toString(),
                            dotColor = null,
                            valueColor = Secondary,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            // ── Staff Cards ───────────────────────────
            if (staff.isEmpty()) {
                item {
                    EmptyStaffList(
                        onAdd = { navController.navigate(Routes.AddEditStaff.createRoute()) }
                    )
                }
            } else {
                items(staff) { member ->
                    StaffCard(
                        member = member,
                        onEdit = { navController.navigate(Routes.AddEditStaff.createRoute()) },
                        onDelete = { staffToDelete = member }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // ── Add New Member ────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            1.5.dp,
                            Divider,
                            RoundedCornerShape(16.dp)
                        )
                        .background(LocalAppColors.current.surface)
                        .clickable { navController.navigate(Routes.AddEditStaff.createRoute()) }
                        .padding(28.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Outlined.PersonAdd,
                            contentDescription = null,
                            tint = OnSurfaceSecondary,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Add New Member",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = LocalAppColors.current.textPrimary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ── Staff Stat Card ───────────────────────────────────────
@Composable
fun StaffStatCard(
    label: String,
    value: String,
    dotColor: Color?,
    valueColor: Color = LocalAppColors.current.textPrimary,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(LocalAppColors.current.surface)
            .padding(14.dp)
    ) {
        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = OnSurfaceSecondary,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                dotColor?.let {
                    Box(
                        modifier = Modifier
                            .size(9.dp)
                            .clip(CircleShape)
                            .background(it)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                }
                Text(
                    text = value,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = valueColor
                )
            }
        }
    }
}

// ── Staff Card ────────────────────────────────────────────
@Composable
fun StaffCard(
    member: StaffMember,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val statusColor = when (member.status) {
        StaffStatus.ACTIVE -> Primary
        StaffStatus.AWAY -> Color(0xFFFFA500)
        StaffStatus.OFF_DUTY -> OnSurfaceSecondary
    }

    val statusLabel = when (member.status) {
        StaffStatus.ACTIVE -> "ACTIVE"
        StaffStatus.AWAY -> "AWAY"
        StaffStatus.OFF_DUTY -> "OFF DUTY"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(LocalAppColors.current.surface)
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar
                Box(modifier = Modifier.size(60.dp)) {
                    Box(
                        modifier = Modifier
                            .size(58.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF1A2A3A),
                                        Color(0xFF0D1A28)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = member.name.split(" ")
                                .take(2)
                                .joinToString("") { it.first().toString() },
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    // Status dot
                    Box(
                        modifier = Modifier
                            .size(13.dp)
                            .clip(CircleShape)
                            .background(statusColor)
                            .border(2.dp, Color.White, CircleShape)
                            .align(Alignment.BottomEnd)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = member.name,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = LocalAppColors.current.textPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Primary.copy(0.1f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = member.role,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Secondary,
                            letterSpacing = 0.5.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.Schedule,
                            contentDescription = null,
                            tint = OnSurfaceSecondary,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = member.shift,
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary
                        )
                    }
                }

                IconButton(onClick = onEdit) {
                    Icon(
                        Icons.Outlined.Edit,
                        contentDescription = "Edit",
                        tint = OnSurfaceSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Outlined.DeleteOutline,
                        contentDescription = "Delete",
                        tint = ErrorColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 10.dp),
                color = Divider
            )

            Text(
                text = statusLabel,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = statusColor
            )
        }
    }
}