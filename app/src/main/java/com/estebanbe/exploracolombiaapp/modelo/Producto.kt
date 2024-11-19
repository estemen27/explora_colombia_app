package com.estebanbe.exploracolombiaapp.model

data class Producto(
    var productoId: String = "", // Identificador único del producto
    val comercioId: String = "", // Identificador del comercio asociado
    val nombre: String = "", // Nombre del producto
    val descripcion: String = "", // Descripción del producto
    val precio: Int = 0, // Precio del producto (en centavos o unidad base)
    val imageUrl: String? = null, // URL opcional de la imagen del producto
    val estado: Boolean = true // Estado del producto (activo/inactivo)
)
