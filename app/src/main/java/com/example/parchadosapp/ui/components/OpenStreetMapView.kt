package com.example.parchadosapp.ui.components

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun OpenStreetMapView(context: Context) {
    Card(
        shape = RoundedCornerShape(16.dp), // 🔹 Bordes redondeados
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), // 🔹 Sombra
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth(0.9f) // 🔹 Ancho del 90% del contenedor padre
            .height(250.dp) // 🔹 Altura fija del mapa
            .padding(16.dp) // 🔹 Espaciado exterior
    ) {
        AndroidView(
            factory = { ctx ->
                // Configurar OSM
                Configuration.getInstance().load(ctx, ctx.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))

                MapView(ctx).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(true)

                    val mapController = controller
                    mapController.setZoom(16.0) // 🔹 Zoom ajustado
                    val startPoint = GeoPoint(4.627457, -74.064533) // 🔹 Javeriana
                    mapController.setCenter(startPoint)

                    // Agregar marcador
                    val marker = Marker(this)
                    marker.position = startPoint
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    marker.title = "Pontificia Universidad Javeriana"
                    overlays.add(marker)
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
