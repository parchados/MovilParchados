package com.example.parchadosapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// 🎨 Definición de colores oscuros
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFEAC67A),   // Amarillo dorado
    background = Color(0xFF003F5C), // Azul profundo
    onPrimary = Color(0xFFF8F5F0),  // Beige claro para contraste
    surface = Color(0xFF2F4B7C),    // Azul más claro
    onSurface = Color(0xFFF8F5F0),  // Beige claro para textos
)

// 🎨 Definición de colores claros
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF003F5C),  // Azul profundo
    background = Color(0xFFF8F5F0), // Beige claro
    onPrimary = Color(0xFF1E293B), // Gris oscuro para textos
    surface = Color(0xFFA5C8E1),   // Azul pastel para detalles sutiles
    onSurface = Color(0xFF1E293B), // Gris oscuro para textos
    secondary = Color(0xFF2F4B7C), // Azul más claro para bordes y detalles
)

// 🌎 Aplicación de los colores en el tema
@Composable
fun ParchadosAppTheme(content: @Composable () -> Unit) {
    val colors = if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
