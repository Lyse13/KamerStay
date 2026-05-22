package com.kamerstay.app.features.traveler

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.NavigationState
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.mock.MockData

@Composable
fun BookingVoucherScreen(navController: NavController) {

    val hotel = MockData.getHotelById(NavigationState.selectedHotelId)
        ?: MockData.hotels.first()
    val room = MockData.rooms.find { it.id == NavigationState.selectedRoomId }
        ?: MockData.rooms.first()

    Scaffold(
        containerColor = WarmIvory
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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = OnSurface
                        )
                    }
                    Text(
                        text = "KamerStay",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepEmerald
                    )
                }
                // Offline Ready badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(SurfaceVariant)
                        .border(1.dp, OutlineVariant, RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.WifiOff,
                            contentDescription = null,
                            tint = OnSurfaceVariant,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "OFFLINE READY",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceVariant,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {

                // ── Header ────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = "Booking\nVoucher",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = OnSurface,
                            lineHeight = 34.sp
                        )
                        Text(
                            text = "Confirmed & stored on\ndevice",
                            fontSize = 12.sp,
                            color = OnSurfaceVariant,
                            lineHeight = 17.sp
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "VOUCHER ID",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OnSurfaceVariant,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = "TT-294-\nBUEA",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = OnSurface,
                            textAlign = TextAlign.End,
                            lineHeight = 22.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ── Hotel Card ────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White)
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        // Hotel image placeholder
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0xFF8B6914),
                                            Color(0xFF5C4A1E)
                                        )
                                    )
                                )
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Outlined.Verified,
                                    contentDescription = null,
                                    tint = WarmAmber,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "VERIFIED PARTNER",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = WarmAmber,
                                    letterSpacing = 0.5.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = hotel.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = OnSurface,
                                lineHeight = 22.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Outlined.Place,
                                    contentDescription = null,
                                    tint = OnSurfaceVariant,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = hotel.address,
                                    fontSize = 12.sp,
                                    color = OnSurfaceVariant,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── QR Code ───────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White)
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // QR Code simulé
                        Box(
                            modifier = Modifier
                                .size(160.dp)
                                .border(1.dp, OutlineVariant, RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            QRCodePattern()
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "SCAN AT RECEPTION",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OnSurfaceVariant,
                            letterSpacing = 1.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Guest Name ────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceVariant)
                        .padding(14.dp)
                ) {
                    Column {
                        Text(
                            text = "GUEST NAME",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OnSurfaceVariant,
                            letterSpacing = 0.8.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Samuel Eto'o Fils",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = OnSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // ── Check-in / Check-out ──────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    VoucherDateCard(
                        label = "CHECK-IN",
                        date = "Oct 24, 2024",
                        time = "After 02:00 PM",
                        modifier = Modifier.weight(1f)
                    )
                    VoucherDateCard(
                        label = "CHECK-OUT",
                        date = "Oct 27, 2024",
                        time = "Before 11:00 AM",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // ── Booking Details ───────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceVariant)
                        .padding(14.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "BOOKING DETAILS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = OnSurfaceVariant,
                                letterSpacing = 0.8.sp
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(StatusConfirmed.copy(alpha = 0.15f))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "PREPAID",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StatusConfirmed
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Room Type", fontSize = 13.sp, color = OnSurfaceVariant)
                            Text(
                                text = "Executive Safari Suite",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = OnSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Guests", fontSize = 13.sp, color = OnSurfaceVariant)
                            Text(
                                text = "2 Adults",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = OnSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Contact Info ──────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.Phone,
                            contentDescription = null,
                            tint = OnSurfaceVariant,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "+237 670 000 000",
                            fontSize = 13.sp,
                            color = OnSurfaceVariant
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = null,
                            tint = OnSurfaceVariant,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "T&C Apply",
                            fontSize = 13.sp,
                            color = OnSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ── Action Buttons ────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DeepEmerald
                        )
                    ) {
                        Icon(
                            Icons.Outlined.Download,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Save as Image",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }

                    OutlinedButton(
                        onClick = { },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp, OutlineVariant
                        )
                    ) {
                        Icon(
                            Icons.Outlined.Print,
                            contentDescription = null,
                            tint = OnSurface,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Print to PDF",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = OnSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Pro Tip Card ──────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(PrimaryContainer.copy(alpha = 0.4f))
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(DeepEmerald),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Outlined.Lightbulb,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Pro Tip for Travelers",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Screenshots are perfect for check-in where network might be unstable. This voucher contains all necessary verification data for offline use at the front desk.",
                                fontSize = 12.sp,
                                color = OnSurfaceVariant,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ── Footer ────────────────────────────
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HorizontalDivider(color = Divider)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "KAMERSTAY CAMEROON",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurfaceVariant.copy(0.5f),
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = "Premium Reliability. Local Soul.",
                        fontSize = 11.sp,
                        color = OnSurfaceVariant.copy(0.4f),
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// ── Voucher Date Card ─────────────────────────────────────
@Composable
fun VoucherDateCard(
    label: String,
    date: String,
    time: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceVariant)
            .padding(12.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.CalendarMonth,
                    contentDescription = null,
                    tint = OnSurfaceVariant,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = label,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = OnSurfaceVariant,
                    letterSpacing = 0.5.sp
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = date,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = OnSurface
            )
            Text(
                text = time,
                fontSize = 11.sp,
                color = OnSurfaceVariant
            )
        }
    }
}

// ── QR Code Pattern ───────────────────────────────────────
@Composable
fun QRCodePattern() {
    val blockColor = DeepEmerald.copy(alpha = 0.7f)
    val grid = listOf(
        listOf(1,1,1,0,1,1,1),
        listOf(1,0,1,0,1,0,1),
        listOf(1,1,1,0,1,1,1),
        listOf(0,0,0,0,0,0,0),
        listOf(1,1,1,0,1,0,1),
        listOf(0,0,1,0,0,0,1),
        listOf(1,1,1,0,1,1,1),
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        grid.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { cell ->
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(
                                if (cell == 1) blockColor else Color.Transparent
                            )
                    )
                }
            }
        }
    }
}