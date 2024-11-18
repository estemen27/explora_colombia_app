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
import com.google.firebase.firestore.FirebaseFirestore
import com.estebanbe.exploracolombiaapp.R
import java.io.ByteArrayOutputStream

class EditGallery : AppCompatActivity() {

    private lateinit var photoGallery: ImageView
    private var imageUri: Uri? = null
    private lateinit var firestore: FirebaseFirestore

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_gallery)

        val ingresarNomImg = findViewById<EditText>(R.id.ingresarNomImg)
        val imgDes = findViewById<EditText>(R.id.imgDes)
        val guardarBtn = findViewById<Button>(R.id.guardarBtn)

        firestore = FirebaseFirestore.getInstance()
        photoGallery = findViewById(R.id.photoGallery)

        guardarBtn.setOnClickListener {
            val nombre = ingresarNomImg.text.toString()
            val descripcion = imgDes.text.toString()

            if (nombre.isNotBlank() && descripcion.isNotBlank()) {
                saveDataToFirestore(nombre, descripcion)
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun saveDataToFirestore(nombre: String, descripcion: String) {
        val data = mapOf("nombre" to nombre, "descripcion" to descripcion)
        firestore.collection("galeria").add(data)
            .addOnSuccessListener { Toast.makeText(this, "Información Guardada", Toast.LENGTH_LONG).show() }
            .addOnFailureListener { Toast.makeText(this, "Error al guardar en Firestore", Toast.LENGTH_LONG).show() }
    }
}