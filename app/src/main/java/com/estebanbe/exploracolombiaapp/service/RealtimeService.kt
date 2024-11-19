package com.estebanbe.exploracolombiaapp.service

import com.estebanbe.exploracolombiaapp.model.Producto
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class RealtimeService {

    private val realtimeDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference

    // Subir datos a Firebase Realtime Database
    fun saveToRealtimeDatabase(path: String, key: String, data: Map<String, Any>, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        realtimeDatabase.child(path).child(key).setValue(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    // Obtener datos de Firebase Realtime Database
    fun getFromRealtimeDatabase(path: String, key: String, onSuccess: (Map<String, Any>?) -> Unit, onFailure: (Exception) -> Unit) {
        realtimeDatabase.child(path).child(key).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val data = snapshot.value as? Map<String, Any> // Convertir el valor a Map<String, Any>
                    onSuccess(data)
                } else {
                    onSuccess(null) // Si no existe el dato, devuelve null
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onFailure(error.toException()) // En caso de error, devuelve la excepción
            }
        })
    }

    // Actualizar un valor en Firebase Realtime Database
    fun updateRealtimeDatabase(path: String, key: String, data: Map<String, Any>, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        realtimeDatabase.child(path).child(key).updateChildren(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    // Eliminar un valor en Firebase Realtime Database
    fun deleteFromRealtimeDatabase(path: String, key: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        realtimeDatabase.child(path).child(key).removeValue()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun generatePushKey(path: String): String {
        return realtimeDatabase.child(path).push().key!!
    }

    fun buscarProductoPorCampo(campo: String, valor: Any, callback: (List<Producto>) -> Unit) {
        val queryRef = realtimeDatabase.child("productos")

        val query = when (valor) {
            is Boolean -> queryRef.orderByChild(campo).equalTo(valor)
            is Double -> queryRef.orderByChild(campo).equalTo(valor)
            is String -> queryRef.orderByChild(campo).equalTo(valor)
            else -> {
                callback(emptyList())
                return
            }
        }

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productos = mutableListOf<Producto>()
                for (productoSnapshot in snapshot.children) {
                    val producto = productoSnapshot.getValue(Producto::class.java)
                    producto?.let { productos.add(it) }
                }
                callback(productos)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(emptyList())
            }
        })
    }


}
