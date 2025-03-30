package com.example.parchadosapp.data

import com.example.parchadosapp.R
import com.example.parchadosapp.ui.components.Patch

object PatchRepository {
    val patches = listOf(
        Patch(
            image = R.drawable.campo_futbol,
            name = "Campo de Fútbol A",
            address = "Cl 63 #15-32, Bogotá",
            date = "Sábado, 29 de Marzo",
            time = "3:00 PM",
            remaining = 5,
            sport = "Fútbol",
            latitude = 4.650133,
            longitude = -74.066019
        ),
        Patch(
            image = R.drawable.cancha_basket,
            name = "Cancha de Baloncesto B",
            address = "Cl 62 #3-50, Bogotá",
            date = "Domingo, 30 de Marzo",
            time = "6:00 PM",
            remaining = 3,
            sport = "Baloncesto",
            latitude = 4.645390,
            longitude = -74.057067
        ),
        Patch(
            image = R.drawable.billarl,
            name = "Sala de Billar Central",
            address = "Cl. 45 #13-40, Santa Fé, Bogotá",
            date = "Lunes, 31 de Marzo",
            time = "8:00 PM",
            remaining = 2,
            sport = "Billar",
            latitude = 4.632527,
            longitude = -74.066987
        ),
        Patch(
            image = R.drawable.cancha_tenis,
            name = "Cancha de Tenis A",
            address = "Cl. 51 #4-06, Bogotá",
            date = "Lunes, 31 de Marzo",
            time = "5:00 PM",
            remaining = 4,
            sport = "Tenis",
            latitude = 4.635916,
            longitude = -74.061317
        )
    )
}
