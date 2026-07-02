package com.kamerstay.app.features.auth

import kotlin.time.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import kamerstay.composeapp.generated.resources.Res
import kamerstay.composeapp.generated.resources.hotel_hero
import org.jetbrains.compose.resources.DrawableResource
import kamerstay.composeapp.generated.resources.kribi_resort
import kamerstay.composeapp.generated.resources.yaounde_heights
import kamerstay.composeapp.generated.resources.buea_escapes
import kamerstay.composeapp.generated.resources.douala_central
import org.jetbrains.compose.resources.painterResource

@Composable
fun WelcomeScreen(navController: NavController) {

    val year = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalAppColors.current.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // ── Hero Section ──────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp)
            ) {
                Image(
                    painter = painterResource(Res.drawable.hotel_hero),
                    contentDescription = "Hotel Hero",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Overlay gradient
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    DarkNavy.copy(alpha = 0.3f),
                                    DarkNavy.copy(alpha = 0.85f)
                                )
                            )
                        )
                )

                // ── Top Bar ───────────────────────────
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "KamerStay",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )
                }

                // ── Hero Content ──────────────────────
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 24.dp, vertical = 32.dp)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(color = Color.White)) {
                                append("Find your next ")
                            }
                            withStyle(SpanStyle(color = Primary)) {
                                append("stay\n")
                            }
                            withStyle(SpanStyle(color = Color.White)) {
                                append("in Cameroon")
                            }
                        },
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 36.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Experience the perfect blend of digital convenience and authentic Cameroonian hospitality. From coastal resorts in Kribi to urban luxury in Yaoundé.",
                        fontSize = 14.sp,
                        color = OnSurfaceSecondary,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { navController.navigate(Routes.SignIn.route) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary
                        )
                    ) {
                        Text("Get Started →")
                    }
                }
            }

            // ── Popular Destinations ──────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LocalAppColors.current.background)
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Popular Destinations",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = LocalAppColors.current.textPrimary
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { navController.navigate(Routes.SignIn.route) }
                    ) {
                        Text(
                            text = "View all",
                            fontSize = 14.sp,
                            color = Primary,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            Icons.Outlined.ArrowForward,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Destination cards
                val destinations = listOf(
                    DestinationData(
                        name = "Kribi Resorts", "Pristine beaches and Atlantic luxury", "COASTAL BLISS",
                        image = Res.drawable.kribi_resort
                    ),
                    DestinationData(
                        name = "Yaoundé Heights", "Urban elegance in the capital", "",
                        image =Res.drawable.yaounde_heights
                    ),
                    DestinationData(
                        name = "Buea Escapes", "Mountain serenity and nature trails", "",
                        image = Res.drawable.buea_escapes
                    ),
                    DestinationData(
                        name = "Douala Central", "Business hub with premium comfort", "",
                        image = Res.drawable.douala_central
                    ),
                )
                destinations.forEach { dest ->
                    DestinationCard(
                        name = dest.name,
                        description = dest.description,
                        badge = dest.badge,
                        image = dest.image
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Footer
                Text(
                    text = "© $year KamerStay · Smart Hotel Booking for Cameroon",
                    fontSize = 12.sp,
                    color = DarkNavy,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

data class DestinationData(
    val name: String,
    val description: String,
    val badge: String,
    val image: DrawableResource
)
// ── Destination Card ──────────────────────────────────────
@Composable
fun DestinationCard(
    name: String,
    description: String,
    badge: String = "",
    image: DrawableResource
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(SurfaceVariant)
    ) {
        Image(
            painter = painterResource(image),
            contentDescription = name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            DarkNavy.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        // Badge
        if (badge.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .padding(12.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Primary.copy(alpha = 0.15f))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .align(Alignment.TopStart)
            ) {
                Text(
                    text = badge,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary,
                    letterSpacing = 0.8.sp
                )
            }
        }

        // Text content
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(
                text = name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = description,
                fontSize = 13.sp,
                color = OnSurfaceSecondary
            )
        }
    }
}


