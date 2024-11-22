package com.estebanbe.exploracolombiaapp.fragments.TabFragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.estebanbe.exploracolombiaapp.R
import com.estebanbe.exploracolombiaapp.controller.artesanias.VisualizerArtesanias

class HandMadeFragment : Fragment() {

    private lateinit var imagenPrueba: ImageView
    private lateinit var btnUbicacion: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el diseño del fragmento
        val view = inflater.inflate(R.layout.fragment_hand_made, container, false)

        // Inicializar vistas
        imagenPrueba = view.findViewById(R.id.main_img_1)
        btnUbicacion = view.findViewById(R.id.btn_1)

        // Configurar listeners
        imagenPrueba.setOnClickListener {
            // Abre la actividad VisualizerArtesanias
            val intent = Intent(requireContext(), VisualizerArtesanias::class.java)
            startActivity(intent)
        }

        btnUbicacion.setOnClickListener {
            // Aquí puedes implementar la funcionalidad de geolocalización o cualquier otra acción
            // Por ejemplo:
            // val geoUri = Uri.parse("geo:0,0?q=Zipaquira, Colombia")
            // val intent = Intent(Intent.ACTION_VIEW, geoUri)
            // startActivity(intent)
        }

        return view
    }
}
