package com.estebanbe.exploracolombiaapp.service

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class AuthService(private val context: Context? = null) {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val mDbRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Enviar correo de restablecimiento de contraseña
    fun sendPasswordResetEmail(email: String, callback: (Boolean, String?) -> Unit) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true, null)
            } else {
                callback(false, task.exception?.message)
            }
        }
    }

    // Iniciar sesión
    fun login(
        email: String,
        password: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = mAuth.currentUser
                if (user != null && user.isEmailVerified) {
                    val uid = user.uid
                    // Obtener el tipo de usuario desde Firestore
                    firestore.collection("users").document(uid).get().addOnSuccessListener { document ->
                        val userType = document.getString("type")
                        if (userType != null) {
                            onSuccess(userType)
                        } else {
                            onError("No se pudo determinar el tipo de usuario.")
                        }
                    }.addOnFailureListener { e ->
                        onError("Error al obtener datos del usuario: ${e.message}")
                    }
                } else {
                    onError("Verifica tu correo para activar la cuenta.")
                }
            } else {
                onError("Error de inicio de sesión: ${task.exception?.message}")
            }
        }
    }

    // Registrar usuario
    fun signUp(
        name: String,
        email: String,
        password: String,
        userType: String,
        idComercio: String? = null,
        passwordComercio: String? = null,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = mAuth.currentUser?.uid ?: return@addOnCompleteListener
                mAuth.currentUser?.sendEmailVerification()?.addOnCompleteListener { verificationTask ->
                    if (verificationTask.isSuccessful) {
                        // Guardar datos del usuario en Firestore
                        addUserToDatabase(name, email, uid, userType, idComercio, passwordComercio)
                        onSuccess(uid)
                    } else {
                        onError("Error al enviar correo de verificación: ${verificationTask.exception?.message}")
                    }
                }
            } else {
                onError("Error de registro: ${task.exception?.message}")
            }
        }
    }

    // Agregar usuario a la base de datos
    private fun addUserToDatabase(
        name: String,
        email: String,
        uid: String,
        userType: String,
        idComercio: String? = null,
        passwordComercio: String? = null
    ) {
        val userData = mapOf(
            "name" to name,
            "email" to email,
            "uid" to uid,
            "type" to userType,
            "idComercio" to idComercio,
            "passwordComercio" to passwordComercio,
            "saldoPuntos" to 0
        )
        firestore.collection("users").document(uid).set(userData).addOnSuccessListener {
            // Éxito al guardar datos
        }.addOnFailureListener { e ->
            // Error al guardar datos
        }
    }

    // Obtener UID del usuario actual
    fun currentUserUid(): String {
        return mAuth.currentUser?.uid ?: ""
    }
}
