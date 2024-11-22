package com.estebanbe.exploracolombiaapp.manager

import com.estebanbe.exploracolombiaapp.model.User
import com.estebanbe.exploracolombiaapp.service.RealtimeService
import com.google.firebase.auth.FirebaseAuth

object UserManager {
    private val realtimeService: RealtimeService = RealtimeService()
    // Guardar un usuario
    fun setUsuario(usuario: User, onComplete: (Boolean) -> Unit) {
        val userMap = mapOf(
            "name" to usuario.name,
            "email" to usuario.email,
            "uid" to usuario.uid,
            "type" to usuario.type,
            "idComercio" to (usuario.idComercio ?: ""),
            "status" to (usuario.status ?: ""),
            "saldoPuntos" to usuario.saldoPuntos
        )

        // Guardar datos en Firebase bajo el nodo "usuarios" y la clave UID del usuario
        realtimeService.saveToRealtimeDatabase("users", usuario.uid, userMap, {
            onComplete(true) // Éxito
        }, {
            onComplete(false) // Error
        })
    }

    // Obtener un usuario
    fun getUsuario(uid: String, onResult: (User) -> Unit) {
        realtimeService.getFromRealtimeDatabase("users", uid, { data ->
            if (data != null) {
                val puntos = data["saldoPuntos"] as Long
                val usuario = User(
                    name = data["name"] as String,
                    email = data["email"] as String,
                    uid = data["uid"] as String,
                    type = data["type"] as String,
                    idComercio = data["idComercio"] as String?,
                    status = data["status"] as String?,
                    saldoPuntos = puntos.toInt()
                )
                onResult(usuario)
            } else {
                onResult(User()) // Si no se encuentran datos
            }
        }, { exception ->
            onResult(User()) // En caso de error
        })
    }

    // Obtener los puntos de un usuario
    fun getPuntos(uid: String, callback: (Int) -> Unit) {

        // Obtener puntos de Firebase bajo el nodo del usuario actual
        realtimeService.getFromRealtimeDatabase("users", uid, { data ->
            val saldoPuntos = data?.get("saldoPuntos").toString()
            callback(saldoPuntos.toInt())  // Llamada al callback con saldoPuntos
        }, { exception ->
            callback(0) // En caso de error, devuelve 0
        })
    }

    // Establecer los puntos de un usuario
    fun setPuntos(uid: String, puntos: Int, callback: (Boolean) -> Unit) {

        val puntosMap = mapOf("saldoPuntos" to puntos)

        // Actualizar puntos en Firebase
        realtimeService.updateRealtimeDatabase("users", uid, puntosMap, {
            callback(true) // Éxito
        }, { exception ->
            callback(false) // Error
        })
    }

}
