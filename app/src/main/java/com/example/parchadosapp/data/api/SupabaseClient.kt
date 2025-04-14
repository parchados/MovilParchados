package com.example.parchadosapp.data.api

import NotificacionRequest
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

suspend fun marcarNotificacionComoLeida(notificacionId: String): Boolean {
    return try {
        supabase.from("notificaciones")
            .update(mapOf("leida" to true)) {
                filter {
                    eq("id", notificacionId)
                }
            }
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}


suspend fun eliminarNotificacionesPorUsuario(usuarioId: String): Boolean {
    return try {
        supabase.from("notificaciones")
            .delete {
                filter {
                    eq("destinatario_id", usuarioId)
                }
            }
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

suspend fun crearNotificacionParaUsuario(usuarioId: String): Boolean {
    return try {
        supabase.from("notificaciones")
            .insert(
                mapOf(
                    "tipo" to "parche",
                    "titulo" to "¡Nuevo parche creado!",
                    "descripcion" to "🎉 Felicidades, tu parche fue creado. ¡Pásala al máximo!",
                    "destinatario_id" to usuarioId,
                    "referencia_tipo" to "parche",
                    "leida" to false,
                    "estado" to "Activo"
                )
            )
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

suspend fun crearNotificacion(notificacion: NotificacionRequest): Boolean {
    return try {
        supabase.from("notificaciones").insert(notificacion)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}


suspend fun obtenerParchesPorUsuarioConImagen(usuarioId: String): List<ParcheConImagen> {
    return try {
        val parches = supabase.from("parches")
            .select {
                filter {
                    eq("creador_id", usuarioId) // <- ✅ Mantiene el filtro igual a la anterior
                }
            }
            .decodeList<ParcheRequest>()

        parches.map { parche ->
            val espacio = obtenerEspacioPorId(parche.espacio_id)
            ParcheConImagen(parche, espacio?.imagen_url)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}


suspend fun eliminarParchePorId(parcheId: String): Boolean {
    return try {
        supabase.from("parches")
            .delete {
                filter {
                    eq("id", parcheId)
                }
            }
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

suspend fun estaInscritoEnParche(usuarioId: String, parcheId: String): Boolean {
    return try {
        val resultado = supabase.from("personas_parches")
            .select {
                filter {
                    eq("persona_id", usuarioId)
                    eq("parche_id", parcheId)
                }
            }
            .decodeList<Any>()
        resultado.isNotEmpty()
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}


suspend fun unirseAParche(usuarioId: String, parcheId: String): Boolean {
    return try {
        supabase.from("personas_parches")
            .insert(
                mapOf(
                    "persona_id" to usuarioId,
                    "parche_id" to parcheId
                )
            )
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}


suspend fun salirDeParche(usuarioId: String, parcheId: String): Boolean {
    return try {
        supabase.from("personas_parches")
            .delete {
                filter {
                    eq("persona_id", usuarioId)
                    eq("parche_id", parcheId)
                }
            }
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}















