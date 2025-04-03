package com.example.parchadosapp.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.parchadosapp.data.PatchRepository
import com.example.parchadosapp.ui.components.BottomNavigationBar
import com.example.parchadosapp.ui.components.Patch

import com.example.parchadosapp.ui.theme.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParcheScreen(navController: NavController, context: Context) {
    var parcheName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var playersNeeded by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }

    // Estado del mapa
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(4.60971, -74.08175), 12f)
    }

    val patches = PatchRepository.patches // <- Tus lugares con coordenadas
    var selectedPatch by remember { mutableStateOf<Patch?>(null) }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Crear un Parche",
                    fontSize = 24.sp,
                    fontFamily = BowlbyOneSC,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(20.dp))

                CustomTextField(value = parcheName, onValueChange = { parcheName = it }, label = "Nombre del Parche", icon = Icons.Default.Call)
                Spacer(modifier = Modifier.height(10.dp))

                // Este campo será llenado automáticamente al tocar un punto en el mapa
                CustomTextField(value = description, onValueChange = { description = it }, label = "Dirección", icon = Icons.Default.LocationOn, readOnly = true)

                Spacer(modifier = Modifier.height(10.dp))
                CustomTextField(value = playersNeeded, onValueChange = { playersNeeded = it }, label = "N° de Jugadores", icon = Icons.Default.AccountCircle)
                Spacer(modifier = Modifier.height(10.dp))
                CustomTextField(value = date, onValueChange = { date = it }, label = "Fecha y Hora", icon = Icons.Default.DateRange)

                Spacer(modifier = Modifier.height(20.dp))

                // 🗺️ Mapa reducido con marcadores
                Text(text = "Selecciona el lugar del parche:", color = Color.White, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    GoogleMap(
                        cameraPositionState = cameraPositionState
                    ) {
                        patches.forEach { patch ->
                            Marker(
                                state = MarkerState(position = LatLng(patch.latitude, patch.longitude)),
                                title = patch.name,
                                snippet = patch.address,
                                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED),
                                onClick = {
                                    selectedPatch = patch
                                    description = patch.address // <- Actualiza el campo
                                    true
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { /* Guardar en DB */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "Crear Parche", fontSize = 18.sp, color = Color.White)
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}


// Componente reutilizable para los campos de entrada
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    readOnly: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        readOnly = readOnly,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Color.Black,
            focusedBorderColor = PrimaryColor,
            unfocusedBorderColor = SecondaryColor,
            cursorColor = PrimaryColor
        )
    )
}




