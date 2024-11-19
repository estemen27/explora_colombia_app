package com.estebanbe.exploracolombiaapp.controller.puntosyrecompensas

import com.estebanbe.exploracolombiaapp.model.Producto

class ProductoActivity {

    fun agregarProductos() {
        // Crear productos
        val producto1 = Producto(
            comercioId = "comercio1",
            nombre = "Producto 1",
            descripcion = "Descripción del producto 1",
            precio = 1000,
            imageUrl = "https://www.semana.com/resizer/v2/GBBYJH5YMZC6PEINHE3HZZH4TY.jpg?auth=f21d7fbf15c15316b80dd213fb2c635e4445db8e08133172d69a4956d7f417db&smart=true&quality=75&width=1280&fitfill=false",
            estado = true
        )

        val producto2 = Producto(
            comercioId = "comercio2",
            nombre = "Producto 2",
            descripcion = "Descripción del producto 2",
            precio = 2000,
            imageUrl = "https://www.mycolombianrecipes.com/wp-content/uploads/2013/11/Sancocho-trifasico.jpg",
            estado = true
        )

        val producto3 = Producto(
            comercioId = "comercio3",
            nombre = "Producto 3",
            descripcion = "Descripción del producto 3",
            precio = 1500,
            imageUrl = "https://www.laylita.com/recetas/wp-content/uploads/Limonada-receta.jpg",
            estado = true
        )

        // Llamar a la función para guardar los productos en la base de datos
        ProductosManager.setProducto(producto1) { success ->
            if (success) {
                println("Producto 1 agregado con éxito")
            } else {
                println("Error al agregar Producto 1")
            }
        }

        ProductosManager.setProducto(producto2) { success ->
            if (success) {
                println("Producto 2 agregado con éxito")
            } else {
                println("Error al agregar Producto 2")
            }
        }

        ProductosManager.setProducto(producto3) { success ->
            if (success) {
                println("Producto 3 agregado con éxito")
            } else {
                println("Error al agregar Producto 3")
            }
        }
    }
}
