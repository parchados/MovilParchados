package com.example.parchadosapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryColor,
    background = DarkBlue,
    onPrimary = White
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    background = White,
    onPrimary = DarkBlue
)

@Composable
fun ParchadosAppTheme(content: @Composable () -> Unit) {
    val colors = if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme
    MaterialTheme(colorScheme = colors, typography = Typography, content = content)
}
