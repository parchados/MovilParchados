package com.example.parchadosapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import com.example.parchadosapp.R
import androidx.navigation.NavController


data class Sport(
    val name: String,
    val expression: String,
    val imageRes: Int
)

@Composable
fun SportsCarousel(navController: NavController, onSportSelected: (String) -> Unit) {
    val sports = listOf(
        Sport("Fútbol", "¿Último gol gana?", R.drawable.futbol),
        Sport("Baloncesto", "¿Alguna reta?", R.drawable.basket),
        Sport("Billar", "¿Tres bandas?", R.drawable.billar),
        Sport("Tenis", "¿Punto al saque?", R.drawable.tenis)
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "¿Qué tipo de parche quieres?",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF003F5C),
            modifier = Modifier.padding(start = 16.dp, bottom = 12.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(sports.size) { index ->
                SportCard(
                    sport = sports[index],
                    onClick = {
                        // Pasamos el nombre del deporte seleccionado
                        onSportSelected(sports[index].name)
                    }
                )
            }
        }
    }
}

@Composable
fun SportCard(sport: Sport, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(120.dp)
            .padding(top = 24.dp)
            .clickable { onClick() } // Accion al hacer clic
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .height(100.dp) // Ajuste de altura
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 36.dp) // Ajustado para dejar espacio para la imagen
            ) {
                Text(
                    text = sport.expression,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = sport.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF003F5C)
                )
            }
        }

        Image(
            painter = painterResource(id = sport.imageRes),
            contentDescription = sport.name,
            modifier = Modifier
                .size(60.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-30).dp)
                .clip(RoundedCornerShape(50))
        )
    }
}


@Composable
fun SportCard(sport: Sport) {
    Box(
        modifier = Modifier
            .width(120.dp)
            .padding(top = 24.dp)
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .height(100.dp) // ⬅️ Ajustado aquí
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 36.dp) // ⬅️ Alineado justo para dejar espacio a la imagen
            ) {
                Text(
                    text = sport.expression,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = sport.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF003F5C)
                )
            }
        }

        Image(
            painter = painterResource(id = sport.imageRes),
            contentDescription = sport.name,
            modifier = Modifier
                .size(60.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-30).dp)
                .clip(RoundedCornerShape(50))
        )
    }
}
