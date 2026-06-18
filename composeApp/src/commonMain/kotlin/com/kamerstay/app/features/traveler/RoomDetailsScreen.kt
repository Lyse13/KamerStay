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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
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
fun RoomDetailsScreen(
    navController: NavController,
    roomId: String
) {
    val viewModel = koinViewModel<TravelerViewModel>()
    val room = MockData.rooms.find { it.id == roomId } ?: MockData.rooms.first()
    val hotel = MockData.getHotelById(room.hotelId) ?: MockData.hotels.first()

    var showShareDialog by remember { mutableStateOf(false) }
    var showFullDescription by remember { mutableStateOf(false) }

    if (showShareDialog) {
        AlertDialog(
            onDismissRequest = { showShareDialog = false },
            title = { Text("Share Room", fontWeight = FontWeight.Bold, color = LocalAppColors.current.textPrimary) },
            text = { Text("Copy the link below to share this room:\nkamerstay.cm/rooms/${room.id}", color = OnSurfaceSecondary, fontSize = 13.sp) },
            confirmButton = {
                Button(
                    onClick = { showShareDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    shape = RoundedCornerShape(10.dp)
                ) { Text("Done", color = OnPrimary) }
            },
            containerColor = LocalAppColors.current.surface,
            shape = RoundedCornerShape(16.dp)
        )
    }
    var selectedGuests by remember { mutableStateOf("2 Adults, 1 Child") }
    var guestsExpanded by remember { mutableStateOf(false) }

    val nights = 4
    val pricePerNight = room.pricePerNight
    val roomTotal = pricePerNight * nights
    val cleaningFee = 85.0
    val serviceFee = 120.0
    val total = roomTotal + cleaningFee + serviceFee

    val amenities = listOf(
        Icons.Outlined.Wifi to "Free Wi-Fi",
        Icons.Outlined.AcUnit to "Climate Control",
        Icons.Outlined.Pool to "Infinity Pool",
        Icons.Outlined.SupportAgent to "24/7 Service"
    )

    val roomFeatures = listOf(
        Triple(Icons.Outlined.Hotel, "The Sleeping Zone",
            "Memory foam king mattress with 800-thread-count Egyptian cotton linens and customizable pillow menu."),
        Triple(Icons.Outlined.Computer, "Executive Hub",
            "Ergonomic workstation featuring 1Gbps fiber internet, multi-port docking station, and soundproof acoustics.")
    )

    Scaffold(
        containerColor = LocalAppColors.current.background,
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
                            text = "\$${pricePerNight.toInt()}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = LocalAppColors.current.textPrimary
                        )
                        Text(
                            text = "/ night",
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary
                        )
                    }
                    Button(
                        onClick = {
                            NavigationState.selectedHotelId = hotel.id
                            NavigationState.selectedRoomId = room.id
                            navController.navigate(
                                Routes.Booking.createRoute(hotel.id, room.id)
                            )
                        },
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 14.dp)
                    ) {
                        Text(
                            text = "Check Availability",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OnPrimary
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // ── Hero Image ────────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF0D2A4A),
                                        Color(0xFF1A4A6A)
                                    )
                                )
                            )
                    )

                    // Top bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(0.3f))
                                .clickable { navController.popBackStack() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Text(
                            text = "KamerStay",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(0.3f))
                                    .clickable { showShareDialog = true },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Outlined.Share,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(0.3f))
                                    .clickable { viewModel.wishlistState.toggleFromHotel(hotel) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    if (viewModel.wishlistState.isInWishlist(hotel.id)) Icons.Outlined.Favorite
                                    else Icons.Outlined.FavoriteBorder,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }

            // ── Room Info ─────────────────────────────
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(LocalAppColors.current.surface)
                        .padding(20.dp)
                ) {
                    // Badge + Rating
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Secondary)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "PREMIUM SUITE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                Icons.Outlined.Star,
                                contentDescription = null,
                                tint = StarRating,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "${hotel.rating} (${hotel.reviewCount} reviews)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = LocalAppColors.current.textPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "The Orion Skyloft Suite",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = LocalAppColors.current.textPrimary,
                        lineHeight = 30.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

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
                            text = "${hotel.address}",
                            fontSize = 13.sp,
                            color = OnSurfaceSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Amenities grid
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        amenities.chunked(2).forEach { row ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                row.forEach { (icon, label) ->
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(LocalAppColors.current.background)
                                            .padding(12.dp)
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Icon(
                                                icon,
                                                contentDescription = null,
                                                tint = Secondary,
                                                modifier = Modifier.size(22.dp)
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = label,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = LocalAppColors.current.textPrimary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ── Description ───────────────────────────
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = "Experience Digital Hospitality",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = LocalAppColors.current.textPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    val desc = "Discover a sanctuary of modern elegance in the heart of the city. The Orion Skyloft Suite offers an unparalleled blend of high-tech convenience and artisanal comfort. Featuring automated lighting systems, voice-controlled concierge, and breathtaking panoramic views through floor-to-ceiling glass walls.\n\nWhether you're visiting for an intensive business sprint or a restorative urban escape, every detail has been meticulously curated to ensure a frictionless, refreshing stay. Immerse yourself in the soft Prussian blue accents and crisp Cyan tones that define our signature Orion Light style."

                    Text(
                        text = desc,
                        fontSize = 14.sp,
                        color = OnSurfaceSecondary,
                        lineHeight = 22.sp,
                        maxLines = if (showFullDescription) Int.MAX_VALUE else 5
                    )

                    Text(
                        text = if (showFullDescription) "Read less ∧" else "Read more ∨",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Primary,
                        modifier = Modifier
                            .clickable { showFullDescription = !showFullDescription }
                            .padding(top = 6.dp)
                    )
                }
            }

            // ── Room Features ─────────────────────────
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    roomFeatures.forEach { (icon, title, desc) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Primary.copy(0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    icon,
                                    contentDescription = null,
                                    tint = Secondary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = title,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = LocalAppColors.current.textPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = desc,
                                    fontSize = 13.sp,
                                    color = OnSurfaceSecondary,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                        HorizontalDivider(color = Divider)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // ── Booking Summary Card ──────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Secondary)
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = "\$${pricePerNight.toInt()}",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Text(
                                text = "/ night",
                                fontSize = 14.sp,
                                color = Color.White.copy(0.7f),
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Dates
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.White.copy(0.15f))
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = "CHECK-IN",
                                    fontSize = 10.sp,
                                    color = Color.White.copy(0.7f),
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "Oct 24, 2023",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.White.copy(0.15f))
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = "CHECK-OUT",
                                    fontSize = 10.sp,
                                    color = Color.White.copy(0.7f),
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "Oct 28, 2023",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Guests dropdown
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White.copy(0.15f))
                                .clickable { guestsExpanded = !guestsExpanded }
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = selectedGuests,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                                Icon(
                                    Icons.Outlined.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        HorizontalDivider(color = Color.White.copy(0.2f))

                        Spacer(modifier = Modifier.height(12.dp))

                        // Price breakdown
                        PriceLineItem(
                            label = "\$${pricePerNight.toInt()} x $nights nights",
                            amount = "\$${roomTotal.toInt()}"
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        PriceLineItem(label = "Cleaning fee", amount = "\$${cleaningFee.toInt()}")
                        Spacer(modifier = Modifier.height(6.dp))
                        PriceLineItem(label = "Service fee", amount = "\$${serviceFee.toInt()}")

                        Spacer(modifier = Modifier.height(10.dp))

                        HorizontalDivider(color = Color.White.copy(0.2f))

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Total",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "\$${total.toInt()}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Book Button
                        Button(
                            onClick = {
                                NavigationState.selectedHotelId = hotel.id
                                NavigationState.selectedRoomId = room.id
                                navController.navigate(
                                    Routes.Booking.createRoute(hotel.id, room.id)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(28.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Primary)
                        ) {
                            Text(
                                text = "Book This Suite Now",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = OnPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "You won't be charged yet",
                            fontSize = 12.sp,
                            color = Color.White.copy(0.6f),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // ── Where you'll be ───────────────────────
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = "Where you'll be",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = LocalAppColors.current.textPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Map placeholder
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF1A3A5C),
                                        Color(0xFF0D2A4A)
                                    )
                                )
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(14.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(LocalAppColors.current.surface)
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = hotel.city,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = LocalAppColors.current.textPrimary
                                )
                                Text(
                                    text = hotel.address,
                                    fontSize = 12.sp,
                                    color = OnSurfaceSecondary
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// ── Price Line Item ───────────────────────────────────────
@Composable
fun PriceLineItem(label: String, amount: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 13.sp, color = Color.White.copy(0.8f))
        Text(
            text = amount,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}