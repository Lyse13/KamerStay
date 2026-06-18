package com.kamerstay.app.features.traveler

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.viewmodel.TravelerViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BookingReviewScreen(navController: NavController) {

    val viewModel = koinViewModel<TravelerViewModel>()
    val state = viewModel.bookingReviewState
    val booking = state.booking
    var showPromoDialog by remember { mutableStateOf(false) }
    var promoInput by remember { mutableStateOf("") }

    if (showPromoDialog) {
        AlertDialog(
            onDismissRequest = { showPromoDialog = false },
            title = { Text("Add Promo Code", fontWeight = FontWeight.Bold, color = LocalAppColors.current.textPrimary) },
            text = {
                OutlinedTextField(
                    value = promoInput,
                    onValueChange = { promoInput = it.uppercase() },
                    placeholder = { Text("Enter code (e.g. STAY20)", color = OnSurfaceSecondary.copy(0.5f)) },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = Divider,
                        focusedContainerColor = LocalAppColors.current.surface,
                        unfocusedContainerColor = LocalAppColors.current.surface,
                        cursorColor = Primary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = { showPromoDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    shape = RoundedCornerShape(10.dp)
                ) { Text("Apply", color = OnPrimary) }
            },
            dismissButton = {
                TextButton(onClick = { showPromoDialog = false; promoInput = "" }) {
                    Text("Cancel", color = Secondary)
                }
            },
            containerColor = LocalAppColors.current.surface,
            shape = RoundedCornerShape(16.dp)
        )
    }

    fun formatPrice(value: Double): String {
        val intPart = value.toLong()
        val decPart = ((value - intPart) * 100).toLong()
        return "$intPart.${decPart.toString().padStart(2, '0')}"
    }

    Scaffold(
        containerColor = LocalAppColors.current.background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
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
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LocalAppColors.current.surface)
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Total Price",
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary
                        )
                        Text(
                            text = "\$${formatPrice(booking.totalUSD)}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = LocalAppColors.current.textPrimary
                        )
                    }
                    Button(
                        onClick = {
                            state.isLoading = true
                            navController.navigate(Routes.Payment.createRoute("review"))
                        },
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary),
                        contentPadding = PaddingValues(horizontal = 28.dp, vertical = 14.dp)
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = OnPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Pay Now",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = OnPrimary
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // ── Header ────────────────────────────────
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                    Text(
                        text = "Review your booking",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = LocalAppColors.current.textPrimary
                    )
                    Text(
                        text = "Please check your details before proceeding to payment.",
                        fontSize = 13.sp,
                        color = OnSurfaceSecondary,
                        lineHeight = 18.sp
                    )
                }
            }

            // ── Hotel Card ────────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(LocalAppColors.current.surface)
                ) {
                    Column {
                        // Image
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 16.dp,
                                        topEnd = 16.dp
                                    )
                                )
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0xFF1A3A5C),
                                            Color(0xFF0D2A4A)
                                        )
                                    )
                                )
                        )

                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = booking.hotelName,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Secondary,
                                    modifier = Modifier.weight(1f)
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(Primary.copy(0.1f))
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            Icons.Outlined.StarOutline,
                                            contentDescription = null,
                                            tint = StarRating,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Text(
                                            text = booking.rating.toString(),
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Secondary
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    Icons.Outlined.Place,
                                    contentDescription = null,
                                    tint = OnSurfaceSecondary,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = booking.location,
                                    fontSize = 13.sp,
                                    color = OnSurfaceSecondary
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                booking.amenities.forEach { amenity ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(20.dp))
                                            .border(
                                                1.dp, Divider,
                                                RoundedCornerShape(20.dp)
                                            )
                                            .padding(horizontal = 10.dp, vertical = 5.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            val icon = when (amenity) {
                                                "Wi-Fi" -> Icons.Outlined.Wifi
                                                "Pool" -> Icons.Outlined.Pool
                                                "Breakfast" -> Icons.Outlined.FreeBreakfast
                                                else -> Icons.Outlined.CheckCircle
                                            }
                                            Icon(
                                                icon,
                                                contentDescription = null,
                                                tint = Secondary,
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Text(
                                                text = amenity,
                                                fontSize = 12.sp,
                                                color = LocalAppColors.current.textPrimary,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // ── Info Cards ────────────────────────────
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Dates
                    ReviewInfoCard(
                        icon = Icons.Outlined.CalendarMonth,
                        label = "Dates"
                    ) {
                        Text(
                            text = "${booking.checkIn} — ${booking.checkOut}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = LocalAppColors.current.textPrimary
                        )
                        Text(
                            text = "${booking.nights} nights",
                            fontSize = 13.sp,
                            color = OnSurfaceSecondary
                        )
                    }

                    // Guests
                    ReviewInfoCard(
                        icon = Icons.Outlined.People,
                        label = "Guests"
                    ) {
                        Text(
                            text = "${booking.guests} Adults",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = LocalAppColors.current.textPrimary
                        )
                        Text(
                            text = "${booking.rooms} Room",
                            fontSize = 13.sp,
                            color = OnSurfaceSecondary
                        )
                    }

                    // Room Type
                    ReviewInfoCard(
                        icon = Icons.Outlined.Hotel,
                        label = "Room Type"
                    ) {
                        Text(
                            text = booking.roomType,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = LocalAppColors.current.textPrimary
                        )
                        Text(
                            text = booking.roomDetail,
                            fontSize = 13.sp,
                            color = OnSurfaceSecondary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // ── Primary Guest ─────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
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
                                text = "Primary Guest",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = LocalAppColors.current.textPrimary
                            )
                            Text(
                                text = "Edit details",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Primary,
                                modifier = Modifier.clickable { navController.popBackStack() }
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        GuestDetailRow("FULL NAME", state.fullName)
                        Spacer(modifier = Modifier.height(10.dp))
                        GuestDetailRow("EMAIL ADDRESS", state.email)
                        Spacer(modifier = Modifier.height(10.dp))
                        GuestDetailRow("PHONE NUMBER", state.phone)
                        Spacer(modifier = Modifier.height(10.dp))
                        GuestDetailRow("SPECIAL REQUESTS", state.specialRequests)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // ── Price Summary ─────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Secondary, DeepBlue)
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Text(
                            text = "Price summary",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        PriceSummaryRow(
                            label = "\$${booking.pricePerNight.toInt()}.00 x ${booking.nights} nights",
                            amount = "\$${formatPrice(booking.roomTotal)}"
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        PriceSummaryRow(
                            label = "Service fee",
                            amount = "\$${formatPrice(booking.serviceFee)}"
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        PriceSummaryRow(
                            label = "Taxes & occupancy fees",
                            amount = "\$${formatPrice(booking.taxesFees)}"
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = Color.White.copy(0.2f)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Column {
                                Text(
                                    text = "Total (USD)",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(0.7f)
                                )
                                Text(
                                    text = "\$${formatPrice(booking.totalUSD)}",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            }
                            Text(
                                text = "Includes all taxes",
                                fontSize = 12.sp,
                                color = Color.White.copy(0.6f),
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Cancellation policy
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White.copy(0.1f))
                                .padding(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Outlined.Info,
                                    contentDescription = null,
                                    tint = Primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Column {
                                    Text(
                                        text = "Cancellation policy",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "Free cancellation before Oct 10. After that, non-refundable.",
                                        fontSize = 12.sp,
                                        color = Color.White.copy(0.7f),
                                        lineHeight = 17.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Continue to Payment
                        Button(
                            onClick = {
                                state.isLoading = true
                                navController.navigate(Routes.Payment.createRoute("review"))
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(28.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Primary)
                        ) {
                            Text(
                                text = "Continue to Payment",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = OnPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Secure 256-bit SSL encrypted connection",
                            fontSize = 12.sp,
                            color = Color.White.copy(0.5f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // ── Coupon ────────────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(LocalAppColors.current.surface)
                        .clickable { showPromoDialog = true }
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Primary.copy(0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Outlined.LocalOffer,
                                    contentDescription = null,
                                    tint = Secondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Have a coupon?",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = LocalAppColors.current.textPrimary
                                )
                                Text(
                                    text = "Apply code at next step",
                                    fontSize = 12.sp,
                                    color = OnSurfaceSecondary
                                )
                            }
                        }
                        Icon(
                            Icons.Outlined.ChevronRight,
                            contentDescription = null,
                            tint = OnSurfaceSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ── Review Info Card ──────────────────────────────────────
@Composable
fun ReviewInfoCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(LocalAppColors.current.surface)
            .padding(16.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Primary.copy(0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = Secondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text(
                    text = label,
                    fontSize = 13.sp,
                    color = OnSurfaceSecondary,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

// ── Guest Detail Row ──────────────────────────────────────
@Composable
fun GuestDetailRow(label: String, value: String) {
    Column {
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = OnSurfaceSecondary,
            letterSpacing = 0.8.sp
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = value,
            fontSize = 15.sp,
            color = LocalAppColors.current.textPrimary,
            fontWeight = FontWeight.Medium
        )
    }
}

// ── Price Summary Row ─────────────────────────────────────
@Composable
fun PriceSummaryRow(label: String, amount: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 14.sp, color = Color.White.copy(0.8f))
        Text(
            text = amount,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}