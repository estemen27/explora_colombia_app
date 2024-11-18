package com.estebanbe.exploracolombiaapp.controller

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.estebanbe.exploracolombiaapp.R
import java.util.*

class PlateEdit : AppCompatActivity() {

    private lateinit var photoPlate: ImageView
    private var imageUri: Uri? = null
    private val database = FirebaseDatabase.getInstance().reference.child("platos")
    private val storage = FirebaseStorage.getInstance()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plate_edit)

        photoPlate = findViewById(R.id.photoPlate)
        val nombrePlato = findViewById<EditText>(R.id.nombre_pm)
        val descripcionMenu = findViewById<EditText>(R.id.des_menu)
        val precioMenu = findViewById<EditText>(R.id.precio_menu)
        val saveButton = findViewById<Button>(R.id.saveButton)
        val cancellButton = findViewById<Button>(R.id.cancellButton)

        cancellButton.setOnClickListener { finish() }

        saveButton.setOnClickListener {
            val nombre = nombrePlato.text.toString()
            val descripcion = descripcionMenu.text.toString()
            val precio = precioMenu.text.toString()

            if (nombre.isNotBlank() && descripcion.isNotBlank() && precio.isNotBlank()) {
                if (imageUri != null) saveDataWithImage(nombre, descripcion, precio)
                else saveDataWithoutImage(nombre, descripcion, precio)
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun saveDataWithImage(nombre: String, descripcion: String, precio: String) {
        val imageRef = storage.reference.child("images/${UUID.randomUUID()}")
        imageRef.putFile(imageUri!!)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val plato = mapOf(
                        "nombre" to nombre,
                        "descripcion" to descripcion,
                        "precio" to precio,
                        "imageUrl" to uri.toString()
                    )
                    database.push().setValue(plato)
                        .addOnSuccessListener { Toast.makeText(this, "Producto guardado exitosamente", Toast.LENGTH_LONG).show() }
                        .addOnFailureListener { Toast.makeText(this, "Error al guardar el producto", Toast.LENGTH_LONG).show() }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al subir la imagen", Toast.LENGTH_LONG).show()
            }
    }

    private fun saveDataWithoutImage(nombre: String, descripcion: String, precio: String) {
        val plato = mapOf("nombre" to nombre, "descripcion" to descripcion, "precio" to precio)
        database.push().setValue(plato)
            .addOnSuccessListener { Toast.makeText(this, "Producto guardado exitosamente", Toast.LENGTH_LONG).show() }
            .addOnFailureListener { Toast.makeText(this, "Error al guardar el producto", Toast.LENGTH_LONG).show() }
    }
}