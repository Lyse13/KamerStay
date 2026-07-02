package com.kamerstay.app.features.traveler

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.kamerstay.app.data.state.FilterState
import com.kamerstay.app.viewmodel.TravelerViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FilterScreen(navController: NavController) {

    val viewModel = koinViewModel<TravelerViewModel>()
    val state = viewModel.filterState

    val starRatings = listOf(1, 2, 3, 4, 5)

    val amenities = listOf(
        Icons.Outlined.Wifi to "Wi-Fi",
        Icons.Outlined.Pool to "Pool",
        Icons.Outlined.FitnessCenter to "Gym",
        Icons.Outlined.Spa to "Spa"
    )

    Scaffold(
        containerColor = LocalAppColors.current.background,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LocalAppColors.current.background)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Button(
                    onClick = {
                        navController.navigate(Routes.HotelSearch.route) {
                            popUpTo(Routes.HotelSearch.route) { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Text(
                        text = "Apply Filters",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnPrimary
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
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Secondary
                    )
                }
                Text(
                    text = "Filters",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Secondary
                )
                Text(
                    text = "Reset",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (state.hasActiveFilters) Primary else OnSurfaceSecondary,
                    modifier = Modifier.clickable { state.clearAll() }
                )
            }

            // ── Price Range ───────────────────────────
            FilterSection(title = "Price Range (XAF)") {

                // Min / Max boxes
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .border(1.dp, Divider, RoundedCornerShape(10.dp))
                            .background(LocalAppColors.current.surface)
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Minimum",
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary
                        )
                        Text(
                            text = formatFilterPrice(state.minPrice),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = LocalAppColors.current.textPrimary
                        )
                    }

                    Box(
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        HorizontalDivider(
                            modifier = Modifier.width(20.dp),
                            color = OnSurfaceSecondary
                        )
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .border(1.dp, Divider, RoundedCornerShape(10.dp))
                            .background(LocalAppColors.current.surface)
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Maximum",
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary
                        )
                        Text(
                            text = formatFilterPrice(state.maxPrice),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = LocalAppColors.current.textPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Range slider
                RangeSlider(
                    value = state.minPrice..state.maxPrice,
                    onValueChange = { range ->
                        state.minPrice = range.start
                        state.maxPrice = range.endInclusive
                    },
                    valueRange = 0f..500000f,
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = Secondary,
                        activeTrackColor = Primary,
                        inactiveTrackColor = Divider
                    )
                )
            }

            FilterDivider()

            // ── Verified Hotels ───────────────────────
            FilterSection(title = "") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (state.isVerifiedOnly) Primary.copy(0.1f)
                                    else LocalAppColors.current.background
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Outlined.Verified,
                                contentDescription = null,
                                tint = if (state.isVerifiedOnly) Primary else OnSurfaceSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Verified Hotels Only",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = LocalAppColors.current.textPrimary
                            )
                            Text(
                                text = "Show only KamerStay-certified hotels",
                                fontSize = 12.sp,
                                color = OnSurfaceSecondary
                            )
                        }
                    }
                    Switch(
                        checked = state.isVerifiedOnly,
                        onCheckedChange = { state.isVerifiedOnly = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = OnPrimary,
                            checkedTrackColor = Primary,
                            uncheckedThumbColor = OnSurfaceSecondary,
                            uncheckedTrackColor = Divider
                        )
                    )
                }
            }

            FilterDivider()

            // ── Star Rating ───────────────────────────
            FilterSection(title = "Star Rating") {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    starRatings.forEach { star ->
                        val isSelected = star in state.selectedStars
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) Primary else LocalAppColors.current.surface)
                                .border(
                                    1.dp,
                                    if (isSelected) Color.Transparent else Divider,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable {
                                    state.selectedStars = if (star in state.selectedStars)
                                        setOf()
                                    else setOf(star)
                                }
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    Icons.Outlined.StarOutline,
                                    contentDescription = null,
                                    tint = if (isSelected) OnPrimary else OnSurfaceSecondary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = star.toString(),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isSelected) OnPrimary else LocalAppColors.current.textPrimary
                                )
                            }
                        }
                    }
                }
            }

            FilterDivider()

            // ── Popular Amenities ─────────────────────
            FilterSection(title = "Popular Amenities") {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    amenities.forEach { (icon, label) ->
                        val isSelected = label in state.selectedAmenities
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    if (isSelected) Primary.copy(0.1f) else LocalAppColors.current.surface
                                )
                                .border(
                                    1.dp,
                                    if (isSelected) Primary else Divider,
                                    RoundedCornerShape(20.dp)
                                )
                                .clickable {
                                    state.selectedAmenities = if (label in state.selectedAmenities)
                                        state.selectedAmenities - label
                                    else state.selectedAmenities + label
                                }
                                .padding(horizontal = 14.dp, vertical = 9.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    icon,
                                    contentDescription = null,
                                    tint = if (isSelected) Primary else OnSurfaceSecondary,
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = label,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold
                                    else FontWeight.Normal,
                                    color = if (isSelected) Secondary else LocalAppColors.current.textPrimary
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ── Filter Section ────────────────────────────────────────
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
                color = LocalAppColors.current.textPrimary
            )
            Spacer(modifier = Modifier.height(14.dp))
        }
        content()
    }
}

// ── Filter Divider ────────────────────────────────────────
@Composable
fun FilterDivider() {
    HorizontalDivider(
        color = Divider,
        modifier = Modifier.padding(horizontal = 20.dp)
    )
}

private fun formatFilterPrice(value: Float): String {
    val s = value.toInt().toString()
    return s.reversed().chunked(3).joinToString(",").reversed()
}