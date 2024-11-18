package com.estebanbe.exploracolombiaapp.controller

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.estebanbe.exploracolombiaapp.R

class Detail : AppCompatActivity (){

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val editDetail1: FloatingActionButton = findViewById(R.id.editDetail1)
        val editDetail2: FloatingActionButton = findViewById(R.id.editDetail2)

        editDetail1.setOnClickListener {
            val intent = Intent(this, EditHorario::class.java)
            startActivity(intent)
        }

        editDetail2.setOnClickListener {
            val intent = Intent(this, EditContact::class.java)
            startActivity(intent)
        }
    }
}