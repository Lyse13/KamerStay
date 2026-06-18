package com.kamerstay.app.features.manager

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.model.ManagerNotification
import com.kamerstay.app.data.model.ManagerNotificationType
import com.kamerstay.app.viewmodel.ManagerViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ManagerNotificationsScreen(navController: NavController) {

    val viewModel = koinViewModel<ManagerViewModel>()
    val newCount = viewModel.todayNotifications.count { !it.isRead }

    Scaffold(
        containerColor = LocalAppColors.current.background
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
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Back",
                            tint = LocalAppColors.current.textPrimary
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
                        text = "Stay on top of your property activity",
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
                ManagerNotificationItem(
                    notification = notification,
                    onAction = {
                        when (notification.type) {
                            ManagerNotificationType.NEW_BOOKING ->
                                navController.navigate(Routes.ReservationDetails.createRoute(notification.id))
                            ManagerNotificationType.CHECK_IN ->
                                navController.navigate(Routes.CheckIn.createRoute(notification.id))
                            ManagerNotificationType.CHECK_OUT ->
                                navController.navigate(Routes.CheckOut.createRoute(notification.id))
                            ManagerNotificationType.PAYMENT ->
                                navController.navigate(Routes.RevenueAnalytics.route)
                            ManagerNotificationType.REVIEW ->
                                navController.navigate(Routes.HotelReviews.route)
                            ManagerNotificationType.STAFF ->
                                navController.navigate(Routes.StaffManagement.route)
                            ManagerNotificationType.ALERT ->
                                navController.navigate(Routes.ManageHotel.createRoute("1"))
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
                ManagerNotificationItem(
                    notification = notification,
                    onAction = {
                        when (notification.type) {
                            ManagerNotificationType.NEW_BOOKING ->
                                navController.navigate(Routes.ReservationDetails.createRoute(notification.id))
                            ManagerNotificationType.CHECK_IN ->
                                navController.navigate(Routes.CheckIn.createRoute(notification.id))
                            ManagerNotificationType.CHECK_OUT ->
                                navController.navigate(Routes.CheckOut.createRoute(notification.id))
                            ManagerNotificationType.PAYMENT ->
                                navController.navigate(Routes.RevenueAnalytics.route)
                            ManagerNotificationType.REVIEW ->
                                navController.navigate(Routes.HotelReviews.route)
                            ManagerNotificationType.STAFF ->
                                navController.navigate(Routes.StaffManagement.route)
                            ManagerNotificationType.ALERT ->
                                navController.navigate(Routes.ManageHotel.createRoute("1"))
                        }
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // ── Boost Visibility Banner ───────────────
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
                                text = "Boost Visibility",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Reply to guest reviews to improve your ranking and attract more bookings.",
                                fontSize = 13.sp,
                                color = Color.White.copy(0.8f),
                                lineHeight = 18.sp
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Primary)
                                    .clickable { navController.navigate(Routes.HotelReviews.route) }
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = "View Reviews",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = OnPrimary
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .align(Alignment.CenterVertically),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Outlined.Hotel,
                                contentDescription = null,
                                tint = Color.White.copy(0.15f),
                                modifier = Modifier.size(70.dp)
                            )
                            Icon(
                                Icons.Outlined.Star,
                                contentDescription = null,
                                tint = Primary.copy(0.7f),
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

// ── Manager Notification Item ─────────────────────────────────────
@Composable
private fun ManagerNotificationItem(
    notification: ManagerNotification,
    onAction: () -> Unit
) {
    val (iconBg, iconTint, icon)         = when (notification.type) {
        ManagerNotificationType.NEW_BOOKING -> Triple(
            Primary.copy(0.12f),
            Secondary, Icons.Outlined.CalendarMonth
        )
        ManagerNotificationType.CHECK_IN -> Triple(
            Primary.copy(0.12f), Secondary, Icons.Outlined.Key
        )
        ManagerNotificationType.CHECK_OUT -> Triple(
            ElectricBlue.copy(0.12f), Secondary, Icons.AutoMirrored.Outlined.ExitToApp
        )
        ManagerNotificationType.PAYMENT -> Triple(
            OnSurfaceSecondary.copy(0.1f), OnSurfaceSecondary, Icons.Outlined.Receipt
        )
        ManagerNotificationType.REVIEW -> Triple(
            ElectricBlue.copy(0.12f), Secondary, Icons.Outlined.Star
        )
        ManagerNotificationType.ALERT -> Triple(
            ErrorColor.copy(0.1f), ErrorColor, Icons.Outlined.Warning
        )
        ManagerNotificationType.STAFF -> Triple(
            SkyBlue.copy(0.15f), Secondary, Icons.Outlined.Group
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
                            color = if (notification.isAlert) ErrorColor
                            else LocalAppColors.current.textPrimary,
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