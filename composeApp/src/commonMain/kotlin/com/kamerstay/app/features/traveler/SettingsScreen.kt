package com.kamerstay.app.features.traveler

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*

@Composable
fun SettingsScreen(navController: NavController) {

    var notificationsEnabled by remember { mutableStateOf(true) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Log Out?", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to log out?") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        navController.navigate(Routes.Welcome.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorColor)
                ) { Text("Log Out", color = Color.White) }
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
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 0.dp
            ) {
                listOf(
                    Icons.Outlined.Search to "Explore",
                    Icons.Outlined.BookOnline to "Bookings",
                    Icons.Outlined.Notifications to "Alerts",
                    Icons.Filled.Settings to "Settings"
                ).forEachIndexed { index, (icon, label) ->
                    NavigationBarItem(
                        selected = index == 3,
                        onClick = {
                            when (index) {
                                0 -> navController.navigate(Routes.HotelSearch.route)
                                1 -> navController.navigate(Routes.BookingHistory.route)
                                2 -> navController.navigate(Routes.Notifications.route)
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
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.Menu, contentDescription = null, tint = OnSurface)
                    }
                    Text(
                        text = "KamerStay",
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
                    Text(
                        text = "S",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepEmerald
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {

                // ── Profile Card ──────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Avatar
                        Box(modifier = Modifier.size(90.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(82.dp)
                                    .clip(CircleShape)
                                    .background(WarmAmber.copy(alpha = 0.15f))
                                    .align(Alignment.Center),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(70.dp)
                                        .clip(CircleShape)
                                        .background(DeepEmerald),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "SE",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White
                                    )
                                }
                            }
                            // Edit badge
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(DeepEmerald)
                                    .align(Alignment.BottomEnd),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Outlined.Edit,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Samuel Eto'o Junior",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = OnSurface
                        )
                        Text(
                            text = "samuel.j@example.cm",
                            fontSize = 13.sp,
                            color = OnSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { },
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = WarmAmber
                            ),
                            contentPadding = PaddingValues(
                                horizontal = 24.dp,
                                vertical = 10.dp
                            )
                        ) {
                            Text(
                                text = "EDIT PROFILE",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnSurface,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ── Account Section ───────────────────
                SettingsSectionLabel(text = "ACCOUNT")
                Spacer(modifier = Modifier.height(8.dp))

                SettingsGroup {
                    SettingsItem(
                        icon = Icons.Outlined.Shield,
                        label = "Security",
                        onClick = { navController.navigate(Routes.ForgotPassword.route) }
                    )
                    SettingsDivider()
                    SettingsItem(
                        icon = Icons.Outlined.Payments,
                        label = "Payment Methods",
                        onClick = { }
                    )
                    SettingsDivider()
                    SettingsItem(
                        icon = Icons.Outlined.Link,
                        label = "Linked Accounts",
                        onClick = { }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ── Preferences Section ───────────────
                SettingsSectionLabel(text = "PREFERENCES")
                Spacer(modifier = Modifier.height(8.dp))

                SettingsGroup {
                    // Notifications toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SurfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Outlined.Notifications,
                                    contentDescription = null,
                                    tint = OnSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Text(
                                text = "Notifications",
                                fontSize = 15.sp,
                                color = OnSurface
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    if (notificationsEnabled) WarmAmber
                                    else OutlineVariant
                                )
                                .clickable { notificationsEnabled = !notificationsEnabled }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (notificationsEnabled) "ON" else "OFF",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (notificationsEnabled) OnSurface else Color.White
                            )
                        }
                    }

                    SettingsDivider()

                    // Language
                    SettingsItemWithValue(
                        icon = Icons.Outlined.Translate,
                        label = "Language",
                        value = "English (CM)",
                        onClick = { }
                    )

                    SettingsDivider()

                    // Currency
                    SettingsItemWithValue(
                        icon = Icons.Outlined.Payments,
                        label = "Currency",
                        value = "XAF (FCFA)",
                        onClick = { }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ── Support Section ───────────────────
                SettingsSectionLabel(text = "SUPPORT")
                Spacer(modifier = Modifier.height(8.dp))

                SettingsGroup {
                    SettingsItemWithTrailing(
                        icon = Icons.Outlined.Help,
                        label = "Help Center",
                        trailingIcon = Icons.Outlined.OpenInNew,
                        onClick = { }
                    )
                    SettingsDivider()
                    SettingsItem(
                        icon = Icons.Outlined.PrivacyTip,
                        label = "Privacy Policy",
                        onClick = { }
                    )
                    SettingsDivider()
                    SettingsItem(
                        icon = Icons.Outlined.Description,
                        label = "Terms of Service",
                        onClick = { }
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // ── Logout ────────────────────────────
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showLogoutDialog = true },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.AutoMirrored.Outlined.Logout,
                        contentDescription = null,
                        tint = ErrorColor,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Log Out",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ErrorColor
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Version 2.4.1 (Stable)",
                    fontSize = 12.sp,
                    color = OnSurfaceVariant.copy(0.5f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ── Helpers ───────────────────────────────────────────────

@Composable
fun SettingsSectionLabel(text: String) {
    Text(
        text = text,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = WarmAmber,
        letterSpacing = 1.sp
    )
}

@Composable
fun SettingsGroup(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
    ) { content() }
}

@Composable
fun SettingsDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp),
        color = Divider
    )
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SurfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = OnSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Text(text = label, fontSize = 15.sp, color = OnSurface)
        }
        Icon(
            Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = OnSurfaceVariant,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
fun SettingsItemWithValue(
    icon: ImageVector,
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SurfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = OnSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Text(text = label, fontSize = 15.sp, color = OnSurface)
        }
        Text(
            text = value,
            fontSize = 13.sp,
            color = OnSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun SettingsItemWithTrailing(
    icon: ImageVector,
    label: String,
    trailingIcon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SurfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = OnSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Text(text = label, fontSize = 15.sp, color = OnSurface)
        }
        Icon(
            trailingIcon,
            contentDescription = null,
            tint = OnSurfaceVariant,
            modifier = Modifier.size(18.dp)
        )
    }
}