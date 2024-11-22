package com.joelalba.exploracolombia2.TabFragments.GastroFragments.MenuFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.estebanbe.exploracolombiaapp.R
import com.estebanbe.exploracolombiaapp.fragments.TabFragments.GastroFragments.Entities.Commerce

class MenuFragment : Fragment() {

    private var restaurantId: String? = null
    private lateinit var databaseReference: DatabaseReference
    private lateinit var headerImage: ImageView
    private lateinit var restaurantName: TextView
    private lateinit var startersRecyclerView: RecyclerView
    private lateinit var mainCoursesRecyclerView: RecyclerView
    private lateinit var drinksRecyclerView: RecyclerView
    private lateinit var dessertsRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu, container, false)

        arguments?.let {
            restaurantId = it.getString("restaurant_id")
        }

        // Inicialización de vistas
        headerImage = view.findViewById(R.id.headerImage)
        restaurantName = view.findViewById(R.id.restaurantName)
        startersRecyclerView = view.findViewById(R.id.startersRecyclerView)
        mainCoursesRecyclerView = view.findViewById(R.id.mainCoursesRecyclerView)
        drinksRecyclerView = view.findViewById(R.id.drinksRecyclerView)
        dessertsRecyclerView = view.findViewById(R.id.dessertsRecyclerView)

        restaurantId?.let {
            loadRestaurantData(it)
        } ?: run {
            Toast.makeText(requireContext(), "ID del restaurante no disponible", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun loadRestaurantData(restaurantId: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Commerce").child(restaurantId)

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val restaurant = snapshot.getValue(Commerce::class.java)

                    restaurant?.let {
                        restaurantName.text = it.name
                        // Aquí puedes cargar la imagen del header si tienes una URL en el objeto Commerce
                        // Por ejemplo, usando Glide o Picasso:
                        // Glide.with(requireContext()).load(it.imageUrl).into(headerImage)
                    }
                } else {
                    Toast.makeText(requireContext(), "Restaurante no encontrado", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error al cargar datos: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}