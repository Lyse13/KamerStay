package com.kamerstay.app.features.manager

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
import com.kamerstay.app.data.mock.PaymentMethodsMockData
import com.kamerstay.app.data.model.PaymentMethod
import com.kamerstay.app.data.model.PaymentMethodType
import com.kamerstay.app.viewmodel.TravelerViewModel
import org.koin.compose.viewmodel.koinViewModel
import com.kamerstay.app.core.components.ManagerBottomNavBar

@Composable
fun PaymentMethodsScreen(navController: NavController) {

    val viewModel = koinViewModel<TravelerViewModel>()
    val state = viewModel.paymentMethodsState

    Scaffold(
        containerColor = LocalAppColors.current.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { state.showAddDialog = true },
                containerColor = Secondary,
                contentColor = Color.White,
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(
                    Icons.Outlined.CreditCardOff,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        bottomBar = {
            ManagerBottomNavBar(navController = navController, currentRoute = "profile")
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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.navigate(Routes.ManagerProfile.route) }) {
                        Icon(
                            Icons.Outlined.Menu,
                            contentDescription = null,
                            tint = Secondary
                        )
                    }
                    Text(
                        text = "Hotel Manager",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Secondary
                    )
                }
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Secondary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {

                // ── Header ────────────────────────────
                Text(
                    text = "Payment Methods",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = LocalAppColors.current.textPrimary
                )
                Text(
                    text = "Manage your payout accounts and linked business cards for hotel operations.",
                    fontSize = 13.sp,
                    color = OnSurfaceSecondary,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // ── Primary Payout Card ───────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Secondary, DeepBlue)
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "PRIMARY PAYOUT",
                                fontSize = 12.sp,
                                color = Color.White.copy(0.7f),
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                            Icon(
                                Icons.Outlined.AccountBalance,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = PaymentMethodsMockData.primaryAccount.name,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "•••• ",
                                    fontSize = 18.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "5928",
                                    fontSize = 18.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Commercial Bank of Cameroon",
                                fontSize = 12.sp,
                                color = Color.White.copy(0.7f)
                            )

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Primary)
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        Icons.Outlined.CheckCircle,
                                        contentDescription = null,
                                        tint = OnPrimary,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Text(
                                        text = "Active",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = OnPrimary
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ── Secondary Methods ─────────────────
                Text(
                    text = "SECONDARY METHODS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    state.methods.forEach { method ->
                        PaymentMethodRow(method = method)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // ── Add New ───────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(
                            width = 1.5.dp,
                            color = Divider,
                            shape = RoundedCornerShape(14.dp)
                        )
                        .background(Color.Transparent)
                        .clickable { state.showAddDialog = true }
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(OnSurfaceSecondary.copy(0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Outlined.Add,
                                contentDescription = null,
                                tint = OnSurfaceSecondary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Text(
                            text = "Add New Payment Method",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = LocalAppColors.current.textPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── About Payouts ─────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Primary.copy(0.06f))
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = null,
                            tint = Secondary,
                            modifier = Modifier.size(20.dp)
                        )
                        Column {
                            Text(
                                text = "About Payouts",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = LocalAppColors.current.textPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Payouts are processed every Thursday at 00:00 GMT. Ensure your primary bank account is verified to avoid delays.",
                                fontSize = 13.sp,
                                color = OnSurfaceSecondary,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ── Payment Method Row ────────────────────────────────────
@Composable
fun PaymentMethodRow(method: PaymentMethod) {
    val icon: ImageVector = when (method.type) {
        PaymentMethodType.MOBILE_MONEY -> Icons.Outlined.PhoneAndroid
        PaymentMethodType.CARD -> Icons.Outlined.CreditCard
        PaymentMethodType.BANK -> Icons.Outlined.AccountBalance
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(LocalAppColors.current.surface)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Primary.copy(0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = Secondary,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Column {
                    Text(
                        text = method.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = LocalAppColors.current.textPrimary
                    )
                    Text(
                        text = method.detail,
                        fontSize = 12.sp,
                        color = OnSurfaceSecondary
                    )
                }
            }
            Icon(
                Icons.Outlined.MoreVert,
                contentDescription = null,
                tint = OnSurfaceSecondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}