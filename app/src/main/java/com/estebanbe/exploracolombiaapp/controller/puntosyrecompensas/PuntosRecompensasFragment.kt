package com.estebanbe.exploracolombiaapp.controller.puntosyrecompensas


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.estebanbe.exploracolombiaapp.manager.UserManager
import com.estebanbe.exploracolombiaapp.service.AuthService
import com.google.firebase.database.*
import com.estebanbe.exploracolombiaapp.R

class PuntosRecompensasFragment : Fragment(R.layout.activity_puntos_recompensas) {

    private lateinit var recyclerViewProductos: RecyclerView
    private lateinit var database: DatabaseReference
    private lateinit var puntosUsuario: TextView
    private lateinit var btnGanarMasPuntos: Button
    private lateinit var adapter: ProductoAdapter
    private var currentToast: Toast? = null
    private lateinit var uid: String
    private lateinit var userManager: UserManager
    private lateinit var productosManager: ProductosManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //ProductoActivity().agregarProductos() -- para pruebas

        val authService = AuthService(requireContext())
        userManager = UserManager
        productosManager = ProductosManager
        puntosUsuario = view.findViewById(R.id.puntosUsuario)
        recyclerViewProductos = view.findViewById(R.id.recyclerViewProductos)
        recyclerViewProductos.layoutManager = GridLayoutManager(requireContext(), 2)


        database = FirebaseDatabase.getInstance().reference.child("productos")
        btnGanarMasPuntos = view.findViewById(R.id.btnGanarMasPuntos)
        println("PuntosRecompensasFragment Botón encontrado: $btnGanarMasPuntos")

        adapter = ProductoAdapter(emptyList()) {
            actualizaPuntos()
        }
        recyclerViewProductos.adapter = adapter

        uid = authService.currentUserUid()

        btnGanarMasPuntos.setOnClickListener {
            val dialog = RegistrarVisitaDialogFragment()
            dialog.show(parentFragmentManager, "RegistrarVisitaDialogFragment")
        }

        cargarProductos()
        recyclerViewProductos.visibility = View.VISIBLE
        actualizaPuntos()
    }



    private fun actualizaPuntos(){
        userManager.getPuntos(uid) { saldoPuntos ->
            puntosUsuario.text = saldoPuntos.toString()
        }
    }

    private fun mostrarMsg(mensaje: String){
        currentToast?.cancel()
        currentToast = Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT)
        currentToast?.show()
    }

    private fun cargarProductos() {
        mostrarIndicadorDeCarga(true) // Mostrar indicador

        productosManager.buscarProductosPorCampo("estado", true) { productos ->
            for (producto in productos){
                println("Encontrado: ${producto.productoId}")
            }
            adapter = ProductoAdapter(productos) {
                actualizaPuntos()
            }
            recyclerViewProductos.adapter = adapter
            recyclerViewProductos.visibility = View.VISIBLE
            mostrarIndicadorDeCarga(false) // Ocultar indicador
        }
    }

    private fun mostrarIndicadorDeCarga(mostrar: Boolean) {
        // Muestra u oculta un ProgressBar o similar
        val progressBar = view?.findViewById<View>(R.id.progressBar)
        progressBar?.visibility = if (mostrar) View.VISIBLE else View.GONE
    }



}
