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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.viewmodel.ManagerViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ManageHotelScreen(navController: NavController) {

    val viewModel = koinViewModel<ManagerViewModel>()
    val state = viewModel.manageHotelState
    var typeExpanded by remember { mutableStateOf(false) }

    val propertyTypes = listOf("Hotel", "Boutique Hotel", "Resort", "Villa", "Apart-hotel")

    val amenityIcons = mapOf(
        "High-speed Wi-Fi" to Icons.Outlined.Wifi,
        "Swimming Pool" to Icons.Outlined.Pool,
        "Full-service Spa" to Icons.Outlined.Spa,
        "24/7 Gym" to Icons.Outlined.FitnessCenter,
        "In-house Dining" to Icons.Outlined.Restaurant,
        "Secure Parking" to Icons.Outlined.LocalParking
    )

    Scaffold(
        containerColor = BackgroundLight,
        bottomBar = {
            NavigationBar(containerColor = Color.White, tonalElevation = 0.dp) {
                listOf(
                    Icons.Outlined.Explore to "Explore",
                    Icons.Outlined.BookOnline to "Bookings",
                    Icons.Outlined.People to "Staff",
                    Icons.Outlined.Person to "Profile"
                ).forEachIndexed { index, (icon, label) ->
                    NavigationBarItem(
                        selected = index == 2,
                        onClick = {
                            when (index) {
                                0 -> navController.navigate(Routes.HotelSearch.route)
                                1 -> navController.navigate(Routes.Reservations.route)
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
                        Icon(Icons.Outlined.Menu, contentDescription = null, tint = Secondary)
                    }
                    Text(
                        text = "Terroir Travel",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Secondary
                    )
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(OnSurfaceSecondary.copy(0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.Person,
                        contentDescription = null,
                        tint = OnSurfaceSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {

                Text(
                    text = "PROPERTY MANAGEMENT",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Edit Property Details",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextDark
                )
                Text(
                    text = "Updates to your property will be synced across all booking channels.",
                    fontSize = 13.sp,
                    color = OnSurfaceSecondary,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedButton(
                    onClick = { navController.navigate(Routes.HotelDetails.createRoute("1")) },
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Divider),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextDark),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Icon(
                        Icons.Outlined.RemoveRedEye,
                        contentDescription = null,
                        tint = OnSurfaceSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Preview Listing", fontSize = 13.sp, color = TextDark)
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ── Basic Information ─────────────────
                ManageSection(icon = Icons.Outlined.Apartment, title = "Basic Information") {

                    ManageField("Property Name") {
                        OutlinedTextField(
                            value = state.propertyName,
                            onValueChange = { state.propertyName = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = manageTextFieldColors(),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    ManageField("Property Type") {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = state.propertyType,
                                onValueChange = { },
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = { typeExpanded = true }) {
                                        Icon(
                                            Icons.Outlined.KeyboardArrowDown,
                                            contentDescription = null,
                                            tint = OnSurfaceSecondary
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { typeExpanded = true },
                                shape = RoundedCornerShape(10.dp),
                                colors = manageTextFieldColors(),
                                singleLine = true
                            )
                            DropdownMenu(
                                expanded = typeExpanded,
                                onDismissRequest = { typeExpanded = false },
                                modifier = Modifier.background(Color.White)
                            ) {
                                propertyTypes.forEach { type ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = type,
                                                color = if (state.propertyType == type) Primary
                                                else TextDark
                                            )
                                        },
                                        onClick = {
                                            state.propertyType = type
                                            typeExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    ManageField("Street Address") {
                        OutlinedTextField(
                            value = state.streetAddress,
                            onValueChange = { state.streetAddress = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = manageTextFieldColors(),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            ManageField("City") {
                                OutlinedTextField(
                                    value = state.city,
                                    onValueChange = { state.city = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = manageTextFieldColors(),
                                    singleLine = true
                                )
                            }
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            ManageField("Region") {
                                OutlinedTextField(
                                    value = state.region,
                                    onValueChange = { state.region = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = manageTextFieldColors(),
                                    singleLine = true
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    ManageField("Postal Code") {
                        OutlinedTextField(
                            value = state.postalCode,
                            onValueChange = { state.postalCode = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = manageTextFieldColors(),
                            singleLine = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Description ───────────────────────
                ManageSection(icon = Icons.Outlined.Description, title = "Property Description") {
                    ManageField("Public Description") {
                        OutlinedTextField(
                            value = state.description,
                            onValueChange = { state.description = it },
                            modifier = Modifier.fillMaxWidth().height(160.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = manageTextFieldColors(),
                            maxLines = 8
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${state.description.length}/1500 characters",
                        fontSize = 11.sp,
                        color = OnSurfaceSecondary,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Photos ────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Outlined.Photo, contentDescription = null,
                                    tint = Secondary, modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "Property\nPhotos",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextDark,
                                    lineHeight = 22.sp
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.clickable { }
                            ) {
                                Icon(
                                    Icons.Outlined.AddAPhoto, contentDescription = null,
                                    tint = Primary, modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Add\nPhoto",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Primary,
                                    lineHeight = 18.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f).height(110.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(Color(0xFF2A1A0D), Color(0xFF1A0D06))
                                            )
                                        )
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .padding(6.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(Primary)
                                            .padding(horizontal = 6.dp, vertical = 3.dp)
                                    ) {
                                        Text("Main", fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold, color = OnPrimary)
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .weight(1f).height(110.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(Color(0xFF1A2A3A), Color(0xFF0D1A28))
                                            )
                                        )
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f).height(110.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(Color(0xFF1A3A2E), Color(0xFF0D2218))
                                            )
                                        )
                                )
                                Box(
                                    modifier = Modifier
                                        .weight(1f).height(110.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .border(1.5.dp, Divider, RoundedCornerShape(10.dp))
                                        .background(BackgroundLight)
                                        .clickable { },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            Icons.Outlined.Add, contentDescription = null,
                                            tint = OnSurfaceSecondary, modifier = Modifier.size(24.dp)
                                        )
                                        Text("Upload More", fontSize = 12.sp, color = OnSurfaceSecondary)
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Amenities ─────────────────────────
                ManageSection(icon = Icons.Outlined.CheckCircle, title = "Amenities") {
                    val amenityKeys = state.amenityChecked.keys.toList()
                    amenityKeys.forEachIndexed { index, label ->
                        val icon = amenityIcons[label] ?: Icons.Outlined.CheckCircle
                        val checked = state.amenityChecked[label] ?: false

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    state.amenityChecked = state.amenityChecked.toMutableMap()
                                        .also { it[label] = !checked }
                                }
                                .padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    icon, contentDescription = null,
                                    tint = if (checked) Secondary else OnSurfaceSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(text = label, fontSize = 14.sp, color = TextDark)
                            }
                            Checkbox(
                                checked = checked,
                                onCheckedChange = {
                                    state.amenityChecked = state.amenityChecked.toMutableMap()
                                        .also { map -> map[label] = it }
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Secondary,
                                    uncheckedColor = Divider,
                                    checkmarkColor = Color.White
                                )
                            )
                        }
                        if (index < amenityKeys.lastIndex) {
                            HorizontalDivider(color = Divider)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Publishing Status ─────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Outlined.Info, contentDescription = null,
                                tint = Secondary, modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Publishing Status",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Primary)
                            )
                            Text(
                                text = "Live on Terroir Travel",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Primary
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Last edited 3 days ago by J. Doe",
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedButton(
                            onClick = { navController.navigate(Routes.HotelDetails.createRoute("1")) },
                            modifier = Modifier.fillMaxWidth().height(46.dp),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Divider),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextDark)
                        ) {
                            Text("View Public Listing", fontSize = 14.sp, color = TextDark)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Changes are saved locally as you type",
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth().height(54.dp),
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
                            text = "Save Changes",
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
fun ManageSection(icon: ImageVector, title: String, content: @Composable ColumnScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(icon, contentDescription = null, tint = Secondary, modifier = Modifier.size(20.dp))
                Text(text = title, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextDark)
            }
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
fun ManageField(label: String, content: @Composable () -> Unit) {
    Column {
        Text(text = label, fontSize = 12.sp, color = OnSurfaceSecondary, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(6.dp))
        content()
    }
}

@Composable
fun manageTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Primary,
    unfocusedBorderColor = Divider,
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    focusedTextColor = TextDark,
    unfocusedTextColor = TextDark,
    cursorColor = Primary
)