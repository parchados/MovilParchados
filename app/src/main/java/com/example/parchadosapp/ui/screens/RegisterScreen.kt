package com.example.parchadosapp.ui.screens

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.util.*
import com.example.parchadosapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var birthdate by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("Masculino") }
    var selectedOption by remember { mutableStateOf("Competir") }
    val context = LocalContext.current

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
            // 🔹 Título "Parchados" más grande
            Text(
                text = "Parchados",
                fontSize = 48.sp, // 🔹 Más grande
                fontFamily = BrightRetro,
                color = Color(0xFF003F5C),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 🔹 Caja blanca con el formulario
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
                    // 🔹 Texto de bienvenida
                    Text(
                        text = "JUEGA, RESERVA Y GANA.\nÚNETE A PARCHADOS.",
                        fontSize = 18.sp,
                        fontFamily = BowlbyOneSC,
                        textAlign = TextAlign.Center,
                        color = Color(0xFF2F4B7C)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // 🔹 Campo de email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("EMAIL", color = Color(0xFF1E293B)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Color.Black,
                            cursorColor = Color.Black
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // 🔹 Campo de nombre
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("NOMBRE", color = Color(0xFF1E293B)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Color.Black,
                            cursorColor = Color.Black
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // 🔹 Campo de contraseña (movido después de nombre)
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("CONTRASEÑA", color = Color(0xFF1E293B)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        visualTransformation = PasswordVisualTransformation(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Color.Black,
                            cursorColor = Color.Black
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // 🔹 Campo de fecha de nacimiento
                    OutlinedTextField(
                        value = birthdate,
                        onValueChange = {},
                        label = { Text("FECHA DE NACIMIENTO", color = Color(0xFF1E293B)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker(context) { birthdate = it } }) {
                                Icon(
                                    imageVector = androidx.compose.material.icons.Icons.Default.DateRange,
                                    contentDescription = "Seleccionar Fecha"
                                )
                            }
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Color.Black,
                            cursorColor = Color.Black
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // 🔹 Selección de género
                    Text(text = "Género", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF003F5C))
                    val genderOptions = listOf("Masculino", "Femenino", "Otro", "Prefiero no decir")
                    var expanded by remember { mutableStateOf(false) }

                    Box {
                        OutlinedTextField(
                            value = selectedGender,
                            onValueChange = {},
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true,
                            shape = RoundedCornerShape(10.dp),
                            trailingIcon = {
                                IconButton(onClick = { expanded = true }) {
                                    Icon(
                                        imageVector = androidx.compose.material.icons.Icons.Default.ArrowDropDown,
                                        contentDescription = "Seleccionar Género"
                                    )
                                }
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = Color.Black,
                                cursorColor = Color.Black
                            )
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(Color.White) // 🔹 Fondo más claro
                        ) {
                            genderOptions.forEach { gender ->
                                DropdownMenuItem(
                                    text = { Text(gender, color = Color.Black) }, // 🔹 Texto negro para mejor visibilidad
                                    onClick = {
                                        selectedGender = gender
                                        expanded = false
                                    },
                                    modifier = Modifier.background(Color(0xFFF8F5F0)) // 🔹 Fondo beige claro para visibilidad
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 🔹 Pregunta "¿Qué buscas?"
                    Text(text = "¿Qué buscas?", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF003F5C))
                    Row {
                        listOf("Competir", "Parchar").forEach { option ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(end = 12.dp)
                            ) {
                                RadioButton(
                                    selected = (selectedOption == option),
                                    onClick = { selectedOption = option }
                                )
                                Text(text = option, color = Color.Black)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // 🔹 Botón de Registro
                    Button(
                        onClick = { /* Acción de Registro */ },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEAC67A)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "¡Parcharme!", fontSize = 18.sp, color = Color(0xFF003F5C))
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    // 🔹 Enlace para regresar a inicio de sesión
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "¿Ya eres un Parchado? ", color = Color(0xFF1E293B))
                        TextButton(onClick = { navController.navigate("login") }) {
                            Text(text = "Iniciar Sesión", color = Color(0xFF003F5C))
                        }
                    }
                }
            }
        }
    }
}

fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            onDateSelected(formattedDate)
        },
        year, month, day
    )
    datePickerDialog.show()
}

