package com.kamerstay.app.core.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kamerstay.app.core.theme.AppThemeController

// ── Shimmer brush ─────────────────────────────────────────────

@Composable
fun rememberShimmerBrush(): Brush {
    val isDark = AppThemeController.darkModeEnabled

    val shimmerColors = if (isDark) listOf(
        Color(0xFF0D2A3D),
        Color(0xFF1A3D57),
        Color(0xFF245070),
        Color(0xFF1A3D57),
        Color(0xFF0D2A3D)
    ) else listOf(
        Color(0xFFE2EDF0),
        Color(0xFFF0F8FA),
        Color(0xFFF8FCFD),
        Color(0xFFF0F8FA),
        Color(0xFFE2EDF0)
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 2000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_x"
    )

    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 600f, 0f),
        end = Offset(translateAnim, 0f)
    )
}

// ── Skeleton primitives ───────────────────────────────────────

@Composable
fun SkeletonBox(
    modifier: Modifier,
    brush: Brush,
    cornerRadius: Dp = 8.dp
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(brush)
    )
}

@Composable
fun SkeletonText(
    modifier: Modifier = Modifier,
    brush: Brush,
    height: Dp = 14.dp,
    cornerRadius: Dp = 6.dp
) {
    Box(
        modifier = modifier
            .height(height)
            .clip(RoundedCornerShape(cornerRadius))
            .background(brush)
    )
}

@Composable
fun SkeletonCircle(size: Dp, brush: Brush) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(brush)
    )
}

// ── Hotel card skeleton (TravelerHomeScreen) ──────────────────

@Composable
fun HotelCardSkeleton() {
    val brush = rememberShimmerBrush()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Column {
            // Image area
            SkeletonBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                brush = brush,
                cornerRadius = 0.dp
            )

            // Content area
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (AppThemeController.darkModeEnabled) Color(0xFF0D2A3D)
                        else Color.White
                    )
                    .padding(14.dp)
            ) {
                // Name row + rating
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SkeletonText(
                        modifier = Modifier.fillMaxWidth(0.62f),
                        brush = brush,
                        height = 16.dp
                    )
                    SkeletonText(
                        modifier = Modifier.width(36.dp),
                        brush = brush,
                        height = 14.dp
                    )
                }

                Spacer(Modifier.height(8.dp))

                // Location row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SkeletonCircle(size = 12.dp, brush = brush)
                    SkeletonText(
                        modifier = Modifier.fillMaxWidth(0.45f),
                        brush = brush,
                        height = 12.dp
                    )
                }

                Spacer(Modifier.height(10.dp))

                HorizontalSkeletonDivider(brush)

                Spacer(Modifier.height(10.dp))

                // Price + button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        SkeletonText(
                            modifier = Modifier.width(50.dp),
                            brush = brush,
                            height = 10.dp
                        )
                        SkeletonText(
                            modifier = Modifier.width(80.dp),
                            brush = brush,
                            height = 20.dp
                        )
                    }
                    SkeletonBox(
                        modifier = Modifier
                            .width(100.dp)
                            .height(36.dp),
                        brush = brush,
                        cornerRadius = 10.dp
                    )
                }
            }
        }
    }
}

// ── Booking card skeleton (BookingHistoryScreen) ──────────────

@Composable
fun BookingCardSkeleton() {
    val brush = rememberShimmerBrush()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Column {
            // Image area with badge
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                SkeletonBox(
                    modifier = Modifier.fillMaxSize(),
                    brush = brush,
                    cornerRadius = 0.dp
                )
                // Status badge placeholder
                SkeletonBox(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .width(80.dp)
                        .height(26.dp),
                    brush = brush,
                    cornerRadius = 13.dp
                )
            }

            // Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (AppThemeController.darkModeEnabled) Color(0xFF0D2A3D)
                        else Color.White
                    )
                    .padding(16.dp)
            ) {
                // Hotel name
                SkeletonText(
                    modifier = Modifier.fillMaxWidth(0.7f),
                    brush = brush,
                    height = 18.dp
                )
                Spacer(Modifier.height(8.dp))

                // Dates row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SkeletonCircle(size = 14.dp, brush = brush)
                    SkeletonText(
                        modifier = Modifier.fillMaxWidth(0.55f),
                        brush = brush,
                        height = 12.dp
                    )
                }

                Spacer(Modifier.height(12.dp))
                HorizontalSkeletonDivider(brush)
                Spacer(Modifier.height(12.dp))

                // Price + button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        SkeletonText(
                            modifier = Modifier.width(60.dp),
                            brush = brush,
                            height = 10.dp
                        )
                        SkeletonText(
                            modifier = Modifier.width(90.dp),
                            brush = brush,
                            height = 20.dp
                        )
                    }
                    SkeletonBox(
                        modifier = Modifier
                            .width(80.dp)
                            .height(38.dp),
                        brush = brush,
                        cornerRadius = 10.dp
                    )
                }
            }
        }
    }
}

// ── Reservation card skeleton (ReservationsScreen) ────────────

@Composable
fun ReservationCardSkeleton() {
    val brush = rememberShimmerBrush()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Column {
            // Room image area with tag badge
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                SkeletonBox(
                    modifier = Modifier.fillMaxSize(),
                    brush = brush,
                    cornerRadius = 0.dp
                )
                SkeletonBox(
                    modifier = Modifier
                        .padding(10.dp)
                        .width(72.dp)
                        .height(22.dp),
                    brush = brush,
                    cornerRadius = 6.dp
                )
            }

            // Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (AppThemeController.darkModeEnabled) Color(0xFF0D2A3D)
                        else Color.White
                    )
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // GUEST label + name
                SkeletonText(modifier = Modifier.width(44.dp), brush = brush, height = 9.dp)
                SkeletonText(modifier = Modifier.fillMaxWidth(0.6f), brush = brush, height = 18.dp)
                SkeletonText(modifier = Modifier.width(88.dp), brush = brush, height = 11.dp)

                Spacer(Modifier.height(2.dp))

                // STAY DURATION
                SkeletonText(modifier = Modifier.width(80.dp), brush = brush, height = 9.dp)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SkeletonCircle(size = 14.dp, brush = brush)
                    SkeletonText(modifier = Modifier.fillMaxWidth(0.5f), brush = brush, height = 13.dp)
                }
                SkeletonText(modifier = Modifier.width(52.dp), brush = brush, height = 11.dp)

                Spacer(Modifier.height(2.dp))

                // STATUS + actions row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        SkeletonCircle(size = 40.dp, brush = brush)
                        SkeletonCircle(size = 40.dp, brush = brush)
                    }
                    SkeletonBox(
                        modifier = Modifier
                            .width(90.dp)
                            .height(36.dp),
                        brush = brush,
                        cornerRadius = 20.dp
                    )
                }
            }
        }
    }
}

// ── Notification item skeleton ────────────────────────────────

@Composable
fun NotificationItemSkeleton() {
    val brush = rememberShimmerBrush()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        SkeletonCircle(size = 48.dp, brush = brush)
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            SkeletonText(modifier = Modifier.fillMaxWidth(0.75f), brush = brush, height = 14.dp)
            SkeletonText(modifier = Modifier.fillMaxWidth(), brush = brush, height = 12.dp)
            SkeletonText(modifier = Modifier.fillMaxWidth(0.85f), brush = brush, height = 12.dp)
            SkeletonText(modifier = Modifier.width(60.dp), brush = brush, height = 10.dp)
        }
    }
}

// ── Internal helper ───────────────────────────────────────────

@Composable
private fun HorizontalSkeletonDivider(brush: Brush) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(brush)
    )
}