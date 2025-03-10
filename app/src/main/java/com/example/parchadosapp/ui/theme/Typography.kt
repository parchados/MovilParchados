package com.example.parchadosapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.parchadosapp.R


val BrightRetro = FontFamily(Font(R.font.bright_retro))

val CustomTypography = Typography(  // Usa otro nombre aquí
    headlineLarge = TextStyle(
        fontFamily = BrightRetro,
        fontSize = 36.sp
    )
)

