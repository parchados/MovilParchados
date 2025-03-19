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
import com.example.parchadosapp.R
import com.google.android.gms.location.*
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONObject
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun OpenStreetMapView(context: Context) {
    var userLocation by remember { mutableStateOf<GeoPoint?>(GeoPoint(4.627457, -74.064533)) } // Ubicación fija en la Javeriana
    var selectedDestination by remember { mutableStateOf<GeoPoint?>(null) } // Destino seleccionado
    var routePoints by remember { mutableStateOf<List<GeoPoint>>(emptyList()) }
    val localContext = LocalContext.current

    val hasLocationPermission = ContextCompat.checkSelfPermission(
        localContext, android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    // Obtener ubicación del usuario
    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission) {
            userLocation = GeoPoint(4.627457, -74.064533) // Se mantiene en la Javeriana Bogotá
        }
    }

    // Calcular la ruta cuando se selecciona un destino
    LaunchedEffect(selectedDestination) {
        if (userLocation != null && selectedDestination != null) {
            routePoints = getRoute(userLocation!!, selectedDestination!!)
        }
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(600.dp)
            .padding(16.dp)
    ) {
        AndroidView(
            factory = { ctx ->
                Configuration.getInstance().load(ctx, ctx.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
                MapView(ctx).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(true)

                    val mapController = controller
                    val startPoint = userLocation ?: GeoPoint(4.627457, -74.064533)
                    mapController.setZoom(15.0)
                    mapController.setCenter(startPoint)

                    overlays.clear() // Limpiar antes de agregar nuevos elementos

                    // Marcador de ubicación inicial
                    val userMarker = Marker(this).apply {
                        position = startPoint
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        title = "Pontificia Universidad Javeriana Bogotá"
                    }
                    overlays.add(userMarker)

                    // Marcador del Club de Ping Pong
                    val pingPongLocation = GeoPoint(4.631812, -74.066665)
                    val pingPongMarker = Marker(this).apply {
                        position = pingPongLocation
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        title = "Club de Ping Pong La Decanatura"
                        icon = ContextCompat.getDrawable(context, R.drawable.pingpong)

                        // 🔹 Cuando se hace clic en el marcador, se actualiza `selectedDestination`
                        setOnMarkerClickListener { _, _ ->
                            selectedDestination = pingPongLocation
                            true
                        }
                    }
                    overlays.add(pingPongMarker)

                    // Evento para seleccionar un punto en el mapa manualmente
                    setOnTouchListener { _, event ->
                        if (event.action == android.view.MotionEvent.ACTION_UP) {
                            val projection = this.projection
                            val iGeoPoint = projection.fromPixels(event.x.toInt(), event.y.toInt())

                            // Convertir a `GeoPoint`
                            selectedDestination = GeoPoint(iGeoPoint.latitude, iGeoPoint.longitude)
                        }
                        false
                    }

                    // 🔹 Dibujar la ruta si está disponible
                    if (routePoints.isNotEmpty()) {
                        val polyline = Polyline().apply {
                            setPoints(routePoints)
                            color = Color.Blue.hashCode()
                            width = 5f
                        }
                        overlays.add(polyline)
                    }

                    invalidate() // Redibujar el mapa para reflejar los cambios
                }
            },
            update = { mapView ->
                mapView.overlays.clear()

                // Marcador de la ubicación del usuario
                val userMarker = Marker(mapView).apply {
                    position = userLocation ?: GeoPoint(4.627457, -74.064533)
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    title = "Pontificia Universidad Javeriana Bogotá"
                }
                mapView.overlays.add(userMarker)

                // Marcador del Club de Ping Pong
                val pingPongLocation = GeoPoint(4.631812, -74.066665)
                val pingPongMarker = Marker(mapView).apply {
                    position = pingPongLocation
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    title = "Club de Ping Pong La Decanatura"
                    icon = ContextCompat.getDrawable(context, R.drawable.pingpong)
                    setOnMarkerClickListener { _, _ ->
                        selectedDestination = pingPongLocation
                        true
                    }
                }
                mapView.overlays.add(pingPongMarker)

                // 🔹 Dibujar la ruta si está disponible
                if (routePoints.isNotEmpty()) {
                    val polyline = Polyline().apply {
                        setPoints(routePoints)
                        color = Color.Blue.hashCode()
                        width = 5f
                    }
                    mapView.overlays.add(polyline)
                }

                mapView.invalidate() // Redibujar el mapa
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

/**
 * Obtiene la ruta desde el usuario hasta el Club de Ping Pong usando el servicio de OpenStreetMap.
 */
suspend fun getRoute(start: GeoPoint, end: GeoPoint): List<GeoPoint> {
    return withContext(Dispatchers.IO) {
        try {
            val url = "https://router.project-osrm.org/route/v1/driving/${start.longitude},${start.latitude};${end.longitude},${end.latitude}?overview=simplified"
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            val response = connection.inputStream.bufferedReader().use { it.readText() }
            connection.disconnect()

            val jsonResponse = JSONObject(response)
            val routes = jsonResponse.getJSONArray("routes")
            if (routes.length() > 0) {
                val geometry = routes.getJSONObject(0).getJSONObject("geometry")
                decodePolyline(geometry.getString("coordinates"))
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("OSM Route", "Error obteniendo la ruta: ${e.message}")
            emptyList()
        }
    }
}

/**
 * Decodifica la respuesta de OSRM en una lista de `GeoPoint`.
 */
fun decodePolyline(encoded: String): List<GeoPoint> {
    val points = mutableListOf<GeoPoint>()
    val coordinates = JSONObject(encoded).getJSONArray("coordinates")
    for (i in 0 until coordinates.length()) {
        val point = coordinates.getJSONArray(i)
        points.add(GeoPoint(point.getDouble(1), point.getDouble(0)))
    }
    return points
}