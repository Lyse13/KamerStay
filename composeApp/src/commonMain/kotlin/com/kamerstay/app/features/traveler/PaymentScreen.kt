package com.kamerstay.app.features.traveler

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.DrawableResource
import com.kamerstay.app.viewmodel.TravelerViewModel
import kamerstay.composeapp.generated.resources.Res
import kamerstay.composeapp.generated.resources.mtn
import kamerstay.composeapp.generated.resources.orange
import org.koin.compose.viewmodel.koinViewModel
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Composable
fun PaymentScreen(
    navController: NavController,
    bookingId: String
) {
    val viewModel    = koinViewModel<TravelerViewModel>()
    val state        = viewModel.paymentState
    val bookingState = viewModel.bookingState
    val hotel        = viewModel.selectedHotel
    val room         = viewModel.hotelRooms.find { it.id == NavigationState.selectedRoomId }
        ?: viewModel.hotelRooms.firstOrNull()

    // If hotel/rooms are missing (rare navigation edge case), reload them
    LaunchedEffect(NavigationState.selectedHotelId) {
        if (hotel == null || viewModel.hotelRooms.isEmpty()) {
            viewModel.loadHotelDetail(NavigationState.selectedHotelId)
        }
    }

    if (hotel == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                CircularProgressIndicator(color = Secondary)
                Text("Chargement des données de réservation…", fontSize = 13.sp, color = OnSurfaceSecondary)
            }
        }
        return
    }

    val pricePerNight = room?.pricePerNight ?: hotel.pricePerNight
    val nights = bookingState.nights.takeIf { it > 0 } ?: 1
    val roomTotal = pricePerNight * nights
    val taxesFees = roomTotal * 0.085
    val totalTTC = roomTotal + taxesFees
    val depositAmount = totalTTC * 0.20

    Scaffold(
        containerColor = LocalAppColors.current.background,
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
                    text = "KamerStay",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Secondary
                )
            }
        },
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
                    color = LocalAppColors.current.textPrimary
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
                    .background(LocalAppColors.current.surface)
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Mobile Money",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = LocalAppColors.current.textPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // MTN MoMo
                    MobileMoneyOption(
                        isSelected = state.selectedMethod == "MTN",
                        onClick = { state.selectedMethod = "MTN" },
                        logo = Res.drawable.mtn,
                        name = "MTN MoMo",
                        subtitle = "Prompt USSD instantané"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Orange Money
                    MobileMoneyOption(
                        isSelected = state.selectedMethod == "ORANGE",
                        onClick = { state.selectedMethod = "ORANGE" },
                        logo = Res.drawable.orange,
                        name = "Orange Money",
                        subtitle = "Sécurisé & rapide"
                    )

                    // Champ numéro de téléphone (visible uniquement pour Mobile Money)
                    if (state.isMobileMoney) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Numéro ${if (state.selectedMethod == "MTN") "MTN" else "Orange"}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = LocalAppColors.current.textPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = state.phoneNumber,
                            onValueChange = { if (it.length <= 9) state.phoneNumber = it.filter { c -> c.isDigit() } },
                            placeholder = {
                                Text("6XXXXXXXX", color = OnSurfaceSecondary.copy(0.4f))
                            },
                            leadingIcon = {
                                Text(
                                    "+237",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = OnSurfaceSecondary,
                                    modifier = Modifier.padding(start = 4.dp)
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = if (state.isPhoneValid) Primary else ErrorColor,
                                unfocusedBorderColor = if (state.phoneNumber.isEmpty()) Divider
                                    else if (state.isPhoneValid) Primary.copy(0.5f) else ErrorColor.copy(0.5f),
                                focusedContainerColor = LocalAppColors.current.surface,
                                unfocusedContainerColor = LocalAppColors.current.surface,
                                cursorColor = Primary
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true,
                            isError = state.phoneNumber.isNotEmpty() && !state.isPhoneValid
                        )
                        if (state.phoneNumber.isNotEmpty() && !state.isPhoneValid) {
                            Text(
                                text = "Entrez un numéro valide (ex: 691234567)",
                                fontSize = 12.sp,
                                color = ErrorColor,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Credit/Debit Card Section ─────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(16.dp))
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
                            text = "Credit / Debit Card",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = LocalAppColors.current.textPrimary
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(OnSurfaceSecondary.copy(0.12f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Prochainement",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OnSurfaceSecondary
                                )
                            }
                            Icon(
                                Icons.Outlined.CreditCard,
                                contentDescription = null,
                                tint = OnSurfaceSecondary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Card Number
                    Text(
                        text = "Card Number",
                        fontSize = 12.sp,
                        color = OnSurfaceSecondary.copy(0.5f)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = state.cardNumber,
                        onValueChange = { },
                        placeholder = {
                            Text(
                                "XXXX XXXX XXXX XXXX",
                                color = OnSurfaceSecondary.copy(0.25f)
                            )
                        },
                        trailingIcon = {
                            Icon(
                                Icons.Outlined.Lock,
                                contentDescription = null,
                                tint = OnSurfaceSecondary.copy(0.4f),
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledBorderColor = Divider.copy(0.5f),
                            disabledContainerColor = LocalAppColors.current.surface.copy(0.5f),
                            disabledTextColor = OnSurfaceSecondary.copy(0.4f),
                            disabledPlaceholderColor = OnSurfaceSecondary.copy(0.25f)
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
                                color = OnSurfaceSecondary.copy(0.5f)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = state.expiryDate,
                                onValueChange = { },
                                placeholder = {
                                    Text("MM / YY", color = OnSurfaceSecondary.copy(0.25f))
                                },
                                enabled = false,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    disabledBorderColor = Divider.copy(0.5f),
                                    disabledContainerColor = LocalAppColors.current.surface.copy(0.5f),
                                    disabledTextColor = OnSurfaceSecondary.copy(0.4f),
                                    disabledPlaceholderColor = OnSurfaceSecondary.copy(0.25f)
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
                                color = OnSurfaceSecondary.copy(0.5f)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = state.cvv,
                                onValueChange = { },
                                placeholder = {
                                    Text("***", color = OnSurfaceSecondary.copy(0.25f))
                                },
                                enabled = false,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    disabledBorderColor = Divider.copy(0.5f),
                                    disabledContainerColor = LocalAppColors.current.surface.copy(0.5f),
                                    disabledTextColor = OnSurfaceSecondary.copy(0.4f),
                                    disabledPlaceholderColor = OnSurfaceSecondary.copy(0.25f)
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
                    .background(LocalAppColors.current.surface)
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
                                text = "$nights ${if (nights == 1) "nuit" else "nuits"} × ${pricePerNight.toInt()} FCFA",
                                fontSize = 14.sp,
                                color = OnSurfaceSecondary
                            )
                            Text(
                                text = "${roomTotal.toInt()} FCFA",
                                fontSize = 14.sp,
                                color = LocalAppColors.current.textPrimary,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Taxes & frais (8,5%)",
                                fontSize = 14.sp,
                                color = OnSurfaceSecondary
                            )
                            Text(
                                text = "${taxesFees.toInt()} FCFA",
                                fontSize = 14.sp,
                                color = LocalAppColors.current.textPrimary,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = Divider
                        )

                        // Total
                        Text(
                            text = "TOTAL TTC",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceSecondary,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "${totalTTC.toInt()} FCFA",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = LocalAppColors.current.textPrimary
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
                                        text = "Acompte à payer (20%)",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = LocalAppColors.current.textPrimary
                                    )
                                    Text(
                                        text = "Solde dû au check-in",
                                        fontSize = 12.sp,
                                        color = OnSurfaceSecondary
                                    )
                                }
                                Text(
                                    text = "${depositAmount.toInt()} FCFA",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Secondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Bandeau d'attente USSD
                        if (state.isPolling) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Secondary.copy(0.08f))
                                    .padding(14.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(18.dp),
                                            color = Secondary,
                                            strokeWidth = 2.dp
                                        )
                                        Text(
                                            text = state.statusMessage,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Secondary
                                        )
                                    }
                                    Text(
                                        text = "Vérifiez votre téléphone et confirmez le paiement via le prompt USSD.",
                                        fontSize = 12.sp,
                                        color = OnSurfaceSecondary,
                                        lineHeight = 17.sp
                                    )
                                    TextButton(
                                        onClick = { viewModel.cancelPaymentPolling() },
                                        contentPadding = PaddingValues(0.dp)
                                    ) {
                                        Text("Annuler", fontSize = 12.sp, color = ErrorColor)
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        // Message d'erreur
                        if (state.error != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(ErrorColor.copy(0.08f))
                                    .padding(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        Icons.Outlined.ErrorOutline,
                                        contentDescription = null,
                                        tint = ErrorColor,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = state.error ?: "",
                                        fontSize = 13.sp,
                                        color = ErrorColor,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        // Bouton payer
                        val canPay = !state.isLoading && !state.isPolling &&
                            (!state.isMobileMoney || state.isPhoneValid)

                        Button(
                            onClick = {
                                state.error = null
                                if (state.isMobileMoney) {
                                    val timestamp = kotlin.uuid.Uuid.random().toString().takeLast(8)
                                    val bookingRef = viewModel.createdBooking?.bookingReference
                                        ?: "KS-$timestamp"

                                    viewModel.initiatePayment(
                                        amount      = depositAmount,
                                        description = "Acompte réservation $bookingRef — ${hotel.name}",
                                        onSuccess   = {
                                            navController.navigate(
                                                Routes.BookingConfirmation.createRoute(bookingId)
                                            ) { popUpTo(Routes.TravelerHome.route) }
                                        },
                                        onFailure   = { msg ->
                                            state.error = msg
                                            navController.navigate(Routes.PaymentFailed.route)
                                        }
                                    )
                                } else {
                                    // Paiement par carte (placeholder — intégration future)
                                    state.error = "Le paiement par carte sera disponible prochainement."
                                }
                            },
                            enabled = canPay,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(28.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Secondary,
                                disabledContainerColor = Secondary.copy(0.4f)
                            )
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
                                    text = "Payer ${depositAmount.toInt()} FCFA",
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
                    .background(LocalAppColors.current.background)
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
                        text = "Your payment information is encrypted and processed securely by KamerStay.",
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
    logo: DrawableResource,
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
        Image(
            painter = painterResource(logo),
            contentDescription = name,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = LocalAppColors.current.textPrimary
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