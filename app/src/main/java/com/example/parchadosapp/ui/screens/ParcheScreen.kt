package com.example.parchadosapp.ui.screens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.parchadosapp.ui.components.BottomNavigationBar
import com.example.parchadosapp.ui.components.OpenStreetMapView
import com.example.parchadosapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParcheScreen(navController: NavController, context: Context) {
    var parcheName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var playersNeeded by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) } // 🔹 Navbar siempre visible
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()) // 🔹 Permite el scroll
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 🔹 Título en blanco para mejor contraste
                Text(
                    text = "Crear un Parche",
                    fontSize = 24.sp,
                    fontFamily = BowlbyOneSC,
                    color = Color.White // 🔹 Ahora en blanco
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Mapa interactivo
                OpenStreetMapView(context)

                Spacer(modifier = Modifier.height(20.dp))

                // Campos de entrada
                CustomTextField(value = parcheName, onValueChange = { parcheName = it }, label = "Nombre del Parche", icon = Icons.Default.Call)
                Spacer(modifier = Modifier.height(10.dp))
                CustomTextField(value = description, onValueChange = { description = it }, label = "Descripción", icon = Icons.Default.LocationOn)
                Spacer(modifier = Modifier.height(10.dp))
                CustomTextField(value = playersNeeded, onValueChange = { playersNeeded = it }, label = "N° de Jugadores", icon = Icons.Default.AccountCircle)
                Spacer(modifier = Modifier.height(10.dp))
                CustomTextField(value = date, onValueChange = { date = it }, label = "Fecha y Hora", icon = Icons.Default.DateRange)

                Spacer(modifier = Modifier.height(20.dp))

                // Botón de Crear Parche
                Button(
                    onClick = { /* Guardar parche en la base de datos */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "Crear Parche", fontSize = 18.sp, color = Color.White)
                }

                Spacer(modifier = Modifier.height(80.dp)) // 🔹 Espaciado para evitar que el botón quede pegado al navbar
            }
        }
    }
}

// Componente reutilizable para los campos de entrada
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(value: String, onValueChange: (String) -> Unit, label: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Color.Black,
            focusedBorderColor = PrimaryColor,
            unfocusedBorderColor = SecondaryColor,
            cursorColor = PrimaryColor
        )
    )
}
