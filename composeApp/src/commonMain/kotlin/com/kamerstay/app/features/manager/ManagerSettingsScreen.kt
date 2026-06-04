package com.kamerstay.app.features.manager

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.features.traveler.SettingsCard
import com.kamerstay.app.features.traveler.SettingsRowDivider
import com.kamerstay.app.features.traveler.SettingsRowItem
import com.kamerstay.app.features.traveler.SettingsRowWithValue
import com.kamerstay.app.features.traveler.SettingsSectionHeader
import com.kamerstay.app.features.traveler.SettingsToggleRow
import com.kamerstay.app.viewmodel.ManagerViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ManagerSettingsScreen(navController: NavController) {

    val viewModel = koinViewModel<ManagerViewModel>()
    val state = viewModel.managerSettingsState
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Sign Out?", fontWeight = FontWeight.Bold, color = TextDark) },
            text = { Text("Are you sure you want to sign out?", color = OnSurfaceSecondary) },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        navController.navigate(Routes.Welcome.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorColor),
                    shape = RoundedCornerShape(10.dp)
                ) { Text("Sign Out", color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel", color = Secondary)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }

    Scaffold(
        containerColor = BackgroundLight,
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
                    .padding(horizontal = 8.dp, vertical = 12.dp),
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
                        text = "Settings",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                }
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {

                // ── Profile Card ──────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(Secondary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "AB",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Column {
                            Text(
                                text = "Amina B.",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                            Text(
                                text = "Property Manager • Akwa Palace",
                                fontSize = 13.sp,
                                color = OnSurfaceSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ── Account & Security ────────────────
                SettingsSectionHeader("ACCOUNT & SECURITY")
                Spacer(modifier = Modifier.height(8.dp))

                SettingsCard {
                    SettingsRowItem(
                        icon = Icons.Outlined.Person,
                        label = "Personal Information",
                        onClick = { navController.navigate(Routes.ManagerPersonalInfo.route) }
                    )
                    SettingsRowDivider()
                    SettingsRowItem(
                        icon = Icons.Outlined.Shield,
                        label = "Password & Security",
                        onClick = { navController.navigate(Routes.ForgotPassword.route) }
                    )
                    SettingsRowDivider()
                    SettingsRowItem(
                        icon = Icons.Outlined.Badge,
                        label = "Professional Credentials",
                        onClick = { navController.navigate(Routes.ManagerVerification.route) }
                    )
                    SettingsRowDivider()
                    SettingsRowItem(
                        icon = Icons.Outlined.Payments,
                        label = "Payout Methods",
                        onClick = { navController.navigate(Routes.PaymentMethods.route) }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ── Hotel Preferences ─────────────────
                SettingsSectionHeader("HOTEL PREFERENCES")
                Spacer(modifier = Modifier.height(8.dp))

                SettingsCard {
                    SettingsRowWithValue(
                        icon = Icons.Outlined.Language,
                        label = "Language",
                        value = state.selectedLanguage.label
                    ) { }
                    SettingsRowDivider()
                    SettingsRowWithValue(
                        icon = Icons.Outlined.Payments,
                        label = "Currency",
                        value = state.selectedCurrency.label
                    ) { }
                    SettingsRowDivider()
                    SettingsToggleRow(
                        icon = Icons.Outlined.Notifications,
                        label = "Booking Alerts",
                        checked = state.bookingAlertsEnabled,
                        onToggle = { state.bookingAlertsEnabled = it }
                    )
                    SettingsRowDivider()
                    SettingsToggleRow(
                        icon = Icons.Outlined.Campaign,
                        label = "Push Notifications",
                        checked = state.notificationsEnabled,
                        onToggle = { state.notificationsEnabled = it }
                    )
                    SettingsRowDivider()
                    SettingsToggleRow(
                        icon = Icons.Outlined.DarkMode,
                        label = "Dark Mode",
                        checked = state.darkModeEnabled,
                        onToggle = { state.darkModeEnabled = it }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ── Support & Legal ───────────────────
                SettingsSectionHeader("SUPPORT & LEGAL")
                Spacer(modifier = Modifier.height(8.dp))

                SettingsCard {
                    SettingsRowItem(
                        icon = Icons.Outlined.SupportAgent,
                        label = "Help & Support",
                        onClick = { navController.navigate(Routes.ManagerSupport.route) }
                    )
                    SettingsRowDivider()
                    SettingsRowItem(
                        icon = Icons.Outlined.Description,
                        label = "Terms of Service",
                        onClick = { navController.navigate(Routes.PrivacyTerms.route) }
                    )
                    SettingsRowDivider()
                    SettingsRowItem(
                        icon = Icons.Outlined.PrivacyTip,
                        label = "Privacy Policy",
                        onClick = { navController.navigate(Routes.PrivacyTerms.route) }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ── Sign Out ──────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, ErrorColor.copy(0.3f), RoundedCornerShape(14.dp))
                        .background(ErrorColor.copy(0.04f))
                        .clickable { showLogoutDialog = true }
                        .padding(vertical = 18.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Outlined.Logout,
                            contentDescription = null,
                            tint = ErrorColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Sign Out",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ErrorColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Version 2.4.1 (Build 4920)",
                    fontSize = 12.sp,
                    color = OnSurfaceSecondary.copy(0.5f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}