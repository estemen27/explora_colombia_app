package com.estebanbe.exploracolombiaapp.controller

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.estebanbe.exploracolombiaapp.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.UUID

class EditMenu : AppCompatActivity() {

    private lateinit var photoPlate: ImageView
    private var imageUri: Uri? = null
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val REQUEST_SELECT_IMAGE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_menu)

        photoPlate = findViewById(R.id.photoPlate)
        val nombrePlato = findViewById<EditText>(R.id.nombrePlato)
        val descripcionMenu = findViewById<EditText>(R.id.desMenu)
        val precioMenu = findViewById<EditText>(R.id.precioMenu)
        val saveButton = findViewById<Button>(R.id.saveBtn)
        val cancelButton = findViewById<Button>(R.id.cancel)
        val imgBtn = findViewById<Button>(R.id.imgBtnMenu)

        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        imgBtn.setOnClickListener {
            showAddImageDialog()
        }

        cancelButton.setOnClickListener {
            finish()
        }

        saveButton.setOnClickListener {
            val nombre = nombrePlato.text.toString().trim()
            val descripcion = descripcionMenu.text.toString().trim()
            val precio = precioMenu.text.toString().trim()

            if (nombre.isEmpty() || descripcion.isEmpty() || precio.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (imageUri != null) {
                saveDataWithImage(nombre, descripcion, precio)
            } else {
                saveDataWithoutImage(nombre, descripcion, precio)
            }
        }
    }

    private fun saveDataWithImage(nombre: String, descripcion: String, precio: String) {
        val imageRef = storage.reference.child("platos/${UUID.randomUUID()}.jpg")
        imageRef.putFile(imageUri!!)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    saveToFirestore(nombre, descripcion, precio, uri.toString())
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al subir la imagen", Toast.LENGTH_LONG).show()
            }
    }

    private fun saveDataWithoutImage(nombre: String, descripcion: String, precio: String) {
        saveToFirestore(nombre, descripcion, precio, null)
    }

    private fun saveToFirestore(nombre: String, descripcion: String, precio: String, imageUrl: String?) {
        val plato = hashMapOf(
            "nombre" to nombre,
            "descripcion" to descripcion,
            "precio" to precio,
            "imageUrl" to (imageUrl ?: "")
        )

        firestore.collection("platos").add(plato)
            .addOnSuccessListener {
                Toast.makeText(this, "Producto guardado exitosamente", Toast.LENGTH_LONG).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al guardar el producto", Toast.LENGTH_LONG).show()
            }
    }

    private fun showAddImageDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.add_img_menu)

        val takePhotoButton: ImageButton = dialog.findViewById(R.id.photo_btn)
        val selectFromGalleryButton: ImageButton = dialog.findViewById(R.id.gallery_btn)
        val cancelDialogButton: Button = dialog.findViewById(R.id.cancelButton)

        takePhotoButton.setOnClickListener {
            openCamera()
            dialog.dismiss()
        }

        selectFromGalleryButton.setOnClickListener {
            openGallery()
            dialog.dismiss()
        }

        cancelDialogButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
    }

    private fun openGallery() {
        val selectImageIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(selectImageIntent, REQUEST_SELECT_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val bitmap = data?.extras?.get("data") as Bitmap
                    imageUri = getImageUriFromBitmap(bitmap)
                    photoPlate.setImageBitmap(bitmap)
                }
                REQUEST_SELECT_IMAGE -> {
                    imageUri = data?.data
                    photoPlate.setImageURI(imageUri)
                }
            }
        }
    }

    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "TempImage", null)
        return Uri.parse(path)
    }
}
