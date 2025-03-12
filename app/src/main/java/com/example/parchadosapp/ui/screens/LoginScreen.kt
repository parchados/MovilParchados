package com.example.parchadosapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.parchadosapp.R
import com.example.parchadosapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Botón de regreso
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = Color.Black)
                }
            }

            // Título "Parchados"
            Text(
                text = "Parchados",
                fontSize = 36.sp,
                fontFamily = BrightRetro,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(15.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "INICIA SESIÓN", fontSize = 20.sp, fontFamily = BowlbyOneSC)

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("EMAIL") })
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("CONTRASEÑA") }, visualTransformation = PasswordVisualTransformation())

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(onClick = { /* Acción de Login */ }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE77D20))) {
                        Text(text = "Iniciar Sesión", fontSize = 18.sp, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    TextButton(onClick = { navController.navigate("register") }) {
                        Text(text = "¿No tienes cuenta? Regístrate", color = Color.Black)
                    }
                }
            }
        }
    }
}
