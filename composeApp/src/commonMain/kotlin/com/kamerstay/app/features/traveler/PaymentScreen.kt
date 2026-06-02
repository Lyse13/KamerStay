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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import com.kamerstay.app.viewmodel.TravelerViewModel
import org.koin.compose.viewmodel.koinViewModel


fun formatPrice(value: Double): String {
    val intPart = value.toLong()
    val decPart = ((value - intPart) * 100).toLong()
    return "$intPart.${decPart.toString().padStart(2, '0')}"
}
@Composable
fun PaymentScreen(
    navController: NavController,
    bookingId: String
) {
    val viewModel = koinViewModel<TravelerViewModel>()
    val state = viewModel.paymentState

    val hotel = MockData.getHotelById(NavigationState.selectedHotelId)
        ?: MockData.hotels.first()
    val room = MockData.rooms.find { it.id == NavigationState.selectedRoomId }
        ?: MockData.rooms.first()

    val nights = 3
    val roomTotal = room.pricePerNight * nights
    val serviceFee = roomTotal * 0.068
    val totalAmount = roomTotal + serviceFee
    val depositAmount = totalAmount * 0.30

    Scaffold(
        containerColor = BackgroundLight,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
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
                    text = "MyStays",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Secondary
                )
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 0.dp
            ) {
                listOf(
                    Icons.Outlined.Home to "Home",
                    Icons.Outlined.Explore to "Explore",
                    Icons.Filled.BookOnline to "Bookings",
                    Icons.Outlined.Person to "Profile"
                ).forEachIndexed { index, (icon, label) ->
                    NavigationBarItem(
                        selected = index == 2,
                        onClick = {
                            when (index) {
                                0 -> navController.navigate(Routes.TravelerHome.route)
                                1 -> navController.navigate(Routes.HotelSearch.route)
                                3 -> navController.navigate(Routes.TravelerProfile.route)
                            }
                        },
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label, fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Secondary,
                            selectedTextColor = Secondary,
                            indicatorColor = Primary.copy(0.15f),
                            unselectedIconColor = OnSurfaceSecondary,
                            unselectedTextColor = OnSurfaceSecondary
                        )
                    )
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
            // ── Header ────────────────────────────────
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "Confirm & Pay",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextDark
                )
                Text(
                    text = "Secure your reservation with a partial deposit.",
                    fontSize = 13.sp,
                    color = OnSurfaceSecondary
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Mobile Money Section ──────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Mobile Money",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // MTN MoMo
                    MobileMoneyOption(
                        isSelected = state.selectedMethod == "MTN",
                        onClick = { state.selectedMethod = "MTN" },
                        logo = "MTN",
                        logoBg = Color(0xFFFFC107),
                        name = "MTN MoMo",
                        subtitle = "Instant confirmation"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Orange Money
                    MobileMoneyOption(
                        isSelected = state.selectedMethod == "ORANGE",
                        onClick = { state.selectedMethod = "ORANGE" },
                        logo = "OR",
                        logoBg = Color(0xFFFF6600),
                        name = "Orange Money",
                        subtitle = "Secure & fast"
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Credit/Debit Card Section ─────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Credit / Debit Card",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Icon(
                            Icons.Outlined.CreditCard,
                            contentDescription = null,
                            tint = OnSurfaceSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Card Number
                    Text(
                        text = "Card Number",
                        fontSize = 12.sp,
                        color = OnSurfaceSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = state.cardNumber,
                        onValueChange = { state.cardNumber = it },
                        placeholder = {
                            Text(
                                "XXXX XXXX XXXX XXXX",
                                color = OnSurfaceSecondary.copy(0.4f)
                            )
                        },
                        trailingIcon = {
                            Icon(
                                Icons.Outlined.Lock,
                                contentDescription = null,
                                tint = OnSurfaceSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Primary,
                            unfocusedBorderColor = Divider,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            cursorColor = Primary
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Expiry Date",
                                fontSize = 12.sp,
                                color = OnSurfaceSecondary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = state.expiryDate,
                                onValueChange = { state.expiryDate = it },
                                placeholder = {
                                    Text("MM / YY", color = OnSurfaceSecondary.copy(0.4f))
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Primary,
                                    unfocusedBorderColor = Divider,
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    cursorColor = Primary
                                ),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number
                                ),
                                singleLine = true
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "CVV",
                                fontSize = 12.sp,
                                color = OnSurfaceSecondary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = state.cvv,
                                onValueChange = { state.cvv = it },
                                placeholder = {
                                    Text("***", color = OnSurfaceSecondary.copy(0.4f))
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Primary,
                                    unfocusedBorderColor = Divider,
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    cursorColor = Primary
                                ),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number
                                ),
                                singleLine = true
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Booking Summary Card ──────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
            ) {
                Column {
                    // Hotel image
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF1A3A5C),
                                        Color(0xFF0D2A4A)
                                    )
                                )
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(12.dp)
                        ) {
                            Text(
                                text = hotel.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = hotel.address,
                                fontSize = 12.sp,
                                color = Color.White.copy(0.7f)
                            )
                        }
                    }

                    Column(modifier = Modifier.padding(16.dp)) {
                        // Line items
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "$nights nights × \$${room.pricePerNight.toInt()}.00",
                                fontSize = 14.sp,
                                color = OnSurfaceSecondary
                            )
                            Text(
                                text = "\$${roomTotal.toInt()}.00",
                                fontSize = 14.sp,
                                color = TextDark,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Service fee",
                                fontSize = 14.sp,
                                color = OnSurfaceSecondary
                            )
                            Text(
                                text = "\$${formatPrice(serviceFee)}",
                                fontSize = 14.sp,
                                color = TextDark,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = Divider
                        )

                        // Total
                        Text(
                            text = "TOTAL PRICE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceSecondary,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "\$${formatPrice(totalAmount)}",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextDark
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Deposit box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Primary.copy(0.08f))
                                .padding(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Pay Deposit Now (30%)",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextDark
                                    )
                                    Text(
                                        text = "Balance due at check-in",
                                        fontSize = 12.sp,
                                        color = OnSurfaceSecondary
                                    )
                                }
                                Text(
                                    text = "\$${formatPrice(depositAmount)}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Secondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Pay button
                        Button(
                            onClick = {
                                state.isLoading = true
                                navController.navigate(Routes.BookingConfirmation.route) {
                                    popUpTo(Routes.TravelerHome.route)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(28.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Secondary)
                        ) {
                            if (state.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    Icons.Outlined.Shield,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Pay \$${formatPrice(depositAmount)}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Cancellation is free until 24h before stay.",
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Security note ─────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(BackgroundLight)
                    .padding(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        Icons.Outlined.Shield,
                        contentDescription = null,
                        tint = OnSurfaceSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Your payment information is encrypted and processed securely by MyStays.",
                        fontSize = 12.sp,
                        color = OnSurfaceSecondary,
                        lineHeight = 17.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ── Mobile Money Option ───────────────────────────────────
@Composable
fun MobileMoneyOption(
    isSelected: Boolean,
    onClick: () -> Unit,
    logo: String,
    logoBg: Color,
    name: String,
    subtitle: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .border(
                1.dp,
                if (isSelected) Primary else Divider,
                RoundedCornerShape(10.dp)
            )
            .background(if (isSelected) Primary.copy(0.04f) else Color.White)
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(logoBg),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = logo,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDark
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = OnSurfaceSecondary
            )
        }

        if (isSelected) {
            Icon(
                Icons.Outlined.CheckCircle,
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}