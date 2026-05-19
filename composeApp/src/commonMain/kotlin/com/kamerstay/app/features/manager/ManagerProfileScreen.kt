package com.kamerstay.app.features.manager

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
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
import com.kamerstay.app.features.traveler.ProfileMenuItem

@Composable
fun ManagerProfileScreen(navController: NavController) {

    var showLogoutDialog by remember { mutableStateOf(false) }

    val barHeights = listOf(0.5f, 0.65f, 0.7f, 1f, 0.6f)

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout?", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to logout?") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        navController.navigate(Routes.Welcome.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorColor)
                ) { Text("Logout", color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel", color = DeepEmerald)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }

    Scaffold(
        containerColor = WarmIvory,
        bottomBar = { ManagerBottomNav(navController, currentRoute = "profile") }
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
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFF8B6914), Color(0xFF5C4A1E))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("AB", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {

                // ── Profile Header ────────────────────
                Row(verticalAlignment = Alignment.Bottom) {
                    Box(modifier = Modifier.size(90.dp)) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFF8B6914),
                                            Color(0xFF5C3D0D)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "AB",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                        // Badge
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(WarmAmber)
                                .align(Alignment.BottomEnd),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.Star,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Amina B.",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = DeepEmerald
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Property Manager",
                        fontSize = 14.sp,
                        color = OnSurfaceVariant
                    )
                    Text(
                        text = " • ",
                        fontSize = 14.sp,
                        color = OnSurfaceVariant
                    )
                    Text(
                        text = "Akwa Palace",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OnSurface
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Region + Verified badges
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(WarmAmber.copy(alpha = 0.15f))
                            .padding(horizontal = 12.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "DOUALA REGION",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF8B6914)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(PrimaryContainer)
                            .padding(horizontal = 12.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "VERIFIED",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepEmerald
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ── Occupancy Rate Card ───────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Occupancy Rate",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = OnSurface
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.TrendingUp,
                                contentDescription = null,
                                tint = StatusConfirmed,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "84%",
                                fontSize = 40.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = OnSurface
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "+12% vs last month",
                                fontSize = 13.sp,
                                color = StatusConfirmed,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Bar chart
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            barHeights.forEachIndexed { index, height ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight(height)
                                        .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                        .background(
                                            if (index == 3) DeepEmerald
                                            else PrimaryContainer
                                        )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // ── Net Revenue Card ──────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(WarmAmber)
                        .padding(20.dp)
                ) {
                    Column {
                        Text(
                            text = "Net Revenue",
                            fontSize = 14.sp,
                            color = Color.White.copy(0.8f)
                        )
                        Text(
                            text = "This Quarter",
                            fontSize = 13.sp,
                            color = Color.White.copy(0.7f)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "4.2M XAF",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DeepEmerald
                            )
                        ) {
                            Text(
                                text = "View Report  →",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ── Menu Items ────────────────────────
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    ProfileMenuItem(
                        icon = Icons.Outlined.Person,
                        iconBg = PrimaryContainer,
                        iconTint = DeepEmerald,
                        title = "Personal Information",
                        subtitle = "Manage your identity and details",
                        onClick = { }
                    )
                    ProfileMenuItem(
                        icon = Icons.Outlined.Hotel,
                        iconBg = WarmAmber.copy(alpha = 0.15f),
                        iconTint = WarmAmber,
                        title = "My Hotels",
                        subtitle = "Manage your hotel listings",
                        onClick = { navController.navigate(Routes.RoomManagement.route) }
                    )
                    ProfileMenuItem(
                        icon = Icons.Outlined.Shield,
                        iconBg = DeepEmerald.copy(alpha = 0.1f),
                        iconTint = DeepEmerald,
                        title = "Security",
                        subtitle = "Password, 2FA, Biometrics",
                        onClick = { navController.navigate(Routes.ForgotPassword.route) }
                    )
                    ProfileMenuItem(
                        icon = Icons.AutoMirrored.Outlined.Help,
                        iconBg = SurfaceVariant,
                        iconTint = OnSurfaceVariant,
                        title = "Help & Support",
                        subtitle = "FAQs, Contact Support, Chat",
                        onClick = { }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Logout
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White)
                            .clickable { showLogoutDialog = true }
                            .padding(horizontal = 16.dp, vertical = 18.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.Logout,
                                contentDescription = null,
                                tint = ErrorColor,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(14.dp))
                            Text(
                                text = "Logout from Account",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ErrorColor
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}