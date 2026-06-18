package com.kamerstay.app.features.manager

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
import com.kamerstay.app.core.navigation.NavigationState
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import kotlinx.coroutines.launch

private data class ManagerOnboardingPage(
    val icon: ImageVector,
    val accentColor: Color,
    val badge: String,
    val title: String,
    val description: String,
    val actionLabel: String? = null,
    val skipLabel: String? = null,
    val actionKey: String? = null
)

private val managerPages = listOf(
    ManagerOnboardingPage(
        icon = Icons.Outlined.Hotel,
        accentColor = Secondary,
        badge = "WELCOME",
        title = "Your hotel is registered!",
        description = "Let's take a few minutes to set up your property so travellers can discover and book with you right away. It only takes 3 quick steps."
    ),
    ManagerOnboardingPage(
        icon = Icons.Outlined.MeetingRoom,
        accentColor = Primary,
        badge = "STEP  1 / 3",
        title = "Add your first room",
        description = "Guests can only book what they can see. Define at least one room type — bed type, capacity, and nightly rate — to start accepting reservations.",
        actionLabel = "Add a Room →",
        skipLabel = "I'll do this later",
        actionKey = "room"
    ),
    ManagerOnboardingPage(
        icon = Icons.Outlined.Pool,
        accentColor = ElectricBlue,
        badge = "STEP  2 / 3",
        title = "Highlight your amenities",
        description = "Wi-Fi, pool, breakfast, parking — tell guests what makes your property special and help your listing stand out from the competition.",
        actionLabel = "Set Amenities →",
        skipLabel = "Skip for now",
        actionKey = "amenities"
    ),
    ManagerOnboardingPage(
        icon = Icons.Outlined.LocalOffer,
        accentColor = Color(0xFFF57C00),
        badge = "STEP  3 / 3",
        title = "Attract your first guests",
        description = "Launching with a 10–15% introductory offer is the fastest way to earn your first bookings and reviews. You can disable it at any time.",
        actionLabel = "Create a Promotion →",
        skipLabel = "Start without offer",
        actionKey = "promotion"
    ),
    ManagerOnboardingPage(
        icon = Icons.Outlined.CheckCircle,
        accentColor = Secondary,
        badge = "ALL SET",
        title = "Your property is live!",
        description = "Congratulations! Your hotel is now visible to travellers across Cameroon. Manage reservations, revenue, and guest reviews from your dashboard."
    )
)

@Composable
fun ManagerOnboardingScreen(navController: NavController) {

    val pagerState = rememberPagerState(pageCount = { managerPages.size })
    val scope = rememberCoroutineScope()

    val currentPage = pagerState.currentPage
    val isWelcomePage = currentPage == 0
    val isFinalPage = currentPage == managerPages.lastIndex

    fun advance() {
        scope.launch {
            pagerState.animateScrollToPage(currentPage + 1)
        }
    }

    fun launchDashboard() {
        navController.navigate(Routes.ManagerDashboard.route) {
            popUpTo(Routes.ManagerOnboarding.route) { inclusive = true }
        }
    }

    fun handleAction(key: String) {
        advance()
        when (key) {
            "room" -> navController.navigate(Routes.AddEditRoom.createRoute("1"))
            "amenities" -> navController.navigate(Routes.HotelAmenities.route)
            "promotion" -> {
                NavigationState.selectedPromoId = ""
                navController.navigate(Routes.AddEditPromotion.route)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkNavy)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── Top bar ───────────────────────────────────
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
                    color = Secondary
                )
                if (!isFinalPage) {
                    TextButton(onClick = { launchDashboard() }) {
                        Text(
                            text = "Skip all",
                            fontSize = 14.sp,
                            color = OnSurfaceSecondary
                        )
                    }
                }
            }

            // ── Progress bar ──────────────────────────────
            if (!isWelcomePage && !isFinalPage) {
                val progress = (currentPage.toFloat()) / (managerPages.size - 2)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Setup progress",
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary.copy(0.7f)
                        )
                        Text(
                            "${currentPage} / 3 completed",
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary.copy(0.7f)
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color.White.copy(0.1f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progress)
                                .height(3.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(Secondary)
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }

            // ── Pager ─────────────────────────────────────
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                userScrollEnabled = false
            ) { pageIndex ->
                ManagerOnboardingPageContent(page = managerPages[pageIndex])
            }

            // ── Bottom section ────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Dot indicators
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(managerPages.size) { index ->
                        val isActive = currentPage == index
                        val width by animateDpAsState(
                            targetValue = if (isActive) 24.dp else 8.dp,
                            animationSpec = tween(300),
                            label = "dot"
                        )
                        Box(
                            modifier = Modifier
                                .height(8.dp)
                                .width(width)
                                .clip(CircleShape)
                                .background(
                                    if (isActive) managerPages[currentPage].accentColor
                                    else OnSurfaceSecondary.copy(0.3f)
                                )
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                val page = managerPages[currentPage]

                when {
                    // Welcome or final: single primary button
                    page.actionKey == null -> {
                        Button(
                            onClick = {
                                if (isFinalPage) launchDashboard() else advance()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(28.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = page.accentColor)
                        ) {
                            Text(
                                text = if (isFinalPage) "Open Dashboard →" else "Let's go →",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    // Action steps: primary action + skip link
                    else -> {
                        Button(
                            onClick = { handleAction(page.actionKey) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(28.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = page.accentColor)
                        ) {
                            Text(
                                text = page.actionLabel ?: "",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(Modifier.height(14.dp))

                        TextButton(onClick = { advance() }) {
                            Text(
                                text = page.skipLabel ?: "Skip",
                                fontSize = 14.sp,
                                color = OnSurfaceSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── Page content ──────────────────────────────────────────────
@Composable
private fun ManagerOnboardingPageContent(page: ManagerOnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Illustration
        Box(
            modifier = Modifier.size(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2f, size.height / 2f)
                drawCircle(color = page.accentColor.copy(0.05f), radius = size.minDimension / 2f, center = center)
                drawCircle(color = page.accentColor.copy(0.09f), radius = size.minDimension / 2.8f, center = center)
                drawCircle(color = page.accentColor.copy(0.15f), radius = size.minDimension / 4.2f, center = center)
            }
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                page.accentColor.copy(0.35f),
                                page.accentColor.copy(0.12f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = page.icon,
                    contentDescription = null,
                    tint = page.accentColor,
                    modifier = Modifier.size(44.dp)
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // Badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(page.accentColor.copy(0.18f))
                .padding(horizontal = 14.dp, vertical = 5.dp)
        ) {
            Text(
                text = page.badge,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = page.accentColor,
                letterSpacing = 1.sp
            )
        }

        Spacer(Modifier.height(14.dp))

        // Title
        Text(
            text = page.title,
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 34.sp
        )

        Spacer(Modifier.height(14.dp))

        // Description
        Text(
            text = page.description,
            fontSize = 15.sp,
            color = OnSurfaceSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        // Tip card for action pages
        if (page.actionKey != null) {
            Spacer(Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(page.accentColor.copy(0.08f))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    Icons.Outlined.Lightbulb,
                    contentDescription = null,
                    tint = page.accentColor,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = when (page.actionKey) {
                        "room" -> "Hotels with 3+ rooms get 40% more bookings on average."
                        "amenities" -> "Listings with amenities filled in appear higher in search."
                        "promotion" -> "Properties with a welcome offer get their first booking 3× faster."
                        else -> ""
                    },
                    fontSize = 12.sp,
                    color = page.accentColor.copy(0.9f),
                    lineHeight = 16.sp,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}