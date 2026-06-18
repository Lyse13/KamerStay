package com.kamerstay.app.features.traveler

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import coil3.compose.AsyncImage
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.NavigationState
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.core.utils.APP_NAME
import com.kamerstay.app.core.components.EmptyWishlist
import com.kamerstay.app.core.components.TravelerBottomNavBar
import com.kamerstay.app.data.model.WishlistHotel
import com.kamerstay.app.viewmodel.TravelerViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WishlistScreen(navController: NavController) {

    val viewModel = koinViewModel<TravelerViewModel>()
    val state = viewModel.wishlistState

    Scaffold(
        containerColor = LocalAppColors.current.background,
        bottomBar = {
            TravelerBottomNavBar(navController = navController, selectedTab = 2)
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
                        .padding(horizontal = 8.dp, vertical = 12.dp),
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
                            text = APP_NAME,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Secondary
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(OnSurfaceSecondary.copy(0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Outlined.Person,
                            contentDescription = null,
                            tint = OnSurfaceSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // ── Header ────────────────────────────────
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = "Your Wishlist",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = LocalAppColors.current.textPrimary
                    )
                    Text(
                        text = "Found ${state.favoriteCount} properties you loved",
                        fontSize = 13.sp,
                        color = OnSurfaceSecondary
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // ── Hotel Cards ───────────────────────────
            items(
                items = state.hotels,
                key = { it.id }
            ) { hotel ->
                WishlistHotelCard(
                    hotel = hotel,
                    onFavoriteToggle = { state.toggleFavorite(hotel.id) },
                    onBook = {
                        NavigationState.selectedHotelId = hotel.id
                        navController.navigate(
                            Routes.HotelDetails.createRoute(hotel.id)
                        )
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (state.hotels.isEmpty()) {
                item {
                    EmptyWishlist(onExplore = { navController.navigate(Routes.HotelSearch.route) })
                }
            }
        }
    }
}

// ── Wishlist Hotel Card ───────────────────────────────────
@Composable
fun WishlistHotelCard(
    hotel: WishlistHotel,
    onFavoriteToggle: () -> Unit,
    onBook: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(LocalAppColors.current.surface)
            .clickable { onBook() }
    ) {
        Column {
            // Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = hotel.gradientColors
                        )
                    )
            ) {
                if (hotel.imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = hotel.imageUrl,
                        contentDescription = hotel.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                // Favorite button
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(LocalAppColors.current.surface)
                        .clickable { onFavoriteToggle() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (hotel.isFavorite) Icons.Outlined.Favorite
                        else Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        tint = ErrorColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Content
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = hotel.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = LocalAppColors.current.textPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    // Rating badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Primary.copy(0.1f))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                Icons.Outlined.StarOutline,
                                contentDescription = null,
                                tint = StarRating,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = hotel.rating.toString(),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Secondary
                            )
                        }
                    }
                }

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
                        text = "${hotel.location}, ${hotel.region}",
                        fontSize = 13.sp,
                        color = OnSurfaceSecondary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                HorizontalDivider(color = Divider)

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Price per night",
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary
                        )
                        Text(
                            text = "FCFA ${
                                hotel.pricePerNight.toString()
                                    .chunked(3)
                                    .joinToString(",")
                            }",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Primary
                        )
                    }

                    Button(
                        onClick = onBook,
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Secondary),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
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

