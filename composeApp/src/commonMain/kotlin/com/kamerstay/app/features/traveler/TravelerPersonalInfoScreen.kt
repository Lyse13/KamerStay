package com.kamerstay.app.features.traveler

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.components.AvatarWithImagePicker
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.viewmodel.TravelerViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TravelerPersonalInfoScreen(navController: NavController) {

    val viewModel = koinViewModel<TravelerViewModel>()
    val state = viewModel.travelerPersonalInfoState

    val cities = listOf(
        "Douala, Littoral Region",
        "Yaoundé, Centre Region",
        "Kribi, South Region",
        "Bafoussam, West Region",
        "Buea, South West Region",
        "Limbe, South West Region",
        "Garoua, North Region",
        "Maroua, Far North Region"
    )

    Scaffold(
        containerColor = LocalAppColors.current.background,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LocalAppColors.current.surface)
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Button(
                    onClick = {
                        state.isLoading = true
                        navController.popBackStack()
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
                            text = "Save Changes",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
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
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Secondary
                        )
                    }
                    Text(
                        text = "Personal Information",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Secondary
                    )
                }
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .border(2.dp, Primary, CircleShape)
                        .background(Primary.copy(0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.Person,
                        contentDescription = null,
                        tint = Secondary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            // ── Avatar ────────────────────────────────
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                AvatarWithImagePicker(
                    imagePicked = state.profileImagePicked,
                    onPickImage = { state.profileImagePicked = true }
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = state.fullName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Secondary
                )
                Text(
                    text = "Explorer • Traveler Account",
                    fontSize = 13.sp,
                    color = OnSurfaceSecondary
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            // ── Form Fields ───────────────────────────
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {

                // Full Name
                TravelerInfoField("Full Name") {
                    OutlinedTextField(
                        value = state.fullName,
                        onValueChange = { state.fullName = it },
                        trailingIcon = {
                            Icon(
                                Icons.Outlined.Person,
                                contentDescription = null,
                                tint = OnSurfaceSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = travelerInfoTextFieldColors(),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Email
                TravelerInfoField("Email Address") {
                    OutlinedTextField(
                        value = state.email,
                        onValueChange = { state.email = it },
                        trailingIcon = {
                            Icon(
                                Icons.Outlined.MailOutline,
                                contentDescription = null,
                                tint = OnSurfaceSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = travelerInfoTextFieldColors(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Phone
                TravelerInfoField("Phone Number") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = state.phoneCode,
                            onValueChange = { state.phoneCode = it },
                            modifier = Modifier.width(90.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = travelerInfoTextFieldColors(),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = state.phoneNumber,
                            onValueChange = { state.phoneNumber = it },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = travelerInfoTextFieldColors(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // City / Location
                TravelerInfoField("City / Location") {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = state.city,
                            onValueChange = { },
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { state.cityExpanded = true }) {
                                    Icon(
                                        Icons.Outlined.KeyboardArrowDown,
                                        contentDescription = null,
                                        tint = OnSurfaceSecondary
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { state.cityExpanded = true },
                            shape = RoundedCornerShape(12.dp),
                            colors = travelerInfoTextFieldColors(),
                            singleLine = true
                        )
                        DropdownMenu(
                            expanded = state.cityExpanded,
                            onDismissRequest = { state.cityExpanded = false },
                            modifier = Modifier.background(LocalAppColors.current.surface)
                        ) {
                            cities.forEach { city ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = city,
                                            color = if (state.city == city) Primary else LocalAppColors.current.textPrimary
                                        )
                                    },
                                    onClick = {
                                        state.city = city
                                        state.cityExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Email Notifications toggle
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
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Primary.copy(0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Outlined.Notifications,
                                    contentDescription = null,
                                    tint = Secondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Email Notifications",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = LocalAppColors.current.textPrimary
                                )
                                Text(
                                    text = "Stay updated on your reservations",
                                    fontSize = 12.sp,
                                    color = OnSurfaceSecondary
                                )
                            }
                        }
                        Switch(
                            checked = state.emailNotifications,
                            onCheckedChange = { state.emailNotifications = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Secondary,
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = OnSurfaceSecondary.copy(0.3f)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun TravelerInfoField(label: String, content: @Composable () -> Unit) {
    Column {
        Text(
            text = label,
            fontSize = 13.sp,
            color = OnSurfaceSecondary,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(6.dp))
        content()
    }
}

@Composable
private fun travelerInfoTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Primary,
    unfocusedBorderColor = Divider,
    focusedContainerColor = LocalAppColors.current.surface,
    unfocusedContainerColor = LocalAppColors.current.surface,
    focusedTextColor = LocalAppColors.current.inputText,
    unfocusedTextColor = LocalAppColors.current.inputText,
    cursorColor = Primary
)