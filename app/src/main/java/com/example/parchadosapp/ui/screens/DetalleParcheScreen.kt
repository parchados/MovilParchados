package com.example.parchadosapp.ui.screens

import NotificacionRequest
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.parchadosapp.R
import kotlinx.coroutines.launch
import com.example.parchadosapp.data.SessionManager.SessionManager
import com.example.parchadosapp.data.api.aumentarCupo
import com.example.parchadosapp.data.api.crearNotificacion
import com.example.parchadosapp.data.api.eliminarParchePorId
import com.example.parchadosapp.data.api.estaInscritoEnParche
import com.example.parchadosapp.data.api.mostrarConfirmacion
import com.example.parchadosapp.data.api.obtenerParchesConImagen
import com.example.parchadosapp.data.api.reducirCupo
import com.example.parchadosapp.data.api.salirDeParche
import com.example.parchadosapp.data.api.supabase
import com.example.parchadosapp.data.api.unirseAParche
import com.example.parchadosapp.data.models.ParcheConImagen
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.Serializable

@Serializable
data class PersonaParche(
    val persona_id: String,
    val parche_id: String
)



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleParcheScreen(navController: NavController, parcheId: String) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var parcheConImagen by remember { mutableStateOf<ParcheConImagen?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var userId by remember { mutableStateOf<String?>(null) }
    var estaInscrito by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        userId = SessionManager.getUserId(context)
        try {
            val todos = obtenerParchesConImagen()
            parcheConImagen = todos.firstOrNull { it.parche.id == parcheId }

            if (userId != null) {
                val resultado = supabase.from("personas_parches")
                    .select {
                        filter {
                            eq("persona_id", userId!!)
                            eq("parche_id", parcheId)
                        }
                    }
                    .decodeList<PersonaParche>()
                estaInscrito = resultado.isNotEmpty()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Parche", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(painter = painterResource(id = R.drawable.atras), contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF003F5C))
            )
        },
        containerColor = Color(0xFFF8F5F0)
    ) { padding ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFEAC67A))
                }
            }

            parcheConImagen == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No se encontró el parche", color = Color.Gray)
                }
            }

            else -> {
                val parche = parcheConImagen!!.parche
                val imagen = parcheConImagen!!.imagenUrl

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    AsyncImage(
                        model = imagen ?: "https://via.placeholder.com/150",
                        contentDescription = "Imagen del parche",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(parche.nombre, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF003F5C))
                    Text("Deporte: ${parche.deporte}", fontSize = 16.sp, color = Color.Gray)
                    Text("Fecha: ${parche.fecha}", fontSize = 16.sp, color = Color.Gray)
                    Text("Hora: ${parche.hora_inicio} - ${parche.hora_fin}", fontSize = 16.sp, color = Color.Gray)
                    Text("Cupo: ${parche.cupo_maximo}", fontSize = 16.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(parche.descripcion ?: "", fontSize = 14.sp, color = Color.DarkGray)

                    Spacer(modifier = Modifier.height(30.dp))

                    if (userId == parche.creador_id) {
                        Button(
                            onClick = {
                                scope.launch {
                                    val confirm = mostrarConfirmacion(context, "¿Estás seguro de eliminar este parche?")
                                    if (confirm) {
                                        val eliminado = eliminarParchePorId(parcheId)
                                        if (eliminado) {
                                            Toast.makeText(context, "Parche eliminado", Toast.LENGTH_SHORT).show()
                                            navController.popBackStack()
                                        }
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Eliminar Parche", color = Color.White)
                        }
                    } else {
                        if (estaInscrito) {
                            Button(
                                onClick = {
                                    scope.launch {
                                        val confirm = mostrarConfirmacion(context, "¿Deseas salirte del parche?")
                                        if (confirm) {
                                            val salio = salirDeParche(userId!!, parcheId)
                                            if (salio) {
                                                estaInscrito = false
                                                Toast.makeText(context, "Has salido del parche", Toast.LENGTH_SHORT).show()
                                                val noti = NotificacionRequest(
                                                    tipo = "parche",
                                                    titulo = "Saliste de un parche",
                                                    descripcion = "🚪 Has salido del parche ${parche.nombre}",
                                                    destinatario_id = userId!!,
                                                    referencia_tipo = "parche"
                                                )
                                                crearNotificacion(noti)
                                                mostrarNotificacionLocal(context)
                                            }
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Salir del Parche", color = Color.White)
                            }
                        } else {
                            Button(
                                onClick = {
                                    scope.launch {
                                        val unido = unirseAParche(userId!!, parcheId)
                                        if (unido) {
                                            estaInscrito = true
                                            Toast.makeText(context, "Te has unido al parche", Toast.LENGTH_SHORT).show()
                                            val noti = NotificacionRequest(
                                                tipo = "parche",
                                                titulo = "¡Te uniste a un parche!",
                                                descripcion = "🎉 Ahora haces parte del parche ${parche.nombre}",
                                                destinatario_id = userId!!,
                                                referencia_tipo = "parche"
                                            )
                                            crearNotificacion(noti)
                                            mostrarNotificacionLocal(context)
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F4B7C)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Unirme al Parche", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}


