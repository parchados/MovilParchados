package com.example.parchadosapp.ui.screens

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.parchadosapp.R
import com.example.parchadosapp.ui.components.BottomNavigationBar
import com.example.parchadosapp.ui.components.Patch
import com.example.parchadosapp.ui.theme.BrightRetro
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import androidx.compose.runtime.Composable
import com.google.android.gms.location.LocationServices
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.MarkerState
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.parchadosapp.ui.theme.*
import com.google.maps.android.compose.CameraPositionState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(navController: NavController, context: Context, selectedSport: String?) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    var userLocation by remember { mutableStateOf<LatLng?>(null) }

    var searchText by remember { mutableStateOf("") }

    LaunchedEffect(true) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    userLocation = LatLng(it.latitude, it.longitude)
                }
            }
        } else {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }

    val patches = listOf(
        Patch(
            image = R.drawable.campo_futbol,
            name = "Campo de Fútbol A",
            address = "Cl 63 #15-32, Bogotá",
            date = "Sábado, 29 de Marzo",
            time = "3:00 PM",
            remaining = 5,
            sport = "Fútbol",
            latitude = 4.650133,
            longitude = -74.066019
        ),
        Patch(
            image = R.drawable.cancha_basket,
            name = "Cancha de Baloncesto B",
            address = "Cl 62 #3-50, Bogotá",
            date = "Domingo, 30 de Marzo",
            time = "6:00 PM",
            remaining = 3,
            sport = "Baloncesto",
            latitude = 4.645390,
            longitude = -74.057067
        ),
        Patch(
            image = R.drawable.billarl,
            name = "Sala de Billar Central",
            address = "Cl. 45 #13-40, Santa Fé, Bogotá",
            date = "Lunes, 31 de Marzo",
            time = "8:00 PM",
            remaining = 2,
            sport = "Billar",
            latitude = 4.632527,
            longitude = -74.066987
        ),
        Patch(
            image = R.drawable.cancha_tenis,
            name = "Cancha de Tenis A",
            address = "Cl. 51 #4-06, Bogotá",
            date = "Lunes, 31 de Marzo",
            time = "5:00 PM",
            remaining = 4,
            sport = "Tenis",
            latitude = 4.635916,
            longitude = -74.061317
        )
    )

    var selectedFilters by remember {
        mutableStateOf(
            selectedSport?.let { setOf(it) } ?: emptySet()
        )
    }

    // 🔍 Nuevo filtro combinado: por deporte y por texto
    val filteredPatches = patches.filter { patch ->
        val matchesSport = selectedFilters.isEmpty() || patch.sport in selectedFilters
        val matchesSearch = searchText.isBlank() || patch.name.contains(searchText, ignoreCase = true)
        matchesSport && matchesSearch
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(4.60971, -74.08175), 12f)
    }

    LaunchedEffect(userLocation) {
        userLocation?.let {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 15f)
        }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            GoogleMapView(
                context = context,
                markers = filteredPatches,
                userLocation = userLocation,
                cameraPositionState = cameraPositionState
            )

            userLocation?.let {
                FloatingActionButton(
                    onClick = {
                        cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 15f)
                    },
                    modifier = Modifier
                        .size(72.dp)
                        .padding(16.dp)
                        .align(Alignment.BottomStart)
                        .background(Color.Transparent)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_location),
                        contentDescription = "Ubicación Actual",
                        tint = White
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                    .background(Color.White)
                    .align(Alignment.TopCenter)
            ) {
                FilterSection(
                    selectedFilters = selectedFilters,
                    onFilterSelected = { filter ->
                        selectedFilters = if (filter == null) {
                            emptySet()
                        } else if (filter in selectedFilters) {
                            selectedFilters - filter
                        } else {
                            selectedFilters + filter
                        }
                    },
                    searchText = searchText,
                    onSearchTextChanged = { searchText = it }
                )
            }
        }
    }
}




@Composable
fun GoogleMapView(context: Context, markers: List<Patch>, userLocation: LatLng?, cameraPositionState: CameraPositionState) {
    GoogleMap(
        cameraPositionState = cameraPositionState
    ) {
        // Todos los marcadores de los parches tendrán el mismo color (gris en este caso)
        markers.forEach { patch ->
            Marker(
                state = MarkerState(position = LatLng(patch.latitude, patch.longitude)),
                title = patch.name,
                snippet = patch.address,
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED) // Color uniforme para todos los parches
            )
        }

        // Mostrar la ubicación del usuario si está disponible
        userLocation?.let {
            Marker(
                state = MarkerState(position = it),
                title = "Tu ubicación",
                snippet = "Estás aquí",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE) // Círculo azul de la ubicación actual
            )
        }
    }
}


/**
 * 🔹 Barra de búsqueda con bordes redondeados y sombra.
 */
@Composable
fun SearchBar(
    modifier: Modifier = Modifier, // ✅ Ahora acepta un Modifier opcional
    searchText: String,
    onSearchTextChanged: (String) -> Unit
) {
    Row(
        modifier = modifier
            .height(50.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_find_game),
            contentDescription = "Buscar",
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.width(10.dp))
        BasicTextField(
            value = searchText,
            onValueChange = onSearchTextChanged,
            textStyle = TextStyle(fontSize = 18.sp, color = Color.Black),
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * 🔹 Botones de filtro con mejor diseño.
 */
@Composable
fun FilterSection(
    selectedFilters: Set<String>,
    onFilterSelected: (String?) -> Unit,
    searchText: String,
    onSearchTextChanged: (String) -> Unit
) {
    var isSportsFilter by remember { mutableStateOf(false) }

    val normalFilters = listOf("Parches", "Torneos", "Clubs", "Eventos")
    val sportsFilters = listOf("Fútbol", "Baloncesto", "Tenis", "Billar")

    val currentFilters = if (isSportsFilter) sportsFilters else normalFilters
    val currentIcon = if (isSportsFilter) R.drawable.deportes else R.drawable.filtro

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp)
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchBar(
                modifier = Modifier.fillMaxWidth(0.85f),
                searchText = searchText,
                onSearchTextChanged = onSearchTextChanged
            )

            IconButton(
                onClick = { isSportsFilter = !isSportsFilter },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    painter = painterResource(id = currentIcon),
                    contentDescription = "Cambiar filtro",
                    tint = Color(0xFF003F5C)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                currentFilters.forEach { filter ->
                    val isSelected = filter in selectedFilters

                    TextButton(
                        onClick = { onFilterSelected(filter) },
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, Color(0xFF003F5C), RoundedCornerShape(8.dp))
                            .background(if (isSelected) Color(0xFF003F5C) else Color.White)
                    ) {
                        Text(
                            text = filter,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else Color(0xFF003F5C)
                        )
                    }
                }
            }

            IconButton(
                onClick = { onFilterSelected(null) },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.reset),
                    contentDescription = "Resetear filtros",
                    modifier = Modifier.size(20.dp),
                    tint = Color(0xFF003F5C)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}


