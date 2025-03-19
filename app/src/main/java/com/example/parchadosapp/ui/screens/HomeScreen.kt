package com.example.parchadosapp.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.parchadosapp.R
import com.example.parchadosapp.ui.components.BottomNavigationBar
import com.example.parchadosapp.ui.theme.BrightRetro

@Composable
fun HomeScreen(navController: NavController, context: Context) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F5F0)), // Fondo beige
        contentAlignment = Alignment.TopCenter // 🔹 Centrar en la parte superior
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp), // 🔹 Espaciado general
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔹 Sección superior centrada
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // 🔹 Imagen de perfil
                Image(
                    painter = painterResource(id = R.drawable.perfil),
                    contentDescription = "Perfil",
                    modifier = Modifier
                        .size(55.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(16.dp)) // 🔹 Más separación del título

                // 🔹 Título centrado con padding extra
                Text(
                    text = "Parchados",
                    fontSize = 42.sp,
                    fontFamily = BrightRetro,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF003F5C),
                    modifier = Modifier.padding(horizontal = 24.dp) // 🔹 Espacio extra a los lados
                )

                Spacer(modifier = Modifier.width(16.dp)) // 🔹 Más separación del título

                // 🔹 Icono de notificaciones
                Box(
                    modifier = Modifier.size(55.dp),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = { /* Acción de notificaciones */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.notificacion),
                            contentDescription = "Notificaciones",
                            tint = Color(0xFF003F5C),
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    // 🔹 Insignia roja de notificaciones
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .align(Alignment.TopEnd)
                            .background(Color.Red, shape = CircleShape)
                    )
                }
            }
        }

        // 🔹 Navbar separado en la parte inferior
        BottomNavigationBar(navController, Modifier.align(Alignment.BottomCenter))
    }
}


