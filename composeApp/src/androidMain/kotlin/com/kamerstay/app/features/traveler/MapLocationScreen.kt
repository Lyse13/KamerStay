package com.kamerstay.app.features.traveler

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.kamerstay.app.core.navigation.NavigationState
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.model.Hotel
import com.kamerstay.app.viewmodel.TravelerViewModel
import org.koin.compose.viewmodel.koinViewModel

@SuppressLint("MissingPermission")
@Composable
fun MapLocationScreen(navController: NavController) {

    val context   = LocalContext.current
    val viewModel = koinViewModel<TravelerViewModel>()

    val yaoundeLatLng = LatLng(3.8480, 11.5021)

    var userLocation       by remember { mutableStateOf<LatLng?>(null) }
    var isSatellite        by remember { mutableStateOf(false) }
    var selectedHotelOnMap by remember { mutableStateOf<Hotel?>(null) }

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED
        )
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(yaoundeLatLng, 6f)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    LaunchedEffect(Unit) {
        if (!hasPermission) {
            permissionLauncher.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        }
        if (viewModel.hotels.isEmpty()) viewModel.loadHotels()

        // Si un hôtel est sélectionné (depuis HotelDetailsScreen), centrer dessus
        val focusHotel = viewModel.selectedHotel
        if (focusHotel != null && focusHotel.latitude != 0.0 && focusHotel.longitude != 0.0) {
            cameraPositionState.animate(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.fromLatLngZoom(
                        LatLng(focusHotel.latitude, focusHotel.longitude), 15f
                    )
                ),
                durationMs = 600
            )
            selectedHotelOnMap = focusHotel
        }
    }

    LaunchedEffect(hasPermission) {
        if (hasPermission) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { location ->
                    location?.let { userLocation = LatLng(it.latitude, it.longitude) }
                }
        }
    }

    LaunchedEffect(userLocation) {
        userLocation?.let { location ->
            cameraPositionState.animate(
                CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(location, 14f)),
                durationMs = 1000
            )
        }
    }

    val hotels = viewModel.hotels

    Box(modifier = Modifier.fillMaxSize()) {

        // ── Google Map ──────────────────────────────────────────────
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = hasPermission,
                mapType = if (isSatellite) MapType.HYBRID else MapType.NORMAL
            ),
            uiSettings = MapUiSettings(
                myLocationButtonEnabled = false,
                zoomControlsEnabled = false,
                compassEnabled = true
            ),
            onMapClick = { selectedHotelOnMap = null }
        ) {
            userLocation?.let { location ->
                Circle(
                    center = location,
                    radius = 80.0,
                    fillColor = Primary.copy(alpha = 0.15f),
                    strokeColor = Primary,
                    strokeWidth = 3f
                )
            }

            hotels.forEach { hotel ->
                if (hotel.latitude != 0.0 && hotel.longitude != 0.0) {
                    val isSelected = selectedHotelOnMap?.id == hotel.id
                    MarkerComposable(
                        state = MarkerState(position = LatLng(hotel.latitude, hotel.longitude)),
                        title = hotel.name,
                        snippet = "${hotel.pricePerNight.toLong()} FCFA / nuit",
                        onClick = {
                            viewModel.selectHotel(hotel.id)
                            selectedHotelOnMap = hotel
                            true  // consomme l'événement, évite la bulle info par défaut
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSelected) Primary else Secondary)
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "${(hotel.pricePerNight / 1000).toInt()}k FCFA",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // ── Top Bar ─────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour", tint = Secondary)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text("Hôtels à proximité", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Secondary)
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Primary)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${hotels.count { it.latitude != 0.0 }}",
                        fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White
                    )
                }
            }
        }

        // ── FAB buttons (se décalent vers le haut quand la card est visible) ──
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = 16.dp,
                    bottom = if (selectedHotelOnMap != null) 220.dp else 32.dp
                ),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FloatingActionButton(
                onClick = { isSatellite = !isSatellite },
                modifier = Modifier.size(46.dp),
                containerColor = Color.White,
                shape = RoundedCornerShape(12.dp),
                elevation = FloatingActionButtonDefaults.elevation(4.dp)
            ) {
                Icon(Icons.Outlined.Layers, contentDescription = "Type de carte",
                    tint = if (isSatellite) Primary else Secondary, modifier = Modifier.size(22.dp))
            }
            FloatingActionButton(
                onClick = {
                    cameraPositionState.move(CameraUpdateFactory.zoomTo(cameraPositionState.position.zoom + 1.5f))
                },
                modifier = Modifier.size(46.dp),
                containerColor = Color.White,
                shape = RoundedCornerShape(12.dp),
                elevation = FloatingActionButtonDefaults.elevation(4.dp)
            ) {
                Icon(Icons.Outlined.Add, contentDescription = "Zoom avant",
                    tint = Secondary, modifier = Modifier.size(22.dp))
            }
            FloatingActionButton(
                onClick = {
                    cameraPositionState.move(CameraUpdateFactory.zoomTo(cameraPositionState.position.zoom - 1.5f))
                },
                modifier = Modifier.size(46.dp),
                containerColor = Color.White,
                shape = RoundedCornerShape(12.dp),
                elevation = FloatingActionButtonDefaults.elevation(4.dp)
            ) {
                Icon(Icons.Outlined.Remove, contentDescription = "Zoom arrière",
                    tint = Secondary, modifier = Modifier.size(22.dp))
            }
            FloatingActionButton(
                onClick = {
                    if (hasPermission) {
                        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                            .addOnSuccessListener { loc ->
                                loc?.let { userLocation = LatLng(it.latitude, it.longitude) }
                            }
                    } else {
                        permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
                    }
                },
                modifier = Modifier.size(52.dp),
                containerColor = Primary,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(6.dp)
            ) {
                Icon(Icons.Outlined.MyLocation, contentDescription = "Ma position",
                    tint = Color.White, modifier = Modifier.size(24.dp))
            }
        }

        // ── Hotel info card (bottom sheet léger, animé) ─────────────
        AnimatedVisibility(
            visible = selectedHotelOnMap != null,
            enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(300))
                  + fadeIn(animationSpec = tween(250)),
            exit  = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(200))
                  + fadeOut(animationSpec = tween(150)),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            selectedHotelOnMap?.let { hotel ->
                HotelMapInfoCard(
                    hotel     = hotel,
                    onClose   = { selectedHotelOnMap = null },
                    onDetails = {
                        NavigationState.selectedHotelId = hotel.id
                        viewModel.selectHotel(hotel.id)
                        navController.navigate(Routes.HotelDetails.createRoute(hotel.id))
                    },
                    onBook    = {
                        NavigationState.selectedHotelId = hotel.id
                        viewModel.loadHotelDetail(hotel.id)
                        navController.navigate(Routes.HotelDetails.createRoute(hotel.id))
                    }
                )
            }
        }

        // ── Permission card (masquée si une hotel card est affichée) ──
        if (!hasPermission && selectedHotelOnMap == null) {
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Secondary),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Autoriser la localisation", fontWeight = FontWeight.Bold,
                        color = Color.White, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Pour voir les hôtels les plus proches de votre position.",
                        color = Color.White.copy(0.85f), fontSize = 13.sp, lineHeight = 18.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            permissionLauncher.launch(arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ))
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Primary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Autoriser l'accès", color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

// ── Hotel info card ─────────────────────────────────────────────────────────

@Composable
private fun HotelMapInfoCard(
    hotel: Hotel,
    onClose: () -> Unit,
    onDetails: () -> Unit,
    onBook: () -> Unit
) {
    val imageUrl  = hotel.imageUrls.firstOrNull() ?: ""
    val gradient  = listOf(Color(0xFF1A3A5C), Color(0xFF0D2A4A))
    val ratingStr = if (hotel.rating > 0) "%.1f".format(hotel.rating) else "—"
    val priceStr  = hotel.pricePerNight.toInt().toString()
        .reversed().chunked(3).joinToString(" ").reversed()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
    ) {
        Column {
            // ── Image ──────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(Brush.verticalGradient(colors = gradient))
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
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(Icons.Outlined.Verified, null, tint = Color.White, modifier = Modifier.size(11.dp))
                            Text("Vérifié", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
                // Close button
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(0.35f))
                ) {
                    Icon(Icons.Outlined.Close, contentDescription = "Fermer",
                        tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }

            // ── Info ────────────────────────────────────
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
                        color = Secondary,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(Icons.Outlined.StarOutline, null, tint = StarRating, modifier = Modifier.size(14.dp))
                        Text(ratingStr, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Secondary)
                        if (hotel.reviewCount > 0) {
                            Text("(${hotel.reviewCount})", fontSize = 12.sp, color = OnSurfaceSecondary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Outlined.Place, null, tint = OnSurfaceSecondary, modifier = Modifier.size(13.dp))
                    Text(hotel.city, fontSize = 13.sp, color = OnSurfaceSecondary)
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("$priceStr FCFA", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Primary)
                    Text("/nuit", fontSize = 13.sp, color = OnSurfaceSecondary,
                        modifier = Modifier.padding(bottom = 2.dp))
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDetails,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, Secondary)
                    ) {
                        Text("Voir les détails", fontSize = 13.sp, color = Secondary,
                            fontWeight = FontWeight.SemiBold)
                    }
                    Button(
                        onClick = onBook,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Secondary)
                    ) {
                        Text("Réserver", fontSize = 13.sp, color = Color.White,
                            fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}