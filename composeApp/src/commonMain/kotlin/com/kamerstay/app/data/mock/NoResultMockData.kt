package com.kamerstay.app.data.mock

import com.kamerstay.app.data.model.SearchSuggestion

object NoResultMockData {
    val suggestions = listOf(
        SearchSuggestion(
            id = "1",
            icon = "calendar",
            title = "Check Dates",
            description = "Try searching for different dates as some properties might be fully booked."
        ),
        SearchSuggestion(
            id = "2",
            icon = "payments",
            title = "Adjust Price",
            description = "Broaden your budget range to see a wider variety of exclusive listings."
        ),
        SearchSuggestion(
            id = "3",
            icon = "map",
            title = "Expand Location",
            description = "Try searching in nearby cities like Yaoundé, Kribi or Limbé."
        )
    )
}