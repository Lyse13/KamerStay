package com.kamerstay.app.features.traveler

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.core.utils.APP_NAME
import com.kamerstay.app.viewmodel.TravelerViewModel
import org.koin.compose.viewmodel.koinViewModel
import com.kamerstay.app.core.components.TravelerBottomNavBar

@Composable
fun WriteReviewScreen(navController: NavController) {

    val viewModel = koinViewModel<TravelerViewModel>()
    val state = viewModel.writeReviewState

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            TravelerBottomNavBar(navController = navController, selectedTab = 3)
        },
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        SecondaryContainer.copy(alpha = 0.35f),
                        BackgroundLight,
                        Color(0xFFE8F4F5)
                    )
                )
            )
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        Icons.Outlined.Notifications,
                        contentDescription = null,
                        tint = Secondary,
                        modifier = Modifier.size(22.dp)
                    )
                    Box(
                        modifier = Modifier
                            .size(38.dp)
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

            // ── Hotel Card ────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(LocalAppColors.current.surface)
            ) {
                Column {
                    val hotel = viewModel.selectedHotel
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(
                                RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 16.dp
                                )
                            )
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF1A3A5C),
                                        Color(0xFF0D2A4A)
                                    )
                                )
                            )
                    )

                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "SÉJOUR TERMINÉ",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Primary,
                            letterSpacing = 0.8.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = hotel?.name ?: "Hôtel",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = LocalAppColors.current.textPrimary
                        )
                        Text(
                            text = hotel?.city ?: "",
                            fontSize = 13.sp,
                            color = OnSurfaceSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Why Review Banner ─────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Primary.copy(0.08f))
                    .padding(14.dp)
            ) {
                Column {
                    Text(
                        text = "Why review?",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Secondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Your feedback helps us maintain the application standard of excellence and assists fellow travelers in finding their perfect stay.",
                        fontSize = 13.sp,
                        color = OnSurfaceSecondary,
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Rating + Review Section ───────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(LocalAppColors.current.surface)
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "How was your stay?",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = LocalAppColors.current.textPrimary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Tap to rate your overall experience",
                        fontSize = 13.sp,
                        color = OnSurfaceSecondary
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // ── Stars ─────────────────────────
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        repeat(5) { index ->
                            val isFilled = index < state.selectedRating
                            Icon(
                                if (isFilled) Icons.Outlined.Star
                                else Icons.Outlined.StarOutline,
                                contentDescription = null,
                                tint = if (isFilled) StarRating
                                else OnSurfaceSecondary.copy(0.3f),
                                modifier = Modifier
                                    .size(40.dp)
                                    .clickable { state.selectedRating = index + 1 }
                            )
                        }
                    }

                    if (state.selectedRating > 0) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = when (state.selectedRating) {
                                1 -> "Poor"
                                2 -> "Fair"
                                3 -> "Good"
                                4 -> "Very Good"
                                5 -> "Excellent!"
                                else -> ""
                            },
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (state.selectedRating >= 4) Primary else StarRating
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // ── Review Text ───────────────────
                    Text(
                        text = "SHARE YOUR EXPERIENCE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurfaceSecondary,
                        letterSpacing = 0.8.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state.reviewText,
                        onValueChange = { state.reviewText = it },
                        placeholder = {
                            Text(
                                text = "What did you love about your stay? Was the staff helpful? How was the breakfast?",
                                color = OnSurfaceSecondary.copy(0.5f),
                                fontSize = 14.sp,
                                lineHeight = 20.sp
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Primary,
                            unfocusedBorderColor = Divider,
                            focusedContainerColor = LocalAppColors.current.surface,
                            unfocusedContainerColor = LocalAppColors.current.surface,
                            cursorColor = Primary
                        ),
                        maxLines = 6
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // ── Photos ────────────────────────
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ADD PHOTOS (OPTIONAL)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceSecondary,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = "Up to 4 photos",
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Upload box
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .border(1.5.dp, Divider, RoundedCornerShape(10.dp))
                                    .background(LocalAppColors.current.background)
                                    .clickable { },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        Icons.Outlined.AddAPhoto,
                                        contentDescription = null,
                                        tint = OnSurfaceSecondary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Text(
                                        text = "Upload",
                                        fontSize = 12.sp,
                                        color = OnSurfaceSecondary
                                    )
                                }
                            }

                            // Photo 1
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color(0xFF0D4A6A),
                                                Color(0xFF1A2A3A)
                                            )
                                        )
                                    )
                                    .clickable { }
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Photo 2
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color(0xFF2A1A0D),
                                                Color(0xFF1A0D06)
                                            )
                                        )
                                    )
                                    .clickable { }
                            )

                            // Empty slot
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(LocalAppColors.current.background),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Outlined.Image,
                                    contentDescription = null,
                                    tint = OnSurfaceSecondary.copy(0.3f),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }

                    // Message d'erreur
                    if (state.error != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(ErrorColor.copy(0.08f))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = state.error ?: "",
                                fontSize = 13.sp,
                                color = ErrorColor,
                                lineHeight = 18.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // ── Buttons ───────────────────────
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(28.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp, Divider
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = LocalAppColors.current.textPrimary
                            )
                        ) {
                            Text(
                                text = "Save Draft",
                                fontSize = 14.sp,
                                color = LocalAppColors.current.textPrimary,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Button(
                            onClick = {
                                val hotelId = viewModel.selectedHotel?.id ?: ""
                                state.error = null
                                viewModel.submitReview(
                                    hotelId  = hotelId,
                                    rating   = state.selectedRating,
                                    comment  = state.reviewText,
                                    onSuccess = { navController.popBackStack() },
                                    onError   = { msg -> state.error = msg }
                                )
                            },
                            enabled = !state.isLoading,
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(28.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Secondary,
                                disabledContainerColor = Secondary.copy(0.4f)
                            )
                        ) {
                            if (state.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    text = "Publier l'avis",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
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