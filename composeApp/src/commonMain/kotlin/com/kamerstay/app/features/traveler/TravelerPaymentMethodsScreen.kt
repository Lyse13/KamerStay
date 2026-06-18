package com.kamerstay.app.features.traveler

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.model.TravelerCard
import com.kamerstay.app.data.model.TravelerCardType
import com.kamerstay.app.viewmodel.TravelerViewModel
import org.koin.compose.viewmodel.koinViewModel
import com.kamerstay.app.core.components.TravelerBottomNavBar

@Composable
fun TravelerPaymentMethodsScreen(navController: NavController) {

    val viewModel = koinViewModel<TravelerViewModel>()
    val state = viewModel.travelerPaymentMethodsState
    val primaryCard = state.primaryCard

    if (state.showAddDialog) {
        AlertDialog(
            onDismissRequest = { state.showAddDialog = false },
            title = { Text("Add Payment Method", fontWeight = FontWeight.Bold, color = LocalAppColors.current.textPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    listOf(
                        Icons.Outlined.CreditCard to "Credit / Debit Card",
                        Icons.Outlined.PhoneAndroid to "MTN Mobile Money",
                        Icons.Outlined.PhoneAndroid to "Orange Money"
                    ).forEach { (icon, label) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(LocalAppColors.current.background)
                                .clickable { state.showAddDialog = false }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(icon, contentDescription = null, tint = Secondary, modifier = Modifier.size(20.dp))
                            Text(label, fontSize = 14.sp, color = LocalAppColors.current.textPrimary)
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { state.showAddDialog = false }) {
                    Text("Cancel", color = OnSurfaceSecondary)
                }
            },
            containerColor = LocalAppColors.current.surface,
            shape = RoundedCornerShape(16.dp)
        )
    }

    Scaffold(
        containerColor = LocalAppColors.current.background,
        bottomBar = {
            TravelerBottomNavBar(navController = navController, selectedTab = 3)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // ── Top Bar ───────────────────────────────
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
                Text("Payment Methods", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = LocalAppColors.current.textPrimary)
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {

                Text(
                    text = "Manage your cards and mobile money accounts used to pay for bookings.",
                    fontSize = 13.sp,
                    color = OnSurfaceSecondary,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // ── Primary Card ──────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(brush = Brush.linearGradient(colors = listOf(Secondary, DeepBlue)))
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "PRIMARY CARD",
                                fontSize = 12.sp,
                                color = Color.White.copy(0.7f),
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                            Icon(Icons.Outlined.CreditCard, contentDescription = null, tint = Color.White, modifier = Modifier.size(26.dp))
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(viewModel.travelerPersonalInfoState.fullName, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("•••• •••• ••••", fontSize = 16.sp, color = Color.White.copy(0.6f))
                            Text(primaryCard.number, fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text("EXPIRES", fontSize = 10.sp, color = Color.White.copy(0.6f))
                                Text(primaryCard.expiry, fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Primary)
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = primaryCard.type.name,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = OnPrimary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ── Saved Methods ─────────────────────
                Text("SAVED METHODS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Primary, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    state.cards.forEach { card ->
                        TravelerPaymentRow(
                            card = card,
                            isSelected = card.id == state.selectedCardId,
                            onClick = { state.selectedCardId = card.id }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // ── Add New ───────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.5.dp, Divider, RoundedCornerShape(14.dp))
                        .clickable { state.showAddDialog = true }
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(44.dp).clip(CircleShape).background(OnSurfaceSecondary.copy(0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Outlined.Add, contentDescription = null, tint = OnSurfaceSecondary, modifier = Modifier.size(22.dp))
                        }
                        Text("Add New Payment Method", fontSize = 15.sp, fontWeight = FontWeight.Medium, color = LocalAppColors.current.textPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Security Note ─────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Primary.copy(0.06f))
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Icon(Icons.Outlined.Security, contentDescription = null, tint = Secondary, modifier = Modifier.size(20.dp))
                        Column {
                            Text("Secure Payments", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = LocalAppColors.current.textPrimary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Your payment details are encrypted and secured with SSL. KamerStay never stores your full card number.",
                                fontSize = 13.sp,
                                color = OnSurfaceSecondary,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

// ── Traveler Payment Row ──────────────────────────────────
@Composable
private fun TravelerPaymentRow(
    card: TravelerCard,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val icon: ImageVector = when (card.type) {
        TravelerCardType.VISA, TravelerCardType.MASTERCARD -> Icons.Outlined.CreditCard
        TravelerCardType.MOBILE_MONEY_MTN, TravelerCardType.MOBILE_MONEY_ORANGE -> Icons.Outlined.PhoneAndroid
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(LocalAppColors.current.surface)
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) Primary else Color.Transparent,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Box(
                    modifier = Modifier.size(46.dp).clip(RoundedCornerShape(12.dp)).background(Primary.copy(0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = Secondary, modifier = Modifier.size(22.dp))
                }
                Column {
                    Text(card.label, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = LocalAppColors.current.textPrimary)
                    Text(
                        text = if (card.expiry == "—") card.number else "•••• ${card.number}  •  Expires ${card.expiry}",
                        fontSize = 12.sp,
                        color = OnSurfaceSecondary
                    )
                }
            }
            if (isSelected) {
                Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = Primary, modifier = Modifier.size(20.dp))
            } else {
                Icon(Icons.Outlined.MoreVert, contentDescription = null, tint = OnSurfaceSecondary, modifier = Modifier.size(20.dp))
            }
        }
    }
}