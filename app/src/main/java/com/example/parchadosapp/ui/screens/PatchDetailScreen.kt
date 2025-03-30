package com.example.parchadosapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.parchadosapp.ui.components.Patch
import com.example.parchadosapp.ui.theme.BrightRetro

@Composable
fun PatchDetailScreen(navController: NavController, patch: Patch) {
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
                text = "Detalles del Parche",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF003F5C)
            )
        }

        // Imagen grande en la parte superior
        Image(
            painter = painterResource(id = patch.image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(20.dp))
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Título del parche
        Text(
            text = patch.name,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF003F5C),
            fontFamily = BrightRetro
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Dirección
        Text(
            text = patch.address,
            fontSize = 16.sp,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Fecha y hora
        Text(
            text = "📅 ${patch.date}    ⏰ ${patch.time}",
            fontSize = 16.sp,
            color = Color(0xFF003F5C),
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Faltantes y deporte
        Text(
            text = "👥 Faltan ${patch.remaining} personas",
            fontSize = 16.sp,
            color = Color(0xFF003F5C)
        )

        Text(
            text = "🏅 Deporte: ${patch.sport}",
            fontSize = 16.sp,
            color = Color(0xFF003F5C)
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Botón para unirse
        Button(
            onClick = { /* lógica para unirse al parche */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF003F5C)),
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            Text(text = "Unirme al Parche", fontSize = 18.sp, color = Color.White)
        }
    }
}
