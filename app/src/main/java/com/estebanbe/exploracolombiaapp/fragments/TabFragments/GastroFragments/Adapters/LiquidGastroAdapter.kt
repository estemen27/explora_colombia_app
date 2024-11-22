package com.estebanbe.exploracolombiaapp.fragments.TabFragments.GastroFragments.Adapters

import android.icu.text.SimpleDateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.estebanbe.exploracolombiaapp.R
import com.estebanbe.exploracolombiaapp.fragments.TabFragments.GastroFragments.Entities.Commerce
import java.text.ParseException
import java.util.Locale

class LiquidGastroAdapter(
    private var liquidGastroList: ArrayList<Commerce>,
    private val listener : RecyclerViewEvent) : RecyclerView.Adapter<LiquidGastroAdapter.LiquidGastroViewHolder>(){


    private var filteredList = liquidGastroList.toMutableList()
    private val activeFilters = mutableListOf<String>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LiquidGastroViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.restaurant_item, parent, false)
        return LiquidGastroViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LiquidGastroViewHolder, position: Int) {
        val currentItem = liquidGastroList[position]

        holder.name.text = currentItem.name
        holder.description.text = currentItem.description
        holder.price.text = "$${currentItem.minPrice} \n      - \n $${currentItem.maxPrice}"
        holder.time.text = currentItem.time

        val input = currentItem.time
        if (!input.isNullOrEmpty()) {
            val times = input.split("-")
            if (times.size == 2) {
                val startTime = times[0].trim()
                val endTime = times[1].trim()

                val dateFormat = SimpleDateFormat("h:mm a", Locale.getDefault()) // Cambiar a "h" para horas de un solo dígito

                try {
                    val start = dateFormat.parse(startTime)
                    val end = dateFormat.parse(endTime)

                    // Obtener la hora actual
                    val currentTime = java.util.Date() // Aquí usamos la hora actual sin formatear
                    val current = dateFormat.parse(dateFormat.format(currentTime)) // Formateamos la hora actual para hacer la comparación

                    // Comprobar si la hora actual está dentro del intervalo
                    if (current != null && start != null && end != null) {
                        if (current.after(start) && current.before(end)) {
                            holder.restaurantState.text = "Abierto"
                            holder.linearIsOpen.setBackgroundResource(R.drawable.restaurant_item_open_style)
                        } else {
                            holder.restaurantState.text = "Cerrado"
                            holder.linearIsOpen.setBackgroundResource(R.drawable.restaurant_item_close_style)
                        }
                    }
                } catch (e: ParseException) {
                    Log.e("DateParseError", "Error al parsear las horas de apertura o cierre", e)
                }
            } else {
                Log.e("TimeFormatError", "El formato de la hora es incorrecto: $input")
            }
        } else {
            Log.e("TimeNullError", "El tiempo es nulo o vacío.")
        }


        if (holder.image != null) {
            holder.image.setImageResource(R.drawable.brunch_dining_24px)
        }

        loadImageFromFirestore(currentItem.uid, holder.image)

    }

    override fun getItemCount(): Int {
        return liquidGastroList.size
    }

    inner class LiquidGastroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val image: ImageView = itemView.findViewById(R.id.imgRestaurant)
        val name: TextView = itemView.findViewById(R.id.tvRestaurantName)
        val description: TextView = itemView.findViewById(R.id.tvRestaurantDescription)
        val price: TextView = itemView.findViewById(R.id.tvPrice)
        val time : TextView = itemView.findViewById(R.id.tvTime)
        val favorite : ImageView = itemView.findViewById(R.id.ivHeart)
        val linearIsOpen : LinearLayout = itemView.findViewById(R.id.linearIsOpen)
        val restaurantState : TextView = itemView.findViewById(R.id.tvRestaurantState)


        init{
            image.setOnClickListener(this)
            favorite.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val selectedRestaurant = liquidGastroList[position]
                    val uid = selectedRestaurant.uid
                    uid?.let { listener.onHeartClick(it) }
                }
            }
        }
        /*
        override fun onClick(p0: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION){
               listener.onItemClick(position)
            }
        }
        */
        override fun onClick(p0: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val selectedRestaurant = liquidGastroList[position]
                val uid = selectedRestaurant.uid
                uid?.let { listener.onItemClick(it) }
            }
        }
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

    //SearchView
    fun updateData(newList: ArrayList<Commerce>) {
        liquidGastroList = newList
        notifyDataSetChanged()
    }

    interface RecyclerViewEvent{
        fun onItemClick(uid: String)
        fun onHeartClick(uid: String)
    }
    /*
     fun addFilter(newFilter: String) {
        activeFilters.add(newFilter)
        applyFilters()
    }

    fun removeFilter(filterToRemove: String) {
        activeFilters.remove(filterToRemove)
        applyFilters()
    }

    private fun applyFilters() {
        filteredList = restaurantList.filter { commerce ->
            activeFilters.all { filter -> commerce.matchesFilter(filter) }
        }.toMutableList()
        notifyDataSetChanged()
    }

    */
}
