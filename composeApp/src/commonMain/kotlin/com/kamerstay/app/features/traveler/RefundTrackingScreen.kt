package com.kamerstay.app.features.traveler

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.mock.CancellationMockData
import com.kamerstay.app.data.model.RefundStatus
import com.kamerstay.app.data.model.RefundStep

@Composable
fun RefundTrackingScreen(
    navController: NavController,
    bookingId: String
) {
    val tracking = CancellationMockData.refundTracking

    val (statusLabel, statusColor, statusIcon) = when (tracking.status) {
        RefundStatus.INITIATED -> Triple("Refund Initiated", Primary, Icons.Outlined.Schedule)
        RefundStatus.PROCESSING -> Triple("Processing", Color(0xFFFFB74D), Icons.Outlined.Autorenew)
        RefundStatus.COMPLETED -> Triple("Refunded", Secondary, Icons.Outlined.CheckCircle)
        RefundStatus.FAILED -> Triple("Failed", ErrorColor, Icons.Outlined.ErrorOutline)
    }

    Scaffold(
        containerColor = LocalAppColors.current.background,
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LocalAppColors.current.surface)
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        navController.navigate(Routes.BookingHistory.route) {
                            popUpTo(Routes.BookingHistory.route) { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Secondary)
                ) {
                    Text("Back to Bookings", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                }
                OutlinedButton(
                    onClick = { navController.navigate(Routes.TravelerSupport.route) },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(28.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Secondary),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Secondary)
                ) {
                    Icon(Icons.Outlined.SupportAgent, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Contact Support", fontSize = 15.sp, fontWeight = FontWeight.Medium)
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
            // ── Top Bar ───────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Secondary)
                }
                Text(
                    text = "Refund Status",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalAppColors.current.textPrimary
                )
            }

            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ── Status Banner ─────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(statusColor.copy(0.1f))
                        .padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(statusColor.copy(0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(statusIcon, contentDescription = null, tint = statusColor, modifier = Modifier.size(28.dp))
                        }
                        Column {
                            Text(
                                text = statusLabel,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = statusColor
                            )
                            Text(
                                text = "Est. arrival: ${tracking.estimatedArrival}",
                                fontSize = 13.sp,
                                color = statusColor.copy(0.8f)
                            )
                        }
                    }
                }

                // ── Refund Summary Card ───────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(LocalAppColors.current.surface)
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            "REFUND DETAILS",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceSecondary,
                            letterSpacing = 0.8.sp
                        )
                        HorizontalDivider(color = Divider)

                        RefundInfoRow("Booking ID", tracking.bookingId)
                        RefundInfoRow("Hotel", tracking.hotelName)
                        RefundInfoRow("Cancelled on", tracking.cancellationDate)
                        RefundInfoRow("Refund to", tracking.paymentMethod)

                        HorizontalDivider(color = Divider)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Refund Amount",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = LocalAppColors.current.textPrimary
                            )
                            Text(
                                tracking.refundAmount,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Secondary
                            )
                        }
                    }
                }

                // ── Timeline ──────────────────────────────
                Text(
                    "REFUND TIMELINE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurfaceSecondary,
                    letterSpacing = 0.8.sp
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(LocalAppColors.current.surface)
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    Column {
                        tracking.steps.forEachIndexed { index, step ->
                            RefundTimelineStep(
                                step = step,
                                isLast = index == tracking.steps.lastIndex
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

// ── Timeline Step ─────────────────────────────────────────────
@Composable
private fun RefundTimelineStep(step: RefundStep, isLast: Boolean) {
    val stepColor = when {
        step.isCompleted -> Secondary
        step.isCurrent -> Primary
        else -> OnSurfaceSecondary.copy(0.3f)
    }

    val pulse = rememberInfiniteTransition(label = "pulse")
    val pulseScale by pulse.animateFloat(
        initialValue = 1f,
        targetValue = if (step.isCurrent) 1.25f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Row(modifier = Modifier.fillMaxWidth()) {
        // ── Left: connector + circle ──────────────────
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(32.dp)
        ) {
            // Step circle
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .scale(if (step.isCurrent) pulseScale else 1f)
                    .clip(CircleShape)
                    .background(
                        when {
                            step.isCompleted -> Secondary
                            step.isCurrent -> Primary
                            else -> LocalAppColors.current.background
                        }
                    )
                    .then(
                        if (!step.isCompleted && !step.isCurrent)
                            Modifier.background(OnSurfaceSecondary.copy(0.1f))
                        else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                when {
                    step.isCompleted -> Icon(
                        Icons.Outlined.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    step.isCurrent -> Icon(
                        Icons.Outlined.Autorenew,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    else -> Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(OnSurfaceSecondary.copy(0.3f))
                    )
                }
            }

            // Connector line
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(52.dp)
                        .background(
                            if (step.isCompleted) Secondary.copy(0.4f)
                            else Divider
                        )
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // ── Right: text ───────────────────────────────
        Column(
            modifier = Modifier
                .padding(bottom = if (isLast) 0.dp else 16.dp)
                .padding(top = 2.dp)
        ) {
            Text(
                text = step.title,
                fontSize = 15.sp,
                fontWeight = if (step.isCompleted || step.isCurrent) FontWeight.SemiBold else FontWeight.Normal,
                color = when {
                    step.isCompleted -> LocalAppColors.current.textPrimary
                    step.isCurrent -> Primary
                    else -> OnSurfaceSecondary.copy(0.5f)
                }
            )
            Text(
                text = step.subtitle,
                fontSize = 12.sp,
                color = OnSurfaceSecondary.copy(if (step.isCompleted || step.isCurrent) 1f else 0.5f),
                lineHeight = 16.sp
            )
            step.date?.let { date ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = date,
                    fontSize = 12.sp,
                    color = if (step.isCompleted) Secondary else Primary.copy(0.8f),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// ── Helper ────────────────────────────────────────────────────
@Composable
private fun RefundInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 14.sp, color = OnSurfaceSecondary)
        Text(
            value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = LocalAppColors.current.textPrimary,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f, fill = false)
        )
    }
}