package com.kamerstay.app.features.traveler

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.NavigationState
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.mock.MockData
import com.kamerstay.app.model.enums.RoomStatus

@Composable
fun RoomDetailsScreen(
    navController: NavController,
    roomId: String
) {
    val room = MockData.rooms.find { it.id == roomId } ?: MockData.rooms.first()
    val hotel = MockData.getHotelById(room.hotelId) ?: MockData.hotels.first()

    val featureIcons = mapOf(
        "King Bed" to Icons.Outlined.KingBed,
        "City View" to Icons.Outlined.LocationCity,
        "Climatisation" to Icons.Outlined.AcUnit,
        "Mini-bar" to Icons.Outlined.LocalBar,
        "WiFi" to Icons.Outlined.Wifi,
        "Balcon" to Icons.Outlined.Balcony,
        "Bureau de travail" to Icons.Outlined.Work,
        "2 Single Beds" to Icons.Outlined.Hotel,
        "Double Bed" to Icons.Outlined.KingBed,
        "Garden View" to Icons.Outlined.Park,
    )

    val featureDescriptions = mapOf(
        "WiFi" to "Ultra-fast Fiber" to "Seamless connectivity for video calls and streaming.",
        "Climatisation" to "Climate Control" to "Precision air conditioning for ultimate tropical relief.",
        "King Bed" to "King Size Bed" to "Premium mattress for a perfect night's sleep.",
        "City View" to "City View" to "Panoramic views of the city skyline.",
        "Mini-bar" to "Mini Bar" to "Stocked with local and international beverages.",
        "Balcon" to "Private Balcony" to "Enjoy fresh air and outdoor seating.",
    )

    Scaffold(
        containerColor = WarmIvory,
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
                            text = "TOTAL PRICE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OnSurfaceVariant,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = "${room.pricePerNight.toLong()} XAF",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = OnSurface
                        )
                    }
                    Button(
                        onClick = {
                            NavigationState.selectedHotelId = hotel.id
                            NavigationState.selectedRoomId = room.id
                            navController.navigate(Routes.Booking.route)
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DeepEmerald
                        ),
                        contentPadding = PaddingValues(
                            horizontal = 24.dp,
                            vertical = 14.dp
                        )
                    ) {
                        Text(
                            text = "Continue to Booking",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            Icons.Filled.ArrowForward,
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
                        .height(300.dp)
                ) {
                    // Image placeholder
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF3D2E1A),
                                        Color(0xFF2A1F10),
                                        Color(0xFF1A1208),
                                    )
                                )
                            )
                    )

                    // Available badge
                    if (room.status == RoomStatus.AVAILABLE) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .statusBarsPadding()
                                .padding(16.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(WarmAmber)
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Outlined.CheckCircle,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Available",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }
                    }

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
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.3f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Outlined.Person,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }

            // ── Room Info Card ────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-24).dp)
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                        .padding(20.dp)
                ) {
                    Column {
                        // Name + Price
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${room.type.name.lowercase()
                                        .replaceFirstChar { it.uppercase() }} Suite",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = OnSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Outlined.Place,
                                        contentDescription = null,
                                        tint = OnSurfaceVariant,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${hotel.name}, ${hotel.address}",
                                        fontSize = 12.sp,
                                        color = OnSurfaceVariant
                                    )
                                }
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "${room.pricePerNight.toLong()},000",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = OnSurface
                                )
                                Text(
                                    text = "XAF",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OnSurface
                                )
                                Text(
                                    text = "PER NIGHT",
                                    fontSize = 10.sp,
                                    color = OnSurfaceVariant,
                                    letterSpacing = 0.8.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Feature chips
                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            room.features.take(4).forEach { feature ->
                                val icon = featureIcons[feature] ?: Icons.Outlined.CheckCircle
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(SurfaceVariant)
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            icon,
                                            contentDescription = null,
                                            tint = OnSurface,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = feature,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = OnSurface
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ── Room Comfort ──────────────────────────
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = "Room Comfort",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Designed for the modern professional, our ${room.type.name.lowercase().replaceFirstChar { it.uppercase() }} Suite blends Cameroonian warmth with international luxury standards. Enjoy expansive views of the ${hotel.city} skyline through soundproofed windows, complemented by hand-carved wooden accents and premium textiles that celebrate local craftsmanship.",
                        fontSize = 14.sp,
                        color = OnSurfaceVariant,
                        lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            // ── Feature Cards Grid ────────────────────
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    val featuresWithDesc = room.features.take(4)
                    for (i in featuresWithDesc.indices step 2) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            RoomFeatureCard(
                                icon = featureIcons[featuresWithDesc[i]]
                                    ?: Icons.Outlined.CheckCircle,
                                title = featuresWithDesc[i],
                                description = getFeatureDescription(featuresWithDesc[i]),
                                modifier = Modifier.weight(1f)
                            )
                            if (i + 1 < featuresWithDesc.size) {
                                RoomFeatureCard(
                                    icon = featureIcons[featuresWithDesc[i + 1]]
                                        ?: Icons.Outlined.CheckCircle,
                                    title = featuresWithDesc[i + 1],
                                    description = getFeatureDescription(featuresWithDesc[i + 1]),
                                    modifier = Modifier.weight(1f)
                                )
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                        if (i + 2 < featuresWithDesc.size) {
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            // ── Check Availability ────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceVariant)
                        .clickable { }
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Outlined.CalendarMonth,
                                    contentDescription = null,
                                    tint = OnSurface,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Check Availability",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = OnSurface
                                )
                                Text(
                                    text = "Book for Oct 12 - Oct 15",
                                    fontSize = 13.sp,
                                    color = OnSurfaceVariant
                                )
                            }
                        }
                        Icon(
                            Icons.Filled.ChevronRight,
                            contentDescription = null,
                            tint = OnSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ── Room Feature Card ─────────────────────────────────────
@Composable
fun RoomFeatureCard(
    icon: ImageVector,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceVariant)
            .padding(14.dp)
    ) {
        Column {
            Icon(
                icon,
                contentDescription = null,
                tint = OnSurface,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = OnSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                fontSize = 12.sp,
                color = OnSurfaceVariant,
                lineHeight = 16.sp
            )
        }
    }
}

fun getFeatureDescription(feature: String): String = when (feature) {
    "WiFi" -> "Seamless connectivity for video calls and streaming."
    "Climatisation" -> "Precision air conditioning for ultimate tropical relief."
    "King Bed" -> "Premium mattress for a perfect night's sleep."
    "City View" -> "Panoramic views of the city skyline."
    "Mini-bar" -> "Stocked with local and international beverages."
    "Balcon" -> "Enjoy fresh air and outdoor seating."
    "2 Single Beds" -> "Comfortable twin beds for shared stays."
    "Double Bed" -> "Spacious double bed for couples."
    "Garden View" -> "Serene views of the hotel garden."
    "Bureau de travail" -> "Dedicated workspace for productivity."
    else -> "Premium amenity for your comfort."
}