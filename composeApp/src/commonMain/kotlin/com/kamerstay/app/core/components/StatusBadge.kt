package com.kamerstay.app.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.model.enums.BookingStatus
import com.kamerstay.app.model.enums.RoomStatus

@Composable
fun RoomStatusBadge(status: RoomStatus) {
    val (bg, text, label) = when (status) {
        RoomStatus.AVAILABLE -> Triple(
            StatusAvailable.copy(0.15f), StatusAvailable, "Disponible"
        )
        RoomStatus.RESERVED -> Triple(
            StatusReserved.copy(0.15f), StatusReserved, "Réservée"
        )
        RoomStatus.OCCUPIED -> Triple(
            StatusOccupied.copy(0.15f), StatusOccupied, "Occupée"
        )
        RoomStatus.CLEANING -> Triple(
            StatusCleaning.copy(0.15f), StatusCleaning, "Nettoyage"
        )
    }
    StatusChip(label = label, backgroundColor = bg, textColor = text)
}

@Composable
fun BookingStatusBadge(status: BookingStatus) {
    val (bg, text, label) = when (status) {
        BookingStatus.PENDING -> Triple(
            StatusPending.copy(0.15f), StatusPending, "En attente"
        )
        BookingStatus.CONFIRMED -> Triple(
            StatusConfirmed.copy(0.15f), StatusConfirmed, "Confirmée"
        )
        BookingStatus.CANCELLED -> Triple(
            StatusCancelled.copy(0.15f), StatusCancelled, "Annulée"
        )
        BookingStatus.COMPLETED -> Triple(
            Secondary.copy(0.15f), Secondary, "Terminée"  // ← DeepBlue
        )
        BookingStatus.CHECKED_IN -> Triple(
            Primary.copy(0.15f), Primary, "Enregistré"    // ← turquoise
        )
        BookingStatus.CHECKED_OUT -> Triple(
            Outline.copy(0.15f), Outline, "Parti"
        )
    }
    StatusChip(label = label, backgroundColor = bg, textColor = text)
}

@Composable
fun VerifiedBadge() {
    StatusChip(
        label = "✓ Vérifié",
        backgroundColor = Primary.copy(alpha = 0.15f),  // ← turquoise
        textColor = OnPrimary                            // ← #002021
    )
}

@Composable
private fun StatusChip(
    label: String,
    backgroundColor: Color,
    textColor: Color
) {
    Text(
        text = label,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = textColor,
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}