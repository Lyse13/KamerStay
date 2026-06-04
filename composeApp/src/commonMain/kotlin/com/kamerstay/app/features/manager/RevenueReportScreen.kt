package com.kamerstay.app.features.manager

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.mock.RevenueReportMockData
import com.kamerstay.app.data.model.TransactionStatus
import com.kamerstay.app.data.model.RecentTransaction
import com.kamerstay.app.viewmodel.ManagerViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RevenueReportScreen(navController: NavController) {

    val viewModel = koinViewModel<ManagerViewModel>()
    val state = viewModel.revenueReportState

    Scaffold(
        containerColor = BackgroundLight,
        bottomBar = {
            NavigationBar(containerColor = Color.White, tonalElevation = 0.dp) {
                listOf(
                    Icons.Outlined.Home to "Home",
                    Icons.Outlined.Explore to "Explore",
                    Icons.Outlined.BookOnline to "Bookings",
                    Icons.Outlined.Person to "Profile"
                ).forEachIndexed { index, (icon, label) ->
                    NavigationBarItem(
                        selected = index == 2,
                        onClick = {
                            when (index) {
                                0 -> navController.navigate(Routes.ManagerDashboard.route)
                                1 -> navController.navigate(Routes.HotelSearch.route)
                                3 -> navController.navigate(Routes.ManagerProfile.route)
                            }
                        },
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label, fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Secondary,
                            selectedTextColor = Secondary,
                            indicatorColor = Primary.copy(0.15f),
                            unselectedIconColor = OnSurfaceSecondary,
                            unselectedTextColor = OnSurfaceSecondary
                        )
                    )
                }
            }
        }
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
                            text = "MyStays",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Secondary
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.CalendarMonth,
                            contentDescription = null,
                            tint = Secondary,
                            modifier = Modifier.size(22.dp)
                        )
                        Icon(
                            Icons.Outlined.FileDownload,
                            contentDescription = null,
                            tint = Secondary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            // ── Header ────────────────────────────────
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = "Revenue Report",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextDark
                    )
                    Text(
                        text = "Performance overview for ${state.period} • ${state.propertyName}",
                        fontSize = 13.sp,
                        color = OnSurfaceSecondary,
                        lineHeight = 18.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // ── Metric Cards ──────────────────────────
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    RevenueReportMockData.metrics.forEach { metric ->
                        val icon = when (metric.icon) {
                            "payments" -> Icons.Outlined.Payments
                            "bed" -> Icons.Outlined.Hotel
                            "chart" -> Icons.Outlined.BarChart
                            else -> Icons.Outlined.TrendingUp
                        }
                        RevenueMetricCard(
                            icon = icon,
                            label = metric.label,
                            value = metric.value,
                            change = metric.change,
                            isPositive = metric.isPositive
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // ── Monthly Earnings Chart ────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Monthly Earnings",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf("Revenue", "Guests").forEach { tab ->
                                    val isSelected = state.selectedTab == tab
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(
                                                if (isSelected) Secondary
                                                else Color.Transparent
                                            )
                                            .clickable { state.selectedTab = tab }
                                            .padding(horizontal = 12.dp, vertical = 5.dp)
                                    ) {
                                        Text(
                                            text = tab,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = if (isSelected) Color.White
                                            else OnSurfaceSecondary
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Bar Chart
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                RevenueReportMockData.monthlyEarnings.forEach { bar ->
                                    val barHeight = if (state.selectedTab == "Revenue")
                                        bar.revenueHeight else bar.guestsHeight

                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Bottom,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        // Tooltip
                                        if (bar.isHighlighted) {
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(Secondary)
                                                    .padding(
                                                        horizontal = 6.dp,
                                                        vertical = 3.dp
                                                    )
                                            ) {
                                                Text(
                                                    text = bar.highlightLabel,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                        } else {
                                            Spacer(
                                                modifier = Modifier.height(
                                                    if (bar.revenueHeight > 0.8f) 0.dp else 20.dp
                                                )
                                            )
                                        }

                                        Box(
                                            modifier = Modifier
                                                .width(16.dp)
                                                .fillMaxHeight(barHeight)
                                                .clip(
                                                    RoundedCornerShape(
                                                        topStart = 4.dp,
                                                        topEnd = 4.dp
                                                    )
                                                )
                                                .background(
                                                    if (bar.isHighlighted) Secondary
                                                    else Primary.copy(0.5f)
                                                )
                                        )

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Text(
                                            text = bar.month,
                                            fontSize = 9.sp,
                                            color = OnSurfaceSecondary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // ── Payment Methods ───────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = "Payment Methods",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        RevenueReportMockData.paymentMethods.forEach { method ->
                            val icon = when (method.icon) {
                                "card" -> Icons.Outlined.CreditCard
                                "bank" -> Icons.Outlined.AccountBalance
                                "wallet" -> Icons.Outlined.Wallet
                                else -> Icons.Outlined.MoreHoriz
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    icon,
                                    contentDescription = null,
                                    tint = OnSurfaceSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = method.name,
                                    fontSize = 14.sp,
                                    color = TextDark,
                                    modifier = Modifier.width(120.dp)
                                )

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(5.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(BackgroundLight)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(method.percentage / 100f)
                                            .fillMaxHeight()
                                            .clip(RoundedCornerShape(3.dp))
                                            .background(Primary)
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "${method.percentage}%",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextDark
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "View detailed breakdown",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Primary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { }
                                .padding(top = 4.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // ── Recent Transactions ───────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Transactions",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.clickable { }
                    ) {
                        Text(
                            text = "See All",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Secondary
                        )
                        Icon(
                            Icons.Outlined.ChevronRight,
                            contentDescription = null,
                            tint = Secondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Transaction Table Header
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                        .background(BackgroundLight)
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "GUEST",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceSecondary,
                            modifier = Modifier.weight(2f),
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "DATE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceSecondary,
                            modifier = Modifier.weight(1.2f),
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "STATUS",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceSecondary,
                            modifier = Modifier.weight(1.5f),
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "A.",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceSecondary,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }

            items(RevenueReportMockData.recentTransactions) { tx ->
                TransactionRow(transaction = tx)
            }
        }
    }
}

// ── Revenue Metric Card ───────────────────────────────────
@Composable
fun RevenueMetricCard(
    icon: ImageVector,
    label: String,
    value: String,
    change: String,
    isPositive: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Primary.copy(0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = Secondary,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Column {
                    Text(
                        text = label,
                        fontSize = 13.sp,
                        color = OnSurfaceSecondary
                    )
                    Text(
                        text = value,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextDark
                    )
                }
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        if (isPositive) Color(0xFF2E7D32).copy(0.1f)
                        else ErrorColor.copy(0.1f)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = change,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isPositive) Color(0xFF2E7D32) else ErrorColor
                )
            }
        }
    }
}

// ── Transaction Row ───────────────────────────────────────
@Composable
fun TransactionRow(transaction: RecentTransaction) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .background(Color.White)
            .padding(horizontal = 12.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Guest
            Row(
                modifier = Modifier.weight(2f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(transaction.avatarColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = transaction.initials,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Column {
                    Text(
                        text = transaction.guestName,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextDark,
                        lineHeight = 15.sp
                    )
                    Text(
                        text = "${transaction.roomType} • ${transaction.nights} Nights",
                        fontSize = 10.sp,
                        color = OnSurfaceSecondary,
                        lineHeight = 13.sp
                    )
                }
            }

            // Date
            Text(
                text = transaction.date,
                fontSize = 11.sp,
                color = OnSurfaceSecondary,
                modifier = Modifier.weight(1.2f),
                lineHeight = 14.sp
            )

            // Status
            Box(
                modifier = Modifier
                    .weight(1.5f)
                    .padding(end = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            when (transaction.status) {
                                TransactionStatus.COMPLETED -> Color(0xFF2E7D32).copy(0.1f)
                                TransactionStatus.PENDING -> Color(0xFFE65100).copy(0.1f)
                                TransactionStatus.FAILED -> ErrorColor.copy(0.1f)
                            }
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = when (transaction.status) {
                            TransactionStatus.COMPLETED -> "Completed"
                            TransactionStatus.PENDING -> "Pending"
                            TransactionStatus.FAILED -> "Failed"
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = when (transaction.status) {
                            TransactionStatus.COMPLETED -> Color(0xFF2E7D32)
                            TransactionStatus.PENDING -> Color(0xFFE65100)
                            TransactionStatus.FAILED -> ErrorColor
                        }
                    )
                }
            }

            // Amount
            Text(
                text = transaction.amount,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
        }
    }

    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 20.dp),
        color = Divider
    )
}