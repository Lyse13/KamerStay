package com.kamerstay.app.features.manager

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.viewmodel.ManagerViewModel
import org.koin.compose.viewmodel.koinViewModel
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.mock.DashboardMockData
import com.kamerstay.app.data.model.RecentActivity
import com.kamerstay.app.core.components.ManagerBottomNavBar
import com.kamerstay.app.data.state.UserSession

@Composable
fun ManagerDashboardScreen(navController: NavController) {

    val viewModel = koinViewModel<ManagerViewModel>()
    val activities = DashboardMockData.recentActivities
    val barHeights = DashboardMockData.revenueBarHeights

    Scaffold(
        containerColor = LocalAppColors.current.background,
        bottomBar = {
            ManagerBottomNavBar(navController = navController, currentRoute = "dashboard")
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Routes.AddEditRoom.createRoute("1")) },
                containerColor = Primary,
                contentColor = OnPrimary,
                shape = CircleShape
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
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
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { navController.navigate(Routes.ManagerProfile.route) }) {
                            Icon(
                                Icons.Filled.Menu,
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
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Secondary)
                            .clickable { navController.navigate(Routes.ManagerNotifications.route) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Outlined.Notifications,
                            contentDescription = "Notifications",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            // ── Greeting ──────────────────────────────
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = "Bonjour, ${UserSession.fullName.ifBlank { "Manager" }}",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = LocalAppColors.current.textPrimary
                    )
                    Text(
                        text = "Here's what's happening at your property today.",
                        fontSize = 13.sp,
                        color = OnSurfaceSecondary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                        // ← Bouton existant — Analytics
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Primary.copy(0.1f))
                                .clickable { navController.navigate(Routes.RevenueAnalytics.route) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    Icons.Outlined.BarChart,
                                    contentDescription = null,
                                    tint = Secondary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "View Analytics →",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Secondary
                                )
                            }
                        }

                        // ← AJOUTEZ ce bouton Report
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Primary.copy(0.1f))
                                .clickable { navController.navigate(Routes.RevenueReport.route) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    Icons.Outlined.Description,
                                    contentDescription = null,
                                    tint = Secondary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "Full Report →",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Secondary
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFF57C00).copy(0.1f))
                                .clickable { navController.navigate(Routes.Promotions.route) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    Icons.Outlined.LocalOffer,
                                    contentDescription = null,
                                    tint = Color(0xFFF57C00),
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "Promotions →",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFFF57C00)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // ── Daily Revenue Card ────────────────────
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Primary.copy(0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Outlined.Payments,
                                    contentDescription = null,
                                    tint = Secondary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Outlined.TrendingUp,
                                    contentDescription = null,
                                    tint = Primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "+12%",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Primary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Daily Revenue",
                            fontSize = 13.sp,
                            color = OnSurfaceSecondary
                        )
                        Text(
                            text = "${viewModel.totalRevenue.toLong()} FCFA",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = LocalAppColors.current.textPrimary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Bar chart
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            barHeights.forEach { height ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight(height)
                                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                        .background(Primary)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // ── Monthly Revenue Card ──────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(DeepBlue)
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White.copy(0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Outlined.BarChart,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Text(
                                text = "Target: \$120k",
                                fontSize = 12.sp,
                                color = Color.White.copy(0.7f)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Monthly Revenue",
                            fontSize = 13.sp,
                            color = Color.White.copy(0.7f)
                        )
                        Text(
                            text = "${viewModel.totalRevenue.toLong()} FCFA",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Progress bar
                        LinearProgressIndicator(
                            progress = { 0.82f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = Primary,
                            trackColor = Color.White.copy(0.2f)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "82% of Monthly Goal",
                            fontSize = 12.sp,
                            color = Color.White.copy(0.7f)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // ── Occupancy Card ────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(LocalAppColors.current.surface)
                        .padding(20.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Circular progress
                        Box(
                            modifier = Modifier.size(140.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            androidx.compose.foundation.Canvas(
                                modifier = Modifier.size(140.dp)
                            ) {
                                val strokeWidth = 16.dp.toPx()
                                drawArc(
                                    color = Divider,
                                    startAngle = -90f,
                                    sweepAngle = 360f,
                                    useCenter = false,
                                    style = Stroke(strokeWidth, cap = StrokeCap.Round)
                                )
                                drawArc(
                                    color = Primary,
                                    startAngle = -90f,
                                    sweepAngle = 360f * 0.9f,
                                    useCenter = false,
                                    style = Stroke(strokeWidth, cap = StrokeCap.Round)
                                )
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "90%",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = LocalAppColors.current.textPrimary
                                )
                                Text(
                                    text = "Occupancy",
                                    fontSize = 12.sp,
                                    color = OnSurfaceSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(Primary)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Booked",
                                    fontSize = 12.sp,
                                    color = OnSurfaceSecondary
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(Divider)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Vacant",
                                    fontSize = 12.sp,
                                    color = OnSurfaceSecondary
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // ── Recent Activity ───────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Activity",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = LocalAppColors.current.textPrimary
                    )
                    Text(
                        text = "View all",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Secondary,
                        modifier = Modifier.clickable {
                            navController.navigate(Routes.Reservations.route)
                        }
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Activity items
            item {
                if (viewModel.isLoadingBookings) {
                    Box(modifier = Modifier.fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Primary)
                    }
                } else if (viewModel.bookings.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(LocalAppColors.current.surface)
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Aucune réservation pour le moment", color = OnSurfaceSecondary, fontSize = 14.sp)
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(LocalAppColors.current.surface)
                    ) {
                        Column {
                            viewModel.bookings.take(5).forEachIndexed { index, booking ->
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier.size(44.dp).clip(CircleShape).background(Primary.copy(0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Outlined.Person, contentDescription = null, tint = Secondary, modifier = Modifier.size(22.dp))
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = booking.hotel?.name ?: "Hôtel",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = LocalAppColors.current.textPrimary,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "${booking.checkInDate} → ${booking.checkOutDate}",
                                            fontSize = 12.sp,
                                            color = OnSurfaceSecondary
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column(horizontalAlignment = Alignment.End) {
                                        Box(
                                            modifier = Modifier.clip(RoundedCornerShape(6.dp))
                                                .background(Primary.copy(0.12f))
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = booking.bookingStatus.name,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Primary
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "${booking.totalAmount.toLong()} FCFA",
                                            fontSize = 12.sp,
                                            color = OnSurfaceSecondary
                                        )
                                    }
                                }
                                if (index < viewModel.bookings.take(5).lastIndex) {
                                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Divider.copy(0.5f))
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // ── Quick Stats ───────────────────────────
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Rooms to clean
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(LocalAppColors.current.surface)
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Primary.copy(0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Outlined.CleaningServices,
                                    contentDescription = null,
                                    tint = Secondary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = "${viewModel.totalBookings} Réservations totales",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = LocalAppColors.current.textPrimary
                                )
                                Text(
                                    text = "Rooms to clean",
                                    fontSize = 13.sp,
                                    color = OnSurfaceSecondary
                                )
                            }
                        }
                    }

                    // Breakfasts served
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(LocalAppColors.current.surface)
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Primary.copy(0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Outlined.FreeBreakfast,
                                    contentDescription = null,
                                    tint = Secondary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = "${viewModel.confirmedBookings} Réservations confirmées",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = LocalAppColors.current.textPrimary
                                )
                                Text(
                                    text = "Breakfasts served",
                                    fontSize = 13.sp,
                                    color = OnSurfaceSecondary
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // ── Property Card ─────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF1A2A3A),
                                    Color(0xFF0D1A28)
                                )
                            )
                        )
                ) {
                    AsyncImage(
                        model = "https://images.unsplash.com/photo-1571896349842-33c89424de2d?w=800&fit=crop&auto=format",
                        contentDescription = "Grand Vista Hotel",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Column {
                            Text(
                                text = UserSession.fullName.ifBlank { "Mon Hôtel" },
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Tableau de bord Manager · KamerStay" ,
                                fontSize = 12.sp,
                                color = Color.White.copy(0.7f)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ── Activity Row ──────────────────────────────────────────
@Composable
fun ActivityRow(activity: RecentActivity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(OnSurfaceSecondary.copy(0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Outlined.Person,
                contentDescription = null,
                tint = OnSurfaceSecondary,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = activity.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = LocalAppColors.current.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = activity.room,
                fontSize = 12.sp,
                color = OnSurfaceSecondary,
                maxLines = 2
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(horizontalAlignment = Alignment.End) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(activity.badgeColor)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = activity.badge,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = activity.badgeTextColor
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = activity.time,
                fontSize = 12.sp,
                color = OnSurfaceSecondary
            )
        }
    }
}
