package com.example.parchadosapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.parchadosapp.R
import com.example.parchadosapp.data.SessionManager.SessionManager
import com.example.parchadosapp.data.api.obtenerParches
import com.example.parchadosapp.data.api.obtenerParchesPorUsuario
import com.example.parchadosapp.data.models.ParcheRequest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestionScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var parches by remember { mutableStateOf<List<ParcheRequest>>(emptyList()) }

    LaunchedEffect(Unit) {
        val userId = SessionManager.getUserId(context)
        if (userId != null) {
            try {
                parches = obtenerParchesPorUsuario(userId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Scaffold(
        containerColor = Color(0xFFF8F5F0),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mis Parches",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.atras),
                            contentDescription = "Atrás",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF003F5C))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (parches.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Aún no has creado parches.", color = Color.Gray)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(parches) { parche ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    parche.nombre,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF003F5C)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Deporte: ${parche.deporte}", color = Color.Gray)
                                Text("Fecha: ${parche.fecha}", color = Color.Gray)
                                Text("Hora: ${parche.hora_inicio} - ${parche.hora_fin}", color = Color.Gray)
                                Text("Estado: ${parche.estado}", color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}


