package com.kamerstay.app.features.traveler

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import com.kamerstay.app.data.model.CancellationPolicyType
import com.kamerstay.app.viewmodel.TravelerViewModel
import org.koin.compose.viewmodel.koinViewModel

private val WarningColor = Color(0xFFFFB74D)

@Composable
fun BookingCancellationScreen(
    navController: NavController,
    bookingId: String
) {
    val viewModel = koinViewModel<TravelerViewModel>()
    val state = viewModel.cancellationState

    if (state.isConfirmed) {
        CancellationSuccessContent(navController, state.refund.estimatedRefund)
        return
    }

    Scaffold(
        containerColor = BackgroundLight,
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { state.isConfirmed = true },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorColor)
                ) {
                    Text("Confirm Cancellation", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                }
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(28.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Secondary),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Secondary)
                ) {
                    Text("Keep My Booking", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Secondary)
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
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Secondary)
                }
                Text("Cancel Booking", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextDark)
            }

            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // ── Warning Banner ────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(ErrorColor.copy(0.08f))
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Icon(Icons.Outlined.Warning, contentDescription = null, tint = ErrorColor, modifier = Modifier.size(22.dp))
                        Column {
                            Text("Are you sure?", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = ErrorColor)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Cancelling this booking may incur fees depending on our cancellation policy.",
                                fontSize = 13.sp,
                                color = ErrorColor.copy(0.8f),
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                // ── Booking Summary ───────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("BOOKING SUMMARY", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = OnSurfaceSecondary, letterSpacing = 0.8.sp)
                        HorizontalDivider(color = Divider)
                        CancellationRow("Booking ID", state.summary.bookingId)
                        CancellationRow("Hotel", state.summary.hotelName)
                        CancellationRow("Room", state.summary.roomType)
                        CancellationRow("Check-in", state.summary.checkIn)
                        CancellationRow("Check-out", state.summary.checkOut)
                        CancellationRow("Guests", state.summary.guests)
                        HorizontalDivider(color = Divider)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Paid", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextDark)
                            Text(state.summary.totalPaid, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Secondary)
                        }
                    }
                }

                // ── Cancellation Policy ───────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("CANCELLATION POLICY", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = OnSurfaceSecondary, letterSpacing = 0.8.sp)
                        HorizontalDivider(color = Divider)
                        state.policies.forEach { policy ->
                            val (icon, color) = when (policy.type) {
                                CancellationPolicyType.FREE -> Icons.Outlined.CheckCircle to Primary
                                CancellationPolicyType.PARTIAL -> Icons.Outlined.Info to WarningColor
                                CancellationPolicyType.NO_REFUND -> Icons.Outlined.Cancel to ErrorColor
                            }
                            PolicyItem(icon = icon, iconColor = color, text = policy.description)
                        }
                    }
                }

                // ── Refund Breakdown ──────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("REFUND ESTIMATE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = OnSurfaceSecondary, letterSpacing = 0.8.sp)
                        HorizontalDivider(color = Divider)
                        CancellationRow("Amount paid", state.refund.totalPaid)
                        CancellationRow(state.refund.feeLabel, state.refund.cancellationFee)
                        HorizontalDivider(color = Divider)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Estimated refund", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextDark)
                            Text(state.refund.estimatedRefund, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Primary)
                        }
                        Text(state.refund.refundNote, fontSize = 12.sp, color = OnSurfaceSecondary, lineHeight = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

// ── Success Screen ────────────────────────────────────────
@Composable
private fun CancellationSuccessContent(navController: NavController, refundAmount: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(ErrorColor.copy(0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.Cancel, contentDescription = null, tint = ErrorColor, modifier = Modifier.size(52.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Booking Cancelled", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = TextDark, textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Your booking has been cancelled. A refund of $refundAmount will be processed within 5-7 business days.",
            fontSize = 14.sp,
            color = OnSurfaceSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(36.dp))

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

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(
            onClick = {
                navController.navigate(Routes.TravelerHome.route) { popUpTo(0) { inclusive = true } }
            }
        ) {
            Text("Go to Home", fontSize = 14.sp, color = OnSurfaceSecondary)
        }
    }
}

// ── Helpers ───────────────────────────────────────────────
@Composable
private fun CancellationRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontSize = 14.sp, color = OnSurfaceSecondary)
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextDark)
    }
}

@Composable
private fun PolicyItem(icon: ImageVector, iconColor: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(18.dp))
        Text(text, fontSize = 13.sp, color = TextDark, lineHeight = 18.sp, modifier = Modifier.weight(1f))
    }
}