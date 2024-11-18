package com.estebanbe.exploracolombiaapp.fragments.TabFragments.GastroFragments.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.estebanbe.exploracolombiaapp.R
import com.estebanbe.exploracolombiaapp.modelo.Offer

class OfferAdapter (private val offerList : ArrayList<Offer>) :RecyclerView.Adapter<OfferAdapter.OfferViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.offer_item,parent,false)
        return  OfferViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return offerList.size
    }

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        val currentItem = offerList[position]
        loadImageFromFirestore(currentItem.resturantUid!!, holder.image)
    }


    class OfferViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val image : ImageView = itemView.findViewById(R.id.offerImage)
    }

    private fun loadImageFromFirestore(uid: String, imageView: ImageView) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("Offers").child(uid)

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
}