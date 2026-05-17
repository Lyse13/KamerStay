package com.kamerstay.app.features.manager

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*

@Composable
fun ManagerDashboardScreen(navController: NavController) {

    Scaffold(
        containerColor = WarmIvory,
        bottomBar = { ManagerBottomNav(navController, currentRoute = "dashboard") }
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
                        IconButton(onClick = { }) {
                            Icon(Icons.Filled.Menu, contentDescription = null, tint = OnSurface)
                        }
                        Text(
                            text = "Hotel Manager",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepEmerald
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(PrimaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Outlined.Person,
                            contentDescription = null,
                            tint = DeepEmerald,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            // ── Greeting ──────────────────────────────
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = "Mbolo, Manager\nDouala",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = OnSurface,
                        lineHeight = 34.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Here is what's happening at Akwa Palace today.",
                        fontSize = 14.sp,
                        color = OnSurfaceVariant,
                        lineHeight = 20.sp
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // ── Stats Grid ────────────────────────────
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    // Row 1: Capacity + Live
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            icon = Icons.Outlined.Hotel,
                            label = "CAPACITY",
                            value = "124",
                            subtitle = "Total Rooms",
                            containerColor = Color.White,
                            contentColor = OnSurface,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            icon = Icons.Outlined.Wifi,
                            label = "LIVE",
                            value = "82",
                            subtitle = "Active Bookings",
                            containerColor = Color.White,
                            contentColor = OnSurface,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Row 2: Occupancy + Financials
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            icon = Icons.Outlined.BarChart,
                            label = "TREND",
                            value = "85%",
                            subtitle = "Occupancy Rate",
                            containerColor = DeepEmerald,
                            contentColor = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            icon = Icons.Outlined.Payments,
                            label = "FINANCIALS",
                            value = "1.2M XAF",
                            subtitle = "Today's Revenue",
                            containerColor = WarmAmber,
                            contentColor = OnSurface,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(28.dp))
            }

            // ── Upcoming Arrivals ─────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Upcoming Arrivals",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface
                    )
                    Text(
                        text = "View All",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = WarmAmber,
                        modifier = Modifier.clickable {
                            navController.navigate(Routes.Reservations.route)
                        }
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Arrivals list
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ArrivalCard(
                        icon = Icons.Outlined.Person,
                        name = "Samuel Eto'o",
                        detail = "Suite 402 • 2 Nights",
                        badge = "EXPECTED 14:00",
                        badgeColor = WarmAmber.copy(alpha = 0.15f),
                        badgeTextColor = Color(0xFF8B6914),
                        tag = "Premium Guest"
                    )
                    ArrivalCard(
                        icon = Icons.Outlined.FamilyRestroom,
                        name = "Nkoa Family",
                        detail = "Standard Room • 3 Nights",
                        badge = "EXPECTED\n16:30",
                        badgeColor = SurfaceVariant,
                        badgeTextColor = OnSurfaceVariant,
                        tag = "Direct Booking"
                    )
                    ArrivalCard(
                        icon = Icons.Outlined.Work,
                        name = "Moussa Ibrahim",
                        detail = "Deluxe King • 1 Night",
                        badge = "CONFIRMED",
                        badgeColor = StatusConfirmed.copy(alpha = 0.12f),
                        badgeTextColor = StatusConfirmed,
                        tag = "Corporate"
                    )
                }
                Spacer(modifier = Modifier.height(28.dp))
            }

            // ── Quick Actions ─────────────────────────
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = "Quick Actions",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Add Room
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(DeepEmerald)
                            .clickable {
                                navController.navigate(Routes.RoomManagement.route)
                            }
                            .padding(horizontal = 20.dp, vertical = 18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Outlined.AddCircleOutline,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Add Room",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                            Icon(
                                Icons.Filled.ChevronRight,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Check-in Guest
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .border(1.dp, OutlineVariant, RoundedCornerShape(12.dp))
                            .clickable {
                                navController.navigate(Routes.Reservations.route)
                            }
                            .padding(horizontal = 20.dp, vertical = 18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Outlined.PersonAdd,
                                    contentDescription = null,
                                    tint = OnSurface,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Check-in Guest",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = OnSurface
                                )
                            }
                            Icon(
                                Icons.Filled.ChevronRight,
                                contentDescription = null,
                                tint = OnSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Maintenance Note
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceVariant)
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                Icons.Outlined.Info,
                                contentDescription = null,
                                tint = OnSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "MAINTENANCE NOTE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OnSurfaceVariant,
                                    letterSpacing = 0.8.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "3 rooms on the 2nd floor require AC maintenance before the weekend rush.",
                                    fontSize = 13.sp,
                                    color = OnSurfaceVariant,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ── Stat Card ─────────────────────────────────────────────
@Composable
fun StatCard(
    icon: ImageVector,
    label: String,
    value: String,
    subtitle: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(containerColor)
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = contentColor.copy(alpha = 0.7f),
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = label,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = contentColor.copy(alpha = 0.7f),
                    letterSpacing = 0.8.sp
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = value,
                fontSize = if (value.length > 6) 20.sp else 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = contentColor
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = contentColor.copy(alpha = 0.7f)
            )
        }
    }
}

// ── Arrival Card ──────────────────────────────────────────
@Composable
fun ArrivalCard(
    icon: ImageVector,
    name: String,
    detail: String,
    badge: String,
    badgeColor: Color,
    badgeTextColor: Color,
    tag: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(SurfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = OnSurfaceVariant,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = OnSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = detail,
                    fontSize = 12.sp,
                    color = OnSurfaceVariant,
                    maxLines = 2
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(horizontalAlignment = Alignment.End) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(badgeColor)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = badge,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeTextColor,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = tag,
                    fontSize = 11.sp,
                    color = OnSurfaceVariant
                )
            }
        }
    }
}

// ── Manager Bottom Nav ────────────────────────────────────
@Composable
fun ManagerBottomNav(
    navController: NavController,
    currentRoute: String
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp
    ) {
        NavigationBarItem(
            selected = currentRoute == "dashboard",
            onClick = { navController.navigate(Routes.ManagerDashboard.route) },
            icon = {
                Icon(
                    if (currentRoute == "dashboard") Icons.Filled.Dashboard
                    else Icons.Outlined.Dashboard,
                    contentDescription = "Dashboard"
                )
            },
            label = { Text("Dashboard", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = DeepEmerald,
                selectedTextColor = DeepEmerald,
                indicatorColor = PrimaryContainer,
                unselectedIconColor = OnSurfaceVariant,
                unselectedTextColor = OnSurfaceVariant
            )
        )
        NavigationBarItem(
            selected = currentRoute == "rooms",
            onClick = { navController.navigate(Routes.RoomManagement.route) },
            icon = {
                Icon(
                    if (currentRoute == "rooms") Icons.Filled.Hotel
                    else Icons.Outlined.Hotel,
                    contentDescription = "Rooms"
                )
            },
            label = { Text("Rooms", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = DeepEmerald,
                selectedTextColor = DeepEmerald,
                indicatorColor = PrimaryContainer,
                unselectedIconColor = OnSurfaceVariant,
                unselectedTextColor = OnSurfaceVariant
            )
        )
        NavigationBarItem(
            selected = currentRoute == "reservations",
            onClick = { navController.navigate(Routes.Reservations.route) },
            icon = {
                Icon(
                    if (currentRoute == "reservations") Icons.Filled.BookOnline
                    else Icons.Outlined.BookOnline,
                    contentDescription = "Bookings"
                )
            },
            label = { Text("Bookings", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = DeepEmerald,
                selectedTextColor = DeepEmerald,
                indicatorColor = PrimaryContainer,
                unselectedIconColor = OnSurfaceVariant,
                unselectedTextColor = OnSurfaceVariant
            )
        )
        NavigationBarItem(
            selected = currentRoute == "profile",
            onClick = { navController.navigate(Routes.ManagerProfile.route) },
            icon = {
                Icon(
                    if (currentRoute == "profile") Icons.Filled.Person
                    else Icons.Outlined.Person,
                    contentDescription = "Profile"
                )
            },
            label = { Text("Profile", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = DeepEmerald,
                selectedTextColor = DeepEmerald,
                indicatorColor = PrimaryContainer,
                unselectedIconColor = OnSurfaceVariant,
                unselectedTextColor = OnSurfaceVariant
            )
        )
    }
}