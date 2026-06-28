package com.kamerstay.app.features.manager

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.components.ImageUploadCard
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.model.PricingResponse
import com.kamerstay.app.viewmodel.ManagerViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddEditRoomScreen(navController: NavController, hotelId: String, roomId: String? = null) {

    val viewModel = koinViewModel<ManagerViewModel>()
    val state = viewModel.roomFormState

    // Si on édite, on charge les données de la chambre
    LaunchedEffect(roomId) {
        if (roomId != null) viewModel.loadRoomForEdit(roomId)
        else state.reset()
    }

    var showCategoryDropdown by remember { mutableStateOf(false) }

    val categories = listOf(
        "Standard", "Deluxe", "Suite",
        "Executive Suite", "Deluxe Suite",
        "Presidential Suite", "Family Room"
    )

    val amenities = listOf(
        "Free Wi-Fi" to Icons.Outlined.Wifi,
        "AC" to Icons.Outlined.AcUnit,
        "Pool Access" to Icons.Outlined.Pool,
        "Smart TV" to Icons.Outlined.Tv,
        "Mini-bar" to Icons.Outlined.LocalBar,
        "Balcony" to Icons.Outlined.Deck,
        "King Bed" to Icons.Outlined.Hotel,
        "City View" to Icons.Outlined.LocationCity,
        "Breakfast" to Icons.Outlined.FreeBreakfast,
        "Parking" to Icons.Outlined.LocalParking,
    )

    Scaffold(
        containerColor = LocalAppColors.current.background,
        topBar = {
            // ── Top Bar ───────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
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
                        text = "Manage Room",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Secondary
                    )
                }

                // Save Changes button
                Button(
                    onClick = {
                        viewModel.saveRoom(hotelId) { navController.popBackStack() }
                    },
                    enabled = !state.isLoading,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Secondary,
                        disabledContainerColor = Secondary.copy(0.4f)
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(14.dp), color = Color.White, strokeWidth = 2.dp)
                    } else {
                        Text(
                            text = if (roomId != null) "Modifier" else "Créer",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.saveRoom(hotelId) { navController.popBackStack() }
                },
                containerColor = Secondary,
                contentColor = Color.White,
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Outlined.Save, contentDescription = "Save")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // ── Room Photos ───────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(LocalAppColors.current.surface)
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Room Photos",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = LocalAppColors.current.textPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Main large photo
                        Box(
                            modifier = Modifier
                                .weight(2f)
                                .height(160.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0xFF1A2A3A),
                                            Color(0xFF0D1A28)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Outlined.Hotel,
                                contentDescription = null,
                                tint = Color.White.copy(0.3f),
                                modifier = Modifier.size(40.dp)
                            )
                        }

                        // Small photos grid
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            repeat(2) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(72.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            brush = Brush.verticalGradient(
                                                colors = listOf(
                                                    Color(0xFF2A3A4A),
                                                    Color(0xFF1A2A3A)
                                                )
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Bed,
                                        contentDescription = null,
                                        tint = Color.White.copy(0.3f),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            // Add More
                            var roomImagePicked by remember { mutableStateOf(false) }
                            ImageUploadCard(
                                imagePicked = roomImagePicked,
                                onPickImage = { roomImagePicked = true },
                                onRemoveImage = { roomImagePicked = false },
                                label = "Add photo",
                                compact = true // ← c'est tout !
                            )
                        }
                    }
                }
            }

            // ── Room Identification ───────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(LocalAppColors.current.surface)
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Room Identification",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = LocalAppColors.current.textPrimary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Room Number
                    RoomFormLabel("Room Number")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state.roomNumber,
                        onValueChange = { state.roomNumber = it },
                        placeholder = {
                            Text(
                                "Suite 402",
                                color = OnSurfaceSecondary.copy(0.5f)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = roomTextFieldColors(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Room Category
                    RoomFormLabel("Room Category")
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = state.category,
                            onValueChange = { },
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = {
                                    showCategoryDropdown = true
                                }) {
                                    Icon(
                                        Icons.Filled.KeyboardArrowDown,
                                        contentDescription = null,
                                        tint = OnSurfaceSecondary
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showCategoryDropdown = true },
                            shape = RoundedCornerShape(10.dp),
                            colors = roomTextFieldColors(),
                            singleLine = true
                        )
                        DropdownMenu(
                            expanded = showCategoryDropdown,
                            onDismissRequest = { showCategoryDropdown = false },
                            modifier = Modifier.background(LocalAppColors.current.surface)
                        ) {
                            categories.forEach { category ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = category,
                                            color = if (state.category == category)
                                                Primary else LocalAppColors.current.textPrimary
                                        )
                                    },
                                    onClick = {
                                        state.category = category
                                        showCategoryDropdown = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Room Description
                    RoomFormLabel("Room Description")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state.description,
                        onValueChange = { state.description = it },
                        placeholder = {
                            Text(
                                "Enter a compelling description for guests...",
                                color = OnSurfaceSecondary.copy(0.5f),
                                fontSize = 13.sp
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = roomTextFieldColors(),
                        maxLines = 4
                    )
                }
            }

            // ── Pricing & Status ──────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceVariant)
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Pricing & Status",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = LocalAppColors.current.textPrimary
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Primary)
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Active",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = OnPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    RoomFormLabel("Price per Night")
                    Spacer(modifier = Modifier.height(8.dp))

                    // Price field
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(LocalAppColors.current.surface)
                            .padding(horizontal = 16.dp, vertical = 14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "$ ",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceSecondary
                            )
                            OutlinedTextField(
                                value = state.pricePerNight,
                                onValueChange = { state.pricePerNight = it },
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = androidx.compose.ui.text.TextStyle(
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = LocalAppColors.current.textPrimary
                                ),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent,
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    cursorColor = Primary
                                ),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Decimal
                                ),
                                singleLine = true
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // ── AI Pricing Suggestion ─────────────────
                    AiPricingCard(
                        isLoading = state.isPricingLoading,
                        suggestion = state.pricingSuggestion,
                        onRequest = { viewModel.suggestPricing() },
                        onApply = { price ->
                            state.pricePerNight = price.toString()
                            state.pricingSuggestion = null
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Instant Booking toggle
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(LocalAppColors.current.surface)
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Instant Booking",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = LocalAppColors.current.textPrimary
                                )
                                Text(
                                    text = "Guests don't need approval",
                                    fontSize = 12.sp,
                                    color = OnSurfaceSecondary
                                )
                            }
                            Switch(
                                checked = true,
                                onCheckedChange = { },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = Primary
                                )
                            )
                        }
                    }
                }
            }

            // ── Amenities ─────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(LocalAppColors.current.surface)
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Amenities",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = LocalAppColors.current.textPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Selected amenities
                        state.selectedFeatures.forEach { feature ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Primary)
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    val icon = amenities.find { it.first == feature }?.second
                                    icon?.let {
                                        Icon(
                                            it,
                                            contentDescription = null,
                                            tint = OnPrimary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                    Text(
                                        text = feature,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = OnPrimary
                                    )
                                    Icon(
                                        Icons.Filled.Close,
                                        contentDescription = null,
                                        tint = OnPrimary,
                                        modifier = Modifier
                                            .size(14.dp)
                                            .clickable {
                                                state.selectedFeatures =
                                                    state.selectedFeatures - feature
                                            }
                                    )
                                }
                            }
                        }

                        // Unselected amenities
                        amenities
                            .filter { it.first !in state.selectedFeatures }
                            .forEach { (name, icon) ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .border(
                                            1.dp,
                                            OnSurfaceSecondary.copy(0.3f),
                                            RoundedCornerShape(20.dp)
                                        )
                                        .background(Color.Transparent)
                                        .clickable {
                                            state.selectedFeatures =
                                                state.selectedFeatures + name
                                        }
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            Icons.Filled.Add,
                                            contentDescription = null,
                                            tint = OnSurfaceSecondary,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Text(
                                            text = name,
                                            fontSize = 13.sp,
                                            color = OnSurfaceSecondary
                                        )
                                    }
                                }
                            }

                        // Add Amenity button
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .border(
                                    1.5.dp,
                                    OnSurfaceSecondary.copy(0.4f),
                                    RoundedCornerShape(20.dp)
                                )
                                .clickable { }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    Icons.Filled.Add,
                                    contentDescription = null,
                                    tint = OnSurfaceSecondary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "Add Amenity",
                                    fontSize = 13.sp,
                                    color = OnSurfaceSecondary,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            // Bandeau d'erreur
            if (state.error != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(ErrorColor.copy(0.08f))
                        .padding(12.dp)
                ) {
                    Text(state.error ?: "", fontSize = 13.sp, color = ErrorColor)
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

// ── AI Pricing Card ───────────────────────────────────────

private val AiPricingGradient = Brush.linearGradient(colors = listOf(Color(0xFF4A00E0), Color(0xFF8E2DE2)))

@Composable
private fun AiPricingCard(
    isLoading: Boolean,
    suggestion: PricingResponse?,
    onRequest: () -> Unit,
    onApply: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF4A00E0).copy(alpha = 0.06f))
            .border(1.dp, Color(0xFF8E2DE2).copy(alpha = 0.25f), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        Icons.Outlined.AutoAwesome,
                        contentDescription = null,
                        tint = Color(0xFF8E2DE2),
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = "Suggestion IA",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF4A00E0)
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(AiPricingGradient)
                        .clickable(enabled = !isLoading) { onRequest() }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(14.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = if (suggestion != null) "Relancer" else "Analyser",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }

            if (suggestion != null) {
                HorizontalDivider(color = Color(0xFF8E2DE2).copy(alpha = 0.15f))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "${"%,d".format(suggestion.suggestedPrice).replace(",", ".")} FCFA",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF4A00E0)
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            SeasonChip(suggestion.season)
                            DemandChip(suggestion.demandLevel)
                        }
                    }
                    Button(
                        onClick = { onApply(suggestion.suggestedPrice) },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A00E0)),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text("Appliquer", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
                Text(
                    text = suggestion.explanation,
                    fontSize = 12.sp,
                    color = OnSurfaceSecondary,
                    lineHeight = 17.sp
                )
            } else if (!isLoading) {
                Text(
                    text = "Kamsa analyse la saisonnalité et le taux d'occupation pour suggérer un prix optimal.",
                    fontSize = 12.sp,
                    color = OnSurfaceSecondary,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

@Composable
private fun SeasonChip(season: String) {
    val color = when (season) {
        "haute", "très haute" -> Color(0xFFE53935)
        "normale" -> Color(0xFF43A047)
        else -> OnSurfaceSecondary
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(text = "Saison $season", fontSize = 11.sp, color = color, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun DemandChip(demandLevel: String) {
    val (label, color) = when (demandLevel) {
        "high" -> "Forte demande" to Color(0xFFE53935)
        "peak" -> "Pic" to Color(0xFFB71C1C)
        "medium" -> "Demande moyenne" to Color(0xFFFF8F00)
        else -> "Faible demande" to OnSurfaceSecondary
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(text = label, fontSize = 11.sp, color = color, fontWeight = FontWeight.Medium)
    }
}

// ── Room Form Helpers ─────────────────────────────────────
@Composable
fun RoomFormLabel(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = LocalAppColors.current.textPrimary
    )
}

@Composable
fun roomTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Primary,
    unfocusedBorderColor = Color(0xFFDDE4EA),
    focusedContainerColor = LocalAppColors.current.surface,
    unfocusedContainerColor = LocalAppColors.current.surface,
    focusedTextColor = LocalAppColors.current.inputText,
    unfocusedTextColor = LocalAppColors.current.inputText,
    cursorColor = Primary
)