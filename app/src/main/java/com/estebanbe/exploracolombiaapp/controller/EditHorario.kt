package com.estebanbe.exploracolombiaapp.controller

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.estebanbe.exploracolombiaapp.R
import com.estebanbe.exploracolombiaapp.model.Horario
import com.estebanbe.exploracolombiaapp.service.FirebaseService
import java.util.*

class EditHorario : AppCompatActivity() {

    private val firebaseService = FirebaseService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_horario)

        setupDias()
        setupButtons()
    }

    private fun setupDias() {
        val dias = listOf("lunes", "martes", "miércoles", "jueves", "viernes", "sábado", "domingo", "festivo")
        val contenedorDias = findViewById<LinearLayout>(R.id.contenedorDias)

        dias.forEachIndexed { index, dia ->
            val diaView = contenedorDias.getChildAt(index)
            diaView.findViewById<TextView>(R.id.nombreDia).text = dia.replaceFirstChar { it.uppercase() }
            diaView.findViewById<Button>(R.id.btnApertura).setOnClickListener { showTimePickerDialog(it as Button) }
            diaView.findViewById<Button>(R.id.btnCierre).setOnClickListener { showTimePickerDialog(it as Button) }
        }
    }

    private fun setupButtons() {
        findViewById<Button>(R.id.btnGuardar).setOnClickListener { saveHorarioToFirebase() }
        findViewById<Button>(R.id.btnCancelar).setOnClickListener { finish() }
    }

    private fun showTimePickerDialog(button: Button) {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            this,
            { _, hour, minute -> button.text = String.format("%02d:%02d", hour, minute) },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun saveHorarioToFirebase() {
        val contenedorDias = findViewById<LinearLayout>(R.id.contenedorDias)
        val horarioList = mutableListOf<Horario>()

        for (i in 0 until contenedorDias.childCount) {
            val diaView = contenedorDias.getChildAt(i)
            val nombreDia = diaView.findViewById<TextView>(R.id.nombreDia).text.toString().lowercase()
            val horaApertura = diaView.findViewById<Button>(R.id.btnApertura).text.toString()
            val horaCierre = diaView.findViewById<Button>(R.id.btnCierre).text.toString()

            horarioList.add(Horario(nombreDia, horaApertura, horaCierre))
        }

        val horarioMap = horarioList.associate { it.dia to mapOf("apertura" to it.apertura, "cierre" to it.cierre) }

        firebaseService.saveToFirestore(
            "horarios",
            "horario_general",
            horarioMap,
            onSuccess = { Toast.makeText(this, "Horarios guardados exitosamente", Toast.LENGTH_SHORT).show() },
            onFailure = { e -> Toast.makeText(this, "Error al guardar horarios: ${e.message}", Toast.LENGTH_LONG).show() }
        )
    }
}