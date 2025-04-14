package com.example.parchadosapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
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
import com.example.parchadosapp.data.api.eliminarParchePorId
import com.example.parchadosapp.data.api.obtenerParchesPorUsuarioConImagen
import com.example.parchadosapp.data.models.ParcheConImagen
import com.example.parchadosapp.ui.components.PatchCardFromSupabase
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestionScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var parchesConImagen by remember { mutableStateOf<List<ParcheConImagen>>(emptyList()) }
    var selectedParcheId by remember { mutableStateOf<String?>(null) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var parcheAEliminar by remember { mutableStateOf<ParcheConImagen?>(null) }

    LaunchedEffect(Unit) {
        val userId = SessionManager.getUserId(context)
        if (userId != null) {
            try {
                parchesConImagen = obtenerParchesPorUsuarioConImagen(userId)
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
            if (parchesConImagen.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Aún no has creado parches.", color = Color.Gray)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(parchesConImagen) { parcheConImagen ->
                        Box {
                            PatchCardFromSupabase(parcheConImagen) {
                                selectedParcheId = parcheConImagen.parche.id
                            }

                            if (selectedParcheId == parcheConImagen.parche.id) {
                                DropdownMenu(
                                    expanded = true,
                                    onDismissRequest = { selectedParcheId = null },
                                    modifier = Modifier.background(Color.White)
                                ) {
                                    DropdownMenuItem(
                                        text = {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.eliminar),
                                                    contentDescription = "Eliminar",
                                                    tint = Color.Red,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text("Eliminar", color = Color.Red)
                                            }
                                        },
                                        onClick = {
                                            parcheAEliminar = parcheConImagen
                                            showDeleteConfirmation = true
                                            selectedParcheId = null
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Confirmación de eliminación
            if (showDeleteConfirmation && parcheAEliminar != null) {
                AlertDialog(
                    onDismissRequest = { showDeleteConfirmation = false },
                    title = { Text("Confirmar eliminación") },
                    text = { Text("¿Estás seguro de que deseas eliminar este parche?") },
                    confirmButton = {
                        TextButton(onClick = {
                            scope.launch {
                                parcheAEliminar?.parche?.id?.let { id ->
                                    val eliminado = eliminarParchePorId(id)
                                    if (eliminado) {
                                        parchesConImagen = parchesConImagen.filterNot { it.parche.id == id }
                                        Toast.makeText(context, "Parche eliminado correctamente", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                showDeleteConfirmation = false
                                parcheAEliminar = null
                            }
                        }) {
                            Text("Eliminar", color = Color.Red)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            showDeleteConfirmation = false
                            parcheAEliminar = null
                        }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}


