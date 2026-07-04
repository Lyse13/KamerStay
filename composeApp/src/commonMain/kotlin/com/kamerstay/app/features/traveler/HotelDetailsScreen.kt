package com.kamerstay.app.features.traveler

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.kamerstay.app.core.navigation.NavigationState
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.model.Room
import com.kamerstay.app.viewmodel.TravelerViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

private val photoLabels = listOf("Extérieur", "Hall d'accueil", "Chambre", "Piscine", "Restaurant")

private fun amenityIcon(label: String): ImageVector {
    val l = label.lowercase()
    return when {
        "wifi" in l || "wi-fi" in l -> Icons.Outlined.Wifi
        "piscine" in l || "pool" in l -> Icons.Outlined.Pool
        "gym" in l || "fitness" in l -> Icons.Outlined.FitnessCenter
        "spa" in l -> Icons.Outlined.Spa
        "parking" in l -> Icons.Outlined.LocalParking
        "restaurant" in l -> Icons.Outlined.Restaurant
        "bar" in l -> Icons.Outlined.LocalBar
        "plage" in l || "beach" in l -> Icons.Outlined.BeachAccess
        "climatisation" in l || "ac" in l -> Icons.Outlined.AcUnit
        "tennis" in l -> Icons.Outlined.SportsTennis
        "golf" in l -> Icons.Outlined.GolfCourse
        "vue" in l || "panoramique" in l -> Icons.Outlined.Landscape
        "business" in l -> Icons.Outlined.BusinessCenter
        else -> Icons.Outlined.CheckCircle
    }
}

@Composable
fun HotelDetailsScreen(
    navController: NavController,
    hotelId: String
) {
    val viewModel = koinViewModel<TravelerViewModel>()
    val hotel = viewModel.selectedHotel
    val rooms = viewModel.hotelRooms
    val isLoading = viewModel.isLoadingHotelDetail

    LaunchedEffect(hotelId) {
        viewModel.loadHotelDetail(hotelId)
    }

    if (hotel == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Secondary)
        }
        return
    }

    val isFavorite = viewModel.wishlistState.isInWishlist(hotel.id)
    var showFullDescription by remember { mutableStateOf(false) }
    var showGallery by remember { mutableStateOf(false) }
    var galleryStartIndex by remember { mutableStateOf(0) }

    val photos = hotel.imageUrls.ifEmpty {
        listOf("https://images.unsplash.com/photo-1566073771259-6a8506099945?w=800&fit=crop&auto=format")
    }

    val heroPagerState = rememberPagerState(pageCount = { photos.size })
    val scope = rememberCoroutineScope()

    val amenities = hotel.amenities.map { label -> amenityIcon(label) to label }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    SecondaryContainer.copy(alpha = 0.35f),
                                    BackgroundLight,
                                    Color(0xFFE8F4F5)
                                )
                            )
                        )
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "À partir de",
                                fontSize = 12.sp,
                                color = OnSurfaceSecondary
                            )
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "${hotel.pricePerNight.toInt()} FCFA",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = LocalAppColors.current.textPrimary
                                )
                                Text(
                                    text = " /nuit",
                                    fontSize = 13.sp,
                                    color = OnSurfaceSecondary
                                )
                            }
                        }
                        Button(
                            onClick = {
                                val hotelId = hotel.id
                                val roomId  = rooms.firstOrNull()?.id ?: ""
                                NavigationState.selectedHotelId = hotelId
                                NavigationState.selectedRoomId  = roomId
                                navController.navigate(Routes.Booking.createRoute(hotelId, roomId))
                            },
                            shape = RoundedCornerShape(28.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Primary),
                            contentPadding = PaddingValues(horizontal = 28.dp, vertical = 14.dp)
                        ) {
                            Text(
                                text = "Check Dates",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
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
                // ── Hero with Pager ───────────────────────
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    ) {
                        HorizontalPager(
                            state = heroPagerState,
                            modifier = Modifier.fillMaxSize()
                        ) { page ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0xFF0D1A2A))
                                    .clickable { galleryStartIndex = page; showGallery = true }
                            ) {
                                AsyncImage(
                                    model = photos.getOrNull(page) ?: "",
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }

                        // Top bar overlay
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
                                        .clickable { navController.navigate(Routes.BookingVoucher.route) },
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
                                        if (isFavorite) Icons.Outlined.Favorite
                                        else Icons.Outlined.FavoriteBorder,
                                        contentDescription = null,
                                        tint = if (isFavorite) ErrorColor else Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        // Photo counter (bottom left)
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(start = 16.dp, bottom = 12.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color.Black.copy(0.5f))
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = "${heroPagerState.currentPage + 1} / ${photos.size} Photos",
                                fontSize = 12.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // Dot indicators (bottom center)
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 14.dp),
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            photos.indices.forEach { index ->
                                Box(
                                    modifier = Modifier
                                        .size(
                                            width = if (heroPagerState.currentPage == index) 16.dp else 6.dp,
                                            height = 6.dp
                                        )
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(
                                            if (heroPagerState.currentPage == index) Primary
                                            else Color.White.copy(0.4f)
                                        )
                                )
                            }
                        }

                        // View All button (bottom right)
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(end = 16.dp, bottom = 12.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Black.copy(0.5f))
                                .clickable {
                                    galleryStartIndex = heroPagerState.currentPage
                                    showGallery = true
                                }
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    Icons.Outlined.GridView,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = "View All",
                                    fontSize = 12.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // ── Thumbnail Strip ───────────────────────
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(LocalAppColors.current.surface)
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        photos.forEachIndexed { index, url ->
                            val isSelected = heroPagerState.currentPage == index
                            Box(
                                modifier = Modifier
                                    .size(70.dp, 50.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF0D1A2A))
                                    .then(
                                        if (isSelected) Modifier.border(2.dp, Primary, RoundedCornerShape(8.dp))
                                        else Modifier
                                    )
                                    .clickable { scope.launch { heroPagerState.animateScrollToPage(index) } },
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                AsyncImage(
                                    model = url,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(0.65f))))
                                        .padding(vertical = 3.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = photoLabels.getOrElse(index) { "${index + 1}" },
                                        fontSize = 9.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }

                // ── Hotel Info ────────────────────────────
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(LocalAppColors.current.surface)
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (hotel.isVerified) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Primary)
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "VERIFIED",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = OnPrimary
                                    )
                                }
                            }
                            Icon(
                                Icons.Outlined.Star,
                                contentDescription = null,
                                tint = StarRating,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "${hotel.rating}(${hotel.reviewCount} Reviews)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = LocalAppColors.current.textPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = hotel.name,
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
                                text = hotel.address,
                                fontSize = 13.sp,
                                color = OnSurfaceSecondary
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "About this stay",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = LocalAppColors.current.textPrimary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        val desc = hotel.description.ifEmpty {
                            "Experience Mediterranean elegance at its finest. ${hotel.name} offers unparalleled views of the sea, combining contemporary design with local architectural heritage. Each suite is a sanctuary of calm, featuring floor-to-ceiling windows, private balconies, and artisanal furnishings."
                        }

                        Text(
                            text = desc,
                            fontSize = 14.sp,
                            color = OnSurfaceSecondary,
                            lineHeight = 22.sp,
                            maxLines = if (showFullDescription) Int.MAX_VALUE else 4,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = if (showFullDescription) "Show less" else "Read more",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Primary,
                            modifier = Modifier
                                .clickable { showFullDescription = !showFullDescription }
                                .padding(top = 4.dp)
                        )
                    }
                }

                // ── Amenities ─────────────────────────────
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                    ) {
                        Text(
                            text = "Amenities",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = LocalAppColors.current.textPrimary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

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
                                                .background(LocalAppColors.current.surface)
                                                .border(1.dp, Divider, RoundedCornerShape(10.dp))
                                                .padding(12.dp)
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Icon(
                                                    icon,
                                                    contentDescription = null,
                                                    tint = Secondary,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                                Text(
                                                    text = label,
                                                    fontSize = 13.sp,
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

                // ── Available Rooms ───────────────────────
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Chambres disponibles",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = LocalAppColors.current.textPrimary
                        )
                        if (hotel.availableRooms > 0) {
                            Text(
                                text = "${hotel.availableRooms} dispo",
                                fontSize = 12.sp,
                                color = Secondary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                if (isLoading && rooms.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Secondary, modifier = Modifier.size(28.dp))
                        }
                    }
                } else {
                    items(rooms) { room ->
                        HotelRoomCard(
                            room = room,
                            onClick = {
                                NavigationState.selectedHotelId = hotel.id
                                NavigationState.selectedRoomId = room.id
                                navController.navigate(Routes.RoomDetails.createRoute(room.id))
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                // ── Location ──────────────────────────────
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Location",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = LocalAppColors.current.textPrimary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        HotelLocationMap(
                            latitude = hotel.latitude,
                            longitude = hotel.longitude,
                            hotelName = hotel.name,
                            onClick = { navController.navigate(Routes.MapLocation.route) }
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        // ── Fullscreen Gallery Overlay ────────────────
        if (showGallery) {
            HotelPhotoGallery(
                photos = photos,
                startIndex = galleryStartIndex,
                onDismiss = { showGallery = false }
            )
        }
    }
}

// ── Fullscreen Gallery ────────────────────────────────────────
@Composable
private fun HotelPhotoGallery(
    photos: List<String>,
    startIndex: Int,
    onDismiss: () -> Unit
) {
    var isZoomed by remember { mutableStateOf(false) }

    val pagerState = rememberPagerState(
        initialPage = startIndex,
        pageCount = { photos.size }
    )

    // Reset zoom when user swipes to another page
    LaunchedEffect(pagerState.currentPage) {
        isZoomed = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = !isZoomed,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            ZoomablePhotoPage(
                url = photos.getOrElse(page) { "" },
                onZoomChanged = { zoomed -> isZoomed = zoomed }
            )
        }

        // Top bar: close button + counter
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
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(0.15f))
                    .clickable { onDismiss() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.Close,
                    contentDescription = "Close gallery",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White.copy(0.15f))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "${pagerState.currentPage + 1} / ${photos.size}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            Spacer(Modifier.size(40.dp))
        }

        // Bottom: label + dots + hint (masqués quand zoomé)
        if (!isZoomed) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = photoLabels.getOrElse(pagerState.currentPage) { "Photo ${pagerState.currentPage + 1}" },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                Spacer(Modifier.height(14.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(photos.size) { index ->
                        Box(
                            modifier = Modifier
                                .size(
                                    width = if (pagerState.currentPage == index) 20.dp else 6.dp,
                                    height = 6.dp
                                )
                                .clip(RoundedCornerShape(3.dp))
                                .background(
                                    if (pagerState.currentPage == index) Primary
                                    else Color.White.copy(0.3f)
                                )
                        )
                    }
                }
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "Pinch to zoom  •  Double-tap to zoom in",
                    fontSize = 12.sp,
                    color = Color.White.copy(0.35f)
                )
            }
        }
    }
}

// ── Zoomable photo page ───────────────────────────────────────
@Composable
private fun ZoomablePhotoPage(
    url: String,
    onZoomChanged: (Boolean) -> Unit
) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val transformableState = rememberTransformableState { zoomChange, panChange, _ ->
        val newScale = (scale * zoomChange).coerceIn(1f, 4f)
        scale = newScale
        offset = if (newScale > 1f) offset + panChange else Offset.Zero
        onZoomChanged(newScale > 1f)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        if (scale > 1f) {
                            scale = 1f
                            offset = Offset.Zero
                            onZoomChanged(false)
                        } else {
                            scale = 2.5f
                            onZoomChanged(true)
                        }
                    }
                )
            }
            .transformable(state = transformableState),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                )
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = url,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

// ── Hotel Room Card ───────────────────────────────────────
@Composable
fun HotelRoomCard(
    room: Room,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(LocalAppColors.current.surface)
            .clickable { onClick() }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF1A3A5C),
                                Color(0xFF0D2A4A)
                            )
                        )
                    )
            )

            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = room.type.name.lowercase()
                        .replaceFirstChar { it.uppercase() } + " " +
                            if (room.type.name != "SUITE") "Room" else "",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalAppColors.current.textPrimary
                )

                Text(
                    text = room.description,
                    fontSize = 13.sp,
                    color = OnSurfaceSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("BREAKFAST INCLUDED", "FREE CANCELLATION").forEach { tag ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Primary.copy(0.1f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = tag,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Secondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = "Prix pour 2 nuits",
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary
                        )
                        Text(
                            text = "${(room.pricePerNight * 2).toInt()} FCFA",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = LocalAppColors.current.textPrimary
                        )
                        Text(
                            text = "total",
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary
                        )
                    }

                    Button(
                        onClick = onClick,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Secondary),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = "Select Room",
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