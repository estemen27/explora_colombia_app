package com.estebanbe.exploracolombiaapp.controller

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.estebanbe.exploracolombiaapp.R
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Menu : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val edit1: FloatingActionButton = findViewById(R.id.edit1)
        val edit2: FloatingActionButton = findViewById(R.id.edit2)
        val edit3: FloatingActionButton = findViewById(R.id.edit3)
        val addMenu: ExtendedFloatingActionButton = findViewById(R.id.addMenu)

        edit1.setOnClickListener { navigateTo(PlateEdit::class.java) }
        edit2.setOnClickListener { navigateTo(PlateEdit::class.java) }
        edit3.setOnClickListener { navigateTo(PlateEdit::class.java) }
        addMenu.setOnClickListener { navigateTo(EditMenu::class.java) }
    }

    private fun navigateTo(targetActivity: Class<*>) {
        startActivity(Intent(this, targetActivity))
    }
}
