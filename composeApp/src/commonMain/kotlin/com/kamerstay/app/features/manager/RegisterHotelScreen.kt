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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.viewmodel.ManagerViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterHotelScreen(navController: NavController) {

    val viewModel = koinViewModel<ManagerViewModel>()
    val state = viewModel.registerHotelState
    var categoryExpanded by remember { mutableStateOf(false) }

    val categories = listOf("Hotel", "Resort", "Villa", "Boutique", "Hostel", "Apart-hotel")

    Scaffold(
        containerColor = BackgroundLight,
        bottomBar = {
            NavigationBar(containerColor = Color.White, tonalElevation = 0.dp) {
                listOf(
                    Icons.Outlined.Home to "Home",
                    Icons.Outlined.Explore to "Explore",
                    Icons.Outlined.Hotel to "Register",
                    Icons.Outlined.Person to "Profile"
                ).forEachIndexed { index, (icon, label) ->
                    NavigationBarItem(
                        selected = index == 2,
                        onClick = {
                            when (index) {
                                0 -> navController.navigate(Routes.ManagerDashboard.route)
                                3 -> navController.navigate(Routes.ManagerProfile.route)
                            }
                        },
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label, fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Secondary,
                            selectedTextColor = Secondary,
                            indicatorColor = Primary.copy(0.15f),
                            unselectedIconColor = OnSurfaceSecondary,
                            unselectedTextColor = OnSurfaceSecondary
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Secondary
                    )
                }
                Text(
                    text = "MyStays",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Secondary
                )
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {

                Text(
                    text = "Grow your business with",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextDark,
                    lineHeight = 32.sp
                )
                Text(
                    text = "MyStays",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Secondary
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Join our curated community of premium accommodations. Complete the details to list your property and start receiving bookings today.",
                    fontSize = 14.sp,
                    color = OnSurfaceSecondary,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Benefits
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        BenefitRow(
                            icon = Icons.Outlined.Shield,
                            title = "Verified Identity",
                            subtitle = "We prioritize safety and trust in our marketplace."
                        )
                        HorizontalDivider(color = Divider)
                        BenefitRow(
                            icon = Icons.Outlined.SupportAgent,
                            title = "24/7 Manager Support",
                            subtitle = "Dedicated account specialists for our hotel partners."
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Hotel Details
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White)
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Outlined.Info,
                                contentDescription = null,
                                tint = Secondary,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Hotel Details",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        RegisterFormField(label = "Hotel Name") {
                            OutlinedTextField(
                                value = state.hotelName,
                                onValueChange = { state.hotelName = it },
                                placeholder = {
                                    Text(
                                        "e.g. The Azure Resort",
                                        color = OnSurfaceSecondary.copy(0.5f)
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = registerTextFieldColors(),
                                singleLine = true
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        RegisterFormField(label = "Category") {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = state.selectedCategory,
                                    onValueChange = { },
                                    placeholder = {
                                        Text(
                                            "Select Category",
                                            color = OnSurfaceSecondary.copy(0.5f)
                                        )
                                    },
                                    readOnly = true,
                                    trailingIcon = {
                                        IconButton(onClick = { categoryExpanded = true }) {
                                            Icon(
                                                Icons.Outlined.KeyboardArrowDown,
                                                contentDescription = null,
                                                tint = OnSurfaceSecondary
                                            )
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { categoryExpanded = true },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = registerTextFieldColors(),
                                    singleLine = true
                                )
                                DropdownMenu(
                                    expanded = categoryExpanded,
                                    onDismissRequest = { categoryExpanded = false },
                                    modifier = Modifier.background(Color.White)
                                ) {
                                    categories.forEach { cat ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = cat,
                                                    color = if (state.selectedCategory == cat)
                                                        Primary else TextDark
                                                )
                                            },
                                            onClick = {
                                                state.selectedCategory = cat
                                                categoryExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        RegisterFormField(label = "Full Address") {
                            OutlinedTextField(
                                value = state.address,
                                onValueChange = { state.address = it },
                                placeholder = {
                                    Text(
                                        "Street, City, Country, Zip Code",
                                        color = OnSurfaceSecondary.copy(0.5f)
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Outlined.Place,
                                        contentDescription = null,
                                        tint = OnSurfaceSecondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = registerTextFieldColors(),
                                singleLine = true
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        RegisterFormField(label = "Description") {
                            OutlinedTextField(
                                value = state.description,
                                onValueChange = { state.description = it },
                                placeholder = {
                                    Text(
                                        "Tell travelers what makes your property unique...",
                                        color = OnSurfaceSecondary.copy(0.5f),
                                        fontSize = 13.sp
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = registerTextFieldColors(),
                                maxLines = 5
                            )
                        }

                        // Error
                        state.error?.let {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = it,
                                fontSize = 13.sp,
                                color = ErrorColor
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Photos
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White)
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Outlined.AddAPhoto,
                                contentDescription = null,
                                tint = Secondary,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Property Photos",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.5.dp, Divider, RoundedCornerShape(12.dp))
                                .background(BackgroundLight)
                                .clickable { }
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(CircleShape)
                                        .background(Primary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Outlined.FileUpload,
                                        contentDescription = null,
                                        tint = OnPrimary,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                                Text(
                                    text = "Upload main property image",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextDark
                                )
                                Text(
                                    text = "PNG, JPG or JPEG (max. 10MB)",
                                    fontSize = 12.sp,
                                    color = OnSurfaceSecondary
                                )
                                OutlinedButton(
                                    onClick = { },
                                    shape = RoundedCornerShape(20.dp),
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp, Divider
                                    ),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = TextDark
                                    )
                                ) {
                                    Text(
                                        text = "Browse Files",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = TextDark
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                TextButton(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Save Draft",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = OnSurfaceSecondary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (state.hotelName.isEmpty() || state.selectedCategory.isEmpty()) {
                            state.error = "Please fill in all required fields."
                        } else {
                            state.error = null
                            navController.navigate(Routes.ManagerDashboard.route)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Secondary)
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Complete Registration",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun BenefitRow(icon: ImageVector, title: String, subtitle: String) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Primary.copy(0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = Secondary, modifier = Modifier.size(20.dp))
        }
        Column {
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextDark)
            Text(text = subtitle, fontSize = 13.sp, color = OnSurfaceSecondary, lineHeight = 17.sp)
        }
    }
}

@Composable
fun RegisterFormField(label: String, content: @Composable () -> Unit) {
    Column {
        Text(text = label, fontSize = 13.sp, color = OnSurfaceSecondary, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(6.dp))
        content()
    }
}

@Composable
fun registerTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Primary,
    unfocusedBorderColor = Divider,
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    focusedTextColor = TextDark,
    unfocusedTextColor = TextDark,
    cursorColor = Primary
)