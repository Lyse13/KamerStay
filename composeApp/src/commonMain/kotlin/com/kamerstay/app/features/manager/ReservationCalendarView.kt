package com.kamerstay.app.features.manager

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.model.CalendarEntry
import kotlinx.datetime.LocalDate

private val monthNames = listOf(
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
)

private val dayLabels = listOf("M", "T", "W", "T", "F", "S", "S")

private fun daysInMonth(month: Int, year: Int): Int = when (month) {
    1, 3, 5, 7, 8, 10, 12 -> 31
    4, 6, 9, 11 -> 30
    2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
    else -> 30
}

private fun occupancyForDay(day: Int, entries: List<CalendarEntry>, month: Int, year: Int): Int =
    entries.count { it.month == month && it.year == year && day in it.checkInDay..it.checkOutDay }

private fun entriesForDay(day: Int, entries: List<CalendarEntry>, month: Int, year: Int): List<CalendarEntry> =
    entries.filter { it.month == month && it.year == year && day in it.checkInDay..it.checkOutDay }

@Composable
fun ReservationCalendarView(
    entries: List<CalendarEntry>,
    totalRooms: Int,
    month: Int,
    year: Int,
    selectedDay: Int?,
    onDaySelected: (Int?) -> Unit,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    modifier: Modifier = Modifier
) {
    val monthName = monthNames[month - 1]
    val daysCount = daysInMonth(month, year)
    // ordinal: MONDAY=0 … SUNDAY=6 → perfect offset for a Mon-first grid
    val firstDayOffset = remember(month, year) {
        LocalDate(year, month, 1).dayOfWeek.ordinal
    }

    val avgOccupancyPercent = remember(entries, month, year) {
        val totalOccupied = (1..daysCount).sumOf { day ->
            occupancyForDay(day, entries, month, year)
        }
        val capacity = daysCount * totalRooms
        if (capacity > 0) (totalOccupied * 100 / capacity) else 0
    }

    Column(modifier = modifier.fillMaxWidth()) {

        // ── Month navigation ──────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onPrevMonth) {
                Icon(Icons.Outlined.ChevronLeft, contentDescription = "Previous month", tint = Secondary)
            }
            Text(
                text = "$monthName $year",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = LocalAppColors.current.textPrimary
            )
            IconButton(onClick = onNextMonth) {
                Icon(Icons.Outlined.ChevronRight, contentDescription = "Next month", tint = Secondary)
            }
        }

        // ── Occupancy summary bar ─────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(LocalAppColors.current.surface)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Avg. Monthly Occupancy",
                        fontSize = 13.sp,
                        color = OnSurfaceSecondary
                    )
                    Text(
                        "$avgOccupancyPercent%",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = when {
                            avgOccupancyPercent >= 70 -> Secondary
                            avgOccupancyPercent >= 40 -> Primary
                            else -> OnSurfaceSecondary
                        }
                    )
                }
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { avgOccupancyPercent / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = when {
                        avgOccupancyPercent >= 70 -> Secondary
                        avgOccupancyPercent >= 40 -> Primary
                        else -> OnSurfaceSecondary
                    },
                    trackColor = Divider
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // ── Day-of-week headers ───────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            dayLabels.forEach { label ->
                Text(
                    text = label,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurfaceSecondary
                )
            }
        }

        Spacer(Modifier.height(6.dp))

        // ── Calendar grid ─────────────────────────────────
        val totalCells = firstDayOffset + daysCount
        val rows = (totalCells + 6) / 7

        for (row in 0 until rows) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                for (col in 0 until 7) {
                    val cellIndex = row * 7 + col
                    val day = cellIndex - firstDayOffset + 1
                    if (day < 1 || day > daysCount) {
                        Box(modifier = Modifier.weight(1f).aspectRatio(1f))
                    } else {
                        val occ = occupancyForDay(day, entries, month, year)
                        CalendarDayCell(
                            day = day,
                            occupancy = occ,
                            totalRooms = totalRooms,
                            isSelected = day == selectedDay,
                            modifier = Modifier.weight(1f),
                            onClick = { onDaySelected(if (day == selectedDay) null else day) }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // ── Legend ────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OccupancyLegendItem(color = Primary.copy(0.35f), label = "Low")
            OccupancyLegendItem(color = Primary, label = "Medium")
            OccupancyLegendItem(color = Secondary, label = "High")
            OccupancyLegendItem(color = ErrorColor, label = "Full")
        }

        // ── Selected day panel ────────────────────────────
        selectedDay?.let { day ->
            val dayEntries = entriesForDay(day, entries, month, year)
            SelectedDayPanel(
                day = day,
                monthName = monthName,
                entries = dayEntries,
                totalRooms = totalRooms
            )
        }
    }
}

// ── Day cell ──────────────────────────────────────────────────
@Composable
private fun CalendarDayCell(
    day: Int,
    occupancy: Int,
    totalRooms: Int,
    isSelected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    val ratio = if (totalRooms > 0) (occupancy.toFloat() / totalRooms).coerceIn(0f, 1f) else 0f
    val barColor = when {
        occupancy == 0 -> Color.Transparent
        ratio < 0.35f -> Primary.copy(0.35f)
        ratio < 0.65f -> Primary
        ratio < 0.9f  -> Secondary
        else          -> ErrorColor
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) Primary else Color.Transparent)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$day",
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color.White else LocalAppColors.current.textPrimary
            )
            Spacer(Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.55f)
                    .height(3.dp)
                    .clip(RoundedCornerShape(1.5.dp))
                    .background(
                        if (isSelected && occupancy > 0) Color.White.copy(0.6f) else barColor
                    )
            )
        }
    }
}

// ── Legend item ───────────────────────────────────────────────
@Composable
private fun OccupancyLegendItem(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(label, fontSize = 12.sp, color = OnSurfaceSecondary)
    }
}

// ── Selected day panel ────────────────────────────────────────
@Composable
private fun SelectedDayPanel(
    day: Int,
    monthName: String,
    entries: List<CalendarEntry>,
    totalRooms: Int
) {
    Spacer(Modifier.height(16.dp))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$monthName $day",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = LocalAppColors.current.textPrimary
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (entries.isEmpty()) LocalAppColors.current.surface
                        else Primary.copy(0.12f)
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (entries.isEmpty()) "Available" else "${entries.size} / $totalRooms occupied",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (entries.isEmpty()) OnSurfaceSecondary else Primary
                )
            }
        }

        if (entries.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(LocalAppColors.current.surface)
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        Icons.Outlined.CalendarMonth,
                        contentDescription = null,
                        tint = Primary.copy(0.5f),
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        "All rooms available on this day",
                        fontSize = 14.sp,
                        color = OnSurfaceSecondary
                    )
                }
            }
        } else {
            entries.forEach { entry -> DayPanelReservationRow(entry) }
        }
    }

    Spacer(Modifier.height(20.dp))
}

// ── Reservation row inside day panel ─────────────────────────
@Composable
private fun DayPanelReservationRow(entry: CalendarEntry) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(LocalAppColors.current.surface)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(entry.accentColor.copy(0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                entry.guestInitials,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = entry.accentColor
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                entry.guestName,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = LocalAppColors.current.textPrimary,
                maxLines = 1
            )
            Text(
                entry.roomType,
                fontSize = 12.sp,
                color = OnSurfaceSecondary
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                "Oct ${entry.checkInDay}",
                fontSize = 12.sp,
                color = OnSurfaceSecondary
            )
            Text(
                "→ Oct ${entry.checkOutDay}",
                fontSize = 12.sp,
                color = OnSurfaceSecondary
            )
        }
    }
}