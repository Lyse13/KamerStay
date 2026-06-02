package com.kamerstay.app.features.traveler

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import androidx.compose.ui.text.input.KeyboardType
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
fun BookingScreen(
    navController: NavController,
    hotelId: String,
    roomId: String
) {
    val viewModel = koinViewModel<TravelerViewModel>()
    val state = viewModel.bookingState

    val hotel = MockData.getHotelById(hotelId) ?: MockData.hotels.first()
    val room = MockData.rooms.find { it.id == roomId } ?: MockData.rooms.first()

    val nights = 3
    val pricePerNight = room.pricePerNight
    val roomTotal = pricePerNight * nights
    val taxesFees = roomTotal * 0.085
    val totalTTC = roomTotal + taxesFees
    val acompte = totalTTC * 0.20
    val soldeRestant = totalTTC - acompte

    Scaffold(
        containerColor = BackgroundLight,
        topBar = {
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
                        text = "Confirm Booking",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                }
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(OnSurfaceSecondary.copy(0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.Bookmark,
                        contentDescription = null,
                        tint = OnSurfaceSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        },
        bottomBar = {
            Box(
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
                    Column {
                        Text(
                            text = "Solde restant",
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary
                        )
                        Text(
                            text = "${soldeRestant.toInt()}€",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextDark
                        )
                    }
                    Button(
                        onClick = {
                            NavigationState.selectedBookingId = "BK-${hotel.id}-${room.id}"
                            navController.navigate(
                                Routes.Payment.createRoute(
                                    NavigationState.selectedBookingId
                                )
                            )
                        },
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 14.dp)
                    ) {
                        Text(
                            text = "Confirmer & Payer",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OnPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = OnPrimary,
                            modifier = Modifier.size(16.dp)
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
            Spacer(modifier = Modifier.height(8.dp))

            // ── Hotel + Room Summary ──────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .padding(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Room image
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF1A3A5C),
                                        Color(0xFF0D2A4A)
                                    )
                                )
                            )
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = hotel.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Secondary
                        )
                        Text(
                            text = "Deluxe Ocean View Suite",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Primary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                Icons.Outlined.CalendarMonth,
                                contentDescription = null,
                                tint = OnSurfaceSecondary,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "12 Oct - 15 Oct 2023",
                                fontSize = 13.sp,
                                color = OnSurfaceSecondary
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                Icons.Outlined.People,
                                contentDescription = null,
                                tint = OnSurfaceSecondary,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "2 Adultes, 1 Chambre",
                                fontSize = 13.sp,
                                color = OnSurfaceSecondary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Traveler Details ──────────────────────
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "Détails du Voyageur",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Secondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        // Full Name
                        BookingTextField(
                            label = "Nom Complet",
                            value = state.specialRequests,
                            placeholder = "Ex: Jean Dupont",
                            onValueChange = { state.specialRequests = it }
                        )

                        // Email
                        BookingTextField(
                            label = "E-mail de contact",
                            value = "",
                            placeholder = "jean.dupont@email.fr",
                            onValueChange = { },
                            keyboardType = KeyboardType.Email
                        )

                        // Phone
                        BookingTextField(
                            label = "Numéro de Téléphone",
                            value = "",
                            placeholder = "+33 6 12 34 56 78",
                            onValueChange = { },
                            keyboardType = KeyboardType.Phone
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Special Requests ──────────────────────
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "Demandes Spéciales",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Secondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = state.specialRequests,
                    onValueChange = { state.specialRequests = it },
                    placeholder = {
                        Text(
                            "Arrivée tardive, allergies, etc...",
                            color = OnSurfaceSecondary.copy(0.5f),
                            fontSize = 14.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = Divider,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        cursorColor = Primary
                    ),
                    maxLines = 4
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Price Breakdown ───────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(BackgroundLight)
                    .padding(16.dp)
            ) {
                Column {
                    BookingPriceRow2(
                        label = "$nights Nuits x ${pricePerNight.toInt()}€",
                        amount = "${roomTotal.toInt()}.00€"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    BookingPriceRow2(
                        label = "Taxes et Frais",
                        amount = "${taxesFees.toInt()}.50€"
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = Divider
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total (TTC)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Secondary
                        )
                        Text(
                            text = "${totalTTC.toInt()}.50€",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Secondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Partial Deposit Card ──────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Primary.copy(0.08f))
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Primary.copy(0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Outlined.Payments,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "Acompte Partiel",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Secondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Payez seulement l'acompte aujourd'hui pour garantir votre réservation.",
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary,
                            lineHeight = 17.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "${acompte.toInt()}.00€",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Primary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "à payer maintenant",
                                fontSize = 12.sp,
                                color = OnSurfaceSecondary,
                                modifier = Modifier.padding(bottom = 3.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// ── Booking Text Field ────────────────────────────────────
@Composable
fun BookingTextField(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column {
        Text(
            text = label,
            fontSize = 12.sp,
            color = OnSurfaceSecondary,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(placeholder, color = OnSurfaceSecondary.copy(0.4f), fontSize = 14.sp)
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
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true
        )
    }
}

// ── Booking Price Row ─────────────────────────────────────
@Composable
fun BookingPriceRow2(label: String, amount: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 14.sp, color = OnSurfaceSecondary)
        Text(
            text = amount,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )
    }
}