package com.example.parchadosapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

@Composable
fun NotificationsScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F5F0))
            .padding(16.dp)
    ) {
        // 🔙 Flecha de regreso
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.atras),
                    contentDescription = "Atrás",
                    tint = Color(0xFF003F5C),
                    modifier = Modifier.size(32.dp)
                )
            }
            Text(
                text = "Notificaciones",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF003F5C)
            )
        }

        // 🔔 Lista de notificaciones (con scroll)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            val userName = "Juan" // Nombre provisional

            val notifications = listOf(
                "🎉 ¡$userName, nuevo torneo de fútbol este sábado!",
                "📅 $userName, has sido invitado a un partido de tenis",
                "🕒 $userName, recuerda tu partido de baloncesto a las 5PM",
                "📍 $userName, hay una nueva ubicación disponible para jugar billar",
                "👥 $userName, tienes 3 nuevos parches cerca",
                "💬 Álvaro te envió un mensaje, $userName",
                "🚨 $userName, se canceló un partido por clima",
                "✅ $userName, tu reserva ha sido confirmada",
                "🏆 $userName, subiste 2 posiciones en el ranking",
                "⚠️ $userName, no olvides confirmar tu asistencia al evento"
            )

            notifications.forEach { notification ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Text(
                        text = notification,
                        modifier = Modifier.padding(16.dp),
                        fontSize = 16.sp,
                        color = Color(0xFF003F5C)
                    )
                }
            }
        }
    }
}
