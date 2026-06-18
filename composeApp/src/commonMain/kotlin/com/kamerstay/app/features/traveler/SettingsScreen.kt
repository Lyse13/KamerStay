package com.kamerstay.app.features.traveler

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
import com.kamerstay.app.data.model.CurrencyOption
import com.kamerstay.app.data.model.LanguageOption
import com.kamerstay.app.viewmodel.TravelerViewModel
import org.koin.compose.viewmodel.koinViewModel
import com.kamerstay.app.core.components.TravelerBottomNavBar

@Composable
fun SettingsScreen(navController: NavController) {

    val viewModel = koinViewModel<TravelerViewModel>()
    val settings = viewModel.travelerSettingsState
    var showLogoutDialog by remember { mutableStateOf(false) }

    // ── Logout dialog ─────────────────────────────────────
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Log Out?", fontWeight = FontWeight.Bold, color = LocalAppColors.current.textPrimary) },
            text = { Text("Are you sure you want to log out?", color = OnSurfaceSecondary) },
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
                ) { Text("Log Out", color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel", color = Secondary)
                }
            },
            containerColor = LocalAppColors.current.surface,
            shape = RoundedCornerShape(16.dp)
        )
    }

    // ── Language picker dialog ────────────────────────────
    if (settings.showLanguagePicker) {
        OptionsPickerDialog(
            title = "Language",
            options = settings.availableLanguages.map { it.label },
            selectedOption = settings.selectedLanguage.label,
            onSelect = { label ->
                settings.selectedLanguage = settings.availableLanguages.first { it.label == label }
                settings.showLanguagePicker = false
            },
            onDismiss = { settings.showLanguagePicker = false }
        )
    }

    // ── Currency picker dialog ────────────────────────────
    if (settings.showCurrencyPicker) {
        OptionsPickerDialog(
            title = "Currency",
            options = settings.availableCurrencies.map { it.label },
            selectedOption = settings.selectedCurrency.label,
            onSelect = { label ->
                settings.selectedCurrency = settings.availableCurrencies.first { it.label == label }
                settings.showCurrencyPicker = false
            },
            onDismiss = { settings.showCurrencyPicker = false }
        )
    }

    Scaffold(
        containerColor = LocalAppColors.current.background,
        bottomBar = {
            TravelerBottomNavBar(navController = navController, selectedTab = 3)
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
                        color = LocalAppColors.current.textPrimary
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

                // ── Profile Card ──────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(LocalAppColors.current.surface)
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
                                .background(OnSurfaceSecondary.copy(0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Outlined.Person,
                                contentDescription = null,
                                tint = OnSurfaceSecondary,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Jean-Pierre Dupont",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = LocalAppColors.current.textPrimary
                            )
                            Text(
                                text = "Premium Member",
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
                        icon = Icons.Outlined.Lock,
                        label = "Change Password",
                        onClick = { navController.navigate(Routes.ChangePassword.route) }
                    )
                    SettingsRowDivider()
                    SettingsRowItem(
                        icon = Icons.Outlined.Payments,
                        label = "Payment Methods",
                        onClick = { navController.navigate(Routes.TravelerPaymentMethods.route) }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ── Preferences ───────────────────────
                SettingsSectionHeader("PREFERENCES")
                Spacer(modifier = Modifier.height(8.dp))

                SettingsCard {
                    SettingsRowWithValue(
                        icon = Icons.Outlined.Language,
                        label = "Language",
                        value = settings.selectedLanguage.label
                    ) { settings.showLanguagePicker = true }

                    SettingsRowDivider()

                    SettingsRowWithValue(
                        icon = Icons.Outlined.Payments,
                        label = "Currency",
                        value = settings.selectedCurrency.label
                    ) { settings.showCurrencyPicker = true }

                    SettingsRowDivider()

                    SettingsToggleRow(
                        icon = Icons.Outlined.Notifications,
                        label = "Push Notifications",
                        checked = settings.notificationsEnabled,
                        onToggle = { settings.notificationsEnabled = it }
                    )

                    SettingsRowDivider()

                    SettingsToggleRow(
                        icon = Icons.Outlined.DarkMode,
                        label = "Dark Mode",
                        checked = settings.darkModeEnabled,
                        onToggle = { settings.darkModeEnabled = it }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ── Support ───────────────────────────
                SettingsSectionHeader("SUPPORT AND HELP")
                Spacer(modifier = Modifier.height(8.dp))

                SettingsCard {
                    SettingsRowItem(
                        icon = Icons.Outlined.Description,
                        label = "Terms of Service",
                        onClick = { navController.navigate(Routes.PrivacyTerms.route) }
                    )
                    SettingsRowDivider()
                    SettingsRowItem(
                        icon = Icons.Outlined.Shield,
                        label = "Privacy Policy",
                        onClick = { navController.navigate(Routes.PrivacyTerms.route) }
                    )
                    SettingsRowDivider()
                    SettingsRowItem(
                        icon = Icons.Outlined.HelpOutline,
                        label = "Help Center",
                        onClick = { navController.navigate(Routes.TravelerSupport.route) }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ── Logout Button ─────────────────────
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
                    Text(
                        text = "Log Out",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ErrorColor
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Version 2.4.1 (Build 882)",
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

// ── Options picker dialog (langue / devise) ───────────────
@Composable
fun OptionsPickerDialog(
    title: String,
    options: List<String>,
    selectedOption: String,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = LocalAppColors.current.surface,
        shape = RoundedCornerShape(16.dp),
        title = {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = LocalAppColors.current.textPrimary
            )
        },
        text = {
            Column {
                options.forEach { option ->
                    val isSelected = option == selectedOption
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) Primary.copy(0.08f) else Color.Transparent)
                            .clickable { onSelect(option) }
                            .padding(horizontal = 12.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = option,
                            fontSize = 15.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (isSelected) Secondary else LocalAppColors.current.textPrimary
                        )
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(Primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Outlined.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = OnSurfaceSecondary)
            }
        }
    )
}

// ── Settings Components ───────────────────────────────────

@Composable
fun SettingsSectionHeader(text: String) {
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = OnSurfaceSecondary,
        letterSpacing = 1.sp
    )
}

@Composable
fun SettingsCard(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(LocalAppColors.current.surface)
    ) { content() }
}

@Composable
fun SettingsRowDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp),
        color = Divider
    )
}

@Composable
fun SettingsRowItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Primary.copy(0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Secondary, modifier = Modifier.size(20.dp))
            }
            Text(text = label, fontSize = 15.sp, color = LocalAppColors.current.textPrimary)
        }
        Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = OnSurfaceSecondary, modifier = Modifier.size(18.dp))
    }
}

@Composable
fun SettingsRowWithValue(
    icon: ImageVector,
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Primary.copy(0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Secondary, modifier = Modifier.size(20.dp))
            }
            Text(text = label, fontSize = 15.sp, color = LocalAppColors.current.textPrimary)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = value, fontSize = 14.sp, color = OnSurfaceSecondary)
            Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = OnSurfaceSecondary, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
fun SettingsToggleRow(
    icon: ImageVector,
    label: String,
    checked: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Primary.copy(0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Secondary, modifier = Modifier.size(20.dp))
            }
            Text(text = label, fontSize = 15.sp, color = LocalAppColors.current.textPrimary)
        }
        Switch(
            checked = checked,
            onCheckedChange = { onToggle(it) },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Primary,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = OnSurfaceSecondary.copy(0.3f)
            )
        )
    }
}