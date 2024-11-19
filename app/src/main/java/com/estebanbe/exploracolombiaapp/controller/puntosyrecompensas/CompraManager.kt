package com.estebanbe.exploracolombiaapp.manager

import android.content.Context
import com.estebanbe.exploracolombiaapp.model.Producto
import com.estebanbe.exploracolombiaapp.service.RealtimeService
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.estebanbe.exploracolombiaapp.modelo.Recompensa

object CompraManager {
    private val realtimeService = RealtimeService()
    private val userManager = UserManager

    fun setCompra(
        context: Context,
        producto: Producto,
        userId: String,
        onCompraCompletada: () -> Unit,
        onCompraFallida: () -> Unit
    ) {
        userManager.getPuntos(userId) { saldoPuntos ->
            if (saldoPuntos >= producto.precio) {
                val nuevoSaldoPuntos = saldoPuntos - producto.precio
                userManager.setPuntos(userId, nuevoSaldoPuntos) { exito ->
                    if (exito) {
                        guardarRedencion(producto, userId, producto.precio,
                            onSuccess = { onCompraCompletada() },
                            onFailure = { onCompraFallida() }
                        )
                    } else {
                        onCompraFallida()
                    }
                }
            } else {
                Toast.makeText(context, "Puntos insuficientes", Toast.LENGTH_SHORT).show()
                onCompraFallida()
            }
        }
    }

    private fun guardarRedencion(
        producto: Producto,
        userId: String,
        puntosUsados: Int,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        val redencionId = realtimeService.generatePushKey("redenciones") ?: ""
        val recompensa = Recompensa(
            rendencionId = redencionId,
            userId = userId,
            productoId = producto.productoId,
            nombreProducto = producto.nombre,
            puntosUsados = puntosUsados,
            fechaRedencion = Timestamp.now()
        )

        val data = recompensaToMap(recompensa)

        realtimeService.saveToRealtimeDatabase(
            path = "redenciones",
            key = redencionId,
            data = data,
            onSuccess = { onSuccess() },
            onFailure = { onFailure() }
        )
    }

    private fun recompensaToMap(recompensa: Recompensa): Map<String, Any> {
        return mapOf(
            "rendencionId" to recompensa.rendencionId,
            "userId" to recompensa.userId,
            "productoId" to recompensa.productoId,
            "nombreProducto" to recompensa.nombreProducto,
            "puntosUsados" to recompensa.puntosUsados,
            "fechaRedencion" to (recompensa.fechaRedencion ?: Timestamp.now())
        )
    }
}
