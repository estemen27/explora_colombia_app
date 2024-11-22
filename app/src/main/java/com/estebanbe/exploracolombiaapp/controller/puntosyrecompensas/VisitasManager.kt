package com.estebanbe.exploracolombiaapp.controller.puntosyrecompensas

import android.content.Context
import android.widget.Toast
import com.estebanbe.exploracolombiaapp.manager.UserManager
import com.estebanbe.exploracolombiaapp.modelo.Visita
import com.estebanbe.exploracolombiaapp.service.AuthService
import com.estebanbe.exploracolombiaapp.service.RealtimeService

class VisitasManager() {
    private val authService = AuthService()
    private val realtimeService = RealtimeService()

    fun registrarVisita(restauranteId: String, puntos: Int) {
        val usuarioId = authService.currentUserUid()

        val visita = Visita(usuarioId, restauranteId, System.currentTimeMillis(), puntos)

        // Guardar la visita usando RealtimeService
        val visitaKey = realtimeService.generatePushKey("visitas")
        if (visitaKey != null) {
            val visitaMap = mapOf(
                "usuarioId" to visita.usuarioId,
                "restauranteId" to visita.restauranteId,
                "timestamp" to visita.timestamp,
                "puntos" to visita.puntos
            )
            realtimeService.saveToRealtimeDatabase("visitas", visitaKey, visitaMap, {
                actualizarPuntosUsuario(usuarioId, puntos)
            }, {})
        }
    }

    private fun actualizarPuntosUsuario(usuarioId: String, puntos: Int) {
        UserManager.getPuntos(usuarioId) { puntosActuales ->
            val nuevosPuntos = puntosActuales + puntos
            UserManager.setPuntos(usuarioId, nuevosPuntos) {}
        }
    }
}
