package com.estebanbe.exploracolombiaapp.controller.puntosyrecompensas

import com.estebanbe.exploracolombiaapp.model.Producto
import com.estebanbe.exploracolombiaapp.service.RealtimeService

object ProductosManager {
    private const val PRODUCTOS_PATH = "productos"
    private val realtimeService = RealtimeService()

    fun obtenerProducto(id: String, callback: (Producto?) -> Unit) {
        realtimeService.getFromRealtimeDatabase(PRODUCTOS_PATH, id,
            onSuccess = { data ->
                val producto = data?.let { mapToProducto(it) }
                callback(producto)
            },
            onFailure = { callback(null) }
        )
    }

    fun setProducto(producto: Producto, callback: (Boolean) -> Unit) {
        producto.productoId = realtimeService.generatePushKey("productos")
        val data = productoToMap(producto)
        realtimeService.saveToRealtimeDatabase(PRODUCTOS_PATH, producto.productoId, data,
            onSuccess = { callback(true) },
            onFailure = { callback(false) }
        )
    }

    fun buscarProductosPorCampo(campo: String, valor: Any, callback: (List<Producto>) -> Unit) {
        realtimeService.buscarProductoPorCampo(campo, valor) { productos ->
            callback(productos)
        }
    }

    // Función auxiliar para convertir un Producto a un Map<String, Any>
    private fun productoToMap(producto: Producto): Map<String, Any> {
        return mapOf(
            "productoId" to producto.productoId,
            "comercioId" to producto.comercioId,
            "nombre" to producto.nombre,
            "descripcion" to producto.descripcion,
            "precio" to producto.precio,
            "imageUrl" to (producto.imageUrl ?: ""),
            "estado" to producto.estado
        )
    }

    // Función auxiliar para convertir un Map<String, Any> a un Producto
    private fun mapToProducto(data: Map<String, Any>): Producto? {
        return try {
            Producto(
                productoId = data["productoId"] as String,
                comercioId = data["comercioId"] as String,
                nombre = data["nombre"] as String,
                descripcion = data["descripcion"] as String,
                precio = (data["precio"] as Number).toInt(),
                imageUrl = data["imageUrl"] as? String,
                estado = data["estado"] as Boolean
            )
        } catch (e: Exception) {
            null
        }
    }

    // Función auxiliar para verificar si un producto cumple con un campo y valor específicos
    private fun Producto.matchesField(campo: String, valor: Any): Boolean {
        return when (campo) {
            "estado" -> this.estado == valor as? Boolean
            "precio" -> this.precio == (valor as? Number)?.toInt()
            "nombre" -> this.nombre == valor as? String
            else -> false
        }
    }
}
