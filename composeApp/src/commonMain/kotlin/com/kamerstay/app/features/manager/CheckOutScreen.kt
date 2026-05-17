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
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*

@Composable
fun CheckOutScreen(
    navController: NavController,
    reservationId: String
) {
    var roomStatusEnabled by remember { mutableStateOf(true) }
    var selectedPayment by remember { mutableStateOf("CREDIT_CARD") }
    var showSuccess by remember { mutableStateOf(false) }

    // Success Dialog
    if (showSuccess) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Check-Out Complete!", fontWeight = FontWeight.Bold) },
            text = { Text("Samuel Eto'o has been checked out. Room 402 is now set to Cleaning.") },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccess = false
                        navController.navigate(Routes.ManagerDashboard.route) {
                            popUpTo(Routes.ManagerDashboard.route) { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepEmerald)
                ) {
                    Text("Done", color = Color.White)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }

    Scaffold(
        containerColor = WarmIvory,
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 0.dp
            ) {
                listOf(
                    Icons.Outlined.BookOnline to "Bookings",
                    Icons.Outlined.Login to "Check-in",
                    Icons.Filled.Logout to "Check-out",
                    Icons.Outlined.Hotel to "Rooms"
                ).forEachIndexed { index, (icon, label) ->
                    NavigationBarItem(
                        selected = index == 2,
                        onClick = {
                            when (index) {
                                0 -> navController.navigate(Routes.Reservations.route)
                                1 -> navController.navigate(Routes.CheckIn.route)
                                3 -> navController.navigate(Routes.RoomManagement.route)
                            }
                        },
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label, fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DeepEmerald,
                            selectedTextColor = DeepEmerald,
                            indicatorColor = PrimaryContainer,
                            unselectedIconColor = OnSurfaceVariant,
                            unselectedTextColor = OnSurfaceVariant
                        )
                    )
                }
            }
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
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = OnSurface)
                    }
                    Text(
                        text = "Reservation Management",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface
                    )
                }
                IconButton(onClick = { }) {
                    Icon(Icons.Filled.MoreVert, contentDescription = null, tint = OnSurface)
                }
            }

            HorizontalDivider(color = Divider)

            Spacer(modifier = Modifier.height(20.dp))

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {

                // ── Guest Card ────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Avatar
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(SurfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "SE",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepEmerald
                                )
                            }

                            // VIP star badge
                            Box(
                                modifier = Modifier
                                    .offset(x = (-12).dp, y = 16.dp)
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(WarmAmber),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Filled.Star,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(12.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(4.dp))

                            Column {
                                Text(
                                    text = "Samuel Eto'o",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = OnSurface
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(SurfaceVariant)
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Text(
                                            text = "ROOM 402",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = OnSurfaceVariant
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(ErrorColor.copy(alpha = 0.1f))
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Text(
                                            text = "VIP STATUS",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ErrorColor
                                        )
                                    }
                                }
                            }
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = Divider
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "CHECK-IN",
                                    fontSize = 10.sp,
                                    color = OnSurfaceVariant,
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "12 Oct 2023",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OnSurface
                                )
                            }
                            Column {
                                Text(
                                    text = "CHECK-OUT",
                                    fontSize = 10.sp,
                                    color = OnSurfaceVariant,
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "15 Oct 2023",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OnSurface
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ── Final Bill Summary ────────────────
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Receipt,
                        contentDescription = null,
                        tint = OnSurface,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Final Bill Summary",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Column {
                        BillRow("Standard Suite (3 Nights)", "135,000 XAF")
                        Spacer(modifier = Modifier.height(8.dp))
                        BillRow("In-room Dining (Oct 13)", "12,500 XAF")
                        Spacer(modifier = Modifier.height(8.dp))
                        BillRow("Laundry Services", "4,200 XAF")
                        Spacer(modifier = Modifier.height(8.dp))
                        BillRow("Airport Transfer", "15,000 XAF")

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = Divider
                        )

                        // Total
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Total Amount",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnSurface
                            )
                            Text(
                                text = "166,700 XAF",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = OnSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Advance paid
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(WarmAmber.copy(alpha = 0.1f))
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Advance Paid",
                                    fontSize = 13.sp,
                                    color = Color(0xFF8B6914)
                                )
                                Text(
                                    text = "- 100,000 XAF",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF8B6914)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Outstanding Balance
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(DeepEmerald)
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "OUTSTANDING BALANCE",
                                        fontSize = 10.sp,
                                        color = Color.White.copy(0.7f),
                                        fontWeight = FontWeight.SemiBold,
                                        letterSpacing = 0.5.sp
                                    )
                                    Text(
                                        text = "66,700 XAF",
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White
                                    )
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Outlined.Payments,
                                        contentDescription = null,
                                        tint = WarmAmber,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "UNPAID",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = WarmAmber
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Room Status Update ────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceVariant)
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(ErrorColor.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Outlined.CleaningServices,
                                    contentDescription = null,
                                    tint = ErrorColor,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Room Status\nUpdate",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = OnSurface,
                                    lineHeight = 20.sp
                                )
                                Text(
                                    text = "Auto-notify housekeeping team",
                                    fontSize = 12.sp,
                                    color = OnSurfaceVariant
                                )
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Switch(
                                checked = roomStatusEnabled,
                                onCheckedChange = { roomStatusEnabled = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = DeepEmerald
                                )
                            )
                            Text(
                                text = "Cleaning",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = OnSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ── Payment Method ────────────────────
                Text(
                    text = "Payment Method",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CheckoutPaymentCard(
                        icon = Icons.Outlined.CreditCard,
                        label = "CREDIT CARD",
                        isSelected = selectedPayment == "CREDIT_CARD",
                        onClick = { selectedPayment = "CREDIT_CARD" },
                        modifier = Modifier.weight(1f)
                    )
                    CheckoutPaymentCard(
                        icon = Icons.Outlined.Payments,
                        label = "CASH/MOBILE",
                        isSelected = selectedPayment == "CASH_MOBILE",
                        onClick = { selectedPayment = "CASH_MOBILE" },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ── Complete Check-Out Button ──────────
                Button(
                    onClick = { showSuccess = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepEmerald)
                ) {
                    Text(
                        text = "Complete Check-Out",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        Icons.Outlined.ExitToApp,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Print Invoice
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp, OutlineVariant
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = OnSurface
                    )
                ) {
                    Text(
                        text = "Print Provisional Invoice",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = OnSurface
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ── Bill Row ──────────────────────────────────────────────
@Composable
fun BillRow(label: String, amount: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 14.sp, color = OnSurfaceVariant)
        Text(text = amount, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = OnSurface)
    }
}

// ── Checkout Payment Card ─────────────────────────────────
@Composable
fun CheckoutPaymentCard(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .then(
                if (isSelected) Modifier.border(
                    1.5.dp, ErrorColor, RoundedCornerShape(12.dp)
                ) else Modifier
            )
            .clickable { onClick() }
            .padding(vertical = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                icon,
                contentDescription = null,
                tint = if (isSelected) ErrorColor else OnSurfaceVariant,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) ErrorColor else OnSurfaceVariant,
                letterSpacing = 0.5.sp
            )
        }
    }
}