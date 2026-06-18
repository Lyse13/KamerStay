package com.kamerstay.app.features.manager

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.components.ManagerBottomNavBar
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.features.traveler.LegalInfoBox
import com.kamerstay.app.features.traveler.LegalNavCard
import com.kamerstay.app.features.traveler.LegalParagraph
import com.kamerstay.app.features.traveler.LegalSection

@Composable
fun ManagerPrivacyTermsScreen(navController: NavController) {

    Scaffold(
        containerColor = LocalAppColors.current.background,
        bottomBar = {
            ManagerBottomNavBar(navController = navController, currentRoute = "settings")
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {

            // ── Top Bar ───────────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 8.dp, vertical = 12.dp),
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
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Primary.copy(0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Outlined.Shield,
                            contentDescription = null,
                            tint = Secondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // ── Badge ─────────────────────────────────────
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Secondary)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                Icons.Outlined.Balance,
                                contentDescription = null,
                                tint = OnPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "PROPERTY MANAGER LEGAL CENTER",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnPrimary,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // ── Header ────────────────────────────────────
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Host Terms & Privacy",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = LocalAppColors.current.textPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Last updated: October 1, 2024. These terms govern your use of KamerStay as a property manager and host.",
                        fontSize = 13.sp,
                        color = OnSurfaceSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // ── Quick Nav Cards ───────────────────────────
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    LegalNavCard(
                        icon = Icons.Outlined.Gavel,
                        title = "Host Agreement",
                        subtitle = "Your obligations as a property manager"
                    )
                    LegalNavCard(
                        icon = Icons.Outlined.Shield,
                        title = "Privacy Policy",
                        subtitle = "How we handle your and your guests' data"
                    )
                    LegalNavCard(
                        icon = Icons.Outlined.FolderShared,
                        title = "Data Processing Agreement",
                        subtitle = "GDPR-aligned guest data handling"
                    )
                    LegalNavCard(
                        icon = Icons.Outlined.Payments,
                        title = "Revenue & Payout Policy",
                        subtitle = "Commission structure and payment terms"
                    )
                }
                Spacer(modifier = Modifier.height(28.dp))
            }

            // ── Section 1: Host Agreement ─────────────────
            item {
                LegalSection(number = "1", title = "Host Agreement & Obligations") {

                    LegalParagraph(
                        text = "By registering as a property manager on KamerStay, you enter into a binding agreement with KamerStay Technology Ltd. You confirm that you are the legal owner or authorised representative of the listed property, and that you have the right to offer it for accommodation."
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(LocalAppColors.current.background)
                            .border(1.dp, Divider, RoundedCornerShape(10.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Text(
                                text = "Host Commitment",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = LocalAppColors.current.textPrimary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "\"As a host, you agree to maintain accurate availability, honour confirmed reservations, and provide guests with the standard of accommodation as described in your listing. Failure to honour a reservation without valid cause may result in account suspension.\"",
                                fontSize = 13.sp,
                                color = OnSurfaceSecondary,
                                lineHeight = 18.sp,
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    listOf(
                        "You must ensure your property complies with all applicable local health, safety, and fire regulations.",
                        "All advertised amenities and room features must be accurately represented and available to guests.",
                        "You are solely responsible for setting and applying your cancellation and refund policies, within the bounds permitted by KamerStay.",
                        "KamerStay acts as an intermediary platform and is not liable for disputes arising between host and guest unless caused by a platform error."
                    ).forEach { bullet ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(Secondary)
                                    .align(Alignment.Top)
                                    .offset(y = 7.dp)
                            )
                            Text(
                                text = bullet,
                                fontSize = 13.sp,
                                color = OnSurfaceSecondary,
                                lineHeight = 19.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // ── Section 2: Listing Standards ──────────────
            item {
                LegalSection(number = "2", title = "Property Listing Standards") {

                    LegalParagraph(
                        text = "All property listings on KamerStay are subject to our quality and content standards. Listings that do not comply may be removed or suspended pending review."
                    )

                    LegalInfoBox(
                        title = "Required Listing Information",
                        content = "Accurate property name, full address, contact details, room types and capacities, correct pricing per room type, high-quality photos, and applicable amenities."
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    LegalInfoBox(
                        title = "Prohibited Content",
                        content = "Misleading descriptions, fraudulent pricing, unlicensed accommodation, properties with active legal disputes, or listings that discriminate against protected groups."
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    LegalParagraph(
                        text = "KamerStay reserves the right to conduct periodic verification of listed properties and may request supporting documentation including business registration certificates or government-issued permits."
                    )
                }
            }

            // ── Section 3: Guest Data Processing ──────────
            item {
                LegalSection(number = "3", title = "Guest Data Processing Agreement") {

                    LegalParagraph(
                        text = "As a property manager using KamerStay, you may access personal data of guests who book through the platform (name, contact details, booking information). This access is granted solely for the purpose of fulfilling confirmed reservations."
                    )

                    LegalInfoBox(
                        title = "Your Responsibilities as Data Controller",
                        content = "You must not use guest data for any purpose other than fulfilling the reservation. You must not share, sell, or transfer guest data to third parties without explicit consent. Guest data must be stored securely and deleted when no longer required."
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    LegalInfoBox(
                        title = "Data Retention",
                        content = "Booking records and associated guest information may be retained for up to 36 months for financial and legal compliance purposes. Requests for early deletion must be submitted to our Data Protection Officer."
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    LegalParagraph(
                        text = "KamerStay processes guest data on your behalf in accordance with the OHADA privacy framework applicable to the CEMAC region and aligns with GDPR principles for properties serving international guests."
                    )
                }
            }

            // ── Section 4: Revenue & Payouts ──────────────
            item {
                LegalSection(number = "4", title = "Revenue, Commissions & Payout Policy") {

                    LegalParagraph(
                        text = "KamerStay charges a service commission on all confirmed bookings processed through the platform. The applicable commission rate is communicated at onboarding and may be revised with 30 days' written notice."
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Secondary.copy(0.06f))
                            .border(1.dp, Secondary.copy(0.25f), RoundedCornerShape(10.dp))
                            .padding(14.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                "Standard Commission Structure",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = LocalAppColors.current.textPrimary
                            )
                            listOf(
                                "Standard properties" to "12% per booking",
                                "Premium / verified properties" to "9% per booking",
                                "Promotional bookings" to "15% per booking"
                            ).forEach { (tier, rate) ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(tier, fontSize = 13.sp, color = OnSurfaceSecondary)
                                    Text(rate, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Secondary)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LegalParagraph(
                        text = "Payouts are processed within 2 business days after a guest's check-in is confirmed by the host. Payouts are transferred to the bank account or mobile money number registered in your payout profile. KamerStay is not responsible for delays caused by banking intermediaries."
                    )

                    LegalInfoBox(
                        title = "Payout Disputes",
                        content = "Any payout discrepancy must be reported within 14 days of the transaction date. Claims raised after this window cannot be guaranteed. Contact finance@mystays.cm with your booking reference for all payout disputes."
                    )
                }
            }

            // ── Section 5: Liability ───────────────────────
            item {
                LegalSection(number = "5", title = "Liability & Indemnification") {

                    LegalParagraph(
                        text = "KamerStay provides the platform infrastructure and booking facilitation only. The property manager is solely responsible for the physical safety, condition, and management of the property and the well-being of guests during their stay."
                    )

                    LegalParagraph(
                        text = "You agree to indemnify and hold harmless KamerStay Technology Ltd., its officers, directors, and employees from any claims, damages, losses, or expenses (including legal fees) arising from your use of the platform, your property, or your interactions with guests."
                    )
                }
            }

            // ── Section 6: Governing Law ───────────────────
            item {
                LegalSection(number = "6", title = "Governing Law & Dispute Resolution") {

                    LegalParagraph(
                        text = "These terms are governed by the laws of the Republic of Cameroon. Any dispute arising from or relating to this agreement shall first be subject to good-faith negotiation. If unresolved within 30 days, disputes shall be referred to the competent courts of Douala, Cameroon."
                    )
                }
            }

            // ── Footer ────────────────────────────────────
            item {
                Spacer(modifier = Modifier.height(20.dp))

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = Divider
                )

                Spacer(modifier = Modifier.height(20.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(3.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Secondary)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "KamerStay Technology Ltd. — Legal Affairs",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = LocalAppColors.current.textPrimary,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "legal@mystays.cm  ·  +237 6XX XXX XXX",
                        fontSize = 12.sp,
                        color = OnSurfaceSecondary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "© 2024 All Rights Reserved",
                        fontSize = 12.sp,
                        color = OnSurfaceSecondary.copy(0.5f),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}