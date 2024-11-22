package com.estebanbe.exploracolombiaapp.controller.perfil

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.estebanbe.exploracolombiaapp.R
import com.estebanbe.exploracolombiaapp.manager.UserManager
import com.estebanbe.exploracolombiaapp.service.AuthService
import com.estebanbe.exploracolombiaapp.service.FirebaseService

class PerfilFragment : Fragment(R.layout.fragment_perfil) {

    private lateinit var tvUsername: TextView
    private lateinit var tvEmail: TextView
    private lateinit var profilePic: ImageView
    private lateinit var btnUpdate: Button
    private var imageUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvUsername = view.findViewById(R.id.tv_username)
        tvEmail = view.findViewById(R.id.tv_email)
        profilePic = view.findViewById(R.id.profile_pic)
        btnUpdate = view.findViewById(R.id.btn_update)

        // Cargar datos
        loadData()
        loadFromStorage()

        // Configurar clics en botones
        btnUpdate.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_container, UpdateProfileFragment()) // Usa el ID del contenedor de fragments
                .addToBackStack(null) // Agrega a la pila de retroceso para volver al perfil
                .commit()
        }

        setupOptionClickListeners(view)

    }

    private fun loadData() {
        val userId = AuthService().currentUserUid()
        UserManager.getUsuario(userId) { usuario ->
            tvUsername.text = usuario.name
            tvEmail.text = usuario.email
        }
    }

    private fun loadFromStorage() {
        val userId = AuthService().currentUserUid()
        val firebaseService = FirebaseService()
        val path = "images/$userId.jpg"

        firebaseService.getFromStorage(
            path = path,
            onSuccess = { url ->
                // Usa la URL con Glide u otro propósito
                Glide.with(this).load(url).into(profilePic)
            },
            onFailure = { exception ->
                // Maneja el error
                Toast.makeText(requireContext(), "Error al obtener la imagen: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun navigateToOption(option: String) {
        val fragment = OptionCurrencyFragment().apply {
            arguments = Bundle().apply {
                putString("option", option)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.frame_container, fragment) // Usa el ID del contenedor de fragments
            .addToBackStack(null) // Agrega a la pila de retroceso para volver al perfil
            .commit()
    }


    private fun setupOptionClickListeners(view: View) {
        Log.d("PerfilFragment", "setupOptionClickListeners called")
        view.findViewById<TextView>(R.id.option_currency).setOnClickListener {
            Log.d("PerfilFragment", "Currency clicked")
            navigateToOption("1")
        }
        view.findViewById<TextView>(R.id.option_language).setOnClickListener {
            navigateToOption("2")
        }
        view.findViewById<TextView>(R.id.option_appearance).setOnClickListener {
            navigateToOption("3")
        }
        view.findViewById<TextView>(R.id.option_alerts).setOnClickListener {
            navigateToOption("4")
        }
        view.findViewById<TextView>(R.id.option_privacy).setOnClickListener {
            navigateToOption("5")
        }
        view.findViewById<TextView>(R.id.option_contactus).setOnClickListener {
            navigateToOption("6")
        }
        view.findViewById<TextView>(R.id.option_rate).setOnClickListener {
            navigateToOption("7")
        }
    }


}
