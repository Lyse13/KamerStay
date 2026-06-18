package com.kamerstay.app.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kamerstay.app.core.theme.*

// ── Base component ────────────────────────────────────────────

@Composable
fun ListEmptyState(
    icon: ImageVector,
    iconTint: Color = Primary,
    title: String,
    subtitle: String = "",
    actionLabel: String? = null,
    actionTint: Color = Primary,
    onAction: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp, horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Layered-circle illustration
        Box(
            modifier = Modifier.size(88.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(iconTint.copy(0.05f))
            )
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(CircleShape)
                    .background(iconTint.copy(0.09f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(iconTint.copy(0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Text(
            text = title,
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
            color = LocalAppColors.current.textPrimary,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )

        if (subtitle.isNotEmpty()) {
            Spacer(Modifier.height(6.dp))
            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = OnSurfaceSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
        }

        if (actionLabel != null && onAction != null) {
            Spacer(Modifier.height(22.dp))
            Button(
                onClick = onAction,
                shape = RoundedCornerShape(22.dp),
                colors = ButtonDefaults.buttonColors(containerColor = actionTint),
                contentPadding = PaddingValues(horizontal = 28.dp, vertical = 12.dp)
            ) {
                Text(
                    text = actionLabel,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}

// ── Context-specific presets ──────────────────────────────────

@Composable
fun EmptyWishlist(onExplore: () -> Unit) = ListEmptyState(
    icon = Icons.Outlined.FavoriteBorder,
    iconTint = ErrorColor,
    title = "Your wishlist is empty",
    subtitle = "Tap the heart icon on any hotel\nto save it for later.",
    actionLabel = "Explore Hotels",
    actionTint = Primary,
    onAction = onExplore
)

@Composable
fun EmptyBookingsUpcoming(onExplore: () -> Unit) = ListEmptyState(
    icon = Icons.Outlined.CalendarMonth,
    iconTint = Primary,
    title = "No upcoming stays",
    subtitle = "Your confirmed reservations will\nappear here once you book.",
    actionLabel = "Browse Hotels",
    actionTint = Primary,
    onAction = onExplore
)

@Composable
fun EmptyBookingsPast() = ListEmptyState(
    icon = Icons.Outlined.History,
    iconTint = OnSurfaceSecondary,
    title = "No completed stays yet",
    subtitle = "Your past stays will appear here\nafter check-out."
)

@Composable
fun EmptyBookingsCancelled() = ListEmptyState(
    icon = Icons.Outlined.CheckCircle,
    iconTint = Secondary,
    title = "No cancelled bookings",
    subtitle = "Good news — you haven't\ncancelled any reservation."
)

@Composable
fun EmptySearchResults(query: String = "", onClearSearch: (() -> Unit)? = null) {
    val subtitle = if (query.isNotBlank())
        "No results for \"$query\".\nTry different keywords or filters."
    else
        "We couldn't find any matching properties.\nTry adjusting your search."
    ListEmptyState(
        icon = Icons.Outlined.SearchOff,
        iconTint = OnSurfaceSecondary,
        title = "No properties found",
        subtitle = subtitle,
        actionLabel = if (onClearSearch != null) "Clear search" else null,
        actionTint = Secondary,
        onAction = onClearSearch
    )
}

@Composable
fun EmptyReservationsSearch(searchQuery: String, onClearSearch: () -> Unit) = ListEmptyState(
    icon = Icons.Outlined.SearchOff,
    iconTint = OnSurfaceSecondary,
    title = "No reservations found",
    subtitle = "No results for \"$searchQuery\".\nTry a different guest name or booking ID.",
    actionLabel = "Clear search",
    actionTint = Secondary,
    onAction = onClearSearch
)

@Composable
fun EmptyReservations(onCheckCalendar: (() -> Unit)? = null) = ListEmptyState(
    icon = Icons.Outlined.CalendarMonth,
    iconTint = Primary,
    title = "No reservations yet",
    subtitle = "Confirmed bookings from guests\nwill appear here.",
    actionLabel = if (onCheckCalendar != null) "View Calendar" else null,
    actionTint = Primary,
    onAction = onCheckCalendar
)

@Composable
fun EmptyStaffList(onAdd: () -> Unit) = ListEmptyState(
    icon = Icons.Outlined.People,
    iconTint = Secondary,
    title = "No staff members yet",
    subtitle = "Add your first team member to manage\nroles, shifts and access.",
    actionLabel = "Add a Member",
    actionTint = Secondary,
    onAction = onAdd
)

@Composable
fun EmptyRoomList(onAdd: () -> Unit) = ListEmptyState(
    icon = Icons.Outlined.MeetingRoom,
    iconTint = Primary,
    title = "No rooms configured",
    subtitle = "Add your first room to start\naccepting reservations.",
    actionLabel = "Add a Room",
    actionTint = Primary,
    onAction = onAdd
)

@Composable
fun EmptyNotifications() = ListEmptyState(
    icon = Icons.Outlined.NotificationsNone,
    iconTint = OnSurfaceSecondary,
    title = "All caught up!",
    subtitle = "You have no new notifications.\nCheck back later."
)

@Composable
fun EmptyReviews() = ListEmptyState(
    icon = Icons.Outlined.StarOutline,
    iconTint = StarRating,
    title = "No reviews yet",
    subtitle = "Guest reviews will appear here\nafter their stay."
)