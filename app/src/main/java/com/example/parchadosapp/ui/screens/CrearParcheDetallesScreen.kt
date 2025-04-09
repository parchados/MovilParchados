package com.example.parchadosapp.ui.screens

import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun CrearParcheDetallesScreen(
    navController: NavController,
    nombre: String,
    jugadores: String,
    fecha: String,
    hora: String,
    nombreLugar: String,
    direccion: String
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Datos recibidos:", style = MaterialTheme.typography.titleLarge)
        Text("Nombre del parche: $nombre")
        Text("Jugadores: $jugadores")
        Text("Fecha: $fecha")
        Text("Hora: $hora")
        Text("Lugar: $nombreLugar")
        Text("Dirección: $direccion")

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            // Aquí podrías guardar el parche en Supabase o seguir otro paso
        }) {
            Text("Finalizar creación")
        }
    }
}
