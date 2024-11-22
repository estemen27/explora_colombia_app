package com.estebanbe.exploracolombiaapp.controller.perfil

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.estebanbe.exploracolombiaapp.R
import com.estebanbe.exploracolombiaapp.manager.UserManager
import com.estebanbe.exploracolombiaapp.service.AuthService
import com.estebanbe.exploracolombiaapp.service.FirebaseService
import com.estebanbe.exploracolombiaapp.service.RealtimeService
import com.github.dhaval2404.imagepicker.ImagePicker

class UpdateProfileFragment : Fragment(R.layout.fragment_update_profile) {

    private lateinit var et_username: EditText
    private lateinit var et_email: EditText
    private lateinit var profile_pic: ImageView
    private lateinit var btn_save: Button

    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout para este fragmento
        val view = inflater.inflate(R.layout.fragment_update_profile, container, false)

        // Asigna las vistas
        et_username = view.findViewById(R.id.et_username)
        et_email = view.findViewById(R.id.et_email)
        btn_save = view.findViewById(R.id.btn_save)
        profile_pic = view.findViewById(R.id.profile_pic)

        // Cargar los datos del usuario
        loadData()
        loadFromStorage()

        // Configurar el clic en la imagen para cambiarla
        profile_pic.setOnClickListener {
            ImagePicker.with(this)
                .crop()  // Permite el recorte de la imagen
                .start()
        }

        // Guardar la nueva información cuando se hace clic en el botón
        btn_save.setOnClickListener {
            updateData()
        }

        return view
    }

    // Este método se llama cuando el usuario selecciona una imagen
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == ImagePicker.REQUEST_CODE) {
            imageUri = data?.data
            profile_pic.setImageURI(imageUri) // Mostrar la imagen seleccionada
        }
    }

    // Método para actualizar los datos del perfil en la base de datos en tiempo real
    private fun updateData() {
        val newUsername = et_username.text.toString()
        val newEmail = et_email.text.toString()

        val userId = AuthService().currentUserUid()

        val updates = hashMapOf<String, Any>(
            "name" to newUsername,
            "email" to newEmail
        )

        // Actualizar en Realtime Database
        RealtimeService().updateRealtimeDatabase("users", userId, updates,
            onSuccess = {
                Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
            },
            onFailure = {
                Toast.makeText(requireContext(), "Failed to update profile", Toast.LENGTH_SHORT).show()
            }
        )

        // Subir la imagen seleccionada a Firebase
        saveImageToFirebase()
    }

    // Método para cargar los datos del usuario desde la base de datos
    private fun loadData() {
        val userId = AuthService().currentUserUid()
        UserManager.getUsuario(userId) { usuario ->
            et_username.setText(usuario.name)
            et_email.setText(usuario.email)
        }
    }

    // Método para cargar la imagen desde Firebase Storage
    private fun loadFromStorage() {
        val userId = AuthService().currentUserUid()
        val firebaseService = FirebaseService()
        val path = "images/$userId.jpg"

        firebaseService.getFromStorage(
            path = path,
            onSuccess = { url ->
                // Usa la URL con Glide u otro propósito
                Glide.with(this).load(url).into(profile_pic)
            },
            onFailure = { exception ->
                // Maneja el error
                Toast.makeText(requireContext(), "Error al obtener la imagen: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // Método para guardar la imagen en Firebase Storage
    private fun saveImageToFirebase() {
        val userId = AuthService().currentUserUid()
        val path = "images/$userId.jpg"

        imageUri?.let { uri ->
            val firebaseService = FirebaseService()
            val inputStream = requireContext().contentResolver.openInputStream(uri)

            if (inputStream != null) {
                val imageData = inputStream.readBytes()
                firebaseService.uploadToStorage(
                    path = path,
                    data = imageData,
                    onSuccess = { imageUrl ->
                        Toast.makeText(requireContext(), "Profile updated successfully: $imageUrl", Toast.LENGTH_SHORT).show()
                    },
                    onFailure = { exception ->
                        Toast.makeText(requireContext(), "Failed to update profile: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
                )
            } else {
                Toast.makeText(requireContext(), "Error reading image data.", Toast.LENGTH_SHORT).show()
            }
        } ?: Toast.makeText(requireContext(), "Select a picture.", Toast.LENGTH_SHORT).show()
    }
}
