// ForgotPassword.kt
package com.estebanbe.exploracolombiaapp.controller

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.estebanbe.exploracolombiaapp.R
import com.estebanbe.exploracolombiaapp.service.AuthService

class ForgotPassword : AppCompatActivity() {
    private lateinit var etEmail: EditText
    private lateinit var btResetPassword: Button
    private lateinit var tvCancel: TextView
    private lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        // Inicializar AuthService
        authService = AuthService(this)

        // Vinculación de los elementos de la vista
        etEmail = findViewById(R.id.etEmail)
        btResetPassword = findViewById(R.id.btResetPassword)
        tvCancel = findViewById(R.id.tvCancel)

        // Acción del botón "Reset Password"
        btResetPassword.setOnClickListener {
            val email = etEmail.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(this, "Por favor ingrese su correo", Toast.LENGTH_SHORT).show()
            } else {
                authService.sendPasswordResetEmail(email) { success, message ->
                    if (success) {
                        Toast.makeText(this, "Se ha enviado el email.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Acción del botón "Cancel"
        tvCancel.setOnClickListener { finish() }
    }
}

