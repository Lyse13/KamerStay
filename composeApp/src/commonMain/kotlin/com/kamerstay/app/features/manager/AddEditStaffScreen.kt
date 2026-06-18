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
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.core.utils.APP_NAME
import com.kamerstay.app.viewmodel.ManagerViewModel
import org.koin.compose.viewmodel.koinViewModel
import com.kamerstay.app.core.components.ManagerBottomNavBar

@Composable
fun AddEditStaffScreen(
    navController: NavController,
    staffId: String = ""
) {
    val viewModel = koinViewModel<ManagerViewModel>()
    val state = viewModel.addEditStaffState
    var staffImagePicked by remember { mutableStateOf(false) }
    var roleExpanded by remember { mutableStateOf(false) }

    val roles = listOf(
        "Receptionist", "Manager", "Housekeeper",
        "Concierge", "Security", "Chef"
    )

    val permissions = listOf(
        Triple(Icons.Outlined.RemoveRedEye, "View Only",
            "Can browse bookings and staff lists without editing."),
        Triple(Icons.Outlined.Edit, "Editor",
            "Can manage bookings and edit basic staff details."),
        Triple(Icons.Outlined.AdminPanelSettings, "Admin",
            "Full access to billing, roles, and branch settings.")
    )

    Scaffold(
        containerColor = LocalAppColors.current.background,
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
                        Icon(Icons.Outlined.Menu, contentDescription = null, tint = Secondary)
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
                        .background(OnSurfaceSecondary.copy(0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.Person, contentDescription = null,
                        tint = OnSurfaceSecondary, modifier = Modifier.size(20.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {

                // Breadcrumb
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = "Staff", fontSize = 13.sp, color = OnSurfaceSecondary)
                    Icon(
                        Icons.Outlined.ChevronRight, contentDescription = null,
                        tint = OnSurfaceSecondary, modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = if (staffId.isEmpty()) "Add Member" else "Edit Member",
                        fontSize = 13.sp,
                        color = OnSurfaceSecondary
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Manage Staff Member",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = LocalAppColors.current.textPrimary
                )
                Text(
                    text = "Update profile information and assign access levels for team members.",
                    fontSize = 13.sp,
                    color = OnSurfaceSecondary,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // ── Profile Photo ─────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(LocalAppColors.current.surface)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(modifier = Modifier.size(90.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(85.dp)
                                    .clip(CircleShape)
                                    .background(OnSurfaceSecondary.copy(0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Outlined.Person, contentDescription = null,
                                    tint = OnSurfaceSecondary, modifier = Modifier.size(44.dp)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(Secondary)
                                    .border(2.dp, Color.White, CircleShape)
                                    .align(Alignment.BottomEnd)
                                    .clickable { staffImagePicked = true },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    if (staffImagePicked) Icons.Outlined.Edit
                                    else Icons.Outlined.CameraAlt,
                                    contentDescription = null,
                                    tint = Color.White, modifier = Modifier.size(14.dp)
                                )
                            }
                        }

                        Text(
                            text = "Profile Identity",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = LocalAppColors.current.textPrimary
                        )
                        Text(
                            text = "JPG, GIF or PNG. Max size of 800K",
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary
                        )

                        HorizontalDivider(color = Divider)

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(
                                onClick = { staffImagePicked = true },
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = "Upload New",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = OnPrimary
                                )
                            }
                            OutlinedButton(
                                onClick = { staffImagePicked = false },
                                shape = RoundedCornerShape(20.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Divider),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = LocalAppColors.current.textPrimary),
                                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                            ) {
                                Text(text = "Remove", fontSize = 13.sp, color = LocalAppColors.current.textPrimary)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Form Fields ───────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(LocalAppColors.current.surface)
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {

                        StaffFormField("Full Name") {
                            OutlinedTextField(
                                value = state.fullName,
                                onValueChange = { state.fullName = it },
                                placeholder = {
                                    Text(
                                        "e.g. Jean Dupont",
                                        color = OnSurfaceSecondary.copy(0.5f)
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Outlined.Person, contentDescription = null,
                                        tint = OnSurfaceSecondary, modifier = Modifier.size(18.dp)
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = staffTextFieldColors(),
                                singleLine = true
                            )
                        }

                        StaffFormField("Role/Position") {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = state.selectedRole,
                                    onValueChange = { },
                                    readOnly = true,
                                    leadingIcon = {
                                        Icon(
                                            Icons.Outlined.Badge, contentDescription = null,
                                            tint = OnSurfaceSecondary, modifier = Modifier.size(18.dp)
                                        )
                                    },
                                    trailingIcon = {
                                        IconButton(onClick = { roleExpanded = true }) {
                                            Icon(
                                                Icons.Outlined.KeyboardArrowDown,
                                                contentDescription = null,
                                                tint = OnSurfaceSecondary
                                            )
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { roleExpanded = true },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = staffTextFieldColors(),
                                    singleLine = true
                                )
                                DropdownMenu(
                                    expanded = roleExpanded,
                                    onDismissRequest = { roleExpanded = false },
                                    modifier = Modifier.background(LocalAppColors.current.surface)
                                ) {
                                    roles.forEach { role ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = role,
                                                    color = if (state.selectedRole == role) Primary
                                                    else LocalAppColors.current.textPrimary
                                                )
                                            },
                                            onClick = {
                                                state.selectedRole = role
                                                roleExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        StaffFormField("Email Address") {
                            OutlinedTextField(
                                value = state.email,
                                onValueChange = { state.email = it },
                                placeholder = {
                                    Text(
                                        "staff@terroir-travel.com",
                                        color = OnSurfaceSecondary.copy(0.5f)
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Outlined.MailOutline, contentDescription = null,
                                        tint = OnSurfaceSecondary, modifier = Modifier.size(18.dp)
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = staffTextFieldColors(),
                                singleLine = true
                            )
                        }

                        StaffFormField("Phone Number") {
                            OutlinedTextField(
                                value = state.phone,
                                onValueChange = { state.phone = it },
                                placeholder = {
                                    Text(
                                        "+237 6XX XXX XXX",
                                        color = OnSurfaceSecondary.copy(0.5f)
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Outlined.Phone, contentDescription = null,
                                        tint = OnSurfaceSecondary, modifier = Modifier.size(18.dp)
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = staffTextFieldColors(),
                                singleLine = true
                            )
                        }

                        // Error
                        state.error?.let {
                            Text(text = it, fontSize = 13.sp, color = ErrorColor)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Permissions Level",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalAppColors.current.textPrimary
                )

                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    permissions.forEach { (icon, label, desc) ->
                        val isSelected = state.selectedPermission == label
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isSelected) Primary.copy(0.08f) else Color.White
                                )
                                .border(
                                    if (isSelected) 1.5.dp else 1.dp,
                                    if (isSelected) Primary else Divider,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { state.selectedPermission = label }
                                .padding(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(
                                                if (isSelected) Primary.copy(0.15f)
                                                else BackgroundLight
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            icon, contentDescription = null,
                                            tint = if (isSelected) Secondary else OnSurfaceSecondary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = label,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = LocalAppColors.current.textPrimary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = desc,
                                        fontSize = 13.sp,
                                        color = OnSurfaceSecondary,
                                        lineHeight = 17.sp
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .size(22.dp)
                                        .clip(CircleShape)
                                        .border(
                                            2.dp,
                                            if (isSelected) Primary else Divider,
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isSelected) {
                                        Box(
                                            modifier = Modifier
                                                .size(12.dp)
                                                .clip(CircleShape)
                                                .background(Primary)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (state.fullName.isEmpty() || state.email.isEmpty()) {
                            state.error = "Please fill all required fields."
                        } else {
                            state.error = null
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            Icons.AutoMirrored.Outlined.Send, contentDescription = null,
                            tint = OnPrimary, modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Invite Staff Member",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OnPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(28.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Divider),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = LocalAppColors.current.textPrimary)
                ) {
                    Text(text = "Cancel", fontSize = 15.sp, color = LocalAppColors.current.textPrimary)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Primary.copy(0.08f))
                        .padding(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            Icons.Outlined.Info, contentDescription = null,
                            tint = Secondary, modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "An invitation email will be sent to the user with a temporary password and security setup instructions.",
                            fontSize = 12.sp,
                            color = OnSurfaceSecondary,
                            lineHeight = 17.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun StaffFormField(label: String, content: @Composable () -> Unit) {
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
fun staffTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Primary,
    unfocusedBorderColor = Divider,
    focusedContainerColor = LocalAppColors.current.surface,
    unfocusedContainerColor = LocalAppColors.current.surface,
    focusedTextColor = LocalAppColors.current.inputText,
    unfocusedTextColor = LocalAppColors.current.inputText,
    cursorColor = Primary
)