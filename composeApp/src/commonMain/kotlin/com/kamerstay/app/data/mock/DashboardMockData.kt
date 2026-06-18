package com.kamerstay.app.data.mock

import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.model.RecentActivity

object DashboardMockData {

    val recentActivities = listOf(
        RecentActivity(
            title = "Arrivée : Félicien Abanda",
            room = "Chambre 402 • Suite Deluxe",
            time = "10:45",
            badge = "Actif",
            badgeColor = Primary.copy(0.15f),
            badgeTextColor = Secondary
        ),
        RecentActivity(
            title = "Nouvelle résa : Christine Mbeye",
            room = "Chambre 105 • Junior Suite • 3 Nuits",
            time = "09:12",
            badge = "Confirmé",
            badgeColor = OnSurfaceSecondary.copy(0.15f),
            badgeTextColor = TextDark
        ),
        RecentActivity(
            title = "Départ : René Tchouaket",
            room = "Chambre 218 • Standard Double",
            time = "08:30",
            badge = "Parti",
            badgeColor = OnSurfaceSecondary.copy(0.1f),
            badgeTextColor = OnSurfaceSecondary
        ),
    )

    val revenueBarHeights = listOf(0.4f, 0.55f, 0.5f, 0.7f, 0.65f, 0.8f, 1f)
}