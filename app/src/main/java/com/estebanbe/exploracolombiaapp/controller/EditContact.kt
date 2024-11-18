package com.estebanbe.exploracolombiaapp.controller

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.estebanbe.exploracolombiaapp.R
import com.estebanbe.exploracolombiaapp.service.FirebaseService

class EditContact : AppCompatActivity() {

    private lateinit var direccionEditText: EditText
    private lateinit var telefonoEditText: EditText
    private lateinit var instaEditText: EditText
    private lateinit var faceEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button

    private val firebaseService = FirebaseService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_contact)

        direccionEditText = findViewById(R.id.direccion)
        telefonoEditText = findViewById(R.id.telefono)
        instaEditText = findViewById(R.id.insta)
        faceEditText = findViewById(R.id.face)
        emailEditText = findViewById(R.id.email)
        saveButton = findViewById(R.id.save)
        cancelButton = findViewById(R.id.can)

        saveButton.setOnClickListener { saveContactInfo() }
        cancelButton.setOnClickListener { finish() }
    }

    private fun saveContactInfo() {
        val contactInfo = mapOf(
            "direccion" to direccionEditText.text.toString().trim(),
            "telefono" to telefonoEditText.text.toString().trim(),
            "instagram" to instaEditText.text.toString().trim(),
            "facebook" to faceEditText.text.toString().trim(),
            "email" to emailEditText.text.toString().trim()
        )

        if (contactInfo.values.any { it.isEmpty() }) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        firebaseService.saveToRealtimeDatabase(
            "restaurantes/comerciante_info",
            null,
            contactInfo,
            onSuccess = { Toast.makeText(this, "Información guardada exitosamente", Toast.LENGTH_SHORT).show() },
            onFailure = { e -> Toast.makeText(this, "Error al guardar la información: ${e.message}", Toast.LENGTH_LONG).show() }
        )
    }
}