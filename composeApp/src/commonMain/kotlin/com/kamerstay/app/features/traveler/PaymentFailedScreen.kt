package com.kamerstay.app.features.traveler

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.core.utils.APP_NAME
import com.kamerstay.app.data.mock.PaymentMethodsMockData
import com.kamerstay.app.data.model.AlternativeHotel
import com.kamerstay.app.viewmodel.TravelerViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PaymentFailedScreen(navController: NavController) {

    val viewModel = koinViewModel<TravelerViewModel>()
    val state = viewModel.paymentFailedState

    Scaffold(
        containerColor = LocalAppColors.current.background
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
                Text(
                    text = APP_NAME,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Secondary,
                    modifier = Modifier.padding(start = 12.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    Icons.Outlined.Notifications,
                    contentDescription = null,
                    tint = Secondary,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
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

                Spacer(modifier = Modifier.height(16.dp))

                // ── Error Card ────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(LocalAppColors.current.surface)
                        .padding(24.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Error icon
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(ErrorColor.copy(0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Outlined.Warning,
                                contentDescription = null,
                                tint = ErrorColor,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Payment Declined",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = LocalAppColors.current.textPrimary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = state.errorMessage,
                            fontSize = 14.sp,
                            color = OnSurfaceSecondary,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Retry button
                        Button(
                            onClick = { state.isRetrying = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(28.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Secondary
                            )
                        ) {
                            if (state.isRetrying) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    Icons.Outlined.Refresh,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Retry Payment",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Change Payment Method
                        OutlinedButton(
                            onClick = { navController.navigate(Routes.Payment.createRoute("failed")) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(28.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Divider),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = LocalAppColors.current.textPrimary
                            )
                        ) {
                            Icon(
                                Icons.Outlined.CreditCard,
                                contentDescription = null,
                                tint = OnSurfaceSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Change Payment Method",
                                fontSize = 15.sp,
                                color = LocalAppColors.current.textPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        HorizontalDivider(color = Divider)

                        Spacer(modifier = Modifier.height(16.dp))

                        // Booking info
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color(0xFF1A2A3A),
                                                Color(0xFF0D1A28)
                                            )
                                        )
                                    )
                            )

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = state.bookingName,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = LocalAppColors.current.textPrimary
                                )
                                Text(
                                    text = state.bookingDetails,
                                    fontSize = 12.sp,
                                    color = OnSurfaceSecondary
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "AMOUNT DUE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OnSurfaceSecondary,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "\$${state.amountDue}",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = LocalAppColors.current.textPrimary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // ── Need Help ─────────────────────────
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
                                Icons.Outlined.HelpOutline,
                                contentDescription = null,
                                tint = Secondary,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Need help?",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = LocalAppColors.current.textPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Our concierge is available 24/7 to help resolve payment issues.",
                            fontSize = 13.sp,
                            color = OnSurfaceSecondary,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Contact Support →",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Primary,
                            modifier = Modifier.clickable { navController.navigate(Routes.TravelerSupport.route) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // ── Security Note ─────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(LocalAppColors.current.background)
                        .border(1.dp, Divider, RoundedCornerShape(12.dp))
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Outlined.Shield,
                            contentDescription = null,
                            tint = OnSurfaceSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Your transaction is protected with 256-bit SSL encryption.",
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ── Alternatives ──────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Still looking? Explore\nalternatives",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = LocalAppColors.current.textPrimary,
                        lineHeight = 24.sp
                    )
                    Text(
                        text = "View\nall",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Primary,
                        modifier = Modifier.clickable {
                            navController.navigate(Routes.HotelSearch.route)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    PaymentMethodsMockData.alternativeHotels.forEach { hotel ->
                        AlternativeHotelCard(
                            hotel = hotel,
                            onClick = {
                                navController.navigate(
                                    Routes.HotelDetails.createRoute(hotel.id)
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ── Alternative Hotel Card ────────────────────────────────
@Composable
fun AlternativeHotelCard(
    hotel: AlternativeHotel,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(LocalAppColors.current.surface)
            .clickable { onClick() }
    ) {
        Column {
            // Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                    .background(
                        brush = Brush.verticalGradient(colors = hotel.gradientColors)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.Black.copy(0.5f))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "${"%,d".format(hotel.pricePerNight).replace(",", " ")} FCFA/nuit",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = hotel.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
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
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = hotel.location,
                        fontSize = 12.sp,
                        color = OnSurfaceSecondary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    hotel.tags.forEach { tag ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(LocalAppColors.current.background)
                                .border(1.dp, Divider, RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = tag,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}