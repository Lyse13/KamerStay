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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.core.utils.APP_NAME
import com.kamerstay.app.data.mock.ReviewsMockData
import com.kamerstay.app.data.model.CategoryRating
import com.kamerstay.app.data.model.GuestReview
import com.kamerstay.app.viewmodel.TravelerViewModel
import org.koin.compose.viewmodel.koinViewModel
import com.kamerstay.app.core.components.TravelerBottomNavBar

@Composable
fun HotelReviewsScreen(navController: NavController) {

    val viewModel   = koinViewModel<TravelerViewModel>()
    val state       = viewModel.reviewState
    val hotel       = viewModel.selectedHotel
    val reviews     = viewModel.hotelReviewsList
    val isLoading   = viewModel.isLoadingReviews

    LaunchedEffect(hotel?.id) {
        hotel?.id?.let { viewModel.loadReviewsForHotel(it) }
    }

    val filters = listOf("Tous", "Avec photos", "Familles", "Business", "Couples")

    val categoryIconMap = mapOf(
        "Cleanliness" to Icons.Outlined.CleaningServices,
        "Free Wi-Fi"  to Icons.Outlined.Wifi,
        "Location"    to Icons.Outlined.Place,
        "Service"     to Icons.Outlined.SupportAgent
    )

    val avgRating   = if (reviews.isNotEmpty()) reviews.map { it.rating }.average() else hotel?.rating ?: 4.8
    val reviewCount = if (reviews.isNotEmpty()) reviews.size else hotel?.reviewCount ?: 0

    val filteredReviews = when (state.selectedFilter) {
        "Avec photos" -> reviews.filter { it.hasImages }
        else          -> reviews
    }

    val displayReviews = filteredReviews.ifEmpty {
        if (!isLoading) ReviewsMockData.reviews else emptyList()
    }

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
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Secondary
                            )
                        }
                        Text(
                            text = APP_NAME,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Secondary
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

            // ── Rating Summary ────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(LocalAppColors.current.surface)
                        .padding(20.dp)
                ) {
                    Column {
                        Text(
                            text = "Guest Reviews",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Secondary
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "%.1f".format(avgRating),
                                fontSize = 42.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = LocalAppColors.current.textPrimary
                            )
                            Text(
                                text = "/5",
                                fontSize = 18.sp,
                                color = OnSurfaceSecondary,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            repeat(4) {
                                Icon(
                                    Icons.Outlined.StarOutline,
                                    contentDescription = null,
                                    tint = Primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Icon(
                                Icons.Outlined.StarHalf,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Basé sur $reviewCount avis vérifiés",
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { navController.navigate(Routes.WriteReview.route) },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Secondary)
                        ) {
                            Icon(
                                Icons.Outlined.Edit,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Write a Review",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // ── Category Ratings ──────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(LocalAppColors.current.surface)
                ) {
                    Column {
                        ReviewsMockData.categoryRatings.forEachIndexed { index, category ->
                            val icon = categoryIconMap[category.label]
                                ?: Icons.Outlined.Star

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            icon,
                                            contentDescription = null,
                                            tint = OnSurfaceSecondary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = category.label,
                                            fontSize = 14.sp,
                                            color = LocalAppColors.current.textPrimary
                                        )
                                    }
                                    Text(
                                        text = category.score.toString(),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = LocalAppColors.current.textPrimary
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                // Progress bar
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(4.dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(LocalAppColors.current.background)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(category.score.toFloat() / 5f)
                                            .fillMaxHeight()
                                            .clip(RoundedCornerShape(2.dp))
                                            .background(Primary)
                                    )
                                }
                            }

                            if (index < ReviewsMockData.categoryRatings.lastIndex) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    color = Divider
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // ── Filter Chips ──────────────────────────
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filters) { filter ->
                        val isSelected = state.selectedFilter == filter
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
                                .clickable { state.selectedFilter = filter }
                                .padding(horizontal = 16.dp, vertical = 9.dp)
                        ) {
                            Text(
                                text = filter,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold
                                else FontWeight.Normal,
                                color = if (isSelected) OnPrimary else LocalAppColors.current.textPrimary
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // ── Review Cards ──────────────────────────
            items(displayReviews) { review ->
                ReviewCard(review = review)
                Spacer(modifier = Modifier.height(12.dp))
            }

            // ── Load More ─────────────────────────────
            item {
                OutlinedButton(
                    onClick = { navController.navigate(Routes.WriteReview.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Divider),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = LocalAppColors.current.textPrimary)
                ) {
                    Text(
                        text = "Load More Reviews",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = LocalAppColors.current.textPrimary
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ── Review Card ───────────────────────────────────────────
@Composable
fun ReviewCard(review: GuestReview) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(LocalAppColors.current.surface)
            .padding(16.dp)
    ) {
        Column {
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
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(review.avatarColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = review.initials,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Column {
                        Text(
                            text = review.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = LocalAppColors.current.textPrimary
                        )
                        Text(
                            text = "${review.stayType} • ${review.date}",
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Primary.copy(0.12f))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = review.rating.toString(),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Secondary
                        )
                        Icon(
                            Icons.Outlined.StarOutline,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = review.comment,
                fontSize = 14.sp,
                color = LocalAppColors.current.textPrimary,
                lineHeight = 20.sp,
                fontStyle = FontStyle.Italic
            )

            if (review.hasImages) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(2) { index ->
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (index == 0) Color(0xFF1A3A5C)
                                    else Color(0xFF1A2A3A)
                                )
                        )
                    }
                }
            }

            if (review.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    review.tags.forEach { tag ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(LocalAppColors.current.background)
                                .border(1.dp, Divider, RoundedCornerShape(6.dp))
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = tag,
                                fontSize = 12.sp,
                                color = OnSurfaceSecondary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}