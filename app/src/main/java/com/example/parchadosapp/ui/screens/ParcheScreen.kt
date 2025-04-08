package com.example.parchadosapp.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
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
import com.google.maps.android.compose.*
import java.util.*
import com.example.parchadosapp.data.api.obtenerLugares
import com.example.parchadosapp.data.models.Lugar
import com.example.parchadosapp.utils.geocodeDireccion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.google.android.gms.maps.CameraUpdateFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParcheScreen(navController: NavController, context: Context) {
    var parcheName by remember { mutableStateOf("") }
    var nombreLugar by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var playersNeeded by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }

    var marcadorSeleccionado by remember { mutableStateOf<String?>(null) }

    val localContext = LocalContext.current
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(4.60971, -74.08175), 12f)
    }

    var lugares by remember { mutableStateOf<List<Pair<Lugar, LatLng>>>(emptyList()) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            try {
                val fetched = obtenerLugares()
                val geo = fetched.mapNotNull { lugar ->
                    val coords = geocodeDireccion(context, lugar.direccion)
                    coords?.let { lugar to it }
                }
                lugares = geo
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        containerColor = BackgroundColor
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Crear un Parche",
                    fontSize = 26.sp,
                    fontFamily = BowlbyOneSC,
                    color = PrimaryColor
                )

                Spacer(modifier = Modifier.height(24.dp))

                CustomTextField(
                    value = parcheName,
                    onValueChange = { parcheName = it },
                    label = "Nombre del Parche",
                    icon = Icons.Default.Call
                )

                Spacer(modifier = Modifier.height(12.dp))

                CustomTextField(
                    value = playersNeeded,
                    onValueChange = { playersNeeded = it },
                    label = "N° de Jugadores",
                    icon = Icons.Default.AccountCircle
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = date,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Fecha") },
                    trailingIcon = {
                        IconButton(onClick = {
                            showParcheDatePicker(localContext) { date = it }
                        }) {
                            Icon(imageVector = Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = TextColor,
                        cursorColor = PrimaryColor,
                        focusedBorderColor = PrimaryColor,
                        unfocusedBorderColor = SecondaryColor,
                        containerColor = White
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = time,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Hora") },
                    trailingIcon = {
                        IconButton(onClick = {
                            showParcheTimePicker(localContext) { time = it }
                        }) {
                            Icon(imageVector = Icons.Default.DateRange, contentDescription = "Seleccionar hora")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = TextColor,
                        cursorColor = PrimaryColor,
                        focusedBorderColor = PrimaryColor,
                        unfocusedBorderColor = SecondaryColor,
                        containerColor = White
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Campo: Nombre del lugar
                CustomTextField(
                    value = nombreLugar,
                    onValueChange = { nombreLugar = it },
                    label = "Nombre del Lugar",
                    icon = Icons.Default.Call,
                    readOnly = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Campo: Dirección
                CustomTextField(
                    value = description,
                    onValueChange = {},
                    label = "Dirección",
                    icon = Icons.Default.LocationOn,
                    readOnly = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Selecciona el lugar del parche:",
                    color = TextColor,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            // Mapa
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(DetailColor)
            ) {
                GoogleMap(cameraPositionState = cameraPositionState) {
                    lugares.forEach { (lugar, coords) ->
                        Marker(
                            state = MarkerState(position = coords),
                            title = lugar.nombre,
                            snippet = lugar.direccion,
                            icon = if (marcadorSeleccionado == lugar.id)
                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                            else
                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED),
                            onClick = {
                                description = lugar.direccion
                                nombreLugar = lugar.nombre
                                marcadorSeleccionado = lugar.id

                                CoroutineScope(Dispatchers.Main).launch {
                                    cameraPositionState.animate(
                                        update = CameraUpdateFactory.newLatLngZoom(coords, 15f),
                                        durationMs = 800
                                    )
                                }

                                true
                            }
                        )
                    }
                }
            }

            // Botón
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(vertical = 16.dp)
            ) {
                Button(
                    onClick = {
                        // Acción para guardar el parche
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentColor),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "Crear Parche", fontSize = 18.sp, color = Black)
                }
            }
        }
    }
}




// ✅ CustomTextField reutilizable
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

// ✅ Selector de fecha sin conflicto
fun showParcheDatePicker(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            onDateSelected(formattedDate)
        },
        year, month, day
    )
    datePickerDialog.show()
}

// ✅ Selector de hora
fun showParcheTimePicker(context: Context, onTimeSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val timePickerDialog = TimePickerDialog(
        context,
        { _, selectedHour, selectedMinute ->
            val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            onTimeSelected(formattedTime)
        },
        hour, minute, true
    )
    timePickerDialog.show()
}
