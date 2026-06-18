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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.components.EmptySearchResults
import com.kamerstay.app.core.components.TravelerBottomNavBar
import com.kamerstay.app.core.navigation.NavigationState
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.mock.SearchResultsMockData
import com.kamerstay.app.data.model.SearchHotelResult
import com.kamerstay.app.viewmodel.TravelerViewModel
import coil3.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HotelSearchScreen(navController: NavController) {

    val viewModel     = koinViewModel<TravelerViewModel>()
    val state         = viewModel.searchState
    val filterState   = viewModel.filterState
    val hasActiveFilters = filterState.hasActiveFilters

    val filteredHotels = SearchResultsMockData.hotels.filter { hotel ->
        filterState.hotelMatches(hotel.pricePerNight, hotel.rating, hotel.amenities, hotel.isVerified) &&
        (state.destination.isBlank() ||
         hotel.name.contains(state.destination, ignoreCase = true) ||
         hotel.location.contains(state.destination, ignoreCase = true))
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
                                value = state.destination,
                                onValueChange = {
                                    state.destination = it
                                    viewModel.searchHotels()
                                },
                                textStyle = TextStyle(
                                    fontSize = 15.sp,
                                    color = LocalAppColors.current.textPrimary,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                decorationBox = { inner ->
                                    if (state.destination.isEmpty()) {
                                        Text(
                                            "Where are you going?",
                                            fontSize = 15.sp,
                                            color = OnSurfaceSecondary.copy(0.5f)
                                        )
                                    }
                                    inner()
                                }
                            )
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
                        Text(
                            text = if (state.destination.isNotBlank()) "Résultats pour « ${state.destination} »"
                                   else "Meilleurs séjours à Douala",
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
                            "Map" to Icons.Outlined.Map
                        ).forEach { (mode, icon) ->
                            val isSelected = state.viewMode == mode
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(
                                        if (isSelected) Secondary else Color.Transparent
                                    )
                                    .clickable {
                                        state.viewMode = mode
                                        if (mode == "Map") {
                                            navController.navigate(Routes.MapLocation.route)
                                        }
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
                                        tint = if (isSelected) Color.White
                                        else OnSurfaceSecondary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = mode,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (isSelected) Color.White
                                        else OnSurfaceSecondary
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // ── Hotel Cards ───────────────────────────
            items(filteredHotels) { hotel ->
                SearchResultCard(
                    hotel = hotel,
                    isInWishlist = viewModel.wishlistState.isInWishlist(hotel.id),
                    onClick = {
                        NavigationState.selectedHotelId = hotel.id
                        navController.navigate(
                            Routes.HotelDetails.createRoute(hotel.id)
                        )
                    },
                    onFavorite = { viewModel.wishlistState.toggleFromSearchResult(hotel) }
                )
                Spacer(modifier = Modifier.height(14.dp))
            }

            // ── No Result ─────────────────────────────
            item {
                if (filteredHotels.isEmpty()) {
                    EmptySearchResults(
                        query = state.destination,
                        onClearSearch = { navController.navigate(Routes.NoResult.route) }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }
}

// ── Search Result Card ────────────────────────────────────
@Composable
fun SearchResultCard(
    hotel: SearchHotelResult,
    isInWishlist: Boolean,
    onClick: () -> Unit,
    onFavorite: () -> Unit
) {
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
                    .background(brush = Brush.verticalGradient(colors = hotel.gradientColors))
            ) {
                if (hotel.imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = hotel.imageUrl,
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
                                text = "Verified",
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
                        text = hotel.location,
                        fontSize = 13.sp,
                        color = OnSurfaceSecondary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Amenity tags
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    hotel.amenities.forEach { amenity ->
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
                                fontWeight = FontWeight.Medium
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
                        if (hotel.originalPrice != null) {
                            Text(
                                text = "${
                                    hotel.originalPrice.toString()
                                        .reversed()
                                        .chunked(3)
                                        .joinToString(",")
                                        .reversed()
                                } FCFA",
                                fontSize = 12.sp,
                                color = OnSurfaceSecondary,
                                textDecoration = TextDecoration.LineThrough
                            )
                        }
                        Text(
                            text = hotel.pricePerNight.toString()
                                .reversed()
                                .chunked(3)
                                .joinToString(",")
                                .reversed(),
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Primary
                        )
                        Text(
                            text = "FCFA/night",
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
                            text = "Book\nNow",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}