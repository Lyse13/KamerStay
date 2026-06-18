package com.kamerstay.app.features.traveler

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.core.utils.APP_NAME
import com.kamerstay.app.core.components.TravelerBottomNavBar

@Composable
fun PrivacyTermsScreen(navController: NavController) {

    Scaffold(
        containerColor = LocalAppColors.current.background,
        bottomBar = {
            TravelerBottomNavBar(navController = navController, selectedTab = 3)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // ── Top Bar ───────────────────────────────
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
                            text = APP_NAME,
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

            // ── Legal Center Badge ────────────────────
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Primary)
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
                                text = "LEGAL CENTER",
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

            // ── Terms & Privacy header ────────────────
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Terms & Privacy",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = LocalAppColors.current.textPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Last updated: October 24, 2023. Please review our updated policies regarding bookings and user data protection.",
                        fontSize = 13.sp,
                        color = OnSurfaceSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // ── Quick Nav Cards ───────────────────────
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    LegalNavCard(
                        icon = Icons.Outlined.Description,
                        title = "Terms of Service",
                        subtitle = "Usage rules & agreements"
                    )
                    LegalNavCard(
                        icon = Icons.Outlined.Shield,
                        title = "Privacy Policy",
                        subtitle = "How we handle your data"
                    )
                    LegalNavCard(
                        icon = Icons.Outlined.Cookie,
                        title = "Cookie Policy",
                        subtitle = "Tracking and preferences"
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))
            }

            // ── Section 1: Terms of Service ───────────
            item {
                LegalSection(
                    number = "1",
                    title = "Terms of Service"
                ) {
                    LegalParagraph(
                        text = "Welcome to $APP_NAME. By accessing our platform, you agree to comply with and be bound by the following terms and conditions of use, which together with our privacy policy govern $APP_NAME's relationship with you in relation to this website and service."
                    )

                    LegalParagraph(
                        text = "The term '$APP_NAME' or 'us' or 'we' refers to the owner of the website. The term 'you' refers to the user or viewer of our website."
                    )

                    // Quote box
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
                                text = "Booking Guarantee",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = LocalAppColors.current.textPrimary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "\"All reservations made through $APP_NAME are subject to the specific hotel's cancellation policy. We act as an intermediary to ensure your booking is secured and confirmed with the venue.\"",
                                fontSize = 13.sp,
                                color = OnSurfaceSecondary,
                                lineHeight = 18.sp,
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LegalParagraph(
                        text = "The use of this website is subject to the following terms of use:"
                    )

                    // Bullet points
                    listOf(
                        "The content of the pages of this website is for your general information and use only. It is subject to change without notice.",
                        "This website uses cookies to monitor browsing preferences. If you do allow cookies to be used, personal information may be stored by us for use by third parties.",
                        "Neither we nor any third parties provide any warranty or guarantee as to the accuracy, timeliness, performance, completeness or suitability of the information and materials found or offered on this website for any particular purpose."
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
                                    .background(Primary)
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

            // ── Section 2: Privacy Policy ─────────────
            item {
                LegalSection(
                    number = "2",
                    title = "Privacy Policy"
                ) {
                    LegalParagraph(
                        text = "$APP_NAME is committed to ensuring that your privacy is protected. Should we ask you to provide certain information by which you can be identified when using this website, then you can be assured that it will only be used in accordance with this privacy statement."
                    )

                    // Info boxes
                    LegalInfoBox(
                        title = "What we collect",
                        content = "Name, contact information including email address, demographic information such as postcode, preferences and interests."
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    LegalInfoBox(
                        title = "Security",
                        content = "We are committed to ensuring that your information is secure. In order to prevent unauthorised access or disclosure we have put in place suitable physical and electronic procedures."
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    LegalParagraph(
                        text = "We will not sell, distribute or lease your personal information to third parties unless we have your permission or are required by law to do so. We may use your personal information to send you promotional information about third parties which we think you may find interesting if you tell us that you wish this to happen."
                    )
                }
            }

            // ── Section 3: Limitation of Liability ────
            item {
                LegalSection(
                    number = "3",
                    title = "Limitation of Liability"
                ) {
                    LegalParagraph(
                        text = "In no event will we be liable for any loss or damage including without limitation, indirect or consequential loss or damage, or any loss or damage whatsoever arising from loss of data or profits arising out of, or in connection with, the use of this website."
                    )
                }
            }

            // ── Section 4: Governing Law ──────────────
            item {
                LegalSection(
                    number = "4",
                    title = "Governing Law"
                ) {
                    LegalParagraph(
                        text = "Your use of this website and any dispute arising out of such use of the website is subject to the laws of the Republic of Cameroon."
                    )
                }
            }

            // ── Footer ────────────────────────────────
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
                    // Decorative line
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(3.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Primary)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "$APP_NAME Legal Affairs Dept.",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = LocalAppColors.current.textPrimary,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "© 2023 All Rights Reserved",
                        fontSize = 12.sp,
                        color = OnSurfaceSecondary,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ── Legal Nav Card ────────────────────────────────────────
@Composable
fun LegalNavCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(LocalAppColors.current.surface)
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Primary.copy(0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = Secondary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LocalAppColors.current.textPrimary
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = OnSurfaceSecondary
                )
            }
            Icon(
                Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = OnSurfaceSecondary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

// ── Legal Section ─────────────────────────────────────────
@Composable
fun LegalSection(
    number: String,
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Text(
            text = "$number. $title",
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            color = LocalAppColors.current.textPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        content()

        Spacer(modifier = Modifier.height(8.dp))
    }
}

// ── Legal Paragraph ───────────────────────────────────────
@Composable
fun LegalParagraph(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        color = OnSurfaceSecondary,
        lineHeight = 21.sp
    )
    Spacer(modifier = Modifier.height(10.dp))
}

// ── Legal Info Box ────────────────────────────────────────
@Composable
fun LegalInfoBox(title: String, content: String) {
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
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = LocalAppColors.current.textPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = content,
                fontSize = 13.sp,
                color = OnSurfaceSecondary,
                lineHeight = 18.sp
            )
        }
    }
}