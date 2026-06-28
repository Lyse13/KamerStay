package com.kamerstay.app.features.traveler

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.compose.material.icons.outlined.Layers
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.kamerstay.app.core.theme.Primary
import com.kamerstay.app.core.theme.Secondary
import com.kamerstay.app.viewmodel.TravelerViewModel
import org.koin.compose.viewmodel.koinViewModel

@SuppressLint("MissingPermission")
@Composable
fun MapLocationScreen(navController: NavController) {

    val context = LocalContext.current
    val viewModel = koinViewModel<TravelerViewModel>()

    val yaoundeLatLng = LatLng(3.8480, 11.5021)

    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    var isSatellite by remember { mutableStateOf(false) }
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(yaoundeLatLng, 6f)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    LaunchedEffect(Unit) {
        if (!hasPermission) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
        if (viewModel.hotels.isEmpty()) {
            viewModel.loadHotels()
        }
    }

    LaunchedEffect(hasPermission) {
        if (hasPermission) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY, null
            ).addOnSuccessListener { location ->
                location?.let { userLocation = LatLng(it.latitude, it.longitude) }
            }
        }
    }

    LaunchedEffect(userLocation) {
        userLocation?.let { location ->
            cameraPositionState.animate(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.fromLatLngZoom(location, 14f)
                ),
                durationMs = 1000
            )
        }
    }

    val hotels = viewModel.hotels

    Box(modifier = Modifier.fillMaxSize()) {

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = hasPermission,
                mapType = if (isSatellite) MapType.HYBRID else MapType.NORMAL
            ),
            uiSettings = MapUiSettings(
                myLocationButtonEnabled = false,
                zoomControlsEnabled = false,
                compassEnabled = true
            )
        ) {
            userLocation?.let { location ->
                Circle(
                    center = location,
                    radius = 80.0,
                    fillColor = Primary.copy(alpha = 0.15f),
                    strokeColor = Primary,
                    strokeWidth = 3f
                )
            }

            hotels.forEach { hotel ->
                if (hotel.latitude != 0.0 && hotel.longitude != 0.0) {
                    MarkerComposable(
                        state = MarkerState(position = LatLng(hotel.latitude, hotel.longitude)),
                        title = hotel.name,
                        snippet = "${hotel.pricePerNight.toLong()} FCFA / nuit",
                        onClick = {
                            viewModel.selectHotel(hotel.id)
                            false
                        }
                    ) {
                        // Marqueur personnalisé avec le prix
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(Secondary)
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "${(hotel.pricePerNight / 1000).toInt()}k FCFA",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Top Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Retour",
                        tint = Secondary
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Hôtels à proximité",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Secondary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Primary)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${hotels.count { it.latitude != 0.0 }}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        // Boutons zoom + localisation en bas à droite
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Bascule Satellite / Plan
            FloatingActionButton(
                onClick = { isSatellite = !isSatellite },
                modifier = Modifier.size(46.dp),
                containerColor = Color.White,
                shape = RoundedCornerShape(12.dp),
                elevation = FloatingActionButtonDefaults.elevation(4.dp)
            ) {
                Icon(
                    Icons.Outlined.Layers,
                    contentDescription = "Type de carte",
                    tint = if (isSatellite) Primary else Secondary,
                    modifier = Modifier.size(22.dp)
                )
            }
            // Zoom In
            FloatingActionButton(
                onClick = {
                    val currentZoom = cameraPositionState.position.zoom
                    cameraPositionState.move(CameraUpdateFactory.zoomTo(currentZoom + 1.5f))
                },
                modifier = Modifier.size(46.dp),
                containerColor = Color.White,
                shape = RoundedCornerShape(12.dp),
                elevation = FloatingActionButtonDefaults.elevation(4.dp)
            ) {
                Icon(
                    Icons.Outlined.Add,
                    contentDescription = "Zoom avant",
                    tint = Secondary,
                    modifier = Modifier.size(22.dp)
                )
            }

            // Zoom Out
            FloatingActionButton(
                onClick = {
                    val currentZoom = cameraPositionState.position.zoom
                    cameraPositionState.move(CameraUpdateFactory.zoomTo(currentZoom - 1.5f))
                },
                modifier = Modifier.size(46.dp),
                containerColor = Color.White,
                shape = RoundedCornerShape(12.dp),
                elevation = FloatingActionButtonDefaults.elevation(4.dp)
            ) {
                Icon(
                    Icons.Outlined.Remove,
                    contentDescription = "Zoom arrière",
                    tint = Secondary,
                    modifier = Modifier.size(22.dp)
                )
            }

            // Ma position
            FloatingActionButton(
                onClick = {
                    if (hasPermission) {
                        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                        fusedLocationClient.getCurrentLocation(
                            Priority.PRIORITY_HIGH_ACCURACY, null
                        ).addOnSuccessListener { loc ->
                            loc?.let { userLocation = LatLng(it.latitude, it.longitude) }
                        }
                    } else {
                        permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
                    }
                },
                modifier = Modifier.size(52.dp),
                containerColor = Primary,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(6.dp)
            ) {
                Icon(
                    Icons.Outlined.MyLocation,
                    contentDescription = "Ma position",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Card permission refusée
        if (!hasPermission) {
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Secondary),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Autoriser la localisation",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Pour voir les hôtels les plus proches de votre position.",
                        color = Color.White.copy(0.85f),
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            permissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Primary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Autoriser l'accès", color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}