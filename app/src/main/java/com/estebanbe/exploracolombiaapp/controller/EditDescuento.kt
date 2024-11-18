package com.estebanbe.exploracolombiaapp.controller

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.estebanbe.exploracolombiaapp.R
import com.estebanbe.exploracolombiaapp.model.Producto
import com.estebanbe.exploracolombiaapp.service.FirebaseService
import java.util.UUID

class EditDescuento : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button

    private var imageUri: Uri? = null

    private val firebaseService = FirebaseService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_descuento)

        imageView = findViewById(R.id.logoImageView)
        saveButton = findViewById(R.id.saveButton)
        cancelButton = findViewById(R.id.cancelButton)

        saveButton.setOnClickListener { saveDescuento() }
        cancelButton.setOnClickListener { finish() }
    }

    private fun saveDescuento() {
        val nombre = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.nameEditText).text.toString().trim()
        val descripcion = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.descriptionEditText).text.toString().trim()
        val precio = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.priceEditText).text.toString().trim()

        if (nombre.isEmpty() || descripcion.isEmpty() || precio.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (imageUri == null) {
            saveProducto(Producto(nombre, descripcion, precio))
        } else {
            uploadImageAndSaveProducto(Producto(nombre, descripcion, precio))
        }
    }

    private fun saveProducto(producto: Producto) {
        firebaseService.saveToRealtimeDatabase(
            "productos",
            null,
            mapOf(
                "nombre" to producto.nombre,
                "descripcion" to producto.descripcion,
                "precio" to producto.precio
            ),
            onSuccess = { Toast.makeText(this, "Descuento guardado exitosamente", Toast.LENGTH_SHORT).show() },
            onFailure = { e -> Toast.makeText(this, "Error al guardar descuento: ${e.message}", Toast.LENGTH_LONG).show() }
        )
    }

    private fun uploadImageAndSaveProducto(producto: Producto) {
        val imagePath = "productos/${UUID.randomUUID()}.jpg"
        firebaseService.uploadToStorage(
            imagePath,
            imageUri!!.toString().toByteArray(),
            onSuccess = { imageUrl ->
                firebaseService.saveToRealtimeDatabase(
                    "productos",
                    null,
                    mapOf(
                        "nombre" to producto.nombre,
                        "descripcion" to producto.descripcion,
                        "precio" to producto.precio,
                        "imageUrl" to imageUrl
                    ),
                    onSuccess = { Toast.makeText(this, "Descuento guardado exitosamente", Toast.LENGTH_SHORT).show() },
                    onFailure = { e -> Toast.makeText(this, "Error al guardar descuento: ${e.message}", Toast.LENGTH_LONG).show() }
                )
            },
            onFailure = { e -> Toast.makeText(this, "Error al subir imagen: ${e.message}", Toast.LENGTH_LONG).show() }
        )
    }
}