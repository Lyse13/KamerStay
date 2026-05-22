package com.kamerstay.app.features.traveler

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.mock.LocalExperience
import com.kamerstay.app.data.mock.mockExperiences
import com.kamerstay.app.data.mock.mockExperts

@Composable
fun LocalTravelGuideScreen(navController: NavController) {

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("ALL GEMS") }

    val categories = listOf("ALL GEMS", "FOOD", "TRANSPORT", "CULTURE", "NATURE")

    Scaffold(
        containerColor = WarmIvory,
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 0.dp
            ) {
                listOf(
                    Icons.Outlined.Explore to "Explore",
                    Icons.Outlined.BookOnline to "Bookings",
                    Icons.Filled.People to "Staff",
                    Icons.Outlined.Person to "Profile"
                ).forEachIndexed { index, (icon, label) ->
                    NavigationBarItem(
                        selected = index == 2,
                        onClick = {
                            when (index) {
                                0 -> navController.navigate(Routes.HotelSearch.route)
                                1 -> navController.navigate(Routes.BookingHistory.route)
                                3 -> navController.navigate(Routes.TravelerProfile.route)
                            }
                        },
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label, fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DeepEmerald,
                            selectedTextColor = DeepEmerald,
                            indicatorColor = PrimaryContainer,
                            unselectedIconColor = OnSurfaceVariant,
                            unselectedTextColor = OnSurfaceVariant
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
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { }) {
                            Icon(Icons.Filled.Menu, contentDescription = null, tint = OnSurface)
                        }
                        Text(
                            text = "KamerStay",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepEmerald
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(PrimaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "L",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepEmerald
                        )
                    }
                }
            }

            // ── Search Bar ────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .padding(horizontal = 14.dp, vertical = 13.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.Search,
                        contentDescription = null,
                        tint = OnSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(fontSize = 14.sp, color = OnSurface),
                        decorationBox = { inner ->
                            if (searchQuery.isEmpty()) {
                                Text(
                                    "Search for 'Local Gems'...",
                                    fontSize = 14.sp,
                                    color = OnSurfaceVariant.copy(0.5f)
                                )
                            }
                            inner()
                        }
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // ── Category Chips ────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { cat ->
                        val isSelected = selectedCategory == cat
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) DeepEmerald else Color.White
                                )
                                .border(
                                    if (!isSelected) 1.dp else 0.dp,
                                    OutlineVariant,
                                    CircleShape
                                )
                                .clickable { selectedCategory = cat }
                                .padding(horizontal = 18.dp, vertical = 9.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                if (cat == "FOOD") {
                                    Icon(
                                        Icons.Outlined.Restaurant,
                                        contentDescription = null,
                                        tint = if (isSelected) Color.White else OnSurface,
                                        modifier = Modifier.size(13.dp)
                                    )
                                }
                                if (cat == "TRANSPORT") {
                                    Icon(
                                        Icons.Outlined.DirectionsBus,
                                        contentDescription = null,
                                        tint = if (isSelected) Color.White else OnSurface,
                                        modifier = Modifier.size(13.dp)
                                    )
                                }
                                Text(
                                    text = cat,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isSelected) Color.White else OnSurface
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // ── Featured Local Pick ───────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Featured Local\nPick",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = OnSurface,
                        lineHeight = 26.sp
                    )
                    Text(
                        text = "SEE\nALL",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = WarmAmber,
                        modifier = Modifier.clickable { }
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Featured Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(220.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF2D4A1E),
                                    Color(0xFF1A3A10),
                                    Color(0xFF0D2208)
                                )
                            )
                        )
                ) {
                    // Verified badge
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(WarmAmber)
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Outlined.Verified,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "VERIFIED BY GUIDES",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    // Content at bottom
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "STREET FOOD MASTERY",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(0.7f),
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = "Best Soya in Yaoundé",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Filled.Star,
                                    contentDescription = null,
                                    tint = StarRating,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "4.9",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Outlined.Place,
                                    contentDescription = null,
                                    tint = Color.White.copy(0.7f),
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "Mvog-Ada Market",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(0.7f)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // ── Local Experiences ─────────────────────
            item {
                Text(
                    text = "Local Experiences",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = OnSurface,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            items(mockExperiences.size) { index ->
                val exp = mockExperiences[index]
                ExperienceCard(experience = exp)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // ── Meet Local Experts ────────────────────
            item {
                Text(
                    text = "Meet Local Experts",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = OnSurface,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    mockExperts.forEach { expert ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(70.dp)
                                    .then(
                                        if (expert.hasBorder) Modifier.border(
                                            2.5.dp, WarmAmber, CircleShape
                                        ) else Modifier
                                    )
                                    .padding(if (expert.hasBorder) 3.dp else 0.dp)
                                    .clip(CircleShape)
                                    .background(expert.avatarColor),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = expert.initials,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = expert.name,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = OnSurface
                            )
                            Text(
                                text = expert.city,
                                fontSize = 11.sp,
                                color = OnSurfaceVariant
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// ── Experience Card ───────────────────────────────────────
@Composable
fun ExperienceCard(experience: LocalExperience) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            // Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = experience.gradientColors
                        )
                    )
            ) {
                // Expert badge
                if (experience.isExpert) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(10.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(DeepEmerald.copy(alpha = 0.85f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Outlined.Shield,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(10.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "EXPERT",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                // Favorite button
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .then(
                            if (!experience.isExpert) Modifier else Modifier.padding(top = 36.dp)
                        )
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        tint = ErrorColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Info
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = experience.title,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface,
                        modifier = Modifier.weight(1f)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = null,
                            tint = StarRating,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = experience.rating.toString(),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OnSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(experience.tagColor.copy(alpha = 0.12f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = experience.tag,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = experience.tagColor
                        )
                    }
                    Text(
                        text = experience.distance,
                        fontSize = 12.sp,
                        color = OnSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (experience.actionLabel == "BOOK EXPERIENCE")
                            DeepEmerald else Color.Transparent
                    ),
                    border = if (experience.actionLabel != "BOOK EXPERIENCE")
                        androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant)
                    else null
                ) {
                    Text(
                        text = experience.actionLabel,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (experience.actionLabel == "BOOK EXPERIENCE")
                            Color.White else OnSurface,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}