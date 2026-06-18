package com.kamerstay.app.features.traveler

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.kamerstay.app.core.components.TravelerBottomNavBar
import com.kamerstay.app.core.navigation.NavigationState
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.mock.MapMockData
import com.kamerstay.app.data.model.MapHotel
import com.kamerstay.app.data.model.emoji
import com.kamerstay.app.viewmodel.TravelerViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.io.File
import java.text.NumberFormat
import java.util.Locale

// ── Formatage FCFA ────────────────────────────────────────────────────────────
private fun Int.toFcfa(): String =
    "${NumberFormat.getNumberInstance(Locale.FRANCE).format(this)} FCFA"

private fun Int.toShortFcfa(): String = "${this / 1_000}k"

@SuppressLint("MissingPermission")
@Composable
fun MapLocationScreen(navController: NavController) {
    val viewModel    = koinViewModel<TravelerViewModel>()
    val state        = viewModel.mapState
    val context      = LocalContext.current
    val density      = LocalDensity.current.density
    val lifecycleOwner = LocalLifecycleOwner.current
    val focusManager = LocalFocusManager.current

    val filterState   = viewModel.filterState
    val mapViewRef    = remember { mutableStateOf<MapView?>(null) }
    val filteredHotels = state.applyFilters(filterState)

    // ── Permission de localisation ─────────────────────────────────────────
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> state.locationGranted = granted }

    LaunchedEffect(Unit) {
        val granted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (granted) state.locationGranted = true
        else permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    // Fix 5 — Sync FilterScreen landmark → repère sélectionné sur la carte
    LaunchedEffect(filterState.selectedLandmark, state.selectedCity) {
        state.syncLandmarkFromFilter(filterState.selectedLandmark)
    }

    // ── Récupération GPS via LocationManager ───────────────────────────────
    val locationManager = remember {
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
    val locationListener = remember {
        object : LocationListener {
            override fun onLocationChanged(loc: Location) {
                state.userLat = loc.latitude
                state.userLng = loc.longitude
            }
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }
    }

    DisposableEffect(state.locationGranted) {
        if (state.locationGranted) {
            try {
                // Essaie les 3 fournisseurs pour une position instantanée
                val last = listOf(
                    LocationManager.GPS_PROVIDER,
                    LocationManager.NETWORK_PROVIDER,
                    LocationManager.PASSIVE_PROVIDER
                ).firstNotNullOfOrNull { provider ->
                    try { locationManager.getLastKnownLocation(provider) } catch (_: Exception) { null }
                }
                last?.let { state.userLat = it.latitude; state.userLng = it.longitude }
                // Mises à jour toutes les 2 s / 5 m sur GPS + réseau
                listOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER).forEach { provider ->
                    if (locationManager.isProviderEnabled(provider))
                        locationManager.requestLocationUpdates(provider, 2000L, 5f, locationListener)
                }
            } catch (_: Exception) {}
        }
        onDispose {
            try { locationManager.removeUpdates(locationListener) } catch (_: Exception) {}
        }
    }

    // ── Sélection d'hôtel valide quand filteredHotels change ───────────────
    LaunchedEffect(filteredHotels) {
        if (filteredHotels.none { it.id == state.selectedHotelId })
            filteredHotels.firstOrNull()?.let { state.selectedHotelId = it.id }
    }

    // ── Changement de ville → réinitialiser search + repère ────────────────
    LaunchedEffect(state.selectedCity) {
        state.searchText = ""; state.selectedLandmarkId = null
        val city = MapMockData.cities[state.selectedCity] ?: return@LaunchedEffect
        mapViewRef.value?.controller?.animateTo(GeoPoint(city.lat, city.lng), city.zoom, 800L)
        state.selectedHotelId = MapMockData.hotels.firstOrNull { it.city == state.selectedCity }?.id ?: ""
    }

    // ── Centrer sur l'hôtel sélectionné (niveau bâtiment) ──────────────────
    LaunchedEffect(state.selectedHotelId) {
        val h = MapMockData.hotels.find { it.id == state.selectedHotelId } ?: return@LaunchedEffect
        mapViewRef.value?.controller?.animateTo(GeoPoint(h.lat, h.lng), 17.5, 500L)
    }

    // ── Centrer sur le repère sélectionné (niveau quartier) ────────────────
    LaunchedEffect(state.selectedLandmarkId) {
        val lm = state.selectedLandmark ?: return@LaunchedEffect
        mapViewRef.value?.controller?.animateTo(GeoPoint(lm.lat, lm.lng), 16.5, 600L)
    }

    // ── GPS reçu → auto-détecter la ville + centrer sur ma position ─────────
    LaunchedEffect(state.userLat, state.userLng) {
        val lat = state.userLat ?: return@LaunchedEffect
        val lng = state.userLng ?: return@LaunchedEffect

        // 1. Détecter la ville la plus proche de ma position réelle
        val detectedCity = state.detectCityFromPosition(lat, lng)
        if (state.selectedCity != detectedCity) state.selectedCity = detectedCity

        // 2. Centrer la carte sur MA position (niveau rue)
        if (state.selectedLandmarkId == null) {
            mapViewRef.value?.controller?.animateTo(GeoPoint(lat, lng), 17.0, 800L)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // ── OpenStreetMap ──────────────────────────────────────────────────
        AndroidView(
            factory = { ctx ->
                Configuration.getInstance().apply {
                    load(ctx, ctx.getSharedPreferences("osm_prefs", 0))
                    osmdroidBasePath = File(ctx.cacheDir, "osmdroid")
                    osmdroidTileCache = File(ctx.cacheDir, "osmdroid/tiles")
                    userAgentValue = "KamerStay/1.0"
                }
                MapView(ctx).also { mv ->
                    mv.setTileSource(TileSourceFactory.MAPNIK)
                    mv.setMultiTouchControls(true)
                    mv.isHorizontalMapRepetitionEnabled = false
                    mv.isVerticalMapRepetitionEnabled = false
                    mv.minZoomLevel = 3.0
                    mv.maxZoomLevel = 21.0
                    mv.controller.setZoom(15.0)
                    mv.controller.setCenter(GeoPoint(MapMockData.DOUALA_LAT, MapMockData.DOUALA_LNG))
                    lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                        override fun onResume(owner: LifecycleOwner) = mv.onResume()
                        override fun onPause(owner: LifecycleOwner) = mv.onPause()
                        override fun onDestroy(owner: LifecycleOwner) = mv.onDetach()
                    })
                    mapViewRef.value = mv
                }
            },
            update = { mv ->
                val selectedId = state.selectedHotelId
                val landmark   = state.selectedLandmark
                val uLat = state.userLat; val uLng = state.userLng

                mv.overlays.clear()

                // Marqueurs hôtels
                filteredHotels.forEach { hotel ->
                    val isSelected = hotel.id == selectedId
                    Marker(mv).apply {
                        position = GeoPoint(hotel.lat, hotel.lng)
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        title = hotel.name
                        icon = BitmapDrawable(mv.context.resources,
                            createPriceMarkerBitmap(hotel.priceXaf, isSelected, density))
                        setOnMarkerClickListener { _, _ -> state.selectedHotelId = hotel.id; true }
                        mv.overlays.add(this)
                    }
                }

                // Marqueur repère sélectionné
                if (landmark != null) {
                    Marker(mv).apply {
                        position = GeoPoint(landmark.lat, landmark.lng)
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        title = landmark.name
                        icon = BitmapDrawable(mv.context.resources,
                            createLandmarkMarkerBitmap(landmark.type.emoji(), density))
                        mv.overlays.add(this)
                    }
                }

                // Point bleu = position utilisateur
                if (uLat != null && uLng != null) {
                    Marker(mv).apply {
                        position = GeoPoint(uLat, uLng)
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                        title = "Ma position"
                        icon = BitmapDrawable(mv.context.resources, createUserDotBitmap(density))
                        mv.overlays.add(this)
                    }
                }

                mv.invalidate()
            },
            modifier = Modifier.fillMaxSize()
        )

        // ── Barre du haut + Recherche + Filtres ───────────────────────────
        Column(modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter)) {

            // Top bar
            Row(
                modifier = Modifier.fillMaxWidth().statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(40.dp).shadow(3.dp, CircleShape)
                            .clip(CircleShape).background(Color.White)
                            .clickable { navController.popBackStack() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, null, tint = Secondary, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("KamerStay", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Secondary,
                        modifier = Modifier.shadow(2.dp, RoundedCornerShape(8.dp))
                            .background(Color.White.copy(alpha = 0.9f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp))
                }
                // Indicateur GPS
                Box(
                    modifier = Modifier.size(40.dp).shadow(3.dp, CircleShape)
                        .clip(CircleShape)
                        .background(if (state.userLat != null) Primary.copy(0.15f) else Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (state.userLat != null) Icons.Outlined.MyLocation else Icons.Outlined.LocationOff,
                        null,
                        tint = if (state.userLat != null) Primary else OnSurfaceSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Barre de recherche
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    .shadow(5.dp, RoundedCornerShape(28.dp)).clip(RoundedCornerShape(28.dp))
                    .background(Color.White).padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.Search, null,
                    tint = if (state.searchText.isEmpty()) Secondary else Primary,
                    modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(10.dp))
                BasicTextField(
                    value = state.searchText,
                    onValueChange = { state.searchText = it },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 15.sp, color = TextDark),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                    decorationBox = { inner ->
                        Box {
                            if (state.searchText.isEmpty()) {
                                Text(
                                    text = MapMockData.cities[state.selectedCity]
                                        ?.let { "Hôtels, repères à ${it.displayName}..." }
                                        ?: "Rechercher un hôtel ou repère...",
                                    fontSize = 15.sp, color = OnSurfaceSecondary
                                )
                            }
                            inner()
                        }
                    }
                )
                if (state.searchText.isNotEmpty()) {
                    Box(
                        modifier = Modifier.size(22.dp).clip(CircleShape)
                            .background(OnSurfaceSecondary.copy(0.15f))
                            .clickable { state.searchText = ""; focusManager.clearFocus() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Outlined.Close, null, tint = OnSurfaceSecondary, modifier = Modifier.size(13.dp))
                    }
                } else {
                    Box(modifier = Modifier.width(1.dp).height(20.dp).background(Divider))
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(modifier = Modifier.size(20.dp).clickable { navController.navigate(Routes.Filter.route) }) {
                        Icon(
                            Icons.Outlined.Tune, null,
                            tint = if (filterState.hasActiveFilters) Primary else Secondary,
                            modifier = Modifier.fillMaxSize()
                        )
                        if (filterState.hasActiveFilters) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp).clip(CircleShape)
                                    .background(ErrorColor)
                                    .align(Alignment.TopEnd)
                            )
                        }
                    }
                }
            }

            // Résultats de recherche
            if (state.searchText.isNotEmpty()) {
                val count = filteredHotels.size
                Text(
                    text = if (count == 0) "Aucun résultat" else "$count hôtel${if (count > 1) "s" else ""} trouvé${if (count > 1) "s" else ""}",
                    fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 24.dp, top = 5.dp)
                        .shadow(2.dp, RoundedCornerShape(6.dp))
                        .background(Secondary.copy(0.85f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Chips de villes
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(MapMockData.cities.entries.toList()) { (key, city) ->
                    val isSelected = key == state.selectedCity
                    Box(
                        modifier = Modifier
                            .shadow(if (isSelected) 4.dp else 2.dp, RoundedCornerShape(20.dp))
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) Secondary else Color.White)
                            .clickable { state.selectedCity = key }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(city.displayName, fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.White else TextDark)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ── Repères camerounais ────────────────────────────────────────
            val cityLandmarks = MapMockData.landmarksForCity(state.selectedCity)
            if (cityLandmarks.isNotEmpty()) {
                // Effacer le repère si actif
                if (state.selectedLandmarkId != null) {
                    Row(
                        modifier = Modifier.padding(start = 16.dp, bottom = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .shadow(3.dp, RoundedCornerShape(20.dp))
                                .clip(RoundedCornerShape(20.dp))
                                .background(ErrorColor.copy(0.12f))
                                .border(1.dp, ErrorColor.copy(0.4f), RoundedCornerShape(20.dp))
                                .clickable { state.selectedLandmarkId = null }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(Icons.Outlined.Close, null, tint = ErrorColor, modifier = Modifier.size(12.dp))
                                Text("Effacer le repère", fontSize = 11.sp, color = ErrorColor, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(cityLandmarks) { landmark ->
                        val isSelected = landmark.id == state.selectedLandmarkId
                        Box(
                            modifier = Modifier
                                .shadow(if (isSelected) 4.dp else 1.dp, RoundedCornerShape(20.dp))
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) Color(0xFFFF8F00) else Color.White)
                                .then(if (isSelected) Modifier.border(0.dp, Color.Transparent, RoundedCornerShape(20.dp))
                                      else Modifier.border(1.dp, OnSurfaceSecondary.copy(0.2f), RoundedCornerShape(20.dp)))
                                .clickable {
                                    state.selectedLandmarkId = if (isSelected) null else landmark.id
                                }
                                .padding(horizontal = 12.dp, vertical = 7.dp)
                        ) {
                            Text(
                                text = "${landmark.type.emoji()} ${landmark.name}",
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else TextDark
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // ── Contrôles zoom + Ma position ──────────────────────────────────
        Column(
            modifier = Modifier.align(Alignment.CenterEnd).padding(end = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MapControlButton(Icons.Outlined.Add) { mapViewRef.value?.controller?.zoomIn() }
            MapControlButton(Icons.Outlined.Remove) { mapViewRef.value?.controller?.zoomOut() }
            MapControlButton(Icons.Outlined.MyLocation) {
                val lat = state.userLat; val lng = state.userLng
                if (lat != null && lng != null) {
                    mapViewRef.value?.controller?.animateTo(GeoPoint(lat, lng), 17.0, 500L)
                } else {
                    val city = MapMockData.cities[state.selectedCity] ?: return@MapControlButton
                    mapViewRef.value?.controller?.animateTo(GeoPoint(city.lat, city.lng), city.zoom, 500L)
                }
            }
        }

        // ── Bandeau repère actif ───────────────────────────────────────────
        state.selectedLandmark?.let { lm ->
            Box(
                modifier = Modifier.align(Alignment.CenterStart).padding(start = 12.dp)
                    .shadow(4.dp, RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFF8F00))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Column {
                    Text("${lm.type.emoji()} ${lm.name}", fontSize = 11.sp,
                        fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Hôtels triés par proximité", fontSize = 9.sp, color = Color.White.copy(0.85f))
                }
            }
        }

        // ── Cards hôtels + BottomNav ───────────────────────────────────────
        Column(modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)) {
            if (filteredHotels.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().height(130.dp), contentAlignment = Alignment.Center) {
                    Text("Aucun hôtel correspondant", fontSize = 14.sp, color = Color.White,
                        modifier = Modifier.shadow(3.dp, RoundedCornerShape(10.dp))
                            .background(Secondary.copy(0.8f), RoundedCornerShape(10.dp))
                            .padding(horizontal = 16.dp, vertical = 10.dp))
                }
            } else {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredHotels, key = { it.id }) { hotel ->
                        MapHotelCard(
                            hotel = hotel,
                            isSelected = hotel.id == state.selectedHotelId,
                            distanceLabel = state.distanceLabel(hotel.lat, hotel.lng),
                            distanceContext = state.distanceContext(),
                            travelTimeLabel = state.travelTimeLabel(hotel.lat, hotel.lng),
                            onSelect = { state.selectedHotelId = hotel.id },
                            onBook = {
                                NavigationState.selectedHotelId = hotel.id
                                navController.navigate(Routes.HotelDetails.createRoute(hotel.id))
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            TravelerBottomNavBar(navController = navController, selectedTab = 1)
        }
    }
}

// ── Bouton de contrôle zoom ───────────────────────────────────────────────────
@Composable
private fun MapControlButton(icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Box(
        modifier = Modifier.size(46.dp).shadow(4.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp)).background(Color.White).clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, null, tint = TextDark, modifier = Modifier.size(22.dp))
    }
}

// ── Card hôtel ────────────────────────────────────────────────────────────────
@Composable
private fun MapHotelCard(
    hotel: MapHotel,
    isSelected: Boolean,
    distanceLabel: String,
    distanceContext: String,
    travelTimeLabel: String,
    onSelect: () -> Unit,
    onBook: () -> Unit
) {
    Box(
        modifier = Modifier.width(295.dp)
            .shadow(if (isSelected) 8.dp else 4.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp)).background(Color.White)
            .then(if (isSelected) Modifier.border(1.5.dp, Primary, RoundedCornerShape(16.dp)) else Modifier)
            .clickable { onSelect() }
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            // Image hôtel
            Box(modifier = Modifier.size(84.dp).clip(RoundedCornerShape(10.dp))) {
                Box(modifier = Modifier.fillMaxSize().background(
                    Brush.linearGradient(listOf(Color(0xFF1A3A5C), Color(0xFF0D2A4A)))))
                AsyncImage(model = hotel.imageUrl, contentDescription = hotel.name,
                    contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                Box(modifier = Modifier.padding(5.dp).size(22.dp).clip(CircleShape)
                    .background(Color.White.copy(0.85f)), contentAlignment = Alignment.Center) {
                    Icon(Icons.Outlined.FavoriteBorder, null, tint = ErrorColor, modifier = Modifier.size(12.dp))
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Nom + note
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                    Text(hotel.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextDark,
                        modifier = Modifier.weight(1f), lineHeight = 17.sp, maxLines = 2)
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp),
                        modifier = Modifier.padding(start = 4.dp)) {
                        Icon(Icons.Outlined.StarOutline, null, tint = StarRating, modifier = Modifier.size(13.dp))
                        Text(hotel.rating.toString(), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextDark)
                    }
                }

                // Distance + temps de trajet
                if (distanceLabel.isNotEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Distance
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            Icon(Icons.Outlined.Place, null, tint = Primary, modifier = Modifier.size(11.dp))
                            Text("$distanceLabel ${if (distanceContext.isNotEmpty()) "• $distanceContext" else ""}".trim(),
                                fontSize = 10.sp, color = Primary, fontWeight = FontWeight.SemiBold,
                                maxLines = 1)
                        }
                    }
                    // Temps de trajet depuis ma position (seulement si GPS actif)
                    if (travelTimeLabel.isNotEmpty()) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            Icon(Icons.Outlined.DirectionsCar, null, tint = OnSurfaceSecondary, modifier = Modifier.size(11.dp))
                            Text(travelTimeLabel, fontSize = 10.sp, color = OnSurfaceSecondary)
                        }
                    }
                } else {
                    Text("${hotel.location} · ${hotel.distance}", fontSize = 11.sp, color = OnSurfaceSecondary)
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Commodités
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    hotel.amenities.take(3).forEach { amenity ->
                        Box(modifier = Modifier.clip(RoundedCornerShape(4.dp))
                            .background(Primary.copy(0.1f)).padding(horizontal = 5.dp, vertical = 2.dp)) {
                            Text(amenity, fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Secondary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Prix + chambres + réserver
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text(hotel.priceXaf.toFcfa(), fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = Primary)
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            Icon(Icons.Outlined.MeetingRoom, null, modifier = Modifier.size(10.dp),
                                tint = if (hotel.availableRooms <= 2) ErrorColor else OnSurfaceSecondary)
                            Text(
                                if (hotel.availableRooms <= 2) "Plus que ${hotel.availableRooms} !"
                                else "${hotel.availableRooms} chambres dispo.",
                                fontSize = 9.sp,
                                color = if (hotel.availableRooms <= 2) ErrorColor else OnSurfaceSecondary,
                                fontWeight = if (hotel.availableRooms <= 2) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                    Button(onClick = onBook, shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Secondary),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)) {
                        Text("Réserver", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                    }
                }
            }
        }
    }
}

// ── Bitmap prix hôtel (bulle foncée) ─────────────────────────────────────────
private fun createPriceMarkerBitmap(priceXaf: Int, isSelected: Boolean, density: Float): Bitmap {
    val text = priceXaf.toShortFcfa()
    val textSizePx = 13f * density
    val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.textSize = textSizePx; typeface = Typeface.DEFAULT_BOLD; textAlign = Paint.Align.CENTER
    }
    val textWidth = textPaint.measureText(text)
    val padH = 14f * density; val padV = 7f * density; val triH = 7f * density
    val bmpW = (textWidth + padH * 2).toInt()
    val pillH = (textSizePx + padV * 2).toInt()
    val bmpH = (pillH + triH).toInt()

    val bmp = Bitmap.createBitmap(bmpW, bmpH, Bitmap.Config.ARGB_8888)
    val cv = Canvas(bmp)
    val bgColor = if (isSelected) android.graphics.Color.parseColor("#1A3A5C")
    else android.graphics.Color.parseColor("#0D2A4A")
    val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = bgColor }
    val pillRect = RectF(0f, 0f, bmpW.toFloat(), pillH.toFloat())
    val cornerR = pillH / 2f
    cv.drawRoundRect(pillRect, cornerR, cornerR, bgPaint)
    val triPath = Path().apply {
        moveTo(bmpW / 2f - 5f * density, pillH.toFloat())
        lineTo(bmpW / 2f + 5f * density, pillH.toFloat())
        lineTo(bmpW / 2f, bmpH.toFloat()); close()
    }
    cv.drawPath(triPath, bgPaint)
    if (isSelected) {
        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.parseColor("#3B82F6")
            style = Paint.Style.STROKE; strokeWidth = 2f * density
        }
        cv.drawRoundRect(pillRect, cornerR, cornerR, borderPaint)
    }
    textPaint.color = android.graphics.Color.WHITE
    cv.drawText(text, bmpW / 2f, padV + textSizePx, textPaint)
    return bmp
}

// ── Bitmap repère (étoile ambre) ─────────────────────────────────────────────
private fun createLandmarkMarkerBitmap(emoji: String, density: Float): Bitmap {
    val size = (44 * density).toInt()
    val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val cv = Canvas(bmp)

    // Cercle ambre
    val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = android.graphics.Color.parseColor("#FF8F00") }
    cv.drawCircle(size / 2f, size / 2f, size / 2f, bgPaint)

    // Bordure blanche
    val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.WHITE; style = Paint.Style.STROKE; strokeWidth = 2f * density
    }
    cv.drawCircle(size / 2f, size / 2f, size / 2f - density, borderPaint)

    // Emoji au centre
    val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 18f * density; textAlign = Paint.Align.CENTER
    }
    val fontMetrics = textPaint.fontMetrics
    val y = size / 2f - (fontMetrics.ascent + fontMetrics.descent) / 2f
    cv.drawText(emoji, size / 2f, y, textPaint)

    return bmp
}

// ── Bitmap point bleu utilisateur ────────────────────────────────────────────
private fun createUserDotBitmap(density: Float): Bitmap {
    val size = (22 * density).toInt()
    val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val cv = Canvas(bmp)

    val outerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.parseColor("#2196F3"); alpha = 70
    }
    cv.drawCircle(size / 2f, size / 2f, size / 2f, outerPaint)

    val innerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.parseColor("#2196F3")
    }
    cv.drawCircle(size / 2f, size / 2f, size / 3.5f, innerPaint)

    val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.WHITE; style = Paint.Style.STROKE; strokeWidth = 2f * density
    }
    cv.drawCircle(size / 2f, size / 2f, size / 3.5f, borderPaint)

    return bmp
}