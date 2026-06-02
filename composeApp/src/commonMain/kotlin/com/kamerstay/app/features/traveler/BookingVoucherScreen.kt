package com.kamerstay.app.features.traveler

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.NavigationState
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.mock.MockData
import com.kamerstay.app.viewmodel.TravelerViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BookingVoucherScreen(navController: NavController) {

    val viewModel = koinViewModel<TravelerViewModel>()
    val hotel = MockData.getHotelById(NavigationState.selectedHotelId)
        ?: MockData.hotels.first()

    Scaffold(
        containerColor = BackgroundLight
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
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Secondary
                        )
                    }
                    Text(
                        text = "MyStays",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Secondary
                    )
                }
                // Print Voucher button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, Divider, RoundedCornerShape(20.dp))
                        .background(Color.White)
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            Icons.Outlined.Print,
                            contentDescription = null,
                            tint = TextDark,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Print Voucher",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextDark
                        )
                    }
                }
            }

            // ── Hero Header Card ──────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF092031),
                                Color(0xFF003761)
                            )
                        )
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Text(
                        text = "BOOKING VOUCHER",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(0.6f),
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = hotel.name,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        lineHeight = 32.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Outlined.Place,
                            contentDescription = null,
                            tint = Color.White.copy(0.7f),
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = hotel.address,
                            fontSize = 12.sp,
                            color = Color.White.copy(0.7f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Booking ID
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Primary)
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Column {
                            Text(
                                text = "BOOKING ID",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnPrimary.copy(0.7f),
                                letterSpacing = 0.8.sp
                            )
                            Text(
                                text = "MS-8829-QX",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = OnPrimary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Guest + Status ────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "PRIMARY GUEST",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceSecondary,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "Alexander J.\nSterling",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextDark,
                            lineHeight = 22.sp
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "BOOKING STATUS",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceSecondary,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                Icons.Outlined.CheckCircle,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "CONFIRMED",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Primary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Check-in / Check-out Card ─────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .padding(20.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Check-in
                    Text(
                        text = "CHECK-IN",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurfaceSecondary,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = "Oct 24",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextDark
                    )
                    Text(
                        text = "Thursday, 3:00 PM",
                        fontSize = 13.sp,
                        color = OnSurfaceSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Nights badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Primary)
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "4 NIGHTS",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Check-out
                    Text(
                        text = "CHECK-OUT",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurfaceSecondary,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = "Oct 28",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextDark
                    )
                    Text(
                        text = "Monday, 11:00 AM",
                        fontSize = 13.sp,
                        color = OnSurfaceSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Room + Meal ───────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    VoucherDetailRow(
                        icon = Icons.Outlined.Hotel,
                        label = "ROOM CATEGORY",
                        value = "Panoramic Ocean Suite",
                        subValue = "Floor 12, Room 1204"
                    )
                    HorizontalDivider(color = Divider)
                    VoucherDetailRow(
                        icon = Icons.Outlined.Restaurant,
                        label = "MEAL PLAN",
                        value = "Half-Board Included",
                        subValue = "Breakfast & Dinner"
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Amenities ─────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        VoucherAmenity(
                            icon = Icons.Outlined.Wifi,
                            label = "Free Wi-Fi",
                            modifier = Modifier.weight(1f)
                        )
                        VoucherAmenity(
                            icon = Icons.Outlined.Pool,
                            label = "Infinity Pool",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        VoucherAmenity(
                            icon = Icons.Outlined.LocalParking,
                            label = "Valet Parking",
                            modifier = Modifier.weight(1f)
                        )
                        VoucherAmenity(
                            icon = Icons.Outlined.FitnessCenter,
                            label = "24/7 Gym",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── QR Code ───────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "EXPRESS CHECK-IN CODE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurfaceSecondary,
                        letterSpacing = 0.8.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // QR container
                    Box(
                        modifier = Modifier
                            .size(160.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Secondary)
                            .border(2.dp, Primary, RoundedCornerShape(12.dp))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Dashed inner box
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .border(
                                    2.dp,
                                    Color.White.copy(0.4f),
                                    RoundedCornerShape(8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Outlined.QrCode,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Guest count
                    Text(
                        text = "GUEST COUNT",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurfaceSecondary,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "2 Adults, 1 Child",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Present this QR code or Booking ID upon\narrival at the concierge desk.",
                        fontSize = 12.sp,
                        color = OnSurfaceSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 17.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Policy Info ───────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    PolicyRow(
                        icon = Icons.Outlined.Badge,
                        title = "Identification",
                        description = "Passport or valid ID required at check-in."
                    )
                    HorizontalDivider(color = Divider)
                    PolicyRow(
                        icon = Icons.Outlined.Cancel,
                        title = "Cancellation",
                        description = "Free cancellation until 48h before arrival."
                    )
                    HorizontalDivider(color = Divider)
                    PolicyRow(
                        icon = Icons.Outlined.SupportAgent,
                        title = "Support",
                        description = "24/7 Concierge: +1(800) MYSTAYS"
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Offline Access Card ───────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Primary.copy(0.08f))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Secondary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Outlined.CheckCircle,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "Offline Access Enabled",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Secondary
                        )
                        Text(
                            text = "This voucher is saved to your device. You can access it without an internet connection in your \"Bookings\" tab.",
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary,
                            lineHeight = 17.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Footer Banner ─────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF092031),
                                Color(0xFF0D1A28)
                            )
                        )
                    )
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = "See you in ${hotel.city}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = "Tap to view resort guide and dining menus",
                            fontSize = 12.sp,
                            color = Color.White.copy(0.6f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ── Voucher Detail Row ────────────────────────────────────
@Composable
fun VoucherDetailRow(
    icon: ImageVector,
    label: String,
    value: String,
    subValue: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
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
        Column {
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurfaceSecondary,
                letterSpacing = 0.5.sp
            )
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Text(
                text = subValue,
                fontSize = 12.sp,
                color = OnSurfaceSecondary
            )
        }
    }
}

// ── Voucher Amenity ───────────────────────────────────────
@Composable
fun VoucherAmenity(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = TextDark,
            fontWeight = FontWeight.Medium
        )
    }
}

// ── Policy Row ────────────────────────────────────────────
@Composable
fun PolicyRow(
    icon: ImageVector,
    title: String,
    description: String
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = Secondary,
            modifier = Modifier.size(18.dp)
        )
        Column {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDark
            )
            Text(
                text = description,
                fontSize = 12.sp,
                color = OnSurfaceSecondary
            )
        }
    }
}