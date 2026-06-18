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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.mock.NoResultMockData
import com.kamerstay.app.viewmodel.TravelerViewModel
import org.koin.compose.viewmodel.koinViewModel
import com.kamerstay.app.core.components.TravelerBottomNavBar

@Composable
fun NoResultScreen(navController: NavController) {

    val viewModel = koinViewModel<TravelerViewModel>()
    val state = viewModel.noResultState

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
            contentPadding = PaddingValues(bottom = 24.dp)
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
                            text = "Explore Cameroon",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Secondary
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .border(2.dp, Primary, CircleShape)
                            .background(Primary.copy(0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Outlined.Person,
                            contentDescription = null,
                            tint = Secondary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            // ── Search Bar ────────────────────────────
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(28.dp))
                            .background(LocalAppColors.current.surface)
                            .border(1.dp, Divider, RoundedCornerShape(28.dp))
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.Search,
                            contentDescription = null,
                            tint = OnSurfaceSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        BasicTextField(
                            value = state.searchQuery.ifEmpty {
                                "Luxury Penthouse, Do..."
                            },
                            onValueChange = { state.searchQuery = it },
                            modifier = Modifier.weight(1f),
                            textStyle = TextStyle(
                                fontSize = 14.sp,
                                color = LocalAppColors.current.textPrimary
                            ),
                            decorationBox = { inner -> inner() }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { navController.navigate(Routes.HotelSearch.route) },
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Secondary),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "Search",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }

                    // Active Filters badge
                    if (state.hasActiveFilters) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Primary.copy(0.15f))
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        Icons.Outlined.FilterList,
                                        contentDescription = null,
                                        tint = Primary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "Active Filters",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Primary
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // ── Empty State Illustration ──────────────
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Illustration circle
                    Box(
                        modifier = Modifier
                            .size(180.dp)
                            .clip(CircleShape)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        Primary.copy(0.15f),
                                        BackgroundLight
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0xFF1A2A3A),
                                            Color(0xFF0D1A28)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Outlined.SearchOff,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(56.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "No properties found",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = LocalAppColors.current.textPrimary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "We couldn't find any stays matching your current filters in ${state.searchLocation}. Try adjusting your preferences or exploring nearby areas for more options.",
                        fontSize = 15.sp,
                        color = OnSurfaceSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Clear All Filters
                    Button(
                        onClick = {
                            state.clearFilters()
                            navController.navigate(Routes.HotelSearch.route)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Secondary)
                    ) {
                        Icon(
                            Icons.Outlined.FilterListOff,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Clear All Filters",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Explore Nearby
                    OutlinedButton(
                        onClick = { navController.navigate(Routes.HotelSearch.route) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(28.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Divider),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Secondary)
                    ) {
                        Text(
                            text = "Explore Nearby",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Secondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))
            }

            // ── Suggestions ───────────────────────────
            items(NoResultMockData.suggestions) { suggestion ->
                val icon = when (suggestion.icon) {
                    "calendar" -> Icons.Outlined.CalendarMonth
                    "payments" -> Icons.Outlined.Payments
                    "map" -> Icons.Outlined.Map
                    else -> Icons.Outlined.Info
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(0.dp))
                        .background(LocalAppColors.current.background)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (suggestion.id == "3") BackgroundLight
                                else BackgroundLight
                            )
                            .padding(vertical = 16.dp, horizontal = 4.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Primary.copy(0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                icon,
                                contentDescription = null,
                                tint = Secondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = suggestion.title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = LocalAppColors.current.textPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = suggestion.description,
                                fontSize = 13.sp,
                                color = OnSurfaceSecondary,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = Divider
                )
            }
        }
    }
}

