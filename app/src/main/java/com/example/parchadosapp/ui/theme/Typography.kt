package com.example.parchadosapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.example.parchadosapp.R

// Definimos las fuentes personalizadas
val BrightRetro = FontFamily(Font(R.font.bright_retro)) // Fuente para "Parchados"
val BowlbyOneSC = FontFamily(Font(R.font.bowlbyonesc_regular)) // Fuente para textos en negrita

// Definimos la tipografía global con ambas fuentes
val CustomTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = BrightRetro,
        fontSize = 36.sp
    ),
    titleLarge = TextStyle(
        fontFamily = BowlbyOneSC,
        fontSize = 20.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = BowlbyOneSC,
        fontSize = 16.sp
    )
)
