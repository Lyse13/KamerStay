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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.NavigationState
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.mock.MockData
import com.kamerstay.app.model.Hotel
import com.kamerstay.app.model.Landmark

@Composable
fun TravelerHomeScreen(navController: NavController) {

    val userName = "Lysette"

    Scaffold(
        bottomBar = { TravelerBottomNav(navController, currentRoute = "home") },
        containerColor = WarmIvory
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // ── Top Bar ───────────────────────────────────
            item {
                HomeTopBar(
                    userName = userName,
                    onMenuClick = { },
                    onProfileClick = {
                        navController.navigate(Routes.TravelerProfile.route)
                    }
                )
            }

            // ── Search Bar ────────────────────────────────
            item {
                HomeSearchBar(
                    onClick = { navController.navigate(Routes.HotelSearch.route) },
                    userName = userName
                )
            }

            // ── Landmark Chips ────────────────────────────
            item {
                LandmarkChips(
                    landmarks = MockData.landmarks,
                    onClick = { navController.navigate(Routes.HotelSearch.route) }
                )
            }

            // ── Recommended Hotels ────────────────────────
            item {
                SectionHeader(
                    title = "Recommended for You",
                    onViewAll = { navController.navigate(Routes.HotelSearch.route) }
                )
            }

            // Featured Hotel (grand card)
            item {
                FeaturedHotelCard(
                    hotel = MockData.recommendedHotels.first(),
                    onClick = {
                        NavigationState.selectedHotelId = MockData.recommendedHotels.first().id
                        navController.navigate(Routes.HotelDetails.route)
                    }
                )
            }

            // Petits hotels recommandés
            items(MockData.recommendedHotels.drop(1).take(2).size) { index ->
                val hotel = MockData.recommendedHotels.drop(1)[index]
                SmallHotelCard(
                    hotel = hotel,
                    onClick = {
                        NavigationState.selectedHotelId = hotel.id
                        navController.navigate(Routes.HotelDetails.route)
                    }
                )
            }

            // ── Popular Hotels ────────────────────────────
            item {
                SectionHeader(
                    title = "Popular in Cameroon",
                    onViewAll = { navController.navigate(Routes.HotelSearch.route) }
                )
            }

            item {
                PopularHotelsRow(
                    hotels = MockData.popularHotels.take(4),
                    onHotelClick = { hotelId ->
                        NavigationState.selectedHotelId = hotelId
                        navController.navigate(Routes.HotelDetails.route)
                    }
                )
            }
        }
    }
}

@Composable
fun HomeTopBar(
    userName: String,
    onMenuClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu",
                    tint = OnSurface
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "KamerStay",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DeepEmerald
            )
        }

        // Avatar
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(PrimaryContainer)
                .clickable { onProfileClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = userName.first().toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DeepEmerald
            )
        }
    }
}

// ── Search Bar ────────────────────────────────────────────
@Composable
fun HomeSearchBar(onClick: () -> Unit, userName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Search,
            contentDescription = null,
            tint = OnSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "Search cities or landmarks...",
            fontSize = 14.sp,
            color = OnSurfaceVariant.copy(0.6f),
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DeepEmerald),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text("Search", fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Greeting
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(
            text = "Hello, $userName 👋",
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            color = OnSurface
        )
        Text(
            text = "Find your next home in the heart of Cameroon.",
            fontSize = 14.sp,
            color = OnSurfaceVariant
        )
    }

    Spacer(modifier = Modifier.height(20.dp))
}

// ── Landmark Chips ────────────────────────────────────────
@Composable
fun LandmarkChips(
    landmarks: List<Landmark>,
    onClick: (Landmark) -> Unit
) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        landmarks.forEach { landmark ->
            FilterChip(
                selected = false,
                onClick = { onClick(landmark) },
                label = {
                    Text(
                        text = landmark.name,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                },
                shape = RoundedCornerShape(20.dp),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = Color.White,
                    labelColor = OnSurface,
                    selectedContainerColor = DeepEmerald,
                    selectedLabelColor = Color.White
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = false,
                    borderColor = OutlineVariant,
                    selectedBorderColor = DeepEmerald
                )
            )
        }
    }
    Spacer(modifier = Modifier.height(24.dp))
}

// ── Section Header ────────────────────────────────────────
@Composable
fun SectionHeader(title: String, onViewAll: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = OnSurface
        )
        Text(
            text = "View all",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = WarmAmber,
            modifier = Modifier.clickable { onViewAll() }
        )
    }
    Spacer(modifier = Modifier.height(12.dp))
}

// ── Featured Hotel Card (grand) ───────────────────────────
@Composable
fun FeaturedHotelCard(hotel: Hotel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF1A3A4A),
                                Color(0xFF2D5016)
                            )
                        )
                    )
            ) {
                // Badge Verified
                if (hotel.isVerified) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(WarmAmber)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Outlined.Verified,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "VERIFIED",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // Info
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = hotel.name,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface,
                        modifier = Modifier.weight(1f)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.StarOutline,
                            contentDescription = null,
                            tint = StarRating,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = hotel.rating.toString(),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OnSurface
                        )
                    }
                }

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
                        text = hotel.address,
                        fontSize = 13.sp,
                        color = OnSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = buildString {
                            append("${hotel.pricePerNight.toLong()} XAF")
                            append("/night")
                        },
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface
                    )
                    Button(
                        onClick = onClick,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DeepEmerald
                        ),
                        contentPadding = PaddingValues(
                            horizontal = 20.dp,
                            vertical = 10.dp
                        )
                    ) {
                        Text(
                            text = "Book Now",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}

// ── Small Hotel Card ──────────────────────────────────────
@Composable
fun SmallHotelCard(hotel: Hotel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp)
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

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = hotel.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = OnSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (hotel.nearbyLandmarks.isNotEmpty()) {
                    Text(
                        text = hotel.nearbyLandmarks.first().name,
                        fontSize = 12.sp,
                        color = OnSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "${hotel.pricePerNight.toLong()} XAF",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurface
                )
            }
        }
    }
}

// ── Popular Hotels Row ────────────────────────────────────
@Composable
fun PopularHotelsRow(
    hotels: List<Hotel>,
    onHotelClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        hotels.forEach { hotel ->
            PopularHotelCard(hotel = hotel, onClick = { onHotelClick(hotel.id) })
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

// ── Popular Hotel Card ────────────────────────────────────
@Composable
fun PopularHotelCard(hotel: Hotel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            // Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF1A3A4A),
                                Color(0xFF2D5016)
                            )
                        )
                    )
            ) {
                if (hotel.isVerified) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.White.copy(alpha = 0.9f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Verified",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DeepEmerald
                        )
                    }
                }
            }

            // Info
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = hotel.name,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = OnSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (hotel.nearbyLandmarks.isNotEmpty()) {
                    Text(
                        text = hotel.nearbyLandmarks.first().name,
                        fontSize = 11.sp,
                        color = DeepEmerald,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${hotel.pricePerNight.toLong()} XAF",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurface
                )
            }
        }
    }
}

// ── Bottom Navigation ─────────────────────────────────────
@Composable
fun TravelerBottomNav(
    navController: NavController,
    currentRoute: String
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp
    ) {
        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = { navController.navigate(Routes.TravelerHome.route) },
            icon = {
                Icon(
                    if (currentRoute == "home") Icons.Filled.Home
                    else Icons.Outlined.Home,
                    contentDescription = "Home"
                )
            },
            label = { Text("Home", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = DeepEmerald,
                selectedTextColor = DeepEmerald,
                indicatorColor = PrimaryContainer,
                unselectedIconColor = OnSurfaceVariant,
                unselectedTextColor = OnSurfaceVariant
            )
        )
        NavigationBarItem(
            selected = currentRoute == "search",
            onClick = { navController.navigate(Routes.HotelSearch.route) },
            icon = {
                Icon(
                    if (currentRoute == "search") Icons.Filled.Search
                    else Icons.Outlined.Search,
                    contentDescription = "Explore"
                )
            },
            label = { Text("Explore", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = DeepEmerald,
                selectedTextColor = DeepEmerald,
                indicatorColor = PrimaryContainer,
                unselectedIconColor = OnSurfaceVariant,
                unselectedTextColor = OnSurfaceVariant
            )
        )
        NavigationBarItem(
            selected = currentRoute == "bookings",
            onClick = { navController.navigate(Routes.BookingHistory.route) },
            icon = {
                Icon(
                    if (currentRoute == "bookings") Icons.Filled.BookOnline
                    else Icons.Outlined.BookOnline,
                    contentDescription = "Bookings"
                )
            },
            label = { Text("Bookings", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = DeepEmerald,
                selectedTextColor = DeepEmerald,
                indicatorColor = PrimaryContainer,
                unselectedIconColor = OnSurfaceVariant,
                unselectedTextColor = OnSurfaceVariant
            )
        )
        NavigationBarItem(
            selected = currentRoute == "profile",
            onClick = { navController.navigate(Routes.TravelerProfile.route) },
            icon = {
                Icon(
                    if (currentRoute == "profile") Icons.Filled.Person
                    else Icons.Outlined.Person,
                    contentDescription = "Profile"
                )
            },
            label = { Text("Profile", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = DeepEmerald,
                selectedTextColor = DeepEmerald,
                indicatorColor = PrimaryContainer,
                unselectedIconColor = OnSurfaceVariant,
                unselectedTextColor = OnSurfaceVariant
            )
        )
    }
}