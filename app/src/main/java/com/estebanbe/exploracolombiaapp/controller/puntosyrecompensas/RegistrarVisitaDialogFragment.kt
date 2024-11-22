package com.estebanbe.exploracolombiaapp.controller.puntosyrecompensas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.estebanbe.exploracolombiaapp.R
import com.estebanbe.exploracolombiaapp.modelo.Visita

class RegistrarVisitaDialogFragment : DialogFragment() {

    private lateinit var spinnerRestaurantes: Spinner
    private lateinit var btnRegistrar: Button
    private val visitasManager = VisitasManager()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.dialog_registrar_visita, container, false)

        spinnerRestaurantes = view.findViewById(R.id.spinnerRestaurantes)
        btnRegistrar = view.findViewById(R.id.btnRegistrar)

        // Cargar lista de restaurantes (simulada aquí)
        val restaurantes = listOf("Restaurante A", "Restaurante B", "Restaurante C")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, restaurantes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRestaurantes.adapter = adapter

        btnRegistrar.setOnClickListener {
            val restauranteSeleccionado = spinnerRestaurantes.selectedItem?.toString()

            if (!restauranteSeleccionado.isNullOrBlank()) {
                visitasManager.registrarVisita(restauranteSeleccionado, 100)
                dismiss() // Cerrar el diálogo después de registrar la visita
            } else {
                Toast.makeText(requireContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.setCancelable(true) // Permitir cerrar con botón atrás o fuera del diálogo
    }
}
