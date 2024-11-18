package com.estebanbe.exploracolombiaapp.controller

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.estebanbe.exploracolombiaapp.R

class Gallery : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        val editGallery1: FloatingActionButton = findViewById(R.id.editGallery1)
        val editGallery2: FloatingActionButton = findViewById(R.id.editGallery2)
        val addGallery: ExtendedFloatingActionButton = findViewById(R.id.addGallery)

        editGallery1.setOnClickListener { navigateTo(EditGallery::class.java) }
        editGallery2.setOnClickListener { navigateTo(EditGallery::class.java) }
        addGallery.setOnClickListener { navigateTo(EditGallery::class.java) }
    }

    private fun navigateTo(targetActivity: Class<*>) {
        startActivity(Intent(this, targetActivity))
    }
}