package com.example.parchadosapp.data.api

import com.example.parchadosapp.data.models.Espacio
import com.example.parchadosapp.data.models.Lugar
import com.example.parchadosapp.data.models.Notificacion
import com.example.parchadosapp.data.models.ParcheRequest
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.serialization.json.Json
import com.example.parchadosapp.data.models.ParcheConImagen
import com.example.parchadosapp.data.models.Persona

val supabase: SupabaseClient = createSupabaseClient(
    supabaseUrl = "https://giynykejishwdmsgshag.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImdpeW55a2VqaXNod2Rtc2dzaGFnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDAyOTE2MDQsImV4cCI6MjA1NTg2NzYwNH0.LIbJB6-QqfwQVZ5Oq44japsF0Lu4AOc5kNgPi1_IHRo"
) {
    install(Postgrest)

    // ✅ Agregamos el serializador personalizado
    defaultSerializer = KotlinXSerializer(
        Json {
            ignoreUnknownKeys = true // <--- Esta línea es clave
        }
    )
}

suspend fun obtenerLugares(): List<Lugar> {
    return supabase.from("lugares").select().decodeList()
}


suspend fun obtenerEspaciosPorLugar(lugarId: String): List<Espacio> {
    return supabase.from("espacios")
        .select {
            filter {
                eq("lugar_id", lugarId)
            }
        }
        .decodeList()
}


suspend fun crearParche(parche: ParcheRequest): Boolean {
    return try {
        supabase.from("parches").insert(parche)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

suspend fun obtenerParches(): List<ParcheRequest> {
    return supabase.from("parches").select().decodeList()
}


suspend fun obtenerParchesConImagen(): List<ParcheConImagen> {
    val parches = obtenerParches() // o tu versión limitada
    return parches.map { parche ->
        val espacio = obtenerEspacioPorId(parche.espacio_id)
        ParcheConImagen(parche, espacio?.imagen_url)
    }
}

suspend fun obtenerEspacioPorId(espacioId: String): Espacio? {
    return supabase.from("espacios")
        .select {
            filter {
                eq("id", espacioId)
            }
        }
        .decodeSingleOrNull()
}


suspend fun obtenerPersonaPorEmail(email: String): Persona? {
    return try {
        supabase.from("personas")
            .select {
                filter {
                    eq("email", email)
                }
                limit(1)
            }
            .decodeSingleOrNull()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


suspend fun obtenerPersonaPorId(id: String): Persona? {
    return supabase.from("personas")
        .select {
            filter {
                eq("id", id)
            }
            limit(1)
        }
        .decodeSingleOrNull()
}


suspend fun obtenerNotificacionesPorUsuario(usuarioId: String): List<Notificacion> {
    return supabase.from("notificaciones")
        .select {
            filter {
                eq("destinatario_id", usuarioId)
            }
        }
        .decodeList()
}





