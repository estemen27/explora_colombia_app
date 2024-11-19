package com.estebanbe.exploracolombiaapp.modelo

data class Comercio(
    val comercioId: String,
    val nombre: String,
    val propietarioId: String,
    val ubicacion: String,
    val correo: String
){
    constructor(): this("", "", "" ,"", "" )
}
