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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.core.utils.APP_NAME
import com.kamerstay.app.data.mock.GuideMockData
import com.kamerstay.app.data.model.*
import com.kamerstay.app.viewmodel.TravelerViewModel
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.kamerstay.app.core.components.TravelerBottomNavBar

@Composable
fun LocalTravelGuideScreen(navController: NavController) {

    val viewModel = koinViewModel<TravelerViewModel>()
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All Guide") }

    // Filtrage par catégorie et recherche
    val q = searchQuery.trim().lowercase()
    val showAll = selectedCategory == "All Guide"

    val filteredLandmarks = if (showAll || selectedCategory == "Landmarks")
        GuideMockData.landmarks.filter { q.isEmpty() || it.name.lowercase().contains(q) || it.description.lowercase().contains(q) }
    else emptyList()

    val filteredFood = if (showAll || selectedCategory == "Food & Dining")
        GuideMockData.foodPlaces.filter { q.isEmpty() || it.name.lowercase().contains(q) || it.location.lowercase().contains(q) || it.tag.lowercase().contains(q) }
    else emptyList()

    val filteredShopping = if (showAll || selectedCategory == "Shopping")
        GuideMockData.shoppingPlaces.filter { q.isEmpty() || it.name.lowercase().contains(q) || it.description.lowercase().contains(q) }
    else emptyList()

    val filteredNightlife = if (showAll || selectedCategory == "Nightlife")
        GuideMockData.nightlifePlaces.filter { q.isEmpty() || it.name.lowercase().contains(q) || it.subtitle.lowercase().contains(q) }
    else emptyList()

    val hasResults = filteredLandmarks.isNotEmpty() || filteredFood.isNotEmpty() || filteredShopping.isNotEmpty() || filteredNightlife.isNotEmpty()

    val categories = listOf(
        Icons.Outlined.AutoAwesome to "All Guide",
        Icons.Outlined.Restaurant to "Food & Dining",
        Icons.Outlined.Place to "Landmarks",
        Icons.Outlined.ShoppingBag to "Shopping",
        Icons.Outlined.MusicNote to "Nightlife"
    )

    Scaffold(
        containerColor = LocalAppColors.current.background,
        bottomBar = {
            TravelerBottomNavBar(navController = navController, selectedTab = 1)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Routes.MapLocation.route) },
                containerColor = Secondary,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Outlined.Map, contentDescription = "Map")
            }
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
                        Column {
                            Text(
                                text = APP_NAME,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Secondary
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .border(2.dp, Primary, CircleShape)
                            .background(Primary.copy(0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Outlined.Person,
                            contentDescription = null,
                            tint = Secondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // ── Header ────────────────────────────────
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = "Local Travel Guide",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = LocalAppColors.current.textPrimary
                    )
                    Text(
                        text = "Curated local experiences and hidden gems around your stay, handpicked by our expert concierges.",
                        fontSize = 13.sp,
                        color = OnSurfaceSecondary,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Search + Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(24.dp))
                                .background(LocalAppColors.current.surface)
                                .border(1.dp, Divider, RoundedCornerShape(24.dp))
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Outlined.Search,
                                contentDescription = null,
                                tint = OnSurfaceSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            BasicTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                modifier = Modifier.weight(1f),
                                textStyle = TextStyle(fontSize = 13.sp, color = LocalAppColors.current.textPrimary),
                                decorationBox = { inner ->
                                    if (searchQuery.isEmpty()) {
                                        Text(
                                            "Repères, restaurants, shopping...",
                                            fontSize = 13.sp,
                                            color = OnSurfaceSecondary.copy(0.5f)
                                        )
                                    }
                                    inner()
                                }
                            )
                            if (searchQuery.isNotEmpty()) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .background(OnSurfaceSecondary.copy(0.2f))
                                        .clickable { searchQuery = "" },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Outlined.Close, null, tint = OnSurfaceSecondary, modifier = Modifier.size(10.dp))
                                }
                            }
                        }

                        Button(
                            onClick = { navController.navigate(Routes.MapLocation.route) },
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Primary),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = "Search",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = OnPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Category chips
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(categories) { (icon, label) ->
                            val isSelected = selectedCategory == label
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(
                                        if (isSelected) Primary else Color.White
                                    )
                                    .border(
                                        1.dp,
                                        if (isSelected) Color.Transparent else Divider,
                                        RoundedCornerShape(20.dp)
                                    )
                                    .clickable { selectedCategory = label }
                                    .padding(horizontal = 14.dp, vertical = 9.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        icon,
                                        contentDescription = null,
                                        tint = if (isSelected) OnPrimary else OnSurfaceSecondary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = label,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isSelected) OnPrimary else LocalAppColors.current.textPrimary
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            // ── État vide si aucun résultat ───────────
            if (!hasResults) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Outlined.SearchOff,
                                contentDescription = null,
                                tint = OnSurfaceSecondary,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Aucun résultat pour « $searchQuery »",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = LocalAppColors.current.textPrimary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Essayez un autre mot-clé ou changez de catégorie.",
                                fontSize = 13.sp,
                                color = OnSurfaceSecondary
                            )
                        }
                    }
                }
            }

            // ── Landmarks ─────────────────────────────
            if (filteredLandmarks.isNotEmpty()) {
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
                                text = "Landmarks",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = LocalAppColors.current.textPrimary
                            )
                            Text(
                                text = "Sites emblématiques et panoramas",
                                fontSize = 12.sp,
                                color = OnSurfaceSecondary
                            )
                        }
                        Text(
                            text = "Voir sur la carte →",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Primary,
                            modifier = Modifier.clickable { navController.navigate(Routes.MapLocation.route) }
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
                items(filteredLandmarks) { landmark ->
                    LandmarkCard(
                        landmark = landmark,
                        onGetDirections = { navController.navigate(Routes.MapLocation.route) },
                        onClick = { navController.navigate(Routes.MapLocation.route) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // ── Food & Dining ─────────────────────────
            if (filteredFood.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Text(
                            text = "Food & Dining",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = LocalAppColors.current.textPrimary
                        )
                        Text(
                            text = "Saveurs locales et gastronomie",
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredFood) { place ->
                            FoodCard(
                                place = place,
                                onBookTable = { navController.navigate(Routes.MapLocation.route) },
                                onClick = { navController.navigate(Routes.MapLocation.route) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            // ── Premium Shopping ──────────────────────
            if (filteredShopping.isNotEmpty()) {
                item {
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Text(
                            text = "Premium Shopping",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = LocalAppColors.current.textPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
                items(filteredShopping) { place ->
                    ShoppingCard(place = place, onClick = { navController.navigate(Routes.MapLocation.route) })
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // ── Nightlife ─────────────────────────────
            if (filteredNightlife.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Text(
                            text = "Nightlife",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = LocalAppColors.current.textPrimary
                        )
                        Text(
                            text = "La ville s'anime après la tombée de la nuit.",
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
                items(filteredNightlife) { place ->
                    NightlifeCard(place = place, onClick = { navController.navigate(Routes.MapLocation.route) })
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

// ── Landmark Card ─────────────────────────────────────────
@Composable
fun LandmarkCard(landmark: Landmark, onGetDirections: () -> Unit = {}, onClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(LocalAppColors.current.surface)
            .clickable { onClick() }
    ) {
        Column {
            // Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                    .background(brush = Brush.verticalGradient(colors = landmark.gradientColors))
            ) {
                if (landmark.imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = landmark.imageUrl,
                        contentDescription = landmark.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                // Rating badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(LocalAppColors.current.surface)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            Icons.Outlined.Star,
                            contentDescription = null,
                            tint = StarRating,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "${landmark.rating} (${landmark.reviewCount})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = LocalAppColors.current.textPrimary
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = landmark.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = LocalAppColors.current.textPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = landmark.price,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Secondary
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = landmark.description,
                    fontSize = 13.sp,
                    color = OnSurfaceSecondary,
                    lineHeight = 18.sp,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { onGetDirections() },
                        modifier = Modifier.weight(1f).height(42.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Secondary)
                    ) {
                        Icon(
                            Icons.Outlined.Navigation,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Get Directions",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .border(1.dp, Divider, RoundedCornerShape(10.dp))
                            .background(LocalAppColors.current.surface)
                            .clickable { onGetDirections() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Outlined.BookmarkBorder,
                            contentDescription = null,
                            tint = OnSurfaceSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

// ── Food Card ─────────────────────────────────────────────
@Composable
fun FoodCard(place: FoodPlace, onBookTable: () -> Unit = {}, onClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .width(180.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(LocalAppColors.current.surface)
            .clickable { onClick() }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                    .background(brush = Brush.verticalGradient(colors = place.gradientColors))
            ) {
                if (place.imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = place.imageUrl,
                        contentDescription = place.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(place.tagColor)
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = place.tag,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = place.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalAppColors.current.textPrimary
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Outlined.Place,
                        contentDescription = null,
                        tint = OnSurfaceSecondary,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = place.location,
                        fontSize = 12.sp,
                        color = OnSurfaceSecondary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { onBookTable() },
                    modifier = Modifier.fillMaxWidth().height(36.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Secondary),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Book Table",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

// ── Shopping Card ─────────────────────────────────────────
@Composable
fun ShoppingCard(place: ShoppingPlace, onClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(180.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(brush = Brush.verticalGradient(colors = place.gradientColors))
            .clickable { onClick() }
    ) {
        if (place.imageUrl.isNotEmpty()) {
            AsyncImage(
                model = place.imageUrl,
                contentDescription = place.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(
                listOf(Color.Transparent, Color.Black.copy(0.7f))
            )))
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(
                text = place.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = place.description,
                fontSize = 12.sp,
                color = Color.White.copy(0.7f),
                lineHeight = 17.sp,
                maxLines = 3
            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(LocalAppColors.current.surface)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Start Shopping",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LocalAppColors.current.textPrimary
                )
            }
        }
    }
}

// ── Nightlife Card ────────────────────────────────────────
@Composable
fun NightlifeCard(place: NightlifePlace, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(LocalAppColors.current.surface)
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(brush = Brush.verticalGradient(colors = place.gradientColors))
        ) {
            if (place.imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = place.imageUrl,
                    contentDescription = place.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = place.name,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = LocalAppColors.current.textPrimary
            )
            Text(
                text = place.subtitle,
                fontSize = 12.sp,
                color = OnSurfaceSecondary
            )
        }

        Icon(
            Icons.Outlined.ChevronRight,
            contentDescription = null,
            tint = OnSurfaceSecondary,
            modifier = Modifier.size(20.dp)
        )
    }
}