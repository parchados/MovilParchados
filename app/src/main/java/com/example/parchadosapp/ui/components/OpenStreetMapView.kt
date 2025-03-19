package com.example.parchadosapp.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import kotlinx.coroutines.suspendCancellableCoroutine
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Composable
fun OpenStreetMapView(context: Context) {
    var userLocation by remember { mutableStateOf<GeoPoint?>(null) }
    var locationLoaded by remember { mutableStateOf(false) }
    val localContext = LocalContext.current

    // Ubicación de respaldo (Pontificia Universidad Javeriana)
    val fallbackLocation = GeoPoint(4.627457, -74.064533)

    // Verificar si los permisos están concedidos
    val hasLocationPermission = ContextCompat.checkSelfPermission(
        localContext, android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission) {
            userLocation = getUserLocation(context) ?: fallbackLocation // Usa Javeriana si falla
            locationLoaded = true
        } else {
            userLocation = fallbackLocation // Si no hay permisos, usa la Javeriana
        }
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(250.dp)
            .padding(16.dp)
    ) {
        AndroidView(
            factory = { ctx ->
                Configuration.getInstance().load(ctx, ctx.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
                MapView(ctx).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(true)

                    val mapController = controller

                    val startPoint = userLocation ?: fallbackLocation
                    mapController.setZoom(18.0) // 🔹 Zoom más alto para mayor detalle
                    mapController.setCenter(startPoint)

                    val marker = Marker(this)
                    marker.position = startPoint
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    marker.title = if (userLocation == fallbackLocation) "Ubicación no encontrada - Javeriana" else "Tu ubicación"
                    overlays.add(marker)
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

/**
 * Obtiene la ubicación actual del usuario de forma asincrónica.
 */
@SuppressLint("MissingPermission")
suspend fun getUserLocation(context: Context): GeoPoint? {
    return try {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val location: Location = fusedLocationClient.getLastLocationAsync() ?: return null
        GeoPoint(location.latitude, location.longitude)
    } catch (e: Exception) {
        Log.e("OpenStreetMap", "Error obteniendo ubicación: ${e.message}")
        null
    }
}

/**
 * Función de extensión para obtener la última ubicación de forma asincrónica.
 */
@SuppressLint("MissingPermission")
suspend fun FusedLocationProviderClient.getLastLocationAsync(): Location? {
    return suspendCancellableCoroutine { cont ->
        lastLocation.addOnSuccessListener { location ->
            cont.resume(location)
        }.addOnFailureListener { e ->
            cont.resumeWithException(e)
        }
    }
}
