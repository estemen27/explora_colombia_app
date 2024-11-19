package com.estebanbe.exploracolombiaapp.modelo

import com.google.firebase.Timestamp

data class Recompensa(
    val rendencionId: String,
    val userId: String,
    val productoId: String,
    val nombreProducto: String,
    val puntosUsados: Int,
    val fechaRedencion: Timestamp?
){
    constructor(): this("", "", "" ,"",0, Timestamp.now(), )
}