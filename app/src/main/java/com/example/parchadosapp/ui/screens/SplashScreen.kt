package com.example.parchadosapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import com.example.parchadosapp.R
import com.example.parchadosapp.ui.theme.BrightRetro
import com.example.parchadosapp.ui.theme.PrimaryColor
import com.example.parchadosapp.ui.theme.AccentColor


@Composable
fun SplashScreen(navController: NavController) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
        delay(3000)  // Espera 3 segundos y navega a Login
        navController.navigate("login") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryColor),  // Fondo azul profundo
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Imagen más centrada y grande
            Image(
                painter = painterResource(id = R.drawable.logo_parchados),
                contentDescription = "Logo Parchados",
                modifier = Modifier
                    .size(280.dp)  // Ajuste de tamaño
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Texto con color de acento para mejor visibilidad
            Text(
                text = "Parchados",
                fontSize = 50.sp,
                fontFamily = BrightRetro,
                color = AccentColor,  // Amarillo dorado para resaltar
                textAlign = TextAlign.Center
            )
        }
    }
}
