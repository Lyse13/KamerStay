package com.kamerstay.app.features.traveler

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.kamerstay.app.core.components.EmptySearchResults
import com.kamerstay.app.core.components.HotelCardSkeleton
import com.kamerstay.app.core.components.TravelerBottomNavBar
import com.kamerstay.app.core.navigation.NavigationState
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.model.Hotel
import com.kamerstay.app.viewmodel.TravelerViewModel
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.absoluteValue


private fun gradientForHotel(hotel: Hotel): List<Color> {
    val gradients = listOf(
        listOf(Color(0xFF0D3A5C), Color(0xFF1A2A3A)),
        listOf(Color(0xFF1A3A2E), Color(0xFF0D2218)),
        listOf(Color(0xFF2A1A3A), Color(0xFF1A0D28)),
        listOf(Color(0xFF3A2A1A), Color(0xFF281A0D)),
        listOf(Color(0xFF1A1A3A), Color(0xFF0D0D28)),
    )
    return gradients[hotel.id.hashCode().absoluteValue % gradients.size]
}

@Composable
fun HotelSearchScreen(navController: NavController) {

    val viewModel      = koinViewModel<TravelerViewModel>()
    val searchState    = viewModel.searchState
    val filterState    = viewModel.filterState
    val isLoading      = viewModel.isLoadingHotels
    val hasActiveFilters = filterState.hasActiveFilters

    // Reset search state and view mode when entering the screen
    LaunchedEffect(Unit) {
        searchState.query    = ""
        searchState.viewMode = "List"
        viewModel.searchHotels()
    }

    // Filtre les hôtels backend par les critères actifs
    val filteredHotels = viewModel.displayedHotels.filter { hotel ->
        filterState.hotelMatches(
            price      = hotel.pricePerNight.toInt(),
            rating     = hotel.rating,
            amenities  = hotel.amenities,
            isVerified = hotel.isVerified
        )
    }

    Scaffold(
        containerColor = LocalAppColors.current.background,
        bottomBar = {
            TravelerBottomNavBar(navController = navController, selectedTab = 1)
        }
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
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Retour",
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
                    Box {
                        Icon(
                            Icons.Outlined.Tune,
                            contentDescription = null,
                            tint = Secondary,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { navController.navigate(Routes.Filter.route) }
                        )
                        if (hasActiveFilters) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(ErrorColor)
                                    .align(Alignment.TopEnd)
                            )
                        }
                    }
                }
            }

            // ── Search Bar ────────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(LocalAppColors.current.surface)
                        .border(1.dp, Divider, RoundedCornerShape(28.dp))
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.Search,
                            contentDescription = null,
                            tint = OnSurfaceSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "DESTINATION",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceSecondary,
                                letterSpacing = 0.5.sp
                            )
                            BasicTextField(
                                value = searchState.query,
                                onValueChange = { query ->
                                    searchState.query = query
                                    viewModel.searchHotels()
                                },
                                textStyle = TextStyle(
                                    fontSize = 15.sp,
                                    color = LocalAppColors.current.textPrimary,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                decorationBox = { inner ->
                                    if (searchState.query.isEmpty()) {
                                        Text(
                                            "Ville, hôtel, adresse…",
                                            fontSize = 15.sp,
                                            color = OnSurfaceSecondary.copy(0.5f)
                                        )
                                    }
                                    inner()
                                }
                            )
                        }
                        if (searchState.query.isNotEmpty()) {
                            Icon(
                                Icons.Outlined.Close,
                                contentDescription = "Effacer",
                                tint = OnSurfaceSecondary,
                                modifier = Modifier
                                    .size(18.dp)
                                    .clickable {
                                        searchState.query = ""
                                        viewModel.searchHotels()
                                    }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Box {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (hasActiveFilters) Primary.copy(0.2f) else Primary.copy(0.1f))
                                    .clickable { navController.navigate(Routes.Filter.route) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Outlined.Tune,
                                    contentDescription = null,
                                    tint = if (hasActiveFilters) Primary else Secondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            if (hasActiveFilters) {
                                Box(
                                    modifier = Modifier
                                        .size(9.dp)
                                        .clip(CircleShape)
                                        .background(ErrorColor)
                                        .align(Alignment.TopEnd)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // ── Results Header ────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        if (isLoading) {
                            Text(
                                text = "Chargement…",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = LocalAppColors.current.textPrimary
                            )
                        } else {
                            Text(
                                text = "${filteredHotels.size} Hôtel${if (filteredHotels.size > 1) "s" else ""}",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = LocalAppColors.current.textPrimary,
                                lineHeight = 30.sp
                            )
                            Text(
                                text = "Trouvé${if (filteredHotels.size > 1) "s" else ""}",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = LocalAppColors.current.textPrimary,
                                lineHeight = 30.sp
                            )
                        }
                        Text(
                            text = if (searchState.query.isNotBlank()) "Résultats pour « ${searchState.query} »"
                            else "Meilleurs séjours au Cameroun",
                            fontSize = 13.sp,
                            color = OnSurfaceSecondary
                        )
                    }

                    // List / Map toggle
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(LocalAppColors.current.surface)
                            .border(1.dp, Divider, RoundedCornerShape(20.dp))
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        listOf(
                            "List" to Icons.Outlined.FormatListBulleted,
                            "Map"  to Icons.Outlined.Map
                        ).forEach { (mode, icon) ->
                            val isSelected = searchState.viewMode == mode
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (isSelected) Secondary else Color.Transparent)
                                    .clickable {
                                        searchState.viewMode = mode
                                        if (mode == "Map") navController.navigate(Routes.MapLocation.route)
                                    }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        icon,
                                        contentDescription = null,
                                        tint = if (isSelected) Color.White else OnSurfaceSecondary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = mode,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (isSelected) Color.White else OnSurfaceSecondary
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // ── Loading skeleton ──────────────────────
            if (isLoading) {
                items(3) {
                    HotelCardSkeleton()
                    Spacer(modifier = Modifier.height(14.dp))
                }
            } else {
                // ── Hotel Cards ───────────────────────────
                items(filteredHotels, key = { it.id }) { hotel ->
                    HotelSearchCard(
                        hotel = hotel,
                        isInWishlist = viewModel.wishlistState.isInWishlist(hotel.id),
                        onClick = {
                            NavigationState.selectedHotelId = hotel.id
                            viewModel.selectHotel(hotel.id)
                            navController.navigate(Routes.HotelDetails.createRoute(hotel.id))
                        },
                        onFavorite = { viewModel.wishlistState.toggleFromHotel(hotel) }
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // ── No Result ─────────────────────────────
                item {
                    if (filteredHotels.isEmpty()) {
                        EmptySearchResults(
                            query = searchState.query,
                            onClearSearch = {
                                searchState.query = ""
                                viewModel.searchHotels()
                            }
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }
}

// ── Hotel Search Card (Hotel model) ──────────────────────────
@Composable
fun HotelSearchCard(
    hotel: Hotel,
    isInWishlist: Boolean,
    onClick: () -> Unit,
    onFavorite: () -> Unit
) {
    val gradient = gradientForHotel(hotel)
    val imageUrl = hotel.imageUrls.firstOrNull() ?: ""

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
                    .height(200.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(brush = Brush.verticalGradient(colors = gradient))
            ) {
                if (imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = hotel.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Verified badge
                if (hotel.isVerified) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
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
                                text = "Vérifié",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnPrimary
                            )
                        }
                    }
                }

                // Favorite button
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(LocalAppColors.current.surface)
                        .clickable { onFavorite() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (isInWishlist) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        tint = ErrorColor,
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Rooms disponibles badge
                if (hotel.availableRooms > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(10.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.Black.copy(0.55f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${hotel.availableRooms} chambre${if (hotel.availableRooms > 1) "s" else ""} dispo",
                            fontSize = 11.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Content
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = hotel.name,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
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
                            Icons.Outlined.StarOutline,
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
                        Text(
                            text = "(${hotel.reviewCount})",
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

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
                        text = hotel.city,
                        fontSize = 13.sp,
                        color = OnSurfaceSecondary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Amenity tags (3 max)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    hotel.amenities.take(3).forEach { amenity ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .border(1.dp, Divider, RoundedCornerShape(20.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = amenity,
                                fontSize = 12.sp,
                                color = OnSurfaceSecondary,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Price + Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = formatFcfa(hotel.pricePerNight.toInt()),
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Primary
                        )
                        Text(
                            text = "FCFA/nuit",
                            fontSize = 13.sp,
                            color = Primary,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Button(
                        onClick = onClick,
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Secondary),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = "Réserver",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

private fun formatFcfa(amount: Int): String =
    amount.toString().reversed().chunked(3).joinToString(".").reversed()
