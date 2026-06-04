package com.kamerstay.app.features.manager

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.model.AmenityCategory
import com.kamerstay.app.data.model.AmenityItem
import com.kamerstay.app.viewmodel.ManagerViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HotelAmenitiesScreen(navController: NavController) {

    val viewModel = koinViewModel<ManagerViewModel>()
    val state = viewModel.amenitiesState

    val essentialAmenities = state.amenities.filter {
        it.category == AmenityCategory.ESSENTIAL
    }
    val luxuryAmenities = state.amenities.filter {
        it.category == AmenityCategory.LUXURY_WELLNESS
    }

    Scaffold(
        containerColor = BackgroundLight,
        bottomBar = {
            NavigationBar(containerColor = Color.White, tonalElevation = 0.dp) {
                listOf(
                    Icons.Outlined.Search to "Explore",
                    Icons.Outlined.BookOnline to "Bookings",
                    Icons.Outlined.FavoriteBorder to "Saved",
                    Icons.Outlined.Person to "Profile"
                ).forEachIndexed { index, (icon, label) ->
                    NavigationBarItem(
                        selected = index == 3,
                        onClick = {
                            when (index) {
                                0 -> navController.navigate(Routes.HotelSearch.route)
                                1 -> navController.navigate(Routes.Reservations.route)
                                3 -> navController.navigate(Routes.ManagerProfile.route)
                            }
                        },
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label, fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Secondary,
                            selectedTextColor = Secondary,
                            indicatorColor = Primary.copy(0.15f),
                            unselectedIconColor = OnSurfaceSecondary,
                            unselectedTextColor = OnSurfaceSecondary
                        )
                    )
                }
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
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { }) {
                            Icon(
                                Icons.Outlined.Menu,
                                contentDescription = null,
                                tint = Secondary
                            )
                        }
                        Text(
                            text = "Orion Stay",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Secondary
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(modifier = Modifier.size(24.dp)) {
                            Icon(
                                Icons.Outlined.Notifications,
                                contentDescription = null,
                                tint = Secondary,
                                modifier = Modifier.size(22.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(ErrorColor)
                                    .align(Alignment.TopEnd)
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
            }

            // ── Breadcrumb + Header ───────────────────
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Dashboard",
                            fontSize = 13.sp,
                            color = OnSurfaceSecondary
                        )
                        Text("/", fontSize = 13.sp, color = OnSurfaceSecondary)
                        Text(
                            text = "Amenities",
                            fontSize = 13.sp,
                            color = OnSurfaceSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Facilities",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextDark
                    )
                    Text(
                        text = "Configure global facilities available at your property. These changes will reflect across all booking channels in real-time.",
                        fontSize = 13.sp,
                        color = OnSurfaceSecondary,
                        lineHeight = 18.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // ── Essential Services ────────────────────
            item {
                AmenitiesSection(
                    title = "Essential Services",
                    badge = "CORE",
                    badgeColor = OnSurfaceSecondary.copy(0.1f),
                    badgeTextColor = OnSurfaceSecondary,
                    amenities = essentialAmenities,
                    isVertical = true,
                    onToggle = { id -> state.toggleAmenity(id) }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            // ── Luxury & Wellness ─────────────────────
            item {
                AmenitiesSection(
                    title = "Luxury & Wellness",
                    badge = null,
                    badgeColor = Color.Transparent,
                    badgeTextColor = Color.Transparent,
                    amenities = luxuryAmenities,
                    isVertical = false,
                    onToggle = { id -> state.toggleAmenity(id) }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ── Amenities Section ─────────────────────────────────────
@Composable
fun AmenitiesSection(
    title: String,
    badge: String?,
    badgeColor: Color,
    badgeTextColor: Color,
    amenities: List<AmenityItem>,
    isVertical: Boolean,
    onToggle: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                if (badge != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(badgeColor)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = badge,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = badgeTextColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (isVertical) {
                // Vertical list style
                amenities.forEachIndexed { index, amenity ->
                    AmenityRow(amenity = amenity, onToggle = onToggle)
                    if (index < amenities.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 4.dp),
                            color = Divider
                        )
                    }
                }
            } else {
                // Card grid style
                amenities.forEach { amenity ->
                    AmenityCard(amenity = amenity, onToggle = onToggle)
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

// ── Amenity Row (Essential style) ─────────────────────────
@Composable
fun AmenityRow(amenity: AmenityItem, onToggle: (String) -> Unit) {
    val icon = amenityIcon(amenity.icon)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(BackgroundLight)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Primary.copy(0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = Secondary,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Column {
                    Text(
                        text = amenity.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextDark
                    )
                    Text(
                        text = amenity.description,
                        fontSize = 12.sp,
                        color = OnSurfaceSecondary,
                        lineHeight = 16.sp
                    )
                }
            }

            Switch(
                checked = amenity.isEnabled,
                onCheckedChange = { onToggle(amenity.id) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Secondary,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = OnSurfaceSecondary.copy(0.3f)
                )
            )
        }
    }
}

// ── Amenity Card (Luxury style) ───────────────────────────
@Composable
fun AmenityCard(amenity: AmenityItem, onToggle: (String) -> Unit) {
    val icon = amenityIcon(amenity.icon)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Divider, RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Primary.copy(0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = Secondary,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Switch(
                    checked = amenity.isEnabled,
                    onCheckedChange = { onToggle(amenity.id) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Secondary,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = OnSurfaceSecondary.copy(0.3f)
                    )
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = amenity.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Text(
                text = amenity.description,
                fontSize = 13.sp,
                color = OnSurfaceSecondary,
                lineHeight = 17.sp
            )
        }
    }
}

// ── Amenity Icon Helper ───────────────────────────────────
@Composable
fun amenityIcon(icon: String): ImageVector = when (icon) {
    "wifi" -> Icons.Outlined.Wifi
    "ac" -> Icons.Outlined.AcUnit
    "pool" -> Icons.Outlined.Pool
    "spa" -> Icons.Outlined.Spa
    "restaurant" -> Icons.Outlined.Restaurant
    "gym" -> Icons.Outlined.FitnessCenter
    "parking" -> Icons.Outlined.LocalParking
    else -> Icons.Outlined.CheckCircle
}