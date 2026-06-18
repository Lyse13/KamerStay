package com.kamerstay.app.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kamerstay.app.core.theme.*
import kotlinx.datetime.*
import kotlin.time.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerBottomSheet(
    initialCheckIn: LocalDate? = null,
    initialCheckOut: LocalDate? = null,
    onDismiss: () -> Unit,
    onConfirm: (checkIn: LocalDate, checkOut: LocalDate) -> Unit
) {
    // Fix 1: use kotl3#in.time.Clock + toLocalDateTime instead of deprecated Clock.System.todayIn
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    var displayedMonth by remember {
        mutableStateOf(
            initialCheckIn?.let { YearMonth(it.year, it.month) }
                ?: YearMonth(today.year, today.month)
        )
    }
    var checkIn by remember { mutableStateOf(initialCheckIn) }
    var checkOut by remember { mutableStateOf(initialCheckOut) }
    var selectingEnd by remember { mutableStateOf(initialCheckIn != null && initialCheckOut == null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = LocalAppColors.current.surface,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            // ── Header ────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Select Dates",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalAppColors.current.textPrimary
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Outlined.Close, contentDescription = "Close", tint = OnSurfaceSecondary)
                }
            }

            // ── Selection hint ────────────────────────
            Text(
                text = if (!selectingEnd) "Tap to select check-in date"
                       else "Tap to select check-out date",
                fontSize = 13.sp,
                color = Primary,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Month navigation ──────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val currentMonth = YearMonth(today.year, today.month)
                IconButton(
                    onClick = { displayedMonth = displayedMonth.minusMonths(1) },
                    enabled = displayedMonth > currentMonth
                ) {
                    Icon(
                        Icons.Outlined.ChevronLeft,
                        contentDescription = "Previous month",
                        tint = if (displayedMonth > currentMonth)
                            Secondary else OnSurfaceSecondary.copy(0.3f)
                    )
                }
                Text(
                    text = "${displayedMonth.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${displayedMonth.year}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Secondary
                )
                IconButton(onClick = { displayedMonth = displayedMonth.plusMonths(1) }) {
                    Icon(Icons.Outlined.ChevronRight, contentDescription = "Next month", tint = Secondary)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ── Day of week headers ───────────────────
            Row(modifier = Modifier.fillMaxWidth()) {
                listOf("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su").forEach { day ->
                    Text(
                        text = day,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OnSurfaceSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ── Calendar grid ─────────────────────────
            val firstDayOfMonth = LocalDate(displayedMonth.year, displayedMonth.month, 1)
            val startOffset = firstDayOfMonth.dayOfWeek.isoDayNumber - 1
            // Fix 2: replace Month.length() with custom helper
            val daysInMonth = daysInMonth(displayedMonth.year, displayedMonth.month)
            val rows = (startOffset + daysInMonth + 6) / 7

            Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                repeat(rows) { row ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        repeat(7) { col ->
                            val cellIndex = row * 7 + col
                            val dayNumber = cellIndex - startOffset + 1

                            if (dayNumber !in 1..daysInMonth) {
                                Box(modifier = Modifier.weight(1f).height(44.dp))
                            } else {
                                val date = LocalDate(displayedMonth.year, displayedMonth.month, dayNumber)
                                val isPast = date < today
                                val isCheckIn = checkIn == date
                                val isCheckOut = checkOut == date
                                val isInRange = checkIn != null && checkOut != null
                                        && date > checkIn!! && date < checkOut!!
                                val isToday = date == today
                                val isRangeStart = isCheckIn && checkOut != null
                                val isRangeEnd = isCheckOut && checkIn != null

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp)
                                        .then(
                                            if (isInRange) Modifier.background(Primary.copy(0.12f))
                                            else if (isRangeStart) Modifier.background(
                                                Primary.copy(0.12f),
                                                RoundedCornerShape(topStart = 22.dp, bottomStart = 22.dp)
                                            )
                                            else if (isRangeEnd) Modifier.background(
                                                Primary.copy(0.12f),
                                                RoundedCornerShape(topEnd = 22.dp, bottomEnd = 22.dp)
                                            )
                                            else Modifier
                                        )
                                        .then(
                                            if (!isPast) Modifier.clickable {
                                                if (!selectingEnd) {
                                                    checkIn = date
                                                    checkOut = null
                                                    selectingEnd = true
                                                } else {
                                                    if (date <= checkIn!!) {
                                                        checkIn = date
                                                        checkOut = null
                                                    } else {
                                                        checkOut = date
                                                        selectingEnd = false
                                                    }
                                                }
                                            } else Modifier
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .then(
                                                when {
                                                    isCheckIn || isCheckOut -> Modifier
                                                        .clip(CircleShape)
                                                        .background(Secondary)
                                                    isToday -> Modifier
                                                        .clip(CircleShape)
                                                        .border(1.5.dp, Primary, CircleShape)
                                                    else -> Modifier
                                                }
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = dayNumber.toString(),
                                            fontSize = 14.sp,
                                            fontWeight = if (isCheckIn || isCheckOut) FontWeight.Bold
                                                         else FontWeight.Normal,
                                            color = when {
                                                isCheckIn || isCheckOut -> Color.White
                                                isPast -> OnSurfaceSecondary.copy(0.3f)
                                                isInRange -> Secondary
                                                else -> LocalAppColors.current.textPrimary
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = Divider.copy(0.5f))
            Spacer(modifier = Modifier.height(16.dp))

            // ── Selected summary ──────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DateSummaryBox(
                    label = "Check-in",
                    date = checkIn?.let { formatShort(it) } ?: "—"
                )

                if (checkIn != null && checkOut != null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        val nights = checkIn!!.daysUntil(checkOut!!)
                        Text(
                            text = nights.toString(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Primary
                        )
                        Text(
                            text = if (nights == 1) "night" else "nights",
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary
                        )
                    }
                } else {
                    Box(modifier = Modifier.size(40.dp))
                }

                DateSummaryBox(
                    label = "Check-out",
                    date = checkOut?.let { formatShort(it) } ?: "—",
                    alignEnd = true
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Confirm button ────────────────────────
            Button(
                onClick = {
                    val ci = checkIn
                    val co = checkOut
                    if (ci != null && co != null) onConfirm(ci, co)
                },
                enabled = checkIn != null && checkOut != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Secondary,
                    disabledContainerColor = OnSurfaceSecondary.copy(0.2f)
                )
            ) {
                Text(
                    text = "Confirm Dates",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun DateSummaryBox(label: String, date: String, alignEnd: Boolean = false) {
    Column(horizontalAlignment = if (alignEnd) Alignment.End else Alignment.Start) {
        Text(text = label, fontSize = 12.sp, color = OnSurfaceSecondary)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = date, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = LocalAppColors.current.textPrimary)
    }
}

// ── Helpers ───────────────────────────────────────────────

private data class YearMonth(val year: Int, val month: Month) : Comparable<YearMonth> {
    override fun compareTo(other: YearMonth): Int =
        compareValuesBy(this, other, { it.year }, { it.month.ordinal })

    fun plusMonths(n: Int): YearMonth {
        // Use Kotlin floorDiv for correct negative month handling (KMP-safe, no Math)
        val total = month.ordinal + n
        val newMonthOrdinal = ((total % 12) + 12) % 12
        val yearOffset = total.floorDiv(12)
        return YearMonth(year + yearOffset, Month.entries[newMonthOrdinal])
    }

    fun minusMonths(n: Int) = plusMonths(-n)
}

// Fix 2: replaces Month.length() — multiplatform-safe days-in-month
private fun daysInMonth(year: Int, month: Month): Int = when (month) {
    Month.JANUARY, Month.MARCH, Month.MAY, Month.JULY,
    Month.AUGUST, Month.OCTOBER, Month.DECEMBER -> 31
    Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> 30
    Month.FEBRUARY -> if (isLeapYear(year)) 29 else 28
}

private fun isLeapYear(year: Int) = (year % 4 == 0 && year % 100 != 0) || year % 400 == 0

// Fix 4: dayOfMonth → day
private fun formatShort(date: LocalDate): String {
    val day = date.day
    val month = date.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
    return "$day $month"
}
