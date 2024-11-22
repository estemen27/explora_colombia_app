package com.estebanbe.exploracolombiaapp.controller.perfil

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.estebanbe.exploracolombiaapp.R

class OptionCurrencyFragment : Fragment(R.layout.fragment_option_currency){

    private val currencies = listOf("Peso colombiano", "Dólar EEUU", "Euro", "Peso mexicano", "Libra esterlina", "Dólar canadiense", "Franco suizo", "Quetzal")
    private val languages = listOf("Español Col", "Español Esp", "Español Mex", "Inglés EEUU", "Inglés UK", "Francés", "Portugues", "Mandarín", "Hindi", "Árabe", "Ruso", "Bengalí")
    private val alerts = listOf("Activar", "Desactivar", "Sonido y vibración", "Prioridad de notificación", "Modo no molestar", "Notificaciones en pantalla de bloqueo")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_option_currency, container, false)

        // Obtiene el argumento pasado al fragmento
        val option = arguments?.getString("option")

        when (option) {
            "1" -> showCurrencyDialog()
            "2" -> showLanguageDialog()
            "3" -> showAppearanceDialog()
            "4" -> showAlertDialog()
            "5" -> showTermsDialog()
            "6" -> showPrivacyDialog()
            "7" -> showContactDialog()
            else -> {
                Toast.makeText(requireContext(), "Opción inválida", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun showCurrencyDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_currency_option)

        val currencyListView = dialog.findViewById<ListView>(R.id.currency_list)
        val closeButton = dialog.findViewById<ImageView>(R.id.close_button)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, currencies)
        currencyListView.adapter = adapter

        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        currencyListView.setOnItemClickListener { _, _, position, _ ->
            val selectedCurrency = currencies[position]
            Toast.makeText(requireContext(), "Moneda seleccionada: $selectedCurrency", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showLanguageDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_language_option)

        val languageListView = dialog.findViewById<ListView>(R.id.language_list)
        val closeButton = dialog.findViewById<ImageView>(R.id.close_button)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, languages)
        languageListView.adapter = adapter

        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        languageListView.setOnItemClickListener { _, _, position, _ ->
            val selectedLanguage = languages[position]
            Toast.makeText(requireContext(), "Idioma seleccionado: $selectedLanguage", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showAppearanceDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_appearance_option)

        val closeButton = dialog.findViewById<ImageView>(R.id.close_button)
        val lightButton = dialog.findViewById<Button>(R.id.light_button)
        val darkButton = dialog.findViewById<Button>(R.id.dark_button)

        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        lightButton.setOnClickListener {
            Toast.makeText(requireContext(), "Tema Light seleccionado", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        darkButton.setOnClickListener {
            Toast.makeText(requireContext(), "Tema Dark seleccionado", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showAlertDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_alerts_option)

        val alertListView = dialog.findViewById<ListView>(R.id.alert_list)
        val closeButton = dialog.findViewById<ImageView>(R.id.close_button)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, alerts)
        alertListView.adapter = adapter

        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        alertListView.setOnItemClickListener { _, _, position, _ ->
            val selectedAlert = alerts[position]
            Toast.makeText(requireContext(), "Alerta seleccionada: $selectedAlert", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun showTermsDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_terms_option)

        val closeButton = dialog.findViewById<ImageView>(R.id.close_button)
        val acceptButton = dialog.findViewById<Button>(R.id.accept_button)
        val termsTextView = dialog.findViewById<TextView>(R.id.terms_text)

        // Configurar el botón de cerrar
        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        // Configurar el botón de aceptar
        acceptButton.setOnClickListener {
            Toast.makeText(requireContext(), "Términos aceptados", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showPrivacyDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_privacy_option)

        val closeButton = dialog.findViewById<ImageView>(R.id.close_button)
        val acceptButton = dialog.findViewById<Button>(R.id.accept_button)
        val privacyTextView = dialog.findViewById<TextView>(R.id.privacy_text)

        // Configurar el botón de cerrar
        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        // Configurar el botón de aceptar
        acceptButton.setOnClickListener {
            Toast.makeText(requireContext(), "Políticas aceptadas", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showContactDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_contactus_option)

        val closeButton = dialog.findViewById<ImageView>(R.id.close_button)
        val emailButton = dialog.findViewById<Button>(R.id.email_button)
        val phoneButton = dialog.findViewById<Button>(R.id.phone_button)

        // Configurar el botón de cerrar
        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        // Configurar el botón de correo electrónico
        emailButton.setOnClickListener {
            //Enviar al correo
        }

        // Configurar el botón de teléfono
        phoneButton.setOnClickListener {
            // Marcar
        }

        dialog.show()
    }

}
