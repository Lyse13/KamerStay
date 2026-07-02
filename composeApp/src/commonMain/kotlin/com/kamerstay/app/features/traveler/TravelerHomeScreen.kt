package com.kamerstay.app.features.traveler

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.kamerstay.app.core.components.HotelCardSkeleton
import com.kamerstay.app.core.components.TravelerBottomNavBar
import com.kamerstay.app.core.navigation.NavigationState
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.state.UserSession
import com.kamerstay.app.data.mock.MockData
import com.kamerstay.app.model.Hotel
import com.kamerstay.app.viewmodel.TravelerViewModel
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TravelerHomeScreen(navController: NavController) {

    val viewModel = koinViewModel<TravelerViewModel>()
    val state = viewModel.searchState
    val hotels = viewModel.hotels

    LaunchedEffect(UserSession.isLoggedIn) {
        if (!UserSession.isLoggedIn) {
            navController.navigate(Routes.Welcome.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        delay(1200L)
        isLoading = false
    }

    val featuredCities = listOf(
        "Douala" to listOf(Color(0xFF1A2A3A), Color(0xFF0D1A28)),
        "Yaoundé" to listOf(Color(0xFF1A3A2E), Color(0xFF0D2218)),
        "Kribi" to listOf(Color(0xFF2A1A3A), Color(0xFF1A0D28)),
        "Limbe" to listOf(Color(0xFF3A2A1A), Color(0xFF281A0D))
    )

    val filterChips = listOf(
        Triple(Icons.Outlined.Verified, "Verified Hotels", { viewModel.filterState.clearAll(); viewModel.filterState.isVerifiedOnly = true }),
        Triple(Icons.Outlined.Pool,     "With Pool",       { viewModel.filterState.clearAll(); viewModel.filterState.selectedAmenities = setOf("Pool") })
    )

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            TravelerBottomNavBar(navController = navController, selectedTab = 0)
        },
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        SecondaryContainer.copy(alpha = 0.35f),
                        BackgroundLight,
                        Color(0xFFE8F4F5)
                    )
                )
            )
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // ── Top Bar ───────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Outlined.TravelExplore,
                            contentDescription = null,
                            tint = Secondary,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "KamerStay",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = SecondaryContainer
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .border(2.dp, SecondaryContainer, CircleShape)
                            .background(Primary.copy(0.15f))
                            .clickable { navController.navigate(Routes.TravelerProfile.route) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Outlined.Person,
                            contentDescription = null,
                            tint = Secondary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            // ── Header ────────────────────────────────
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = "Where are you going?",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = LocalAppColors.current.textPrimary
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // ── Search Bar ────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(LocalAppColors.current.surface)
                        .border(1.dp, Divider, RoundedCornerShape(24.dp))
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.Search,
                        contentDescription = null,
                        tint = SecondaryContainer,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    BasicTextField(
                        value = state.query,
                        onValueChange = {
                            state.query = it
                            viewModel.searchHotels()
                        },
                        modifier = Modifier.weight(1f),
                        textStyle = TextStyle(fontSize = 14.sp, color = LocalAppColors.current.textPrimary),
                        decorationBox = { inner ->
                            if (state.query.isEmpty()) {
                                Text(
                                    "Where are you traveling next ?",
                                    fontSize = 14.sp,
                                    color = Outline
                                )
                            }
                            inner()
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            viewModel.searchHotels()
                            navController.navigate(Routes.HotelSearch.route)
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SecondaryContainer),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Search",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // ── Filter Chips ──────────────────────────
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filterChips) { (icon, label, applyFilter) ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Primary)
                                .clickable {
                                    applyFilter()
                                    navController.navigate(Routes.HotelSearch.route)
                                }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    icon,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = label,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // ── Concierge IA Banner ───────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFF4A00E0), Color(0xFF8E2DE2))
                            )
                        )
                        .clickable { navController.navigate(Routes.AIConcierge.route) }
                        .padding(horizontal = 20.dp, vertical = 18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    Icons.Outlined.AutoAwesome,
                                    contentDescription = null,
                                    tint = Color.White.copy(0.85f),
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "CONCIERGE IA",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White.copy(0.85f),
                                    letterSpacing = 1.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Discutez avec Kamsa",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Text(
                                text = "Votre expert hôtelier camerounais",
                                fontSize = 13.sp,
                                color = Color.White.copy(0.75f)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(0.18f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Outlined.SmartToy,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // ── Landmark Search Banner ────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFF006994), Color(0xFF00BCD4))
                            )
                        )
                        .clickable { navController.navigate(Routes.LandmarkSearch.route) }
                        .padding(horizontal = 20.dp, vertical = 18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    Icons.Outlined.Place,
                                    contentDescription = null,
                                    tint = Color.White.copy(0.85f),
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "RECHERCHE PAR LIEU",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White.copy(0.85f),
                                    letterSpacing = 1.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Recherche par lieu",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Text(
                                text = "Trouvez un hôtel près d'un lieu connu",
                                fontSize = 13.sp,
                                color = Color.White.copy(0.75f)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(0.18f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Outlined.Map,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // ── Featured Cities ───────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Featured Cities",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = LocalAppColors.current.textPrimary
                    )
                    Text(
                        text = "View All",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Secondary,
                        modifier = Modifier.clickable {
                            navController.navigate(Routes.HotelSearch.route)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 2x2 grid
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Left column - one big card
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(200.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = featuredCities[0].second
                                )
                            )
                            .clickable { navController.navigate(Routes.HotelSearch.route) }
                    ) {
                        Text(
                            text = featuredCities[0].first,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(12.dp)
                        )
                    }

                    // Right column - two smaller cards
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        featuredCities.drop(1).take(2).forEach { (city, colors) ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(95.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(
                                        brush = Brush.verticalGradient(colors = colors)
                                    )
                                    .clickable {
                                        navController.navigate(Routes.HotelSearch.route)
                                    }
                            ) {
                                Text(
                                    text = city,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(10.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // ── Explore Like a Local Banner ───────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(ElectricBlue.copy(0.15f))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Explore Like a Local",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = SecondaryContainer
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Get curated guides for the best landmarks and hidden gems.",
                                fontSize = 12.sp,
                                color = OnSurfaceSecondary,
                                lineHeight = 17.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = { navController.navigate(Routes.LocalGuide.route) },
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SecondaryContainer),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = "Download Guide",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Icon(
                            Icons.Outlined.Map,
                            contentDescription = null,
                            tint = Secondary.copy(0.15f),
                            modifier = Modifier.size(60.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // ── Recommended Hotels ────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recommended for you",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = LocalAppColors.current.textPrimary
                    )
                    Text(
                        text = when (viewModel.priceSortAscending) {
                            true  -> "Price ↑"
                            false -> "Price ↓"
                            null  -> "Sort by Price"
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Secondary,
                        modifier = Modifier.clickable { viewModel.togglePriceSort() }
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (isLoading) {
                items(3) {
                    HotelCardSkeleton()
                    Spacer(Modifier.height(16.dp))
                }
            } else items(viewModel.displayedHotels.take(3)) { hotel ->
                HomeHotelCard(
                    hotel = hotel,
                    isInWishlist = viewModel.wishlistState.isInWishlist(hotel.id),
                    onFavoriteToggle = { viewModel.wishlistState.toggleFromHotel(hotel) },
                    onClick = {
                        NavigationState.selectedHotelId = hotel.id
                        viewModel.selectHotel(hotel.id)
                        navController.navigate(
                            Routes.HotelDetails.createRoute(hotel.id)
                        )
                    }
                )
                Spacer(modifier = Modifier.height(14.dp))
            }
        }
    }
}

// ── Home Hotel Card ───────────────────────────────────────
@Composable
fun HomeHotelCard(hotel: Hotel, isInWishlist: Boolean, onFavoriteToggle: () -> Unit, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(LocalAppColors.current.surface)
            .clickable { onClick() }
    ) {
        Column {
            // Image
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
                val heroUrl = hotel.imageUrls.firstOrNull() ?: ""
                if (heroUrl.isNotEmpty()) {
                    AsyncImage(
                        model = heroUrl,
                        contentDescription = hotel.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                // Verified badge
                if (hotel.isVerified) {
                    Box(
                        modifier = Modifier
                            .padding(10.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Primary)
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                Icons.Outlined.Verified,
                                contentDescription = null,
                                tint = OnPrimary,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = "Verified",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = OnPrimary
                            )
                        }
                    }
                }

                // Favorite
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(LocalAppColors.current.surface)
                        .clickable { onFavoriteToggle() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (isInWishlist) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        tint = ErrorColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(14.dp)) {
                // Name + Rating
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = hotel.name,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = LocalAppColors.current.textPrimary,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            Icons.Outlined.Star,
                            contentDescription = null,
                            tint = StarRating,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = hotel.rating.toString(),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = LocalAppColors.current.textPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Location
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
                        text = if (hotel.nearbyLandmarks.isNotEmpty())
                            "Near ${hotel.nearbyLandmarks.first().name}"
                        else hotel.address,
                        fontSize = 12.sp,
                        color = OnSurfaceSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Amenity tags
                if (hotel.amenities.isNotEmpty()) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        hotel.amenities.take(3).forEach { amenity ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(LocalAppColors.current.background)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = amenity,
                                    fontSize = 12.sp,
                                    color = OnSurfaceSecondary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                HorizontalDivider(color = Divider)

                Spacer(modifier = Modifier.height(10.dp))

                // Price + Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = "${hotel.pricePerNight.toInt()} FCFA",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = LocalAppColors.current.textPrimary
                        )
                        Text(
                            text = "per night",
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary
                        )
                    }
                    Button(
                        onClick = onClick,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SecondaryContainer),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = "Book Now",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
