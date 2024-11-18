package com.estebanbe.exploracolombiaapp.controller

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.estebanbe.exploracolombiaapp.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Offer : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offer)

        val editOffer1: FloatingActionButton = findViewById(R.id.editOffer1)
        val editOffer2: FloatingActionButton = findViewById(R.id.editOffer2)
        val addOffer: MaterialButton = findViewById(R.id.addOffer)

        editOffer1.setOnClickListener { navigateTo(PlateEdit::class.java) }
        editOffer2.setOnClickListener { navigateTo(PlateEdit::class.java) }
        addOffer.setOnClickListener { navigateTo(EditDescuento::class.java) }
    }

    private fun navigateTo(targetActivity: Class<*>) {
        startActivity(Intent(this, targetActivity))
    }
}
