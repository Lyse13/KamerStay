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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.kamerstay.app.core.navigation.NavigationState
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.viewmodel.TravelerViewModel
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun BookingConfirmationScreen(
    navController: NavController,
    bookingId: String
) {


    val viewModel = koinViewModel<TravelerViewModel>()
    // APRÈS
    val booking = viewModel.createdBooking
    val hotel = viewModel.selectedHotel

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
            .background(LocalAppColors.current.background)
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
                    text = "KamerStay",
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
                    color = LocalAppColors.current.textPrimary,
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
                            append(hotel?.name ?: "")
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
                        .background(LocalAppColors.current.surface)
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
                                        colors = listOf(Color(0xFF1A2A3A), Color(0xFF0D1A28))
                                    )
                                )
                        ) {
                            val imageUrl = hotel?.imageUrls?.firstOrNull() ?: ""
                            if (imageUrl.isNotEmpty()) {
                                AsyncImage(
                                    model = imageUrl,
                                    contentDescription = hotel?.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }

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
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Primary
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = hotel?.name ?: "",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = LocalAppColors.current.textPrimary
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
                                    text = hotel?.address ?: "",
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
                                        text = booking?.checkInDate ?: "",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = LocalAppColors.current.textPrimary
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
                                        text = booking?.checkOutDate ?: "",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = LocalAppColors.current.textPrimary
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
                        .background(LocalAppColors.current.surface)
                        .padding(20.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "BOOKING REFERENCE",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Primary,
                            letterSpacing = 0.8.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = booking?.bookingReference ?: "KS-XXXXXXXX",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Secondary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        val bookingRef = booking?.bookingReference ?: "KS-XXXXXXXX"
                        BookingQrCode(
                            data = bookingRef,
                            modifier = Modifier.size(130.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Présentez votre référence à l'accueil",
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary,
                            textAlign = TextAlign.Center
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
                                        .background(LocalAppColors.current.surface)
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
                                            color = LocalAppColors.current.textPrimary
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
                        contentColor = LocalAppColors.current.textPrimary
                    )
                ) {
                    Icon(
                        Icons.Outlined.Home,
                        contentDescription = null,
                        tint = LocalAppColors.current.textPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Go to Home",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = LocalAppColors.current.textPrimary
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ── Footer note ───────────────────────
                Text(
                    text = buildAnnotatedString {
                        append("A confirmation email has been sent to ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = LocalAppColors.current.textPrimary)) {
                            append(com.kamerstay.app.data.state.UserSession.email.ifBlank { "votre email" })
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

// ── QR Code visuel (basé sur la référence de réservation) ─────
@Composable
fun BookingQrCode(data: String, modifier: Modifier = Modifier) {
    val modules = 21
    val seed    = data.hashCode().toLong()
    val rng     = kotlin.random.Random(seed)
    val matrix  = Array(modules) { r ->
        BooleanArray(modules) { c ->
            val inTL = r < 8 && c < 8
            val inTR = r < 8 && c >= modules - 8
            val inBL = r >= modules - 8 && c < 8
            when {
                inTL || inTR || inBL -> false
                else                 -> rng.nextBoolean()
            }
        }
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            val cell = size.width / modules
            val dark = Color(0xFF1A2A3A)

            fun drawFinder(startCol: Int, startRow: Int) {
                for (r in 0..6) for (c in 0..6) {
                    val fill = r == 0 || r == 6 || c == 0 || c == 6 || (r in 2..4 && c in 2..4)
                    if (fill) drawRect(
                        color    = dark,
                        topLeft  = Offset((startCol + c) * cell, (startRow + r) * cell),
                        size     = Size(cell, cell)
                    )
                }
            }

            drawFinder(0, 0)
            drawFinder(modules - 7, 0)
            drawFinder(0, modules - 7)

            for (r in 0 until modules) for (c in 0 until modules) {
                if (matrix[r][c]) drawRect(
                    color    = dark,
                    topLeft  = Offset(c * cell + cell * 0.05f, r * cell + cell * 0.05f),
                    size     = Size(cell * 0.9f, cell * 0.9f)
                )
            }
        }
    }
}