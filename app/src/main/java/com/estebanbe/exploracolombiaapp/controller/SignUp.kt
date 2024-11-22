package com.estebanbe.exploracolombiaapp.controller


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.estebanbe.exploracolombiaapp.R
import com.estebanbe.exploracolombiaapp.service.AuthService

class SignUp : AppCompatActivity() {

    private lateinit var editName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var editIdComercio: EditText
    private lateinit var editPasswordComercio: EditText
    private lateinit var btnSignUp: Button
    private lateinit var authService: AuthService
    private lateinit var userTypeRadioGroup: RadioGroup
    private lateinit var propietarioFieldsContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singup)
        supportActionBar?.hide()

        // Inicializar AuthService
        authService = AuthService(this)

        // Enlazar vistas
        editName = findViewById(R.id.nameEditText)
        editEmail = findViewById(R.id.emailEditText)
        editPassword = findViewById(R.id.passwordEditText)
        editIdComercio = findViewById(R.id.idComercioEditText)
        editPasswordComercio = findViewById(R.id.passwordComercioEditText)
        btnSignUp = findViewById(R.id.registerButton)
        userTypeRadioGroup = findViewById(R.id.userTypeRadioGroup)
        propietarioFieldsContainer = findViewById(R.id.propietarioFieldsContainer)

        // Mostrar u ocultar campos adicionales basados en la selección
        userTypeRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.propietarioRadioButton) {
                propietarioFieldsContainer.visibility = View.VISIBLE
            } else {
                propietarioFieldsContainer.visibility = View.GONE
            }
        }

        btnSignUp.setOnClickListener {
            val name = editName.text.toString().trim()
            val email = editEmail.text.toString().trim()
            val password = editPassword.text.toString().trim()

            // Obtener userType basado en selección
            val selectedUserTypeId = userTypeRadioGroup.checkedRadioButtonId
            val selectedRadioButton = findViewById<RadioButton>(selectedUserTypeId)
            val userType = selectedRadioButton.text.toString().toLowerCase()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                if (userType == "propietario") {
                    val idComercio = editIdComercio.text.toString().trim()
                    val passwordComercio = editPasswordComercio.text.toString().trim()

                    if (idComercio.isEmpty() || passwordComercio.isEmpty()) {
                        Toast.makeText(this, "Por favor, complete los campos de propietario.", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    authService.signUp(name, email, password, userType, idComercio, passwordComercio, {
                        val intent = Intent(this@SignUp, Login::class.java)
                        startActivity(intent)
                        finish()
                    }, { errorMessage ->
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    })

                } else {
                    authService.signUp(name, email, password, userType, null, null, {
                        val intent = Intent(this@SignUp, Login::class.java)
                        startActivity(intent)
                        finish()
                    }, { errorMessage ->
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    })
                }
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
