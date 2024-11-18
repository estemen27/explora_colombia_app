package com.estebanbe.exploracolombiaapp.controller

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.estebanbe.exploracolombiaapp.R

class Inicio : AppCompatActivity() {

    private lateinit var btnSignUp: MaterialButton
    private lateinit var btnLogin: MaterialButton
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        // Ocultar la barra de acción
        supportActionBar?.hide()

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        // Inicializar las vistas
        initializeViews()

        // Aplicar animaciones
        applyAnimations()

        // Configurar los clics de los botones
        setupClickListeners()

        // Verificar si hay una sesión activa
        checkCurrentUser()
    }

    private fun initializeViews() {
        btnSignUp = findViewById(R.id.btnSignUp)
        btnLogin = findViewById(R.id.btnLogin)
    }

    private fun applyAnimations() {
        // Cargar las animaciones
        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)

        // Ajustar la duración de las animaciones
        fadeIn.duration = 1000
        slideUp.duration = 800

        // Aplicar animación fade in al logo
        findViewById<View>(R.id.imgLogo).startAnimation(fadeIn)

        // Aplicar animación slide up a los botones
        btnSignUp.startAnimation(slideUp)
        btnLogin.startAnimation(slideUp)
    }

    private fun setupClickListeners() {
        btnSignUp.setOnClickListener {
            // Animación de clic
            it.animate()
                .scaleX(0.95f)
                .scaleY(0.95f)
                .setDuration(100)
                .withEndAction {
                    it.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .withEndAction {
                            // Navegar a la pantalla de registro
                            navigateToSignUp()
                        }
                        .start()
                }
                .start()
        }

        btnLogin.setOnClickListener {
            // Animación de clic
            it.animate()
                .scaleX(0.95f)
                .scaleY(0.95f)
                .setDuration(100)
                .withEndAction {
                    it.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .withEndAction {
                            // Navegar a la pantalla de login
                            navigateToLogin()
                        }
                        .start()
                }
                .start()
        }
    }

    private fun checkCurrentUser() {
        // Si hay un usuario activo, ir directamente a la pantalla principal
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            navigateToMain()
        }
    }

    private fun navigateToSignUp() {
        val intent = Intent(this@Inicio, SignUp::class.java)
        startActivity(intent)
        // Aplicar animación de transición
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun navigateToLogin() {
        val intent = Intent(this@Inicio, Login::class.java)
        startActivity(intent)
        // Aplicar animación de transición
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun navigateToMain() {
        val intent = Intent(this@Inicio, MainActivityUsuarios::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Cerrar la aplicación al presionar el botón de retroceso
        finishAffinity()
    }
}