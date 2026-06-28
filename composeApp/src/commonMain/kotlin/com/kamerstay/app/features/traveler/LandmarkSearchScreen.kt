package com.kamerstay.app.features.traveler

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.kamerstay.app.model.HotelWithDistance
import com.kamerstay.app.model.Landmark
import com.kamerstay.app.viewmodel.TravelerViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LandmarkSearchScreen(navController: NavController) {

    val viewModel          = koinViewModel<TravelerViewModel>()
    val landmarks          = viewModel.landmarks
    val isLoadingLandmarks = viewModel.isLoadingLandmarks
    val selected           = viewModel.selectedLandmark
    val nearbyHotels       = viewModel.hotelsNearLandmark
    val isLoadingNearby    = viewModel.isLoadingNearbyHotels

    // Grouper par ville, triés alphabétiquement — sans toSortedMap() (JVM-only)
    val grouped: Map<String, List<Landmark>> = remember(landmarks) {
        landmarks
            .groupBy { it.city }
            .entries
            .sortedBy { it.key }
            .associate { it.key to it.value }
    }

    // Index du premier item résultat pour le scroll auto
    val resultsStartIndex: Int = remember(grouped) {
        var count = 1 // "Choisir un lieu" header
        grouped.entries.forEach { entry ->
            count += 1 + entry.value.size // city header + ses landmarks
        }
        count
    }

    val listState = rememberLazyListState()
    val scope     = rememberCoroutineScope()

    LaunchedEffect(Unit) { viewModel.loadLandmarks() }

    Scaffold(
        containerColor = LocalAppColors.current.background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Retour",
                        tint = Secondary
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Recherche par lieu",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalAppColors.current.textPrimary
                )
            }
        }
    ) { padding ->

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {

            // ── En-tête de section ────────────────────────────────
            item(key = "section_header") {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                    Text(
                        text = "Choisir un lieu",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = LocalAppColors.current.textPrimary
                    )
                    Text(
                        text = "Sélectionnez un lieu pour trouver les hôtels à proximité",
                        fontSize = 12.sp,
                        color = OnSurfaceSecondary,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            // ── Chargement des landmarks ──────────────────────────
            if (isLoadingLandmarks) {
                item(key = "loading_landmarks") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator(color = Secondary) }
                }
            } else {
                // ── Landmarks groupés par ville ───────────────────
                grouped.entries.forEach { entry ->
                    val city          = entry.key
                    val cityLandmarks = entry.value

                    item(key = "city_$city") {
                        Text(
                            text = city.uppercase(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Secondary,
                            letterSpacing = 1.sp,
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .padding(top = 16.dp, bottom = 6.dp)
                        )
                    }

                    items(
                        items = cityLandmarks,
                        key   = { landmark: Landmark -> "lm_${landmark.id}" }
                    ) { landmark ->
                        LandmarkRow(
                            landmark   = landmark,
                            isSelected = selected?.id == landmark.id,
                            onClick    = {
                                viewModel.searchHotelsNearLandmark(landmark)
                                scope.launch {
                                    listState.animateScrollToItem(resultsStartIndex)
                                }
                            }
                        )
                    }
                }
            }

            // ── Séparateur ────────────────────────────────────────
            item(key = "results_divider") {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(
                    modifier  = Modifier.padding(horizontal = 20.dp),
                    color     = LocalAppColors.current.surface,
                    thickness = 1.dp
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // ── En-tête résultats ─────────────────────────────────
            item(key = "results_header") {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 12.dp),
                    verticalAlignment    = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Outlined.Hotel,
                        contentDescription = null,
                        tint   = Secondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = if (selected != null)
                            "Hôtels près de ${selected.name}"
                        else
                            "Hôtels à proximité",
                        fontSize   = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = LocalAppColors.current.textPrimary
                    )
                }
            }

            // ── États résultats ───────────────────────────────────
            when {
                selected == null -> item(key = "prompt") { EmptyPrompt() }
                isLoadingNearby  -> item(key = "loading_hotels") { NearbyLoadingIndicator() }
                nearbyHotels.isEmpty() -> item(key = "no_results") { NoNearbyResults() }
                else -> items(
                    items = nearbyHotels,
                    key   = { hwd: HotelWithDistance -> "hw_${hwd.hotel.id}" }
                ) { hwd ->
                    HotelWithDistanceCard(
                        hwd     = hwd,
                        onClick = {
                            NavigationState.selectedHotelId = hwd.hotel.id
                            navController.navigate(Routes.HotelDetails.createRoute(hwd.hotel.id))
                        }
                    )
                }
            }
        }
    }
}

// ── Composables d'état ────────────────────────────────────────

@Composable
private fun EmptyPrompt() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Secondary.copy(0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Outlined.Place,
                contentDescription = null,
                tint     = Secondary,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text       = "Choisissez un lieu ci-dessus",
            fontSize   = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color      = LocalAppColors.current.textPrimary
        )
        Text(
            text     = "Les hôtels proches s'afficheront ici",
            fontSize = 13.sp,
            color    = OnSurfaceSecondary,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun NearbyLoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = Secondary)
            Spacer(modifier = Modifier.height(12.dp))
            Text("Recherche des hôtels…", fontSize = 13.sp, color = OnSurfaceSecondary)
        }
    }
}

@Composable
private fun NoNearbyResults() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text     = "Aucun hôtel trouvé près de ce lieu",
            fontSize = 14.sp,
            color    = OnSurfaceSecondary
        )
    }
}

// ── Ligne landmark ────────────────────────────────────────────

@Composable
private fun LandmarkRow(
    landmark: Landmark,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor    = if (isSelected) Secondary.copy(0.1f)  else Color.Transparent
    val borderColor = if (isSelected) Secondary.copy(0.4f) else Color.Transparent
    val textColor  = if (isSelected) Secondary else LocalAppColors.current.textPrimary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 3.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(
                    if (isSelected) Secondary.copy(0.15f)
                    else LocalAppColors.current.surface
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = landmarkIcon(landmark.type),
                contentDescription = null,
                tint     = if (isSelected) Secondary else OnSurfaceSecondary,
                modifier = Modifier.size(18.dp)
            )
        }
        Text(
            text       = landmark.name,
            fontSize   = 14.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color      = textColor,
            modifier   = Modifier.weight(1f),
            maxLines   = 1,
            overflow   = TextOverflow.Ellipsis
        )
        if (isSelected) {
            Icon(
                Icons.Outlined.CheckCircle,
                contentDescription = null,
                tint     = Secondary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

// ── Carte hôtel avec distance ─────────────────────────────────

@Composable
private fun HotelWithDistanceCard(
    hwd: HotelWithDistance,
    onClick: () -> Unit
) {
    val hotel = hwd.hotel

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(LocalAppColors.current.surface)
            .clickable { onClick() }
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment     = Alignment.Top
    ) {
        // Image
        AsyncImage(
            model              = hotel.imageUrls.firstOrNull() ?: "",
            contentDescription = hotel.name,
            contentScale       = ContentScale.Crop,
            modifier           = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(LocalAppColors.current.background)
        )

        Column(modifier = Modifier.weight(1f)) {
            // Nom
            Text(
                text       = hotel.name,
                fontSize   = 15.sp,
                fontWeight = FontWeight.Bold,
                color      = LocalAppColors.current.textPrimary,
                maxLines   = 1,
                overflow   = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Ville + rating
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    Icons.Outlined.LocationOn,
                    contentDescription = null,
                    tint     = OnSurfaceSecondary,
                    modifier = Modifier.size(13.dp)
                )
                Text(hotel.city, fontSize = 12.sp, color = OnSurfaceSecondary)
                if (hotel.rating > 0) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        Icons.Outlined.Star,
                        contentDescription = null,
                        tint     = Color(0xFFFFC107),
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text     = formatRating(hotel.rating),
                        fontSize = 12.sp,
                        color    = OnSurfaceSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Prix
            Text(
                text       = "${formatFcfa(hotel.pricePerNight)} FCFA/nuit",
                fontSize   = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color      = LocalAppColors.current.textPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Badge distance — bien visible
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF006994), Color(0xFF00BCD4))
                        )
                    )
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Icon(
                    Icons.Outlined.NearMe,
                    contentDescription = null,
                    tint     = Color.White,
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    text       = formatDistance(hwd.distanceKm),
                    fontSize   = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color      = Color.White
                )
                Text("·", fontSize = 12.sp, color = Color.White.copy(0.7f))
                Text(
                    text     = "${hwd.estimatedMinutes} min de trajet",
                    fontSize = 12.sp,
                    color    = Color.White
                )
            }
        }
    }
}

// ── Utilitaires ───────────────────────────────────────────────

private fun landmarkIcon(type: String): ImageVector = when (type.uppercase()) {
    "AIRPORT"         -> Icons.Outlined.Flight
    "BUS_STATION"     -> Icons.Outlined.DirectionsBus
    "CITY_HALL"       -> Icons.Outlined.AccountBalance
    "GOVERNMENT"      -> Icons.Outlined.AccountBalance
    "HOSPITAL"        -> Icons.Outlined.LocalHospital
    "SHOPPING_CENTER" -> Icons.Outlined.ShoppingCart
    "STADIUM"         -> Icons.Outlined.SportsSoccer
    "UNIVERSITY"      -> Icons.Outlined.School
    "MARKET"          -> Icons.Outlined.Storefront
    "TOURIST"         -> Icons.Outlined.Place
    else              -> Icons.Outlined.Place
}

// Formatage sans String.format (non disponible en commonMain)
private fun formatDistance(km: Double): String = when {
    km < 1.0 -> "${(km * 1000).toInt()} m"
    else -> {
        val tenths = (km * 10).toInt()
        "${tenths / 10}.${tenths % 10} km"
    }
}

private fun formatRating(rating: Double): String {
    val tenths = (rating * 10).toInt()
    return "${tenths / 10}.${tenths % 10}"
}

private fun formatFcfa(amount: Double): String =
    amount.toInt().toString().reversed().chunked(3).joinToString(" ").reversed()