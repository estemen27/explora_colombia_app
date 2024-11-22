package com.estebanbe.exploracolombiaapp.controller.artesanias

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.estebanbe.exploracolombiaapp.R

class VisualizerArtesanias : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualizer_artesanias)
        supportActionBar?.hide()

    }
}