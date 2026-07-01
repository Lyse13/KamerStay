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
import com.kamerstay.app.core.components.ManagerBottomNavBar
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.state.UserSession
import com.kamerstay.app.viewmodel.ManagerViewModel
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun ManagerProfileScreen(navController: NavController) {

    val viewModel = koinViewModel<ManagerViewModel>()
    var showLogoutDialog by remember { mutableStateOf(false) }

    val managerName = viewModel.managerPersonalInfoState.fullName
    val managerInitials = managerName.trim().split("\\s+".toRegex())
        .filter { it.isNotEmpty() }.take(2)
        .joinToString("") { it.first().uppercaseChar().toString() }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Sign Out?", fontWeight = FontWeight.Bold, color = LocalAppColors.current.textPrimary) },
            text = { Text("Are you sure you want to sign out?", color = OnSurfaceSecondary) },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        UserSession.logout()
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
        bottomBar = { ManagerBottomNavBar(navController, currentRoute = "profile") }
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
                    IconButton(onClick = { navController.navigate(Routes.ManagerDashboard.route) }) {
                        Icon(
                            Icons.Outlined.Menu,
                            contentDescription = null,
                            tint = Secondary
                        )
                    }
                    Text(
                        text = "Hotel Manager",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Secondary
                    )
                }
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .border(2.5.dp, Primary, CircleShape)
                        .background(OnSurfaceSecondary.copy(0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = managerInitials,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Secondary
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ── Profile Avatar ────────────────────
                Box(modifier = Modifier.size(110.dp)) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(3.dp, Primary, CircleShape)
                            .background(OnSurfaceSecondary.copy(0.2f))
                            .align(Alignment.Center),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = managerInitials,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Secondary
                        )
                    }
                    // Online dot
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(Primary)
                            .border(2.dp, Color.White, CircleShape)
                            .align(Alignment.BottomEnd)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = managerName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Secondary
                )

                Text(
                    text = "Property Manager${viewModel.managedHotel?.name?.let { " • $it" } ?: ""}",
                    fontSize = 14.sp,
                    color = OnSurfaceSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Badges
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(OnSurfaceSecondary.copy(0.1f))
                            .padding(horizontal = 12.dp, vertical = 5.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Outlined.Place,
                                contentDescription = null,
                                tint = OnSurfaceSecondary,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Douala Region",
                                fontSize = 12.sp,
                                color = OnSurfaceSecondary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Primary.copy(0.12f))
                            .padding(horizontal = 12.dp, vertical = 5.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Outlined.Verified,
                                contentDescription = null,
                                tint = Secondary,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Verified",
                                fontSize = 12.sp,
                                color = Secondary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ── Menu Items Card ───────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(LocalAppColors.current.surface)
                ) {
                    Column {
                        ManagerProfileItem(
                            icon = Icons.Outlined.Person,
                            title = "Personal Information",
                            onClick = { navController.navigate(Routes.ManagerPersonalInfo.route) }
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = Divider
                        )
                        ManagerProfileItem(
                            icon = Icons.Outlined.Lock,
                            title = "Security & Password",
                            onClick = { navController.navigate(Routes.ForgotPassword.route) }
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = Divider
                        )
                        ManagerProfileItem(
                            icon = Icons.Outlined.Payments,
                            title = "Payment Methods",
                            onClick = { navController.navigate(Routes.PaymentMethods.route) }
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = Divider
                        )
                        ManagerProfileItem(
                            icon = Icons.Outlined.Badge,
                            title = "Professional Credentials",
                            onClick = { navController.navigate(Routes.ManagerVerification.route) }
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = Divider
                        )
                        ManagerProfileItem(
                            icon = Icons.Outlined.Language,
                            title = "Language Preferences",
                            onClick = { navController.navigate(Routes.ManagerSettings.route) }
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = Divider
                        )
                        ManagerProfileItem(
                            icon = Icons.Outlined.Hotel,
                            title = "Hotel Amenities",
                            onClick = { navController.navigate(Routes.HotelAmenities.route) }
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = Divider
                        )
                        ManagerProfileItem(
                            icon = Icons.Outlined.PrivacyTip,
                            title = "Privacy & Terms",
                            onClick = { navController.navigate(Routes.PrivacyTerms.route) }
                        )
                        ManagerProfileItem(
                            icon = Icons.Outlined.HelpOutline,
                            title = "Help & Support",
                            onClick = { navController.navigate(Routes.ManagerSupport.route) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Sign Out Button ───────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, ErrorColor.copy(0.3f), RoundedCornerShape(14.dp))
                        .background(ErrorColor.copy(0.05f))
                        .clickable { showLogoutDialog = true }
                        .padding(horizontal = 20.dp, vertical = 18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.AutoMirrored.Outlined.Logout,
                            contentDescription = null,
                            tint = ErrorColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Sign Out",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ErrorColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // ── Version ───────────────────────────
                Text(
                    text = "Version 2.4.1 (Build 4920)",
                    fontSize = 12.sp,
                    color = OnSurfaceSecondary.copy(0.5f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// ── Manager Profile Item ──────────────────────────────────
@Composable
fun ManagerProfileItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Primary.copy(0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = Secondary,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = title,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = LocalAppColors.current.textPrimary,
            modifier = Modifier.weight(1f)
        )
        Icon(
            Icons.Outlined.ChevronRight,
            contentDescription = null,
            tint = OnSurfaceSecondary,
            modifier = Modifier.size(20.dp)
        )
    }
}