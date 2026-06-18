package com.kamerstay.app.features.traveler

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.model.AppNotification
import com.kamerstay.app.data.model.NotificationType
import com.kamerstay.app.viewmodel.TravelerViewModel
import org.koin.compose.viewmodel.koinViewModel
import com.kamerstay.app.core.components.TravelerBottomNavBar

@Composable
fun NotificationsScreen(navController: NavController) {

    val viewModel = koinViewModel<TravelerViewModel>()
    val newCount = viewModel.todayNotifications.count { !it.isRead }

    Scaffold(
        containerColor = LocalAppColors.current.background,
        bottomBar = {
            TravelerBottomNavBar(navController = navController, selectedTab = -1)
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
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { navController.navigate(Routes.TravelerProfile.route) }) {
                            Icon(
                                Icons.Outlined.Menu,
                                contentDescription = null,
                                tint = Secondary
                            )
                        }
                        Text(
                            text = "KamerStay",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Secondary
                        )
                    }
                    TextButton(onClick = { viewModel.markAllNotificationsRead() }) {
                        Text(
                            text = "Mark all read",
                            fontSize = 13.sp,
                            color = if (newCount > 0) Primary else OnSurfaceSecondary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // ── Header ────────────────────────────────
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = "Notifications",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = LocalAppColors.current.textPrimary
                    )
                    Text(
                        text = "Stay updated with your latest booking activity",
                        fontSize = 13.sp,
                        color = OnSurfaceSecondary
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // ── Today ─────────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Today",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )
                    if (newCount > 0) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Primary.copy(0.15f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "$newCount New",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Primary
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            items(viewModel.todayNotifications) { notification ->
                NotificationItem(
                    notification = notification,
                    onAction = {
                        when (notification.type) {
                            NotificationType.BOOKING   -> navController.navigate(Routes.BookingHistory.route)
                            NotificationType.CHECK_IN  -> navController.navigate(Routes.BookingHistory.route)
                            NotificationType.PAYMENT   -> navController.navigate(Routes.TravelerPaymentMethods.route)
                            NotificationType.PROMO     -> navController.navigate(Routes.HotelSearch.route)
                            NotificationType.ALERT     -> navController.navigate(Routes.BookingHistory.route)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // ── Earlier ───────────────────────────────
            item {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Earlier",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalAppColors.current.textPrimary,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            items(viewModel.earlierNotifications) { notification ->
                NotificationItem(
                    notification = notification,
                    onAction = {
                        when (notification.type) {
                            NotificationType.BOOKING   -> navController.navigate(Routes.BookingHistory.route)
                            NotificationType.CHECK_IN  -> navController.navigate(Routes.BookingHistory.route)
                            NotificationType.PAYMENT   -> navController.navigate(Routes.TravelerPaymentMethods.route)
                            NotificationType.PROMO     -> navController.navigate(Routes.HotelSearch.route)
                            NotificationType.ALERT     -> navController.navigate(Routes.BookingHistory.route)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // ── Rewards Banner ────────────────────────
            item {
                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Secondary, DeepBlue)
                            )
                        )
                        .padding(20.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Unlock Rewards",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "You're only one stay away from Gold Status membership benefits.",
                                fontSize = 13.sp,
                                color = Color.White.copy(0.8f),
                                lineHeight = 18.sp
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Primary)
                                    .clickable { navController.navigate(Routes.TravelerProfile.route) }
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = "View Progress",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = OnPrimary
                                )
                            }
                        }

                        // Decorative shield icon
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .align(Alignment.CenterVertically),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Outlined.Shield,
                                contentDescription = null,
                                tint = Color.White.copy(0.15f),
                                modifier = Modifier.size(70.dp)
                            )
                            Icon(
                                Icons.Outlined.Star,
                                contentDescription = null,
                                tint = Primary.copy(0.6f),
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ── Notification Item ─────────────────────────────────────
@Composable
fun NotificationItem(
    notification: AppNotification,
    onAction: () -> Unit
) {
    val (iconBg, iconTint, icon) = when (notification.type) {
        NotificationType.BOOKING -> Triple(
            Primary.copy(0.12f), Secondary, Icons.Outlined.CalendarMonth
        )
        NotificationType.CHECK_IN -> Triple(
            Primary.copy(0.12f), Secondary, Icons.Outlined.Key
        )
        NotificationType.ALERT -> Triple(
            ErrorColor.copy(0.1f), ErrorColor, Icons.Outlined.Warning
        )
        NotificationType.PROMO -> Triple(
            ElectricBlue.copy(0.12f), Secondary, Icons.Outlined.AutoAwesome
        )
        NotificationType.PAYMENT -> Triple(
            OnSurfaceSecondary.copy(0.1f), OnSurfaceSecondary, Icons.Outlined.Receipt
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(LocalAppColors.current.surface)
            .then(
                if (notification.isAlert) Modifier.border(
                    1.dp, ErrorColor.copy(0.3f), RoundedCornerShape(14.dp)
                ) else Modifier
            )
            .padding(14.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Icon
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = notification.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (notification.isAlert) ErrorColor else LocalAppColors.current.textPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = notification.time,
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = notification.message,
                        fontSize = 13.sp,
                        color = OnSurfaceSecondary,
                        lineHeight = 18.sp
                    )

                    // Action button
                    if (notification.hasAction && notification.actionLabel.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = onAction,
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Secondary),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = notification.actionLabel,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}