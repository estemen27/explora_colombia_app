package com.estebanbe.exploracolombiaapp.TabFragments.GastroFragments.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.estebanbe.exploracolombiaapp.R
import com.estebanbe.exploracolombiaapp.TabFragments.GastroFragments.Entities.Restaurant

class RestaurantAdapter(private val restaurantList: ArrayList<Restaurant>) : RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    private var filteredList = restaurantList.toMutableList()
    private val activeFilters = mutableListOf<String>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RestaurantViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.restaurant_item, parent, false)
        return RestaurantViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val currentItem = restaurantList[position]

        holder.name.text = currentItem.name
        holder.description.text = currentItem.description
        holder.price.text = "$${currentItem.minPrice} - $${currentItem.maxPrice}"
        holder.time.text = currentItem.time

        loadImageFromFirestore(currentItem.uid, holder.image)
    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

    class RestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.imgRestaurant)
        val name: TextView = itemView.findViewById(R.id.tvRestaurantName)
        val description: TextView = itemView.findViewById(R.id.tvRestaurantDescription)
        val price: TextView = itemView.findViewById(R.id.tvPrice)
        val time : TextView = itemView.findViewById(R.id.tvTime)
    }

    private fun loadImageFromFirestore(uid: String, imageView: ImageView) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("Restaurants").child(uid)

        databaseRef.get().addOnSuccessListener { snapshot ->
            val imageUrl = snapshot.child("imageUrl").value.toString()
            if (imageUrl.isNotEmpty()) {
                Glide.with(imageView.context).load(imageUrl).into(imageView)
            } else {
                Log.e("LoadImage", "La URL de la imagen está vacía.")
            }
        }.addOnFailureListener {
            Log.e("LoadImage", "Error al obtener la URL de la imagen.", it)
        }
    }

    fun addFilter(newFilter: String) {
        activeFilters.add(newFilter)
        applyFilters()
    }

    fun removeFilter(filterToRemove: String) {
        activeFilters.remove(filterToRemove)
        applyFilters()
    }

    private fun applyFilters() {
        filteredList = restaurantList.filter { restaurant ->
            activeFilters.all { filter -> restaurant.matchesFilter(filter) }
        }.toMutableList()
        notifyDataSetChanged()
    }
}
