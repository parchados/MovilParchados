package com.example.parchadosapp.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    var searchText by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Courts") }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 🔹 Mapa de Google ocupando todo el fondo
            GoogleMapView(context = context)

            // 🔹 Elementos flotantes sobre el mapa
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 🔹 Barra de búsqueda
                SearchBar(
                    searchText = searchText,
                    onSearchTextChanged = { searchText = it }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // 🔹 Filtros: Courts | Games
                FilterButtons(
                    selectedFilter = selectedFilter,
                    onFilterSelected = { selectedFilter = it }
                )
            }
        }
    }
}

/**
 * 🔹 Barra de búsqueda sin el botón de filtros.
 */
@Composable
fun SearchBar(searchText: String, onSearchTextChanged: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(50.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
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
 * 🔹 Botones de filtro para cambiar entre 'Courts' y 'Games'.
 */
@Composable
fun FilterButtons(selectedFilter: String, onFilterSelected: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        listOf("Courts", "Games").forEach { filter ->
            TextButton(
                onClick = { onFilterSelected(filter) },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (selectedFilter == filter) Color.White else Color(0xFF003F5C),
                    containerColor = if (selectedFilter == filter) Color(0xFF003F5C) else Color.White
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text(text = filter, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
