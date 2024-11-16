package com.estebanbe.exploracolombiaapp.model

data class User(
    val name: String = "",
    val email: String = "",
    val uid: String = "",
    val type: String = "", // "turista" o "propietario"
    val idComercio: String? = null,
    val status: String? = null // Para propietarios: "pending", "approved", "rejected"
)
