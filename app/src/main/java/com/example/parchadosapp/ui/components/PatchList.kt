package com.example.parchadosapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text

import com.example.parchadosapp.R

data class Patch(
    val image: Int,
    val name: String,
    val address: String,
    val date: String,
    val time: String,
    val remaining: Int,
    val sport: String,
    val latitude: Double,
    val longitude: Double
)

@Composable
fun PatchCard(patch: Patch) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del lugar
            Image(
                painter = painterResource(id = patch.image),
                contentDescription = "Imagen del lugar",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Información del parche
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = patch.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF003F5C)
                )
                Text(
                    text = patch.address,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = "${patch.date} | ${patch.time}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Faltan ${patch.remaining} personas",
                    fontSize = 14.sp,
                    color = Color(0xFF003F5C)
                )
                Text(
                    text = "Deporte: ${patch.sport}",
                    fontSize = 14.sp,
                    color = Color(0xFF003F5C)
                )
            }
        }
    }
}