package com.example.parchadosapp.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.parchadosapp.R
import com.example.parchadosapp.ui.components.BottomNavigationBar
import com.example.parchadosapp.ui.components.GoogleMapView
import com.example.parchadosapp.ui.theme.BrightRetro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(navController: NavController, context: Context) {
    var selectedFilters by remember { mutableStateOf<Set<String>>(emptySet()) } // ✅ Ahora es un conjunto de filtros

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 🔹 Mapa de Google
            GoogleMapView(context = context)

            // 🔹 Sección de filtros y búsqueda
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                    .background(Color.White)
                    .align(Alignment.TopCenter),
            ) {
                FilterSection(
                    selectedFilters = selectedFilters,
                    onFilterSelected = { filter ->
                        selectedFilters = if (filter == null) {
                            emptySet() // ✅ Borra todos los filtros cuando el botón de reset es presionado
                        } else if (filter in selectedFilters) {
                            selectedFilters - filter // ✅ Si ya estaba seleccionado, lo quita
                        } else {
                            selectedFilters + filter // ✅ Agrega si no estaba seleccionado
                        }
                    }
                )
            }
        }
    }
}



/**
 * 🔹 Barra de búsqueda con bordes redondeados y sombra.
 */
@Composable
fun SearchBar(
    modifier: Modifier = Modifier, // ✅ Ahora acepta un Modifier opcional
    searchText: String,
    onSearchTextChanged: (String) -> Unit
) {
    Row(
        modifier = modifier
            .height(50.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_find_game),
            contentDescription = "Buscar",
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.width(10.dp))
        BasicTextField(
            value = searchText,
            onValueChange = onSearchTextChanged,
            textStyle = TextStyle(fontSize = 18.sp, color = Color.Black),
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * 🔹 Botones de filtro con mejor diseño.
 */
@Composable
fun FilterSection(selectedFilters: Set<String>, onFilterSelected: (String?) -> Unit) {
    var isSportsFilter by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    val normalFilters = listOf("Courts", "Games", "Tournaments", "Leagues", "Events", "Training", "Clubs")
    val sportsFilters = listOf("Soccer", "Basketball", "Tennis", "Volleyball", "Baseball", "Swimming", "Running")

    val currentFilters = if (isSportsFilter) sportsFilters else normalFilters
    val currentIcon = if (isSportsFilter) R.drawable.deportes else R.drawable.filtro

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp)
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 🔹 Barra de búsqueda + Botón de filtro en la misma fila
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 🔹 Barra de búsqueda más pequeña
            SearchBar(
                modifier = Modifier.fillMaxWidth(0.85f),
                searchText = searchText,
                onSearchTextChanged = { searchText = it }
            )

            // 🔹 Botón para cambiar entre filtros normales y deportes
            IconButton(
                onClick = { isSportsFilter = !isSportsFilter },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    painter = painterResource(id = currentIcon),
                    contentDescription = "Cambiar filtro",
                    tint = Color(0xFF003F5C)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 🔹 Filtros en carrusel + Botón Reset alineado correctamente
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 🔹 Carrusel de filtros dentro de un Row con scroll
            Row(
                modifier = Modifier
                    .weight(1f)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                currentFilters.forEach { filter ->
                    val isSelected = filter in selectedFilters

                    TextButton(
                        onClick = { onFilterSelected(filter) },
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, Color(0xFF003F5C), RoundedCornerShape(8.dp))
                            .background(if (isSelected) Color(0xFF003F5C) else Color.White) // ✅ Ahora cubre todo el botón
                    ) {
                        Text(
                            text = filter,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else Color(0xFF003F5C) // ✅ Mantiene el contraste correcto
                        )
                    }
                }
            }

            // 🔹 Botón de Reset alineado correctamente al lado del carrusel
            IconButton(
                onClick = { onFilterSelected(null) }, // ✅ Ahora resetea todos los filtros
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.reset),
                    contentDescription = "Resetear filtros",
                    modifier = Modifier.size(20.dp), // ✅ Reduce solo el tamaño del icono
                    tint = Color(0xFF003F5C)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}




