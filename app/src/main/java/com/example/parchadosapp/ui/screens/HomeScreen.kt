package com.example.parchadosapp.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.parchadosapp.R
import com.example.parchadosapp.ui.theme.BrightRetro
import com.example.parchadosapp.ui.components.OpenStreetMapView

@Composable
fun HomeScreen(navController: NavController, context: Context) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F5F0)) // Fondo Beige elegante
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Sección de perfil y logo
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { /* Acción del perfil */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_profile),
                    contentDescription = "Perfil",
                    tint = Color(0xFF1E293B) // Gris oscuro para icono
                )
            }
            Text(
                text = "Parchados",
                fontSize = 32.sp,
                fontFamily = BrightRetro,
                color = Color(0xFF003F5C) // Azul profundo
            )
            IconButton(onClick = { /* Acción del menú */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_menu),
                    contentDescription = "Menú",
                    tint = Color(0xFF1E293B) // Gris oscuro
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Tarjeta con imagen y texto principal
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFA5C8E1)) // Azul pastel
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_sports),
                    contentDescription = "Deporte",
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "RESERVA, JUEGA Y CONÉCTATE...",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF1E293B) // Gris oscuro para mejor legibilidad
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { navController.navigate("login") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF003F5C)), // Azul profundo
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "Ingresar", color = Color.White)
                    }

                    Button(
                        onClick = { navController.navigate("register") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEAC67A)), // Amarillo dorado
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "¡Párchate!", color = Color(0xFF003F5C)) // Azul profundo en texto
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Sección del Mapa
        OpenStreetMapView(context)
    }
}
