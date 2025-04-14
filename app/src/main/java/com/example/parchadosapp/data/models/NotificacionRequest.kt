
import kotlinx.serialization.Serializable


@Serializable
data class NotificacionRequest(
    val tipo: String,
    val titulo: String,
    val descripcion: String,
    val destinatario_id: String,
    val remitente_id: String? = null,
    val referencia_id: String? = null,
    val referencia_tipo: String? = null,
    val leida: Boolean = false,
    val estado: String = "Activo"
)
