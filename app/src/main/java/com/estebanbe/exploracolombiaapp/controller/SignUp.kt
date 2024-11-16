package com.estebanbe.exploracolombiaapp.controller

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.estebanbe.exploracolombiaapp.service.AuthService
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.estebanbe.exploracolombiaapp.R

class SignUp : AppCompatActivity() {
    private lateinit var editName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var editIdComercio: EditText
    private lateinit var editPasswordComercio: EditText
    private lateinit var btnSignUp: Button
    private lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singup)
        supportActionBar?.hide()

        // Inicializar AuthService
        authService = AuthService(this)

        // Inicializar vistas
        editName = findViewById(R.id.nameEditText)
        editEmail = findViewById(R.id.emailEditText)
        editPassword = findViewById(R.id.passwordEditText)
        editIdComercio = findViewById(R.id.idComercioEditText)
        editPasswordComercio = findViewById(R.id.passwordComercioEditText)
        btnSignUp = findViewById(R.id.registerButton)

        btnSignUp.setOnClickListener {
            val name = editName.text.toString().trim()
            val email = editEmail.text.toString().trim()
            val password = editPassword.text.toString().trim()
            val userType = "turista" // Por simplicidad

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                authService.signUp(name, email, password, userType, {
                    val intent = Intent(this@SignUp, Login::class.java)
                    finish()
                    startActivity(intent)
                }, { errorMessage ->
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                })
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

