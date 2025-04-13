package com.example.parchadosapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.parchadosapp.ui.theme.BowlbyOneSC
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.parchadosapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F5F0)), // Fondo beige elegante
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo en lugar del texto "Parchados"
            Image(
                painter = painterResource(id = R.drawable.logo_parchados),
                contentDescription = "Logo Parchados",
                modifier = Modifier
                    .size(220.dp) // Puedes ajustar el tamaño si lo ves necesario
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(15.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "INICIA SESIÓN",
                        fontSize = 22.sp,
                        fontFamily = BowlbyOneSC,
                        color = Color(0xFF2F4B7C) // Azul claro para encabezados
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    // Campo de email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("EMAIL", color = Color(0xFF1E293B)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFF2F4B7C),
                            unfocusedBorderColor = Color.Gray,
                            textColor = Color.Black,
                            cursorColor = Color.Black
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Campo de contraseña
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("CONTRASEÑA", color = Color(0xFF1E293B)) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFF2F4B7C),
                            unfocusedBorderColor = Color.Gray,
                            textColor = Color.Black,
                            cursorColor = Color.Black
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Mensaje de error
                    if (errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage,
                            color = Color.Red,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    // Botón de iniciar sesión
                    Button(
                        onClick = {
                            if (email == "admin" && password == "admin") {
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            } else {
                                errorMessage = "Credenciales incorrectas"
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEAC67A)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "Iniciar Sesión", fontSize = 18.sp, color = Color(0xFF003F5C))
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Botón para ir a registro
                    TextButton(onClick = { navController.navigate("register") }) {
                        Text(text = "¿No tienes cuenta? Regístrate", color = Color(0xFF1E293B))
                    }
                }
            }
        }
    }
}
