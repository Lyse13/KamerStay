package com.kamerstay.app.features.traveler

import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.NavigationState
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.mock.MockData
import com.kamerstay.app.viewmodel.TravelerViewModel
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun BookingConfirmationScreen(
    navController: NavController,
    bookingId: String
) {
    val viewModel = koinViewModel<TravelerViewModel>()
    val hotel = MockData.getHotelById(NavigationState.selectedHotelId) ?: MockData.hotels.first()
    val room = MockData.rooms.find { it.id == NavigationState.selectedRoomId }
        ?: MockData.rooms.first()

    val scale = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        )
    }

    val amenities = listOf(
        Icons.Outlined.Wifi to "Free Wi-Fi",
        Icons.Outlined.Pool to "Infinity Pool",
        Icons.Outlined.LocalParking to "Valet Parking",
        Icons.Outlined.FreeBreakfast to "Breakfast Incl."
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp)
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
                        Icons.Outlined.Close,
                        contentDescription = "Close",
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

            // ── Success Icon ──────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .scale(scale.value)
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.CheckCircle,
                        contentDescription = null,
                        tint = OnPrimary,
                        modifier = Modifier.size(44.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Pack your bags!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextDark,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = buildAnnotatedString {
                        append("Your stay at ")
                        withStyle(SpanStyle(
                            color = Secondary,
                            fontWeight = FontWeight.SemiBold
                        )) {
                            append(hotel.name)
                        }
                        append(" is confirmed and ready for your arrival.")
                    },
                    fontSize = 14.sp,
                    color = OnSurfaceSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ── Hotel Card ────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                ) {
                    Column {
                        // Room image
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0xFF1A2A3A),
                                            Color(0xFF0D1A28)
                                        )
                                    )
                                )
                        )

                        Column(modifier = Modifier.padding(16.dp)) {
                            // Confirmed badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Primary.copy(0.12f))
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = "CONFIRMED",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Primary
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = hotel.name,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextDark
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    Icons.Outlined.Place,
                                    contentDescription = null,
                                    tint = OnSurfaceSecondary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = hotel.address,
                                    fontSize = 13.sp,
                                    color = OnSurfaceSecondary
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Dates
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(24.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "CHECK-IN",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = OnSurfaceSecondary,
                                        letterSpacing = 0.5.sp
                                    )
                                    Text(
                                        text = "Oct 24, 2024",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextDark
                                    )
                                }
                                Column {
                                    Text(
                                        text = "CHECK-OUT",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = OnSurfaceSecondary,
                                        letterSpacing = 0.5.sp
                                    )
                                    Text(
                                        text = "Oct 28, 2024",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextDark
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Booking Reference Card ────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(20.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "BOOKING REFERENCE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Primary,
                            letterSpacing = 0.8.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "MS-8829-X",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Secondary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // QR code placeholder
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, Divider, RoundedCornerShape(12.dp))
                                .background(BackgroundLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                // QR arrows decoration
                                Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                                    Icon(
                                        Icons.Outlined.ArrowBack,
                                        contentDescription = null,
                                        tint = Primary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Icon(
                                        Icons.Outlined.ArrowForward,
                                        contentDescription = null,
                                        tint = Primary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Icon(
                                    Icons.Outlined.QrCode,
                                    contentDescription = null,
                                    tint = OnSurfaceSecondary,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Show this at check-in",
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Amenities ─────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    amenities.chunked(2).forEach { rowItems ->
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            rowItems.forEach { (icon, label) ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.White)
                                        .padding(12.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            icon,
                                            contentDescription = null,
                                            tint = Secondary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Text(
                                            text = label,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = TextDark
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ── View Voucher Button ───────────────
                Button(
                    onClick = { navController.navigate(Routes.BookingVoucher.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Secondary
                    )
                ) {
                    Icon(
                        Icons.Outlined.Receipt,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "View Voucher",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // ── Go to Home Button ─────────────────
                OutlinedButton(
                    onClick = {
                        navController.navigate(Routes.TravelerHome.route) {
                            popUpTo(Routes.TravelerHome.route) { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(28.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Divider),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = TextDark
                    )
                ) {
                    Icon(
                        Icons.Outlined.Home,
                        contentDescription = null,
                        tint = TextDark,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Go to Home",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextDark
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ── Footer note ───────────────────────
                Text(
                    text = buildAnnotatedString {
                        append("A confirmation email has been sent to ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = TextDark)) {
                            append("traveler@example.com")
                        }
                        append(". Need to make changes? You can manage your booking in the ")
                        withStyle(SpanStyle(
                            color = Primary,
                            fontWeight = FontWeight.SemiBold
                        )) {
                            append("Profile")
                        }
                        append(" section.")
                    },
                    fontSize = 12.sp,
                    color = OnSurfaceSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )
            }
        }
    }
}