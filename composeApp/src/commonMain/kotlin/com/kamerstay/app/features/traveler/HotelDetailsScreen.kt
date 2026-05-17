package com.kamerstay.app.features.traveler

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.NavigationState
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.mock.MockData
import com.kamerstay.app.model.Room
import com.kamerstay.app.model.enums.RoomStatus

@Composable
fun HotelDetailsScreen(
    navController: NavController,
    hotelId: String
) {

    val hotel = MockData.getHotelById(hotelId) ?: MockData.hotels.first()
    val rooms = MockData.getRoomsForHotel(hotelId)
    var isFavorite by remember { mutableStateOf(false) }
    var currentImageIndex by remember { mutableStateOf(0) }

    val amenityIcons = mapOf(
        "WiFi" to Icons.Outlined.Wifi,
        "Piscine" to Icons.Outlined.Pool,
        "Climatisation" to Icons.Outlined.AcUnit,
        "Restaurant" to Icons.Outlined.Restaurant,
        "Parking" to Icons.Outlined.LocalParking,
        "Bar" to Icons.Outlined.LocalBar,
    )

    Scaffold(
        containerColor = WarmIvory,
        bottomBar = {
            // ── Bottom Bar ────────────────────────────
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
                            text = "Starting from",
                            fontSize = 12.sp,
                            color = OnSurfaceVariant
                        )
                        Text(
                            text = "${hotel.pricePerNight.toLong()} FCFA",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = OnSurface
                        )
                    }
                    Button(
                        onClick = {
                            val firstRoom = rooms.firstOrNull() ?: MockData.rooms.first()
                            NavigationState.selectedHotelId = hotel.id
                            NavigationState.selectedRoomId = firstRoom.id
                            navController.navigate(Routes.Booking.route)
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DeepEmerald
                        ),
                        contentPadding = PaddingValues(
                            horizontal = 28.dp,
                            vertical = 14.dp
                        )
                    ) {
                        Text(
                            text = "Book Now",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            Icons.Outlined.CalendarMonth,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
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
                    // Image placeholder
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF2C4A3E),
                                        Color(0xFF1A2E28),
                                        Color(0xFF0D1F1A),
                                    )
                                )
                            )
                    )

                    // Top bar overlay
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Back
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.3f))
                                .clickable { navController.popBackStack() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.ArrowBack,
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
                            // Share
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.3f))
                                    .clickable { },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Outlined.Share,
                                    contentDescription = "Share",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            // Favorite
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.3f))
                                    .clickable { isFavorite = !isFavorite },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    if (isFavorite) Icons.Filled.Favorite
                                    else Icons.Outlined.FavoriteBorder,
                                    contentDescription = "Favorite",
                                    tint = if (isFavorite) Color.Red else Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    // Image indicators
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        repeat(3) { index ->
                            Box(
                                modifier = Modifier
                                    .height(4.dp)
                                    .width(if (index == currentImageIndex) 20.dp else 6.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(
                                        if (index == currentImageIndex) Color.White
                                        else Color.White.copy(alpha = 0.5f)
                                    )
                            )
                        }
                    }
                }
            }

            // ── Hotel Info ────────────────────────────
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(WarmIvory)
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    // Name + Verified badge
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = hotel.name,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = OnSurface,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        if (hotel.isVerified) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(WarmAmber.copy(alpha = 0.15f))
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Outlined.Verified,
                                        contentDescription = null,
                                        tint = Color(0xFF8B6914),
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "VERIFIED HOTEL",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF8B6914)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Rating
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = null,
                            tint = StarRating,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${hotel.rating}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )
                        Text(
                            text = " (${hotel.reviewCount} reviews)",
                            fontSize = 13.sp,
                            color = OnSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Description
                    Text(
                        text = hotel.description.ifEmpty {
                            "Experience unparalleled comfort in the heart of ${hotel.city}. ${hotel.name} combines Sahelian warmth with modern amenities, offering a tranquil escape from the bustling city streets."
                        },
                        fontSize = 14.sp,
                        color = OnSurfaceVariant,
                        lineHeight = 22.sp
                    )
                }
            }

            // ── Amenities ─────────────────────────────
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = "Top Amenities",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        hotel.amenities.take(5).forEach { amenity ->
                            val icon = amenityIcons[amenity] ?: Icons.Outlined.CheckCircle
                            val shortLabel = when (amenity) {
                                "Climatisation" -> "AC"
                                "Restaurant" -> "Dining"
                                "Piscine" -> "Pool"
                                "Parking" -> "Parking"
                                else -> amenity
                            }
                            AmenityItem(icon = icon, label = shortLabel)
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            // ── Nearby Landmarks ──────────────────────
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = "Nearby Landmarks",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Mock landmarks
                        listOf(
                            "Near ${hotel.city} General Hospital",
                            "Near Central Market",
                            "Near University"
                        ).forEach { landmark ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color.White)
                                    .padding(horizontal = 14.dp, vertical = 10.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Outlined.Place,
                                        contentDescription = null,
                                        tint = DeepEmerald,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = landmark,
                                        fontSize = 13.sp,
                                        color = OnSurface,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            // ── Available Rooms ───────────────────────
            item {
                Text(
                    text = "Available Rooms",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurface,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            val displayRooms = rooms.ifEmpty { MockData.rooms.take(2) }
            items(displayRooms) { room ->
                RoomCard(
                    room = room,
                    onClick = {
                        NavigationState.selectedRoomId = room.id
                        navController.navigate(Routes.RoomDetails.route)
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

// ── Amenity Item ──────────────────────────────────────────
@Composable
fun AmenityItem(icon: ImageVector, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(SurfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = OnSurface,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = OnSurface,
            fontWeight = FontWeight.Medium
        )
    }
}

// ── Room Card ─────────────────────────────────────────────
@Composable
fun RoomCard(room: Room, onClick: () -> Unit) {
    val availableCount = if (room.status == RoomStatus.AVAILABLE) 2 else 0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(DeepEmerald, Color(0xFF1C3D2E))
                        )
                    )
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${room.type.name.lowercase().replaceFirstChar { it.uppercase() }} ${
                        if (room.type.name == "SUITE") "" else "Room"
                    }".trim(),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = OnSurface
                )
                Text(
                    text = room.description,
                    fontSize = 12.sp,
                    color = OnSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "${room.pricePerNight.toLong()} FCFA",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = OnSurface
                        )
                        Text(
                            text = "/night",
                            fontSize = 11.sp,
                            color = OnSurfaceVariant
                        )
                    }
                    if (availableCount > 0) {
                        Text(
                            text = "$availableCount LEFT",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = StatusReserved
                        )
                    }
                }
            }
        }
    }
}