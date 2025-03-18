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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.parchadosapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Título "Parchados"
            Text(
                text = "Parchados",
                fontSize = 38.sp,
                fontFamily = BrightRetro,
                color = Color(0xFF003F5C), // Azul profundo
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Caja blanca con el formulario
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
                    // Texto de bienvenida
                    Text(
                        text = "JUEGA, RESERVA Y GANA.\nÚNETE A PARCHADOS.",
                        fontSize = 18.sp,
                        fontFamily = BowlbyOneSC,
                        textAlign = TextAlign.Center,
                        color = Color(0xFF2F4B7C) // Azul más claro
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Campo de email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("EMAIL", color = Color(0xFF1E293B)) }, // Gris oscuro
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFF2F4B7C), // Azul claro
                            unfocusedBorderColor = Color.Gray,
                            textColor = Color.Black, // Color del texto ingresado
                            cursorColor = Color.Black // Color del cursor
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Campo de nombre
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("NOMBRE", color = Color(0xFF1E293B)) },
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
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        visualTransformation = PasswordVisualTransformation(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFF2F4B7C),
                            unfocusedBorderColor = Color.Gray,
                            textColor = Color.Black,
                            cursorColor = Color.Black
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Botón de Registro
                    Button(
                        onClick = { /* Acción de Registro */ },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEAC67A)), // Amarillo dorado
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "¡Parcharme!", fontSize = 18.sp, color = Color(0xFF003F5C)) // Azul profundo en el texto
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    // Texto de "¿Ya eres un Parchado?"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "¿Ya eres un Parchado? ", color = Color(0xFF1E293B)) // Gris oscuro
                        TextButton(onClick = { navController.navigate("login") }) {
                            Text(text = "Iniciar Sesión", color = Color(0xFF003F5C)) // Azul profundo
                        }
                    }
                }
            }
        }
    }
}
