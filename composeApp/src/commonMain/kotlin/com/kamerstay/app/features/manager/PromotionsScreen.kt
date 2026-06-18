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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.NavigationState
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.mock.PromotionsMockData
import com.kamerstay.app.data.model.DiscountType
import com.kamerstay.app.data.model.Promotion
import com.kamerstay.app.data.model.PromotionStatus
import com.kamerstay.app.data.model.PromotionType

@Composable
fun PromotionsScreen(navController: NavController) {

    var selectedTab by remember { mutableStateOf(PromotionStatus.ACTIVE) }
    val promotions = remember { PromotionsMockData.promotions }

    val tabs = listOf(
        PromotionStatus.ACTIVE to "Active",
        PromotionStatus.SCHEDULED to "Scheduled",
        PromotionStatus.EXPIRED to "Expired"
    )

    val currentList = promotions.filter { it.status == selectedTab }

    val activeCount = promotions.count { it.status == PromotionStatus.ACTIVE }
    val totalUsed = promotions.sumOf { it.usedCount }

    Scaffold(
        containerColor = LocalAppColors.current.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    NavigationState.selectedPromoId = ""
                    navController.navigate(Routes.AddEditPromotion.route)
                },
                containerColor = Secondary,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Outlined.Add, contentDescription = "New promotion")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            // ── Top Bar ───────────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Secondary)
                        }
                        Text(
                            "Promotions",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = LocalAppColors.current.textPrimary
                        )
                    }
                }
            }

            // ── Summary stats ─────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PromoStatCard(
                        modifier = Modifier.weight(1f),
                        value = "$activeCount",
                        label = "Active",
                        color = Secondary
                    )
                    PromoStatCard(
                        modifier = Modifier.weight(1f),
                        value = "$totalUsed",
                        label = "Total Uses",
                        color = Primary
                    )
                    PromoStatCard(
                        modifier = Modifier.weight(1f),
                        value = "${promotions.size}",
                        label = "All Promos",
                        color = OnSurfaceSecondary
                    )
                }
                Spacer(Modifier.height(20.dp))
            }

            // ── Tabs ──────────────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(LocalAppColors.current.surface)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    tabs.forEach { (status, label) ->
                        val isSelected = selectedTab == status
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) Secondary else Color.Transparent)
                                .clickable { selectedTab = status }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else OnSurfaceSecondary
                                )
                                val count = promotions.count { it.status == status }
                                if (count > 0) {
                                    Box(
                                        modifier = Modifier
                                            .size(18.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (isSelected) Color.White.copy(0.25f)
                                                else Primary.copy(0.15f)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            "$count",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.White else Primary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            // ── Promotion cards ───────────────────────────
            if (currentList.isEmpty()) {
                item { PromoEmptyState(selectedTab) }
            } else {
                items(currentList, key = { it.id }) { promo ->
                    PromotionCard(
                        promotion = promo,
                        onEdit = {
                            NavigationState.selectedPromoId = promo.id
                            navController.navigate(Routes.AddEditPromotion.route)
                        },
                        onDelete = { promotions.remove(promo) }
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}

// ── Stat card ─────────────────────────────────────────────────
@Composable
private fun PromoStatCard(
    modifier: Modifier,
    value: String,
    label: String,
    color: Color
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(LocalAppColors.current.surface)
            .padding(14.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = color)
            Text(label, fontSize = 12.sp, color = OnSurfaceSecondary, textAlign = TextAlign.Center)
        }
    }
}

// ── Promotion card ────────────────────────────────────────────
@Composable
fun PromotionCard(
    promotion: Promotion,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor = LocalAppColors.current.surface,
            shape = RoundedCornerShape(16.dp),
            title = { Text("Delete Promotion?", fontWeight = FontWeight.Bold, color = LocalAppColors.current.textPrimary) },
            text = { Text("\"${promotion.name}\" will be permanently deleted.", color = OnSurfaceSecondary) },
            confirmButton = {
                Button(
                    onClick = { showDeleteDialog = false; onDelete() },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorColor),
                    shape = RoundedCornerShape(10.dp)
                ) { Text("Delete", color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel", color = Secondary)
                }
            }
        )
    }

    val typeColor = when (promotion.type) {
        PromotionType.PROMO_CODE -> Color(0xFF7B1FA2)
        PromotionType.SEASONAL -> Color(0xFF1976D2)
        PromotionType.EARLY_BIRD -> Color(0xFF388E3C)
        PromotionType.LAST_MINUTE -> Color(0xFFF57C00)
    }

    val statusColor = when (promotion.status) {
        PromotionStatus.ACTIVE -> Secondary
        PromotionStatus.SCHEDULED -> Primary
        PromotionStatus.EXPIRED -> OnSurfaceSecondary
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(LocalAppColors.current.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // ── Header row ────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    // Status + type chips
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        PromoBadge(
                            text = promotion.status.name.lowercase()
                                .replaceFirstChar { it.uppercase() },
                            color = statusColor
                        )
                        PromoBadge(text = promotion.type.label, color = typeColor)
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        promotion.name,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = LocalAppColors.current.textPrimary
                    )
                }

                // Discount value badge (prominent)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            when (promotion.discountType) {
                                DiscountType.PERCENTAGE -> Secondary.copy(0.12f)
                                DiscountType.FIXED_AMOUNT -> Primary.copy(0.12f)
                            }
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = promotion.discountLabel,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = when (promotion.discountType) {
                            DiscountType.PERCENTAGE -> Secondary
                            DiscountType.FIXED_AMOUNT -> Primary
                        }
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // ── Code pill ─────────────────────────────
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Outlined.ConfirmationNumber,
                    contentDescription = null,
                    tint = OnSurfaceSecondary,
                    modifier = Modifier.size(14.dp)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .border(1.dp, Divider, RoundedCornerShape(6.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        promotion.code,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = LocalAppColors.current.textPrimary,
                        letterSpacing = 1.sp
                    )
                }
                Spacer(Modifier.width(4.dp))
                Icon(
                    Icons.Outlined.ContentCopy,
                    contentDescription = "Copy",
                    tint = OnSurfaceSecondary.copy(0.6f),
                    modifier = Modifier.size(14.dp)
                )
            }

            Spacer(Modifier.height(10.dp))
            HorizontalDivider(color = Divider)
            Spacer(Modifier.height(10.dp))

            // ── Details grid ──────────────────────────
            Row(modifier = Modifier.fillMaxWidth()) {
                PromoInfoItem(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Outlined.CalendarMonth,
                    label = "Validity",
                    value = "${promotion.startDate}\n→ ${promotion.endDate}"
                )
                PromoInfoItem(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Outlined.Hotel,
                    label = "Applies to",
                    value = promotion.appliesTo
                )
            }

            // ── Usage bar ─────────────────────────────
            if (promotion.maxUsages != null) {
                Spacer(Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Usage",
                        fontSize = 12.sp,
                        color = OnSurfaceSecondary
                    )
                    Text(
                        promotion.usageLabel,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = LocalAppColors.current.textPrimary
                    )
                }
                Spacer(Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { promotion.usageRatio },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = when {
                        promotion.usageRatio >= 0.9f -> ErrorColor
                        promotion.usageRatio >= 0.6f -> Color(0xFFF57C00)
                        else -> Primary
                    },
                    trackColor = Divider
                )
            } else {
                Spacer(Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(Icons.Outlined.AllInclusive, contentDescription = null, tint = Primary, modifier = Modifier.size(14.dp))
                    Text(promotion.usageLabel, fontSize = 12.sp, color = OnSurfaceSecondary)
                }
            }

            Spacer(Modifier.height(14.dp))

            // ── Action buttons ────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (promotion.status != PromotionStatus.EXPIRED) {
                    OutlinedButton(
                        onClick = onEdit,
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Secondary),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Secondary),
                        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp)
                    ) {
                        Icon(Icons.Outlined.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Edit", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(Modifier.width(8.dp))
                }
                OutlinedButton(
                    onClick = { showDeleteDialog = true },
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ErrorColor.copy(0.5f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorColor),
                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp)
                ) {
                    Icon(Icons.Outlined.DeleteOutline, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Delete", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

// ── Empty state ───────────────────────────────────────────────
@Composable
private fun PromoEmptyState(status: PromotionStatus) {
    val (icon, message) = when (status) {
        PromotionStatus.ACTIVE -> Icons.Outlined.LocalOffer to "No active promotions.\nTap + to create one."
        PromotionStatus.SCHEDULED -> Icons.Outlined.Schedule to "No scheduled promotions.\nPlan ahead with future deals."
        PromotionStatus.EXPIRED -> Icons.Outlined.History to "No expired promotions yet."
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(Primary.copy(0.08f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = Primary.copy(0.5f), modifier = Modifier.size(34.dp))
        }
        Spacer(Modifier.height(16.dp))
        Text(
            message,
            fontSize = 14.sp,
            color = OnSurfaceSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
    }
}

// ── Small helpers ─────────────────────────────────────────────
@Composable
private fun PromoBadge(text: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(0.12f))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(text, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
private fun PromoInfoItem(
    modifier: Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(icon, contentDescription = null, tint = OnSurfaceSecondary, modifier = Modifier.size(14.dp).padding(top = 2.dp))
        Column {
            Text(label, fontSize = 10.sp, color = OnSurfaceSecondary, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
            Text(value, fontSize = 13.sp, color = LocalAppColors.current.textPrimary, lineHeight = 18.sp)
        }
    }
}