package com.kamerstay.app.core.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*

// ── Voyageur ──────────────────────────────────────────────
// selectedTab: 0=Home 1=Explore 2=Bookings 3=Profile  -1=none
@Composable
fun TravelerBottomNavBar(navController: NavController, selectedTab: Int) {
    NavigationBar(containerColor = LocalAppColors.current.surface, tonalElevation = 0.dp) {
        listOf(
            Triple(0, "Home",     Icons.Outlined.Home         to Icons.Filled.Home),
            Triple(1, "Explore",  Icons.Outlined.Explore      to Icons.Filled.Explore),
            Triple(2, "Bookings", Icons.Outlined.BookOnline   to Icons.Filled.BookOnline),
            Triple(3, "Profile",  Icons.Outlined.Person       to Icons.Filled.Person)
        ).forEach { (index, label, icons) ->
            val (unselected, selected) = icons
            NavigationBarItem(
                selected = selectedTab == index,
                onClick = {
                    when (index) {
                        0 -> navController.navigate(Routes.TravelerHome.route)
                        1 -> navController.navigate(Routes.HotelSearch.route)
                        2 -> navController.navigate(Routes.BookingHistory.route)
                        3 -> navController.navigate(Routes.TravelerProfile.route)
                    }
                },
                icon = {
                    Icon(
                        if (selectedTab == index) selected else unselected,
                        contentDescription = label
                    )
                },
                label = { Text(label, fontSize = 12.sp) },
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

// ── Manager ───────────────────────────────────────────────
// currentRoute: "dashboard" | "rooms" | "reservations" | "profile"
@Composable
fun ManagerBottomNavBar(navController: NavController, currentRoute: String) {
    NavigationBar(containerColor = LocalAppColors.current.surface, tonalElevation = 0.dp) {
        listOf(
            Triple("dashboard",    "Overview",     Icons.Outlined.Dashboard    to Icons.Filled.Dashboard),
            Triple("rooms",        "Rooms",        Icons.Outlined.Hotel        to Icons.Filled.Hotel),
            Triple("reservations", "Reservations", Icons.Outlined.BookOnline   to Icons.Filled.BookOnline),
            Triple("profile",      "Profile",      Icons.Outlined.Person       to Icons.Filled.Person)
        ).forEach { (route, label, icons) ->
            val (unselected, selected) = icons
            NavigationBarItem(
                selected = currentRoute == route,
                onClick = {
                    when (route) {
                        "dashboard"    -> navController.navigate(Routes.ManagerDashboard.route)
                        "rooms"        -> navController.navigate(Routes.RoomManagement.createRoute("1"))
                        "reservations" -> navController.navigate(Routes.Reservations.route)
                        "profile"      -> navController.navigate(Routes.ManagerProfile.route)
                    }
                },
                icon = {
                    Icon(
                        if (currentRoute == route) selected else unselected,
                        contentDescription = label
                    )
                },
                label = { Text(label, fontSize = 12.sp) },
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