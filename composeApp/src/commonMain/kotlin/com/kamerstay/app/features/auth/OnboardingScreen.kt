package com.kamerstay.app.features.auth

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import kotlinx.coroutines.launch

private data class OnboardingPage(
    val icon: ImageVector,
    val accentColor: Color,
    val title: String,
    val description: String
)

private val pages = listOf(
    OnboardingPage(
        icon = Icons.Outlined.Hotel,
        accentColor = Primary,
        title = "Discover Cameroon's Finest Hotels",
        description = "From luxury resorts in Kribi to business hotels in Douala. Find the perfect place for every stay, every occasion."
    ),
    OnboardingPage(
        icon = Icons.Outlined.BookOnline,
        accentColor = ElectricBlue,
        title = "Book in Minutes, Stay with Confidence",
        description = "Secure your reservation instantly. Receive e-vouchers, manage check-ins and track your entire journey in one place."
    ),
    OnboardingPage(
        icon = Icons.Outlined.BusinessCenter,
        accentColor = SkyBlue,
        title = "Grow Your Hotel Business",
        description = "Register your property, manage bookings, handle your staff and track your revenue — all from a single dashboard."
    )
)

@Composable
fun OnboardingScreen(navController: NavController) {

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    val isLastPage = pagerState.currentPage == pages.lastIndex

    fun goToWelcome() {
        navController.navigate(Routes.Welcome.route) {
            popUpTo(Routes.Onboarding.route) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkNavy)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── Skip button ───────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "KamerStay",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
                if (!isLastPage) {
                    TextButton(onClick = { goToWelcome() }) {
                        Text(
                            text = "Skip",
                            fontSize = 14.sp,
                            color = OnSurfaceSecondary
                        )
                    }
                }
            }

            // ── Pages ─────────────────────────────────
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { pageIndex ->
                OnboardingPageContent(page = pages[pageIndex])
            }

            // ── Bottom section ────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Dot indicators
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(pages.size) { index ->
                        val isActive = pagerState.currentPage == index
                        val width by animateDpAsState(
                            targetValue = if (isActive) 24.dp else 8.dp,
                            animationSpec = tween(300),
                            label = "dot_width"
                        )
                        Box(
                            modifier = Modifier
                                .height(8.dp)
                                .width(width)
                                .clip(CircleShape)
                                .background(
                                    if (isActive) Primary
                                    else OnSurfaceSecondary.copy(alpha = 0.4f)
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Next / Get Started button
                Button(
                    onClick = {
                        if (isLastPage) {
                            goToWelcome()
                        } else {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Text(
                        text = if (isLastPage) "Get Started →" else "Next →",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun OnboardingPageContent(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // ── Illustration ──────────────────────────────
        Box(
            modifier = Modifier
                .size(220.dp),
            contentAlignment = Alignment.Center
        ) {
            // Decorative background circles
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2f, size.height / 2f)
                drawCircle(
                    color = page.accentColor.copy(alpha = 0.06f),
                    radius = size.minDimension / 2f,
                    center = center
                )
                drawCircle(
                    color = page.accentColor.copy(alpha = 0.10f),
                    radius = size.minDimension / 2.8f,
                    center = center
                )
                drawCircle(
                    color = page.accentColor.copy(alpha = 0.16f),
                    radius = size.minDimension / 4.2f,
                    center = center
                )
            }

            // Icon
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                page.accentColor.copy(alpha = 0.3f),
                                page.accentColor.copy(alpha = 0.1f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = page.icon,
                    contentDescription = null,
                    tint = page.accentColor,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // ── Title ─────────────────────────────────────
        Text(
            text = page.title,
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 34.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ── Description ───────────────────────────────
        Text(
            text = page.description,
            fontSize = 15.sp,
            color = OnSurfaceSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
    }
}