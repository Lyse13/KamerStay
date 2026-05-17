package com.kamerstay.app.features.traveler

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.NavigationState
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.mock.MockData

@Composable
fun PaymentScreen(
    navController: NavController,
    bookingId: String
) {
    val hotel = MockData.getHotelById(NavigationState.selectedHotelId) ?: MockData.hotels.first()
    val room = MockData.rooms.find { it.id == NavigationState.selectedRoomId } ?: MockData.rooms.first()

    val totalAmount = room.pricePerNight * 3
    val depositAmount = totalAmount * 0.20 // 20% deposit
    val remainingAmount = totalAmount - depositAmount

    var selectedMethod by remember { mutableStateOf("MTN") }
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = WarmIvory,
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total Deposit Due",
                        fontSize = 14.sp,
                        color = OnSurfaceVariant
                    )
                    Text(
                        text = "${depositAmount.toLong()} XAF",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = OnSurface
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        isLoading = true
                        navController.navigate(Routes.BookingConfirmation.route) {
                            popUpTo(Routes.TravelerHome.route)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DeepEmerald
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            Icons.Outlined.Lock,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Pay Deposit",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
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
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = OnSurface
                    )
                }
                Text(
                    text = "Payment",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepEmerald
                )
            }

            HorizontalDivider(color = Divider)

            Spacer(modifier = Modifier.height(20.dp))

            // ── Deposit Summary Card ──────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                text = "RESERVATION DEPOSIT",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = OnSurfaceVariant,
                                letterSpacing = 0.8.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${depositAmount.toLong()} XAF",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = OnSurface
                            )
                        }
                        // Money icon box
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(WarmAmber.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Outlined.Payments,
                                contentDescription = null,
                                tint = WarmAmber,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = Divider
                    )

                    // Amount to pay now
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Amount to Pay Now (Deposit)",
                            fontSize = 13.sp,
                            color = OnSurfaceVariant
                        )
                        Text(
                            text = "${depositAmount.toLong()} XAF",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Remaining balance
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "Remaining Balance",
                            fontSize = 13.sp,
                            color = OnSurfaceVariant
                        )
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${remainingAmount.toLong()} XAF",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = WarmAmber
                            )
                            Text(
                                text = "(Pay at Hotel)",
                                fontSize = 11.sp,
                                color = WarmAmber,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Payment Methods ───────────────────────
            Text(
                text = "Select Payment Method",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurface,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // MTN Mobile Money
            PaymentMethodCard(
                isSelected = selectedMethod == "MTN",
                onClick = { selectedMethod = "MTN" },
                color = Color(0xFFFFC107),
                label = "MTN Mobile Money",
                subtitle = "Instant processing",
                emoji = "📱"
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Orange Money
            PaymentMethodCard(
                isSelected = selectedMethod == "ORANGE",
                onClick = { selectedMethod = "ORANGE" },
                color = Color(0xFFFF6600),
                label = "Orange Money",
                subtitle = "Instant processing",
                emoji = "🟠"
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── Secure Badge ──────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Outlined.Shield,
                    contentDescription = null,
                    tint = OnSurfaceVariant.copy(0.5f),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "SECURE ENCRYPTED PAYMENT",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = OnSurfaceVariant.copy(0.5f),
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ── Payment Method Card ───────────────────────────────────
@Composable
fun PaymentMethodCard(
    isSelected: Boolean,
    onClick: () -> Unit,
    color: Color,
    label: String,
    subtitle: String,
    emoji: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .then(
                if (isSelected) Modifier.border(
                    width = 1.5.dp,
                    color = DeepEmerald,
                    shape = RoundedCornerShape(14.dp)
                ) else Modifier
            )
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Logo box
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, fontSize = 26.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = OnSurface
                )
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = OnSurfaceVariant
                )
            }

            if (isSelected) {
                Icon(
                    Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = DeepEmerald,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}