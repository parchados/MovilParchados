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
import androidx.compose.material.icons.filled.ArrowDropDown
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
import com.example.parchadosapp.data.api.obtenerEspaciosPorLugar
import com.example.parchadosapp.ui.components.BottomNavigationBar
import com.example.parchadosapp.ui.components.Patch
import com.example.parchadosapp.ui.theme.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import java.util.*
import com.example.parchadosapp.data.api.obtenerLugares
import com.example.parchadosapp.data.models.Espacio
import com.example.parchadosapp.data.models.Lugar
import com.example.parchadosapp.utils.geocodeDireccion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.google.android.gms.maps.CameraUpdateFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParcheScreen(navController: NavController, context: Context) {
    var parcheName by remember { mutableStateOf("") }
    var nombreLugar by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var nombreEspacio by remember { mutableStateOf("") }
    var playersNeeded by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var horaFin by remember { mutableStateOf("") }
    var descripcionParche by remember { mutableStateOf("") }

    var selectedSport by remember { mutableStateOf("") }
    var expandedSport by remember { mutableStateOf(false) }
    var expandedLugar by remember { mutableStateOf(false) }
    var expandedEspacio by remember { mutableStateOf(false) }

    var marcadorSeleccionado by remember { mutableStateOf<String?>(null) }

    val localContext = LocalContext.current
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(4.60971, -74.08175), 12f)
    }

    var lugares by remember { mutableStateOf<List<Pair<Lugar, LatLng>>>(emptyList()) }
    var espacios by remember { mutableStateOf<List<Espacio>>(emptyList()) }

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

                CustomTextField(parcheName, { parcheName = it }, "Nombre del Parche", Icons.Default.Call)
                Spacer(modifier = Modifier.height(12.dp))
                CustomTextField(playersNeeded, { playersNeeded = it }, "N° de Jugadores", Icons.Default.AccountCircle)
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = date,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Fecha") },
                    trailingIcon = {
                        IconButton(onClick = { showParcheDatePicker(localContext) { date = it } }) {
                            Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = TextColor, cursorColor = PrimaryColor,
                        focusedBorderColor = PrimaryColor, unfocusedBorderColor = SecondaryColor,
                        containerColor = White
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = time,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Hora Inicio") },
                    trailingIcon = {
                        IconButton(onClick = { showParcheTimePicker(localContext) { time = it } }) {
                            Icon(Icons.Default.DateRange, contentDescription = null)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = TextColor, cursorColor = PrimaryColor,
                        focusedBorderColor = PrimaryColor, unfocusedBorderColor = SecondaryColor,
                        containerColor = White
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = horaFin,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Hora Fin") },
                    trailingIcon = {
                        IconButton(onClick = { showParcheTimePicker(localContext) { horaFin = it } }) {
                            Icon(Icons.Default.DateRange, contentDescription = null)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = TextColor, cursorColor = PrimaryColor,
                        focusedBorderColor = PrimaryColor, unfocusedBorderColor = SecondaryColor,
                        containerColor = White
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Selector de deporte
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = selectedSport,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Deporte") },
                        trailingIcon = {
                            IconButton(onClick = { expandedSport = !expandedSport }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = TextColor, cursorColor = PrimaryColor,
                            focusedBorderColor = PrimaryColor, unfocusedBorderColor = SecondaryColor,
                            containerColor = White
                        )
                    )

                    DropdownMenu(
                        expanded = expandedSport,
                        onDismissRequest = { expandedSport = false },
                        modifier = Modifier.fillMaxWidth().background(White)
                    ) {
                        listOf("Fútbol", "Baloncesto", "Tenis", "Voleibol", "Ultimate", "Otro").forEach { deporte ->
                            DropdownMenuItem(
                                text = { Text(deporte, color = Color.Black) },
                                onClick = {
                                    selectedSport = deporte
                                    expandedSport = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Nombre del lugar
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = nombreLugar,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Nombre del Lugar") },
                        trailingIcon = {
                            IconButton(onClick = { expandedLugar = !expandedLugar }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = TextColor, cursorColor = PrimaryColor,
                            focusedBorderColor = PrimaryColor, unfocusedBorderColor = SecondaryColor,
                            containerColor = White
                        )
                    )

                    DropdownMenu(
                        expanded = expandedLugar,
                        onDismissRequest = { expandedLugar = false },
                        modifier = Modifier.fillMaxWidth().background(White)
                    ) {
                        lugares.forEach { (lugar, coords) ->
                            DropdownMenuItem(
                                text = { Text(lugar.nombre, color = Color.Black) },
                                onClick = {
                                    nombreLugar = lugar.nombre
                                    description = lugar.direccion
                                    marcadorSeleccionado = lugar.id
                                    expandedLugar = false

                                    CoroutineScope(Dispatchers.Main).launch {
                                        cameraPositionState.animate(
                                            update = CameraUpdateFactory.newLatLngZoom(coords, 15f),
                                            durationMs = 800
                                        )
                                    }

                                    CoroutineScope(Dispatchers.IO).launch {
                                        val fetchedEspacios = obtenerEspaciosPorLugar(lugar.id)
                                        withContext(Dispatchers.Main) {
                                            espacios = fetchedEspacios
                                            nombreEspacio = ""
                                        }
                                    }
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Selector de espacio
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = nombreEspacio,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Espacio") },
                        trailingIcon = {
                            IconButton(onClick = { if (espacios.isNotEmpty()) expandedEspacio = !expandedEspacio }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        enabled = espacios.isNotEmpty(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = TextColor,
                            containerColor = White,
                            focusedBorderColor = PrimaryColor,
                            unfocusedBorderColor = SecondaryColor,
                            disabledTextColor = Color.Gray,
                            disabledBorderColor = SecondaryColor
                        )
                    )

                    DropdownMenu(
                        expanded = expandedEspacio,
                        onDismissRequest = { expandedEspacio = false },
                        modifier = Modifier.fillMaxWidth().background(White)
                    ) {
                        espacios.forEach { espacio ->
                            DropdownMenuItem(
                                text = { Text(espacio.nombre, color = Color.Black) },
                                onClick = {
                                    nombreEspacio = espacio.nombre
                                    expandedEspacio = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Dirección
                CustomTextField(description, {}, "Dirección", Icons.Default.LocationOn, readOnly = true)
                Spacer(modifier = Modifier.height(12.dp))

                // Descripción del parche
                OutlinedTextField(
                    value = descripcionParche,
                    onValueChange = { descripcionParche = it },
                    label = { Text("Descripción del Parche") },
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = TextColor, cursorColor = PrimaryColor,
                        focusedBorderColor = PrimaryColor, unfocusedBorderColor = SecondaryColor,
                        containerColor = White
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Selecciona el lugar del parche:",
                    color = TextColor,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            Box(
                modifier = Modifier.fillMaxWidth().height(200.dp)
                    .padding(horizontal = 20.dp).clip(RoundedCornerShape(10.dp)).background(DetailColor)
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
                                nombreLugar = lugar.nombre
                                description = lugar.direccion
                                marcadorSeleccionado = lugar.id

                                CoroutineScope(Dispatchers.Main).launch {
                                    cameraPositionState.animate(
                                        update = CameraUpdateFactory.newLatLngZoom(coords, 15f),
                                        durationMs = 800
                                    )
                                }

                                CoroutineScope(Dispatchers.IO).launch {
                                    val fetchedEspacios = obtenerEspaciosPorLugar(lugar.id)
                                    withContext(Dispatchers.Main) {
                                        espacios = fetchedEspacios
                                        nombreEspacio = ""
                                    }
                                }

                                true
                            }
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Button(
                    onClick = {
                        // Lógica para crear el parche
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
