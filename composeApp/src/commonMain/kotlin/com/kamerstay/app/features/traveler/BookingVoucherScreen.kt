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
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.NavigationState
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.viewmodel.TravelerViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingVoucherScreen(navController: NavController) {

    val viewModel = koinViewModel<TravelerViewModel>()
    val booking = viewModel.createdBooking
    val hotel = viewModel.selectedHotel
    val bookingReference = booking?.bookingReference?.ifBlank { null } ?: "—"

    val clipboard = LocalClipboardManager.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var showShareSheet by remember { mutableStateOf(false) }
    var idCopied by remember { mutableStateOf(false) }

    // ── Share bottom sheet ────────────────────────────────
    if (showShareSheet) {
        VoucherShareSheet(
            hotelName = hotel?.name ?: "—",
            bookingReference = bookingReference,
            onCopyId = {
                idCopied = true
                showShareSheet = false
                scope.launch {
                    clipboard.setText(AnnotatedString(bookingReference))
                    snackbarHostState.showSnackbar("Booking ID copié")
                }
            },
            onShareWhatsApp = {
                showShareSheet = false
                scope.launch { snackbarHostState.showSnackbar("WhatsApp sharing — coming soon") }
            },
            onShareEmail = {
                showShareSheet = false
                scope.launch { snackbarHostState.showSnackbar("Email sharing — coming soon") }
            },
            onDownloadPdf = {
                showShareSheet = false
                scope.launch { snackbarHostState.showSnackbar("PDF download — coming soon") }
            },
            onDismiss = { showShareSheet = false }
        )
    }

    Scaffold(
        containerColor = LocalAppColors.current.background,
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = Secondary,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
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
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Secondary
                        )
                    }
                    Text(
                        text = "KamerStay",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Secondary
                    )
                }
                // ── Print / Share button ──────────────
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, Primary.copy(0.4f), RoundedCornerShape(20.dp))
                        .background(Primary.copy(0.06f))
                        .clickable { showShareSheet = true }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            Icons.Outlined.Share,
                            contentDescription = "Share voucher",
                            tint = Secondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Share",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Secondary
                        )
                    }
                }
            }

            // ── Hero Header Card ──────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF092031), Color(0xFF003761))
                        )
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Text(
                        text = "BOOKING VOUCHER",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(0.6f),
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = hotel?.name ?: "—",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        lineHeight = 32.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Outlined.Place, contentDescription = null, tint = Color.White.copy(0.7f), modifier = Modifier.size(13.dp))
                        Text(text = hotel?.address ?: "—", fontSize = 12.sp, color = Color.White.copy(0.7f))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    // Booking ID — tap to copy
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (idCopied) Primary.copy(0.7f) else Primary)
                            .clickable {
                                clipboard.setText(AnnotatedString(bookingReference))
                                idCopied = true
                                scope.launch { snackbarHostState.showSnackbar("Booking ID copié !") }
                            }
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Column {
                                Text(text = "BOOKING ID", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OnPrimary.copy(0.7f), letterSpacing = 0.8.sp)
                                Text(text = bookingReference, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = OnPrimary)
                            }
                            Icon(
                                if (idCopied) Icons.Outlined.CheckCircle else Icons.Outlined.ContentCopy,
                                contentDescription = "Copy",
                                tint = OnPrimary.copy(0.8f),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Guest + Status ────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(LocalAppColors.current.surface)
                    .padding(16.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text(text = "PRIMARY GUEST", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OnSurfaceSecondary, letterSpacing = 0.5.sp)
                        Text(text = com.kamerstay.app.data.state.UserSession.fullName.ifBlank { "Voyageur KamerStay" }, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = LocalAppColors.current.textPrimary, lineHeight = 22.sp)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "BOOKING STATUS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OnSurfaceSecondary, letterSpacing = 0.5.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = Primary, modifier = Modifier.size(14.dp))
                            Text(text = "CONFIRMED", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Primary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Check-in / Check-out Card ─────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(LocalAppColors.current.surface)
                    .padding(20.dp)
            ) {
                val nights = booking?.numberOfNights ?: 1
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "CHECK-IN", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OnSurfaceSecondary, letterSpacing = 0.8.sp)
                    Text(text = booking?.checkInDate ?: "—", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = LocalAppColors.current.textPrimary)
                    Text(text = "À partir de 14h00", fontSize = 13.sp, color = OnSurfaceSecondary)
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(Primary).padding(horizontal = 14.dp, vertical = 6.dp)) {
                        Text(text = "$nights NUIT${if (nights > 1) "S" else ""}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = OnPrimary)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "CHECK-OUT", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OnSurfaceSecondary, letterSpacing = 0.8.sp)
                    Text(text = booking?.checkOutDate ?: "—", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = LocalAppColors.current.textPrimary)
                    Text(text = "Avant 12h00", fontSize = 13.sp, color = OnSurfaceSecondary)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Room + Meal ───────────────────────────
            Box(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(14.dp)).background(LocalAppColors.current.surface).padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    VoucherDetailRow(icon = Icons.Outlined.Hotel, label = "ROOM CATEGORY", value = "Panoramic Ocean Suite", subValue = "Floor 12, Room 1204")
                    HorizontalDivider(color = Divider)
                    VoucherDetailRow(icon = Icons.Outlined.Restaurant, label = "MEAL PLAN", value = "Half-Board Included", subValue = "Breakfast & Dinner")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Amenities ─────────────────────────────
            Box(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(14.dp)).background(LocalAppColors.current.surface).padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        VoucherAmenity(icon = Icons.Outlined.Wifi, label = "Free Wi-Fi", modifier = Modifier.weight(1f))
                        VoucherAmenity(icon = Icons.Outlined.Pool, label = "Infinity Pool", modifier = Modifier.weight(1f))
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        VoucherAmenity(icon = Icons.Outlined.LocalParking, label = "Valet Parking", modifier = Modifier.weight(1f))
                        VoucherAmenity(icon = Icons.Outlined.FitnessCenter, label = "24/7 Gym", modifier = Modifier.weight(1f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── QR Code ───────────────────────────────
            Box(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(14.dp)).background(LocalAppColors.current.surface).padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "EXPRESS CHECK-IN CODE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OnSurfaceSecondary, letterSpacing = 0.8.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier.size(160.dp).clip(RoundedCornerShape(12.dp))
                            .background(Secondary).border(2.dp, Primary, RoundedCornerShape(12.dp)).padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(modifier = Modifier.size(120.dp).border(2.dp, Color.White.copy(0.4f), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                            Icon(Icons.Outlined.QrCode, contentDescription = null, tint = Color.White, modifier = Modifier.size(48.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "GUEST COUNT", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OnSurfaceSecondary, letterSpacing = 0.5.sp)
                    Text(text = "2 Adults, 1 Child", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = LocalAppColors.current.textPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Present this QR code or Booking ID upon\narrival at the concierge desk.", fontSize = 12.sp, color = OnSurfaceSecondary, textAlign = TextAlign.Center, lineHeight = 17.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Policy Info ───────────────────────────
            Box(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(14.dp)).background(LocalAppColors.current.surface).padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    PolicyRow(icon = Icons.Outlined.Badge, title = "Identification", description = "Passport or valid ID required at check-in.")
                    HorizontalDivider(color = Divider)
                    PolicyRow(icon = Icons.Outlined.Cancel, title = "Cancellation", description = "Free cancellation until 48h before arrival.")
                    HorizontalDivider(color = Divider)
                    PolicyRow(icon = Icons.Outlined.SupportAgent, title = "Support", description = "24/7 Concierge: +1(800) MYSTAYS")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Offline Access Card ───────────────────
            Box(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(14.dp)).background(Primary.copy(0.08f)).padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(44.dp).clip(CircleShape).background(Secondary), contentAlignment = Alignment.Center) {
                        Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(text = "Offline Access Enabled", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Secondary)
                        Text(text = "This voucher is saved to your device. You can access it without an internet connection.", fontSize = 12.sp, color = OnSurfaceSecondary, lineHeight = 17.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Footer Banner ─────────────────────────
            Box(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(brush = Brush.verticalGradient(colors = listOf(Color(0xFF092031), Color(0xFF0D1A28))))
            ) {
                Box(modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)) {
                    Column {
                        Text(text = "À bientôt à ${hotel?.city ?: ""}", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        Text(text = "Tap to view resort guide and dining menus", fontSize = 12.sp, color = Color.White.copy(0.6f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Share Voucher button ──────────────────
            Button(
                onClick = { showShareSheet = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Secondary)
            ) {
                Icon(Icons.Outlined.Share, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Share Voucher", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ── Share Bottom Sheet ────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VoucherShareSheet(
    hotelName: String,
    bookingReference: String,
    onCopyId: () -> Unit,
    onShareWhatsApp: () -> Unit,
    onShareEmail: () -> Unit,
    onDownloadPdf: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = LocalAppColors.current.surface,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 36.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Share Voucher", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = LocalAppColors.current.textPrimary)
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Outlined.Close, contentDescription = "Close", tint = OnSurfaceSecondary)
                }
            }

            Text(text = hotelName, fontSize = 13.sp, color = OnSurfaceSecondary)
            Text(text = "Booking ID: $bookingReference", fontSize = 13.sp, color = Primary, fontWeight = FontWeight.SemiBold)

            Spacer(modifier = Modifier.height(20.dp))

            ShareOption(icon = Icons.Outlined.ContentCopy, title = "Copy Booking ID", subtitle = "Paste it anywhere", onClick = onCopyId)
            HorizontalDivider(color = Divider.copy(0.5f), modifier = Modifier.padding(start = 56.dp))
            ShareOption(icon = Icons.AutoMirrored.Outlined.Chat, title = "Share via WhatsApp", subtitle = "Send to a contact", onClick = onShareWhatsApp)
            HorizontalDivider(color = Divider.copy(0.5f), modifier = Modifier.padding(start = 56.dp))
            ShareOption(icon = Icons.Outlined.MailOutline, title = "Share via Email", subtitle = "Send as attachment", onClick = onShareEmail)
            HorizontalDivider(color = Divider.copy(0.5f), modifier = Modifier.padding(start = 56.dp))
            ShareOption(icon = Icons.Outlined.PictureAsPdf, title = "Download PDF", subtitle = "Save to your device", onClick = onDownloadPdf)
        }
    }
}

@Composable
private fun ShareOption(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Primary.copy(0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = Secondary, modifier = Modifier.size(20.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = LocalAppColors.current.textPrimary)
            Text(text = subtitle, fontSize = 12.sp, color = OnSurfaceSecondary)
        }
        Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = OnSurfaceSecondary, modifier = Modifier.size(16.dp))
    }
}

// ── Sub-composables ───────────────────────────────────────

@Composable
fun VoucherDetailRow(icon: ImageVector, label: String, value: String, subValue: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(Primary.copy(0.1f)), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = Secondary, modifier = Modifier.size(20.dp))
        }
        Column {
            Text(text = label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OnSurfaceSecondary, letterSpacing = 0.5.sp)
            Text(text = value, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = LocalAppColors.current.textPrimary)
            Text(text = subValue, fontSize = 12.sp, color = OnSurfaceSecondary)
        }
    }
}

@Composable
fun VoucherAmenity(icon: ImageVector, label: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Icon(icon, contentDescription = null, tint = Primary, modifier = Modifier.size(16.dp))
        Text(text = label, fontSize = 12.sp, color = LocalAppColors.current.textPrimary, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun PolicyRow(icon: ImageVector, title: String, description: String) {
    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Icon(icon, contentDescription = null, tint = Secondary, modifier = Modifier.size(18.dp))
        Column {
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = LocalAppColors.current.textPrimary)
            Text(text = description, fontSize = 12.sp, color = OnSurfaceSecondary)
        }
    }
}