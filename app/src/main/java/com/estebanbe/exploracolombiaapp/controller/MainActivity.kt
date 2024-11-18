package com.estebanbe.exploracolombiaapp.controller

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.estebanbe.exploracolombiaapp.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val menuCard = findViewById<CardView>(R.id.menu)
        val reviewCard = findViewById<CardView>(R.id.reseña)
        val detailsCard = findViewById<CardView>(R.id.details)
        val offersCard = findViewById<CardView>(R.id.offers)
        val galleryCard = findViewById<CardView>(R.id.gallery)

        menuCard.setOnClickListener { navigateTo(Menu::class.java) }
        reviewCard.setOnClickListener { navigateTo(Review::class.java) }
        detailsCard.setOnClickListener { navigateTo(Detail::class.java) }
        offersCard.setOnClickListener { navigateTo(Offer::class.java) }
        galleryCard.setOnClickListener { navigateTo(Gallery::class.java) }
    }

    private fun navigateTo(targetActivity: Class<*>) {
        startActivity(Intent(this, targetActivity))
    }
}