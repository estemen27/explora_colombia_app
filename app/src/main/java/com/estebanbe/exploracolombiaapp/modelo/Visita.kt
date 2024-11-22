package com.estebanbe.exploracolombiaapp.modelo

data class Visita(
    val usuarioId: String,
    val restauranteId: String,
    val timestamp: Long,
    val puntos: Int
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "usuarioId" to usuarioId,
            "restauranteId" to restauranteId,
            "timestamp" to timestamp,
            "puntos" to puntos
        )
    }
}
