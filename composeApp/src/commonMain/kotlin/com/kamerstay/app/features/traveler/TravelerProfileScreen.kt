package com.kamerstay.app.features.traveler

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.components.AvatarWithImagePicker
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.viewmodel.TravelerViewModel
import org.koin.compose.viewmodel.koinViewModel
import com.kamerstay.app.core.components.TravelerBottomNavBar

@Composable
fun TravelerProfileScreen(navController: NavController) {

    val viewModel = koinViewModel<TravelerViewModel>()
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Sign Out?", fontWeight = FontWeight.Bold, color = LocalAppColors.current.textPrimary) },
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
            containerColor = LocalAppColors.current.surface,
            shape = RoundedCornerShape(16.dp)
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
                        text = "KamerStay",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Secondary
                    )
                }
                IconButton(onClick = { navController.navigate(Routes.Settings.route) }) {
                    Icon(
                        Icons.Outlined.Settings,
                        contentDescription = null,
                        tint = Secondary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            // ── Profile Card ──────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(LocalAppColors.current.surface)
                    .padding(20.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Avatar
                    AvatarWithImagePicker(
                        imagePicked = viewModel.travelerPersonalInfoState.profileImagePicked,
                        onPickImage = { viewModel.travelerPersonalInfoState.profileImagePicked = true },
                        avatarSize = 85,
                        badgeSize = 28
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = viewModel.travelerPersonalInfoState.fullName,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = LocalAppColors.current.textPrimary
                    )
                    Text(
                        text = "Explorer • Since 2022",
                        fontSize = 13.sp,
                        color = OnSurfaceSecondary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Stats
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ProfileStat("12", "Stays")
                        ProfileStatDivider()
                        ProfileStat("4", "Reviews")
                        ProfileStatDivider()
                        ProfileStat("2.4k", "Points")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Menu Items ────────────────────────────
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TravelerProfileItem(
                    icon = Icons.Outlined.Person,
                    title = "Personal Information",
                    subtitle = "Edit your name, email, phone and location",
                    onClick = { navController.navigate(Routes.TravelerPersonalInfo.route) }
                )
                TravelerProfileItem(
                    icon = Icons.Outlined.BookOnline,
                    title = "My Bookings",
                    subtitle = "Manage your upcoming and past trips",
                    onClick = { navController.navigate(Routes.BookingHistory.route) }
                )
                TravelerProfileItem(
                    icon = Icons.Outlined.FavoriteBorder,
                    title = "Saved Places",
                    subtitle = "Hotels and resorts you've pinned",
                    onClick = { navController.navigate(Routes.Wishlist.route) }
                )
                TravelerProfileItem(
                    icon = Icons.Outlined.CreditCard,
                    title = "Payment Methods",
                    subtitle = "MasterCard ending in 4242",
                    onClick = { navController.navigate(Routes.TravelerPaymentMethods.route) }
                )
                TravelerProfileItem(
                    icon = Icons.Outlined.HelpOutline,
                    title = "Help Center",
                    subtitle = "24/7 support and traveler guides",
                    onClick = { navController.navigate(Routes.TravelerSupport.route) }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Account Details ───────────────────────
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "Account Details",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalAppColors.current.textPrimary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(LocalAppColors.current.surface)
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        AccountDetailRow(
                            label = "Adresse Email",
                            value = viewModel.travelerPersonalInfoState.email.ifBlank { "—" },
                            hasVerified = true
                        )
                        HorizontalDivider(color = Divider)
                        AccountDetailRow(
                            label = "Téléphone",
                            value = "${viewModel.travelerPersonalInfoState.phoneCode} ${viewModel.travelerPersonalInfoState.phoneNumber}".trim().ifBlank { "—" }
                        )
                        HorizontalDivider(color = Divider)
                        AccountDetailRow(
                            label = "Ville",
                            value = viewModel.travelerPersonalInfoState.city.ifBlank { "—" }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Sign Out ──────────────────────────────
            Text(
                text = "Sign Out",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = ErrorColor,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showLogoutDialog = true }
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Version 2.4.1 (Build 1204)",
                fontSize = 12.sp,
                color = OnSurfaceSecondary.copy(0.5f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ── Profile Stat ──────────────────────────────────────────
@Composable
fun ProfileStat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Secondary
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = OnSurfaceSecondary
        )
    }
}

@Composable
fun ProfileStatDivider() {
    Box(
        modifier = Modifier
            .height(30.dp)
            .width(1.dp)
            .background(Divider)
    )
}

// ── Traveler Profile Item ─────────────────────────────────
@Composable
fun TravelerProfileItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(LocalAppColors.current.surface)
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Primary.copy(0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = Secondary,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LocalAppColors.current.textPrimary
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = OnSurfaceSecondary,
                    lineHeight = 16.sp
                )
            }
            Icon(
                Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = OnSurfaceSecondary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

// ── Account Detail Row ────────────────────────────────────
@Composable
fun AccountDetailRow(
    label: String,
    value: String,
    hasVerified: Boolean = false
) {
    Column {
        Text(
            text = label,
            fontSize = 12.sp,
            color = OnSurfaceSecondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = LocalAppColors.current.textPrimary
            )
            if (hasVerified) {
                Icon(
                    Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}