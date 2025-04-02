package com.example.parchadosapp.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.parchadosapp.data.PatchRepository
import com.example.parchadosapp.ui.components.BottomNavigationBar
import com.example.parchadosapp.ui.components.PatchCard
import com.example.parchadosapp.ui.components.Patch

@Composable
fun BuscarScreen(navController: NavController, context: Context) {

    val patches = PatchRepository.patches

    var searchText by remember { mutableStateOf("") }
    var selectedFilters by remember { mutableStateOf(emptySet<String>()) }
    var isSportsFilter by remember { mutableStateOf(false) }

    val filteredPatches = patches.filter { patch ->
        val matchesSearch = searchText.isBlank() || patch.name.contains(searchText, ignoreCase = true)
        val matchesFilter = selectedFilters.isEmpty() || patch.sport in selectedFilters || selectedFilters.any { patch.name.contains(it, ignoreCase = true) }
        matchesSearch && matchesFilter
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F5F0)),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                    .background(Color.White)
            ) {
                FilterSection(
                    selectedFilters = selectedFilters,
                    onFilterSelected = { filter ->
                        selectedFilters = if (filter == null) {
                            emptySet()
                        } else if (filter in selectedFilters) {
                            selectedFilters - filter
                        } else {
                            selectedFilters + filter
                        }
                    },
                    searchText = searchText,
                    onSearchTextChanged = { searchText = it }
                )
            }


            Text(
                text = "Filtra, Busca y Encuentra",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF003F5C),
                modifier = Modifier.padding(vertical = 16.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                itemsIndexed(filteredPatches) { index, patch ->
                    PatchCard(patch = patch) {
                        val originalIndex = patches.indexOf(patch)
                        navController.navigate("patch_detail/$originalIndex")
                    }
                }
            }

            Spacer(modifier = Modifier.height(120.dp))
        }

        BottomNavigationBar(navController, Modifier.align(Alignment.BottomCenter))
    }
}
