package com.estebanbe.exploracolombiaapp.controller

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.estebanbe.exploracolombiaapp.R
import com.estebanbe.exploracolombiaapp.service.AuthService

class Login : AppCompatActivity() {
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvForgotPassword: TextView
    private lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        // Inicializar AuthService
        authService = AuthService(this)

        // Enlazar vistas
        editEmail = findViewById(R.id.etEmail)
        editPassword = findViewById(R.id.etpassword)
        btnLogin = findViewById(R.id.btLogin)
        tvForgotPassword = findViewById(R.id.tvforgotPassword)

        // Configurar acciones
        btnLogin.setOnClickListener {
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, ingresa un correo electrónico y una contraseña.", Toast.LENGTH_SHORT).show()
            } else {
                authService.login(email, password, {
                    val intent = Intent(this@Login, MainActivityUsuarios::class.java)
                    finish()
                    startActivity(intent)
                }, { errorMessage ->
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                })
            }
        }

        // Enlace para restablecer contraseña
        tvForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
        }
    }
}
