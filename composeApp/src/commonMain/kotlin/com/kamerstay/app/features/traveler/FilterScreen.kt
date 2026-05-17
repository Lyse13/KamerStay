package com.kamerstay.app.features.traveler

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.core.utils.Constants
import com.kamerstay.app.model.enums.LandmarkType
import com.kamerstay.app.model.enums.RoomType

@Composable
fun FilterScreen(navController: NavController) {

    // ── États des filtres ──────────────────────────
    var selectedCities by remember { mutableStateOf(setOf("Douala")) }
    var minPrice by remember { mutableStateOf(10000f) }
    var maxPrice by remember { mutableStateOf(250000f) }
    var priceSlider by remember { mutableStateOf(0.3f) }
    var selectedLandmarks by remember { mutableStateOf(setOf(LandmarkType.UNIVERSITY)) }
    var selectedAmenities by remember { mutableStateOf(setOf("WiFi", "Climatisation")) }
    var selectedRoomType by remember { mutableStateOf(RoomType.DOUBLE) }

    val cities = listOf("Douala", "Yaoundé", "Kribi", "Limbé")
    val landmarks = listOf(
        LandmarkType.HOSPITAL to "Hospital",
        LandmarkType.UNIVERSITY to "University",
        LandmarkType.STADIUM to "Stadium",
        LandmarkType.MARKET to "Market",
    )
    val amenities = listOf(
        Icons.Outlined.Wifi to "High-speed WiFi",
        Icons.Outlined.AcUnit to "Air Conditioning",
        Icons.Outlined.Pool to "Swimming Pool",
        Icons.Outlined.LocalParking to "Free Parking",
    )
    val roomTypes = listOf(
        Icons.Outlined.Person to "Single" to RoomType.SINGLE,
        Icons.Outlined.People to "Double" to RoomType.DOUBLE,
        Icons.Outlined.KingBed to "Suite" to RoomType.SUITE,
    )

    Scaffold(
        containerColor = WarmIvory,
        bottomBar = {
            // ── Bottom Button ──────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(WarmIvory)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DeepEmerald
                    )
                ) {
                    Text(
                        text = "Show 148 results  →",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // ── Top Bar ───────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "Close",
                        tint = OnSurface
                    )
                }
                Text(
                    text = "Filters",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepEmerald
                )
                Text(
                    text = "Reset",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = OnSurfaceVariant,
                    modifier = Modifier.clickable {
                        selectedCities = setOf()
                        selectedLandmarks = setOf()
                        selectedAmenities = setOf()
                        priceSlider = 0.3f
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ── Destination ───────────────────────────
            FilterSection(title = "Destination") {
                // Grid 2 colonnes
                for (i in cities.indices step 2) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        CityChip(
                            city = cities[i],
                            isSelected = cities[i] in selectedCities,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                selectedCities = if (cities[i] in selectedCities)
                                    selectedCities - cities[i]
                                else selectedCities + cities[i]
                            }
                        )
                        if (i + 1 < cities.size) {
                            CityChip(
                                city = cities[i + 1],
                                isSelected = cities[i + 1] in selectedCities,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    selectedCities = if (cities[i + 1] in selectedCities)
                                        selectedCities - cities[i + 1]
                                    else selectedCities + cities[i + 1]
                                }
                            )
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    if (i + 2 < cities.size) Spacer(modifier = Modifier.height(10.dp))
                }
            }

            FilterDivider()

            // ── Price Range ───────────────────────────
            FilterSection(title = "Price range") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "Per night",
                        fontSize = 12.sp,
                        color = OnSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Slider(
                    value = priceSlider,
                    onValueChange = { priceSlider = it },
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = DeepEmerald,
                        activeTrackColor = DeepEmerald,
                        inactiveTrackColor = OutlineVariant
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Min
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceVariant)
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "MINIMUM",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OnSurfaceVariant,
                            letterSpacing = 0.8.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "10,000 XAF",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )
                    }
                    // Max
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceVariant)
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "MAXIMUM",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OnSurfaceVariant,
                            letterSpacing = 0.8.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "250,000+\nXAF",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            FilterDivider()

            // ── Near Landmarks ────────────────────────
            FilterSection(title = "Near Landmarks") {
                androidx.compose.foundation.layout.FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    landmarks.forEach { (type, label) ->
                        val isSelected = type in selectedLandmarks
                        val icon = when (type) {
                            LandmarkType.HOSPITAL -> Icons.Outlined.LocalHospital
                            LandmarkType.UNIVERSITY -> Icons.Outlined.School
                            LandmarkType.STADIUM -> Icons.Outlined.Stadium
                            LandmarkType.MARKET -> Icons.Outlined.Store
                            else -> Icons.Outlined.Place
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.White)
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) DeepEmerald else OutlineVariant,
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .clickable {
                                    selectedLandmarks = if (type in selectedLandmarks)
                                        selectedLandmarks - type
                                    else selectedLandmarks + type
                                }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    icon,
                                    contentDescription = null,
                                    tint = if (isSelected) DeepEmerald else OnSurfaceVariant,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = label,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold
                                    else FontWeight.Normal,
                                    color = if (isSelected) DeepEmerald else OnSurface
                                )
                            }
                        }
                    }
                }
            }

            FilterDivider()

            // ── Amenities ─────────────────────────────
            FilterSection(title = "Amenities") {
                amenities.forEach { (icon, label) ->
                    val isChecked = label in selectedAmenities
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                selectedAmenities = if (label in selectedAmenities)
                                    selectedAmenities - label
                                else selectedAmenities + label
                            }
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                icon,
                                contentDescription = null,
                                tint = OnSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = label,
                                fontSize = 15.sp,
                                color = OnSurface
                            )
                        }
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = {
                                selectedAmenities = if (label in selectedAmenities)
                                    selectedAmenities - label
                                else selectedAmenities + label
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = DeepEmerald,
                                uncheckedColor = OutlineVariant,
                                checkmarkColor = Color.White
                            )
                        )
                    }
                    if (label != amenities.last().second) {
                        HorizontalDivider(color = Divider)
                    }
                }
            }

            FilterDivider()

            // ── Room Type ─────────────────────────────
            FilterSection(title = "Room Type") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    roomTypes.forEach { (iconLabel, type) ->
                        val (icon, label) = iconLabel
                        val isSelected = selectedRoomType == type
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isSelected) DeepEmerald.copy(alpha = 0.1f)
                                    else SurfaceVariant
                                )
                                .border(
                                    width = if (isSelected) 1.5.dp else 0.dp,
                                    color = if (isSelected) DeepEmerald else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { selectedRoomType = type }
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    icon,
                                    contentDescription = null,
                                    tint = if (isSelected) DeepEmerald else OnSurfaceVariant,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = label,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold
                                    else FontWeight.Normal,
                                    color = if (isSelected) DeepEmerald else OnSurface
                                )
                            }
                        }
                    }
                }
            }

            FilterDivider()

            // ── Map View ──────────────────────────────
            FilterSection(title = "") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceVariant)
                        .clickable { },
                    contentAlignment = Alignment.BottomStart
                ) {
                    // Map placeholder
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Outlined.Place,
                                contentDescription = null,
                                tint = OnSurfaceVariant.copy(0.3f),
                                modifier = Modifier.size(40.dp)
                            )
                            Icon(
                                Icons.Outlined.Place,
                                contentDescription = null,
                                tint = OnSurfaceVariant.copy(0.2f),
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                    // Show Map label
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.White)
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.Map,
                            contentDescription = null,
                            tint = OnSurface,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Show Map View",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = OnSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ── Composants réutilisables ──────────────────────────────

@Composable
fun FilterSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        if (title.isNotEmpty()) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        content()
    }
}

@Composable
fun FilterDivider() {
    HorizontalDivider(
        color = Divider,
        modifier = Modifier.padding(horizontal = 20.dp)
    )
}

@Composable
fun CityChip(
    city: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) DeepEmerald else OutlineVariant,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Outlined.Place,
                contentDescription = null,
                tint = if (isSelected) DeepEmerald else OnSurfaceVariant,
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = city,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isSelected) DeepEmerald else OnSurface
            )
        }
    }
}