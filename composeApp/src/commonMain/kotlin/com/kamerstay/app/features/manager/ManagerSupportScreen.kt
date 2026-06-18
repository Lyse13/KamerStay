package com.kamerstay.app.features.manager

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.NavigationState
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.mock.SupportMockData
import com.kamerstay.app.viewmodel.ManagerViewModel
import org.koin.compose.viewmodel.koinViewModel
import com.kamerstay.app.core.components.ManagerBottomNavBar

@Composable
fun ManagerSupportScreen(navController: NavController) {

    val viewModel = koinViewModel<ManagerViewModel>()
    val state = viewModel.supportState
    var showContactDialog by remember { mutableStateOf(false) }

    if (showContactDialog) {
        AlertDialog(
            onDismissRequest = { showContactDialog = false },
            title = { Text("Contact Support", fontWeight = FontWeight.Bold, color = LocalAppColors.current.textPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Our team is available 24/7:", color = OnSurfaceSecondary, fontSize = 14.sp)
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Outlined.Chat, contentDescription = null, tint = Primary, modifier = Modifier.size(18.dp))
                        Text("Live chat — ~2 min wait", fontWeight = FontWeight.SemiBold, color = LocalAppColors.current.textPrimary)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Outlined.MailOutline, contentDescription = null, tint = Primary, modifier = Modifier.size(18.dp))
                        Text("managers@kamerstay.cm", fontWeight = FontWeight.SemiBold, color = LocalAppColors.current.textPrimary)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Outlined.Call, contentDescription = null, tint = Primary, modifier = Modifier.size(18.dp))
                        Text("+237 6 55 00 33 44", fontWeight = FontWeight.SemiBold, color = LocalAppColors.current.textPrimary)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showContactDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    shape = RoundedCornerShape(10.dp)
                ) { Text("Got it", color = OnPrimary) }
            },
            containerColor = LocalAppColors.current.surface,
            shape = RoundedCornerShape(16.dp)
        )
    }

    Scaffold(
        containerColor = LocalAppColors.current.background,
        bottomBar = {
            ManagerBottomNavBar(navController = navController, currentRoute = "profile")
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
                        IconButton(onClick = { navController.navigate(Routes.ManagerProfile.route) }) {
                            Icon(
                                Icons.Outlined.Menu,
                                contentDescription = null,
                                tint = Secondary
                            )
                        }
                        Text(
                            text = "Support",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Secondary
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Secondary)
                            .clickable { navController.navigate(Routes.ManagerProfile.route) },
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
            }

            // ── Header ────────────────────────────────
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = "How can we help?",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = LocalAppColors.current.textPrimary
                    )
                    Text(
                        text = "Search our knowledge base or browse categories below.",
                        fontSize = 13.sp,
                        color = OnSurfaceSecondary,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Search Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(28.dp))
                            .background(LocalAppColors.current.surface)
                            .border(1.dp, Divider, RoundedCornerShape(28.dp))
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.Search,
                            contentDescription = null,
                            tint = OnSurfaceSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        BasicTextField(
                            value = state.searchQuery,
                            onValueChange = { state.searchQuery = it },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(fontSize = 14.sp, color = LocalAppColors.current.textPrimary),
                            decorationBox = { inner ->
                                if (state.searchQuery.isEmpty()) {
                                    Text(
                                        "Search FAQs, guides, or issues...",
                                        fontSize = 14.sp,
                                        color = OnSurfaceSecondary.copy(0.5f)
                                    )
                                }
                                inner()
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // ── Browse Categories ─────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Browse Categories",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Secondary
                    )
                    Text(
                        text = "View All FAQ",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Primary,
                        modifier = Modifier.clickable {
                            NavigationState.helpCenterRole = "manager"
                            navController.navigate(Routes.HelpCenter.route)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Featured card
                    val featured = SupportMockData.categories.first()
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Primary.copy(0.1f))
                            .clickable {
                                NavigationState.helpCenterRole = "manager"
                                navController.navigate(Routes.HelpCenter.route)
                            }
                            .padding(16.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(LocalAppColors.current.surface),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Outlined.Payments,
                                        contentDescription = null,
                                        tint = Secondary,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Icon(
                                    Icons.Outlined.ArrowForward,
                                    contentDescription = null,
                                    tint = Secondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = featured.title,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Secondary
                            )
                            Text(
                                text = featured.subtitle,
                                fontSize = 13.sp,
                                color = OnSurfaceSecondary
                            )
                        }
                    }

                    // 2x2 grid
                    val gridItems = SupportMockData.categories.drop(1)
                    gridItems.chunked(2).forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            row.forEach { category ->
                                val icon = supportCategoryIcon(category.icon)
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(LocalAppColors.current.surface)
                                        .clickable {
                                            NavigationState.helpCenterRole = "manager"
                                            navController.navigate(Routes.HelpCenter.route)
                                        }
                                        .padding(16.dp)
                                ) {
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
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
                                        Text(
                                            text = category.title,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = LocalAppColors.current.textPrimary,
                                            lineHeight = 18.sp
                                        )
                                    }
                                }
                            }
                            if (row.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // ── Contact Support ───────────────────────
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = "Contact Support",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = LocalAppColors.current.textPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(LocalAppColors.current.surface)
                    ) {
                        // Live Chat
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Secondary)
                                .clickable { showContactDialog = true }
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
                                            .clip(CircleShape)
                                            .background(Color.White.copy(0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Outlined.Chat,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "Live Chat",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "Wait time: ~2 mins",
                                            fontSize = 12.sp,
                                            color = Color.White.copy(0.7f)
                                        )
                                    }
                                }
                                Icon(
                                    Icons.Outlined.ChevronRight,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        // Email Support
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showContactDialog = true }
                                .padding(16.dp),
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
                                        .clip(CircleShape)
                                        .background(Primary.copy(0.1f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Outlined.MailOutline,
                                        contentDescription = null,
                                        tint = Secondary,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Column {
                                    Text(
                                        text = "Email Support",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = LocalAppColors.current.textPrimary
                                    )
                                    Text(
                                        text = "Response within 24 hours",
                                        fontSize = 12.sp,
                                        color = OnSurfaceSecondary
                                    )
                                }
                            }
                            Icon(
                                Icons.Outlined.ChevronRight,
                                contentDescription = null,
                                tint = OnSurfaceSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // ── Trending Topics ───────────────────────
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = "Trending Topics",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = LocalAppColors.current.textPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(LocalAppColors.current.surface)
                    ) {
                        Column {
                            SupportMockData.trendingTopics.forEachIndexed { index, topic ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            NavigationState.helpCenterRole = "manager"
                                            navController.navigate(Routes.HelpCenter.route)
                                        }
                                        .padding(horizontal = 16.dp, vertical = 16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = topic.title,
                                        fontSize = 14.sp,
                                        color = LocalAppColors.current.textPrimary,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Icon(
                                        Icons.Outlined.OpenInNew,
                                        contentDescription = null,
                                        tint = OnSurfaceSecondary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                if (index < SupportMockData.trendingTopics.lastIndex) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                        color = Divider
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // ── Footer ────────────────────────────────
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "KamerStay Manager v2.4.1",
                        fontSize = 12.sp,
                        color = OnSurfaceSecondary.copy(0.5f)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Privacy Policy",
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary.copy(0.5f),
                            modifier = Modifier.clickable {
                                navController.navigate(Routes.ManagerPrivacyTerms.route)
                            }
                        )
                        Text(
                            text = "•",
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary.copy(0.5f)
                        )
                        Text(
                            text = "Terms of Service",
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary.copy(0.5f),
                            modifier = Modifier.clickable {
                                navController.navigate(Routes.ManagerPrivacyTerms.route)
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ── Support Category Icon Helper ──────────────────────────
@Composable
fun supportCategoryIcon(icon: String): ImageVector = when (icon) {
    "payments" -> Icons.Outlined.Payments
    "booking" -> Icons.Outlined.BookOnline
    "tech" -> Icons.Outlined.PhoneAndroid
    "shield" -> Icons.Outlined.Shield
    "settings" -> Icons.Outlined.Settings
    else -> Icons.Outlined.HelpOutline
}