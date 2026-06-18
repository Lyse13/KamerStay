package com.kamerstay.app.features.manager

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.components.authTextFieldColors
import com.kamerstay.app.core.navigation.NavigationState
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.mock.PromotionsMockData
import com.kamerstay.app.data.model.DiscountType
import com.kamerstay.app.data.model.Promotion
import com.kamerstay.app.data.model.PromotionStatus
import com.kamerstay.app.data.model.PromotionType
import kotlin.random.Random

@Composable
fun AddEditPromotionScreen(navController: NavController) {

    val promoId = NavigationState.selectedPromoId
    val existing = if (promoId.isNotBlank()) PromotionsMockData.getById(promoId) else null
    val isEditMode = existing != null

    var name by remember { mutableStateOf(existing?.name ?: "") }
    var code by remember { mutableStateOf(existing?.code ?: "") }
    var selectedType by remember { mutableStateOf(existing?.type ?: PromotionType.PROMO_CODE) }
    var discountType by remember { mutableStateOf(existing?.discountType ?: DiscountType.PERCENTAGE) }
    var discountValue by remember { mutableStateOf(existing?.discountValue?.toString() ?: "") }
    var startDate by remember { mutableStateOf(existing?.startDate ?: "") }
    var endDate by remember { mutableStateOf(existing?.endDate ?: "") }
    var maxUsagesText by remember { mutableStateOf(existing?.maxUsages?.toString() ?: "") }
    var isUnlimited by remember { mutableStateOf(existing?.maxUsages == null) }
    var appliesTo by remember { mutableStateOf(existing?.appliesTo ?: "All Rooms") }
    var isActive by remember { mutableStateOf(existing?.isActive ?: true) }

    val appliesToOptions = listOf("All Rooms", "Standard Room", "Deluxe Suite", "Junior Suite", "Ocean View", "Executive King", "Penthouse")

    val canSave = name.isNotBlank() && code.isNotBlank() && discountValue.isNotBlank()
            && startDate.isNotBlank() && endDate.isNotBlank()

    Scaffold(containerColor = LocalAppColors.current.background) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // ── Top Bar ───────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Secondary)
                }
                Text(
                    text = if (isEditMode) "Edit Promotion" else "New Promotion",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalAppColors.current.textPrimary
                )
            }

            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                // ── Basic Info ────────────────────────────
                PromoFormSection(title = "BASIC INFO", icon = Icons.Outlined.Info) {

                    PromoLabel("Promotion Name")
                    Spacer(Modifier.height(6.dp))
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = { Text("e.g. Summer Special 2024", color = OnSurfaceSecondary.copy(0.5f)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = authTextFieldColors(),
                        singleLine = true
                    )

                    Spacer(Modifier.height(14.dp))

                    PromoLabel("Promo Code")
                    Spacer(Modifier.height(6.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = code,
                            onValueChange = { code = it.uppercase() },
                            placeholder = { Text("e.g. SUMMER20", color = OnSurfaceSecondary.copy(0.5f)) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = authTextFieldColors(),
                            singleLine = true
                        )
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Primary.copy(0.1f))
                                .border(1.dp, Primary.copy(0.3f), RoundedCornerShape(12.dp))
                                .clickable {
                                    code = ('A'..'Z').shuffled().take(4).joinToString("") +
                                            (10..99).random()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Outlined.Casino, contentDescription = "Auto-generate", tint = Secondary, modifier = Modifier.size(22.dp))
                        }
                    }
                    Text(
                        "Tap the dice icon to auto-generate a code",
                        fontSize = 12.sp,
                        color = OnSurfaceSecondary.copy(0.6f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // ── Promotion Type ────────────────────────
                PromoFormSection(title = "PROMOTION TYPE", icon = Icons.Outlined.Category) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(PromotionType.PROMO_CODE, PromotionType.SEASONAL).forEach { type ->
                            PromoTypeChip(
                                label = type.label,
                                isSelected = selectedType == type,
                                modifier = Modifier.weight(1f),
                                onClick = { selectedType = type }
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(PromotionType.EARLY_BIRD, PromotionType.LAST_MINUTE).forEach { type ->
                            PromoTypeChip(
                                label = type.label,
                                isSelected = selectedType == type,
                                modifier = Modifier.weight(1f),
                                onClick = { selectedType = type }
                            )
                        }
                    }
                }

                // ── Discount ──────────────────────────────
                PromoFormSection(title = "DISCOUNT", icon = Icons.Outlined.Discount) {

                    // Discount type toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(LocalAppColors.current.background)
                            .border(1.dp, Divider, RoundedCornerShape(10.dp))
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        DiscountType.entries.forEach { type ->
                            val isSelected = discountType == type
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) Secondary else Color.Transparent)
                                    .clickable { discountType = type }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        if (type == DiscountType.PERCENTAGE) Icons.Outlined.Percent
                                        else Icons.Outlined.AttachMoney,
                                        contentDescription = null,
                                        tint = if (isSelected) Color.White else OnSurfaceSecondary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        type.label,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) Color.White else OnSurfaceSecondary
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    PromoLabel(
                        if (discountType == DiscountType.PERCENTAGE) "Discount Percentage (%)"
                        else "Discount Amount (XAF)"
                    )
                    Spacer(Modifier.height(6.dp))
                    OutlinedTextField(
                        value = discountValue,
                        onValueChange = { discountValue = it.filter { c -> c.isDigit() } },
                        placeholder = {
                            Text(
                                if (discountType == DiscountType.PERCENTAGE) "e.g. 20" else "e.g. 25000",
                                color = OnSurfaceSecondary.copy(0.5f)
                            )
                        },
                        trailingIcon = {
                            Text(
                                if (discountType == DiscountType.PERCENTAGE) "%" else "XAF",
                                fontSize = 13.sp,
                                color = OnSurfaceSecondary,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = authTextFieldColors(),
                        singleLine = true
                    )
                }

                // ── Validity Period ───────────────────────
                PromoFormSection(title = "VALIDITY PERIOD", icon = Icons.Outlined.DateRange) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            PromoLabel("Start Date")
                            Spacer(Modifier.height(6.dp))
                            OutlinedTextField(
                                value = startDate,
                                onValueChange = { startDate = it },
                                placeholder = { Text("e.g. Nov 1, 2024", color = OnSurfaceSecondary.copy(0.5f)) },
                                trailingIcon = {
                                    Icon(Icons.Outlined.CalendarMonth, contentDescription = null, tint = OnSurfaceSecondary, modifier = Modifier.size(18.dp))
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = authTextFieldColors(),
                                singleLine = true
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            PromoLabel("End Date")
                            Spacer(Modifier.height(6.dp))
                            OutlinedTextField(
                                value = endDate,
                                onValueChange = { endDate = it },
                                placeholder = { Text("e.g. Dec 31, 2024", color = OnSurfaceSecondary.copy(0.5f)) },
                                trailingIcon = {
                                    Icon(Icons.Outlined.CalendarMonth, contentDescription = null, tint = OnSurfaceSecondary, modifier = Modifier.size(18.dp))
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = authTextFieldColors(),
                                singleLine = true
                            )
                        }
                    }
                }

                // ── Additional Settings ───────────────────
                PromoFormSection(title = "ADDITIONAL SETTINGS", icon = Icons.Outlined.Tune) {

                    // Max usages
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Unlimited Usages", fontSize = 15.sp, color = LocalAppColors.current.textPrimary)
                            Text("No cap on number of uses", fontSize = 12.sp, color = OnSurfaceSecondary)
                        }
                        Switch(
                            checked = isUnlimited,
                            onCheckedChange = { isUnlimited = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Primary,
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = OnSurfaceSecondary.copy(0.3f)
                            )
                        )
                    }

                    if (!isUnlimited) {
                        Spacer(Modifier.height(12.dp))
                        PromoLabel("Maximum Usages")
                        Spacer(Modifier.height(6.dp))
                        OutlinedTextField(
                            value = maxUsagesText,
                            onValueChange = { maxUsagesText = it.filter { c -> c.isDigit() } },
                            placeholder = { Text("e.g. 100", color = OnSurfaceSecondary.copy(0.5f)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = authTextFieldColors(),
                            singleLine = true
                        )
                    }

                    Spacer(Modifier.height(14.dp))
                    HorizontalDivider(color = Divider)
                    Spacer(Modifier.height(14.dp))

                    // Applies to
                    PromoLabel("Applies To")
                    Spacer(Modifier.height(8.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        appliesToOptions.chunked(2).forEach { row ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                row.forEach { option ->
                                    val isSelected = appliesTo == option
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(
                                                if (isSelected) Secondary.copy(0.12f)
                                                else LocalAppColors.current.background
                                            )
                                            .border(
                                                1.dp,
                                                if (isSelected) Secondary else Divider,
                                                RoundedCornerShape(10.dp)
                                            )
                                            .clickable { appliesTo = option }
                                            .padding(vertical = 10.dp, horizontal = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            option,
                                            fontSize = 12.sp,
                                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                            color = if (isSelected) Secondary else LocalAppColors.current.textPrimary
                                        )
                                    }
                                }
                                if (row.size < 2) Box(Modifier.weight(1f))
                            }
                        }
                    }

                    Spacer(Modifier.height(14.dp))
                    HorizontalDivider(color = Divider)
                    Spacer(Modifier.height(14.dp))

                    // Active toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Activate Immediately", fontSize = 15.sp, color = LocalAppColors.current.textPrimary)
                            Text("Promotion goes live right away", fontSize = 12.sp, color = OnSurfaceSecondary)
                        }
                        Switch(
                            checked = isActive,
                            onCheckedChange = { isActive = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Secondary,
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = OnSurfaceSecondary.copy(0.3f)
                            )
                        )
                    }
                }

                // ── Save Button ───────────────────────────
                Button(
                    onClick = {
                        val newPromo = Promotion(
                            id = existing?.id ?: "p_${Random.nextLong().and(0x7FFFFFFF)}",
                            code = code,
                            name = name,
                            type = selectedType,
                            discountType = discountType,
                            discountValue = discountValue.toIntOrNull() ?: 0,
                            startDate = startDate,
                            endDate = endDate,
                            maxUsages = if (isUnlimited) null else maxUsagesText.toIntOrNull(),
                            usedCount = existing?.usedCount ?: 0,
                            appliesTo = appliesTo,
                            status = if (isActive) PromotionStatus.ACTIVE else PromotionStatus.SCHEDULED,
                            isActive = isActive
                        )
                        if (isEditMode) {
                            val idx = PromotionsMockData.promotions.indexOfFirst { it.id == newPromo.id }
                            if (idx >= 0) PromotionsMockData.promotions[idx] = newPromo
                        } else {
                            PromotionsMockData.promotions.add(0, newPromo)
                        }
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Secondary),
                    enabled = canSave
                ) {
                    Icon(
                        if (isEditMode) Icons.Outlined.Save else Icons.Outlined.Add,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        if (isEditMode) "Save Changes" else "Create Promotion",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

// ── Form helpers ──────────────────────────────────────────────

@Composable
private fun PromoFormSection(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(LocalAppColors.current.surface)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Primary.copy(0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Secondary, modifier = Modifier.size(16.dp))
            }
            Text(
                title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurfaceSecondary,
                letterSpacing = 0.8.sp
            )
        }
        content()
    }
}

@Composable
private fun PromoLabel(text: String) {
    Text(text, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = LocalAppColors.current.textPrimary)
}

@Composable
private fun PromoTypeChip(
    label: String,
    isSelected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) Secondary.copy(0.12f) else LocalAppColors.current.background)
            .border(1.dp, if (isSelected) Secondary else Divider, RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Secondary else OnSurfaceSecondary
        )
    }
}