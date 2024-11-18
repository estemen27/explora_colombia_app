package com.estebanbe.exploracolombiaapp.model

data class Producto(
    val nombre: String,
    val descripcion: String,
    val precio: String,
    val imageUrl: String? = null
)