package com.estebanbe.exploracolombiaapp.service

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AuthService(private val context: Context) {
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val mDbRef: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun sendPasswordResetEmail(email: String, callback: (Boolean, String?) -> Unit) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true, null)
            } else {
                callback(false, task.exception?.message)
            }
        }
    }

    fun login(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = mAuth.currentUser
                if (user != null && user.isEmailVerified) {
                    onSuccess()
                } else {
                    onError("Verifica tu correo para activar la cuenta.")
                }
            } else {
                onError("Error de inicio de sesión: ${task.exception?.message}")
            }
        }
    }

    fun signUp(name: String, email: String, password: String, userType: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = mAuth.currentUser?.uid ?: return@addOnCompleteListener
                mAuth.currentUser?.sendEmailVerification()?.addOnCompleteListener { verificationTask ->
                    if (verificationTask.isSuccessful) {
                        addUserToDatabase(name, email, uid, userType)
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

    private fun addUserToDatabase(name: String, email: String, uid: String, userType: String) {
        val userData = mapOf(
            "name" to name,
            "email" to email,
            "uid" to uid,
            "type" to userType
        )
        mDbRef.child("users").child(uid).setValue(userData)
    }
}
