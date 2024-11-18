package com.estebanbe.exploracolombiaapp.controller

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.estebanbe.exploracolombiaapp.R

class Review : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        setupComment(R.id.ratingBar, R.id.text1, R.id.send)
        setupComment(R.id.ratingBar1, R.id.text2, R.id.send1)
        setupComment(R.id.ratingBar2, R.id.text3, R.id.send2)
    }

    private fun setupComment(ratingBarId: Int, editTextId: Int, buttonId: Int) {
        val ratingBar = findViewById<RatingBar>(ratingBarId)
        val editText = findViewById<EditText>(editTextId)
        val sendButton = findViewById<Button>(buttonId)

        sendButton.setOnClickListener {
            val rating = ratingBar.rating
            val comment = editText.text.toString().trim()

            if (comment.isNotEmpty()) saveCommentToFirebase(rating, comment)
            else Toast.makeText(this, "Por favor, ingresa un comentario", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveCommentToFirebase(rating: Float, comment: String) {
        val commentId = database.child("comments").push().key

        if (commentId != null) {
            val commentData = mapOf("rating" to rating, "comment" to comment)
            database.child("comments").child(commentId).setValue(commentData)
                .addOnSuccessListener { Toast.makeText(this, "Comentario guardado", Toast.LENGTH_SHORT).show() }
                .addOnFailureListener { Toast.makeText(this, "Error al guardar el comentario", Toast.LENGTH_SHORT).show() }
        }
    }
}