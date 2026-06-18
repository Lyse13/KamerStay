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
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.core.utils.APP_NAME
import com.kamerstay.app.data.mock.VerificationMockData
import com.kamerstay.app.viewmodel.ManagerViewModel
import org.koin.compose.viewmodel.koinViewModel
import com.kamerstay.app.core.components.ManagerBottomNavBar

@Composable
fun ManagerVerificationScreen(navController: NavController) {

    val viewModel = koinViewModel<ManagerViewModel>()
    val state = viewModel.verificationState

    Scaffold(
        containerColor = LocalAppColors.current.background,
        bottomBar = {
            ManagerBottomNavBar(navController = navController, currentRoute = "profile")
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
                        text = APP_NAME,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Secondary
                    )
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(OnSurfaceSecondary.copy(0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.Person,
                        contentDescription = null,
                        tint = OnSurfaceSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {

                // ── Header ────────────────────────────
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        Icons.Outlined.Shield,
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "SECURITY VERIFICATION",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Primary,
                        letterSpacing = 0.8.sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Get Your Verified\nBadge",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = LocalAppColors.current.textPrimary,
                    lineHeight = 32.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "To maintain the integrity of our premium hotel network, we require all managers to verify their identity and business registration.",
                    fontSize = 13.sp,
                    color = OnSurfaceSecondary,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // ── Progress ──────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(LocalAppColors.current.surface)
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Verification Status",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = LocalAppColors.current.textPrimary
                            )
                            Text(
                                text = "Step ${state.currentStep} of ${state.totalSteps}",
                                fontSize = 13.sp,
                                color = OnSurfaceSecondary
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        LinearProgressIndicator(
                            progress = { state.progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = Primary,
                            trackColor = Divider
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Uploading document: National ID / Passport",
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Official ID Document ──────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(LocalAppColors.current.surface)
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = "Official ID Document",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = LocalAppColors.current.textPrimary
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Front Side
                        Text(
                            text = "Front Side",
                            fontSize = 13.sp,
                            color = OnSurfaceSecondary,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        UploadBox(
                            isUploaded = state.frontIdUploaded,
                            onClick = { state.frontIdUploaded = !state.frontIdUploaded }
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Back Side
                        Text(
                            text = "Back Side",
                            fontSize = 13.sp,
                            color = OnSurfaceSecondary,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        UploadBox(
                            isUploaded = state.backIdUploaded,
                            onClick = { state.backIdUploaded = !state.backIdUploaded }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Business License ──────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(LocalAppColors.current.surface)
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = "Business License",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = LocalAppColors.current.textPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Upload your hotel's official operating license or registration certificate.",
                            fontSize = 13.sp,
                            color = OnSurfaceSecondary,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .border(
                                    1.5.dp,
                                    if (state.businessLicenseUploaded) Primary else Divider,
                                    RoundedCornerShape(12.dp)
                                )
                                .background(
                                    if (state.businessLicenseUploaded)
                                        Primary.copy(0.05f) else BackgroundLight
                                )
                                .clickable {
                                    state.businessLicenseUploaded =
                                        !state.businessLicenseUploaded
                                }
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    if (state.businessLicenseUploaded)
                                        Icons.Outlined.CheckCircle
                                    else Icons.Outlined.Description,
                                    contentDescription = null,
                                    tint = if (state.businessLicenseUploaded) Primary
                                    else OnSurfaceSecondary,
                                    modifier = Modifier.size(32.dp)
                                )
                                Text(
                                    text = if (state.businessLicenseUploaded)
                                        "Document uploaded"
                                    else "Click to select or drag and drop",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (state.businessLicenseUploaded) Primary
                                    else LocalAppColors.current.textPrimary
                                )
                                if (!state.businessLicenseUploaded) {
                                    Text(
                                        text = "PDF, JPEG or PNG (Max. 10MB)",
                                        fontSize = 12.sp,
                                        color = OnSurfaceSecondary
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ── Submit Button ─────────────────────
                Button(
                    onClick = {
                        state.isSubmitting = true
                        state.isSubmitted = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Secondary)
                ) {
                    if (state.isSubmitting && !state.isSubmitted) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Submit for Verification",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            Icons.Outlined.Send,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(28.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Divider),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = LocalAppColors.current.textPrimary)
                ) {
                    Text("Save as Draft", fontSize = 15.sp, color = LocalAppColors.current.textPrimary)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Requirements ──────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Primary.copy(0.08f))
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Outlined.Info,
                                contentDescription = null,
                                tint = Secondary,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Requirements",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = LocalAppColors.current.textPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        VerificationMockData.requirements.forEach { req ->
                            Row(
                                modifier = Modifier.padding(vertical = 3.dp),
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Outlined.CheckCircle,
                                    contentDescription = null,
                                    tint = Primary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = req,
                                    fontSize = 13.sp,
                                    color = OnSurfaceSecondary,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Profile Preview ───────────────────
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "PROFILE PREVIEW",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurfaceSecondary,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(LocalAppColors.current.surface)
                            .padding(14.dp)
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(CircleShape)
                                        .background(
                                            androidx.compose.ui.graphics.Brush.verticalGradient(
                                                colors = listOf(
                                                    Color(0xFF1A3A5C),
                                                    Color(0xFF0D2A4A)
                                                )
                                            )
                                        )
                                )
                                Column {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = "Akwa Palace Hotel",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = LocalAppColors.current.textPrimary
                                        )
                                        Icon(
                                            Icons.Outlined.Verified,
                                            contentDescription = null,
                                            tint = Primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Text(
                                        text = "Verified Partner since 2024",
                                        fontSize = 12.sp,
                                        color = OnSurfaceSecondary
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(color = Divider)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "\"Your profile will look like this once verified, boosting guest trust by up to 40%.\"",
                                fontSize = 12.sp,
                                color = OnSurfaceSecondary,
                                fontStyle = FontStyle.Italic,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // ── Security note ─────────────────────
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Outlined.Lock,
                        contentDescription = null,
                        tint = OnSurfaceSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Your data is encrypted and stored securely. We only use this information for verification purposes in accordance with our Privacy Policy.",
                        fontSize = 12.sp,
                        color = OnSurfaceSecondary,
                        lineHeight = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ── Upload Box ────────────────────────────────────────────
@Composable
fun UploadBox(isUploaded: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(
                1.5.dp,
                if (isUploaded) Primary else Divider,
                RoundedCornerShape(12.dp)
            )
            .background(
                if (isUploaded) Primary.copy(0.05f) else BackgroundLight
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                if (isUploaded) Icons.Outlined.CheckCircle
                else Icons.Outlined.AddAPhoto,
                contentDescription = null,
                tint = if (isUploaded) Primary else OnSurfaceSecondary,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = if (isUploaded) "Uploaded ✓" else "Upload Image",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = if (isUploaded) Primary else OnSurfaceSecondary
            )
        }
    }
}