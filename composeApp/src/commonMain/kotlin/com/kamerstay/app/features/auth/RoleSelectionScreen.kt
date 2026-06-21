package com.kamerstay.app.features.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import com.kamerstay.app.core.navigation.NavigationState
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.core.utils.APP_NAME

@Composable
fun RoleSelectionScreen(navController: NavController) {

    var selectedRole by remember { mutableStateOf("") }

    Scaffold(containerColor = LocalAppColors.current.background) { paddingValues ->
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
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Logo box
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Secondary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.Landscape,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Text(
                    text = APP_NAME,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Secondary
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Main Card ─────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(LocalAppColors.current.surface)
                    .padding(24.dp)
            ) {
                Column {
                    // Hero text
                    Text(
                        text = "Welcome to the future of hospitality.",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = LocalAppColors.current.textPrimary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Whether you're looking for your next breathtaking escape or looking to elevate your property's reach, we have the tools you need.",
                        fontSize = 15.sp,
                        color = OnSurfaceSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    // Choose path label
                    Text(
                        text = "Choose your path",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = LocalAppColors.current.textPrimary
                    )
                    Text(
                        text = "Select an account type to get started.",
                        fontSize = 13.sp,
                        color = OnSurfaceSecondary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // ── Traveler Option ───────────────
                    RoleOptionCard(
                        icon = Icons.Outlined.Explore,
                        iconBg = Secondary.copy(0.12f),
                        title = "Traveler",
                        description = "Explore curated stays, book unique experiences, and manage your global trips effortlessly.",
                        isSelected = selectedRole == "TRAVELER",
                        onClick = { selectedRole = "TRAVELER" }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // ── Manager Option ────────────────
                    RoleOptionCard(
                        icon = Icons.Outlined.BusinessCenter,
                        iconBg = Primary.copy(0.12f),
                        title = "Hotel Manager",
                        description = "List your properties, manage staff roles, monitor bookings, and grow your hospitality brand.",
                        isSelected = selectedRole == "MANAGER",
                        onClick = { selectedRole = "MANAGER" }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // ── Continue Button ───────────────
                    Button(
                        onClick = {
                            if (selectedRole.isNotEmpty()) {
                                NavigationState.pendingRole = selectedRole
                                navController.navigate(Routes.SignIn.route)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedRole.isEmpty())
                                Primary.copy(0.5f) else Primary,
                            disabledContainerColor = Primary.copy(0.4f)
                        ),
                        enabled = selectedRole.isNotEmpty()
                    ) {
                        Text(
                            text = "Continue",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (selectedRole.isEmpty())
                                OnPrimary.copy(0.6f) else OnPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "→",
                            fontSize = 18.sp,
                            color = if (selectedRole.isEmpty())
                                OnPrimary.copy(0.6f) else OnPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "By continuing, you agree to $APP_NAME's Terms of Service and Privacy Policy.",
                        fontSize = 12.sp,
                        color = OnSurfaceSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 17.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// ── Role Option Card ──────────────────────────────────────
@Composable
fun RoleOptionCard(
    icon: ImageVector,
    iconBg: Color,
    title: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(
                if (isSelected) Primary.copy(0.04f) else BackgroundLight
            )
            .border(
                if (isSelected) 1.5.dp else 1.dp,
                if (isSelected) Primary else Divider,
                RoundedCornerShape(14.dp)
            )
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = Secondary,
                    modifier = Modifier.size(26.dp)
                )
            }

            // Text
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalAppColors.current.textPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 13.sp,
                    color = OnSurfaceSecondary,
                    lineHeight = 18.sp
                )
            }

            // Radio button
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .border(
                        2.dp,
                        if (isSelected) Primary else Divider,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(Primary)
                    )
                }
            }
        }
    }
}