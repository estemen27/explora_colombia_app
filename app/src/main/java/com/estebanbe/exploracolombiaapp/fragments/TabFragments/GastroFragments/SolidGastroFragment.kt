package com.estebanbe.exploracolombiaapp.fragments.TabFragments.GastroFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.estebanbe.exploracolombiaapp.NavBarFragments.SearchFragment
import com.estebanbe.exploracolombiaapp.R
import com.estebanbe.exploracolombiaapp.fragments.TabFragments.GastroFragments.Adapters.OfferAdapter
import com.estebanbe.exploracolombiaapp.fragments.TabFragments.GastroFragments.Adapters.SolidGastroAdapter
import com.estebanbe.exploracolombiaapp.fragments.TabFragments.GastroFragments.Entities.Commerce
import com.estebanbe.exploracolombiaapp.fragments.TabFragments.GastroFragments.Entities.Favorite
import com.estebanbe.exploracolombiaapp.fragments.TabFragments.GastroFragments.Entities.Offer
import com.joelalba.exploracolombia2.TabFragments.GastroFragments.MenuFragment.MenuFragment


class SolidGastroFragment : Fragment(), SolidGastroAdapter.RecyclerViewEvent, SearchFragment.Filterable{

    private lateinit var dbref: DatabaseReference
    private lateinit var solidGastroRecyclerView: RecyclerView
    private lateinit var solidGastroArrayList: ArrayList<Commerce>
    private lateinit var offerRecyclerView: RecyclerView
    private lateinit var offerArrayList : ArrayList<Offer>
    private lateinit var solidGastroAdapter: SolidGastroAdapter
    private lateinit var offerAdapter : OfferAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_solid_gastro, container, false)

        offerRecyclerView = view.findViewById(R.id.solidOfferRecyclerView)
        offerRecyclerView.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)
        offerRecyclerView.setHasFixedSize(true)

        solidGastroRecyclerView = view.findViewById(R.id.solidGastroList)
        solidGastroRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        solidGastroRecyclerView.setHasFixedSize(true)

        solidGastroArrayList = arrayListOf()
        offerArrayList = arrayListOf()

        solidGastroAdapter = SolidGastroAdapter(solidGastroArrayList,this)
        solidGastroRecyclerView.adapter = solidGastroAdapter

        offerAdapter = OfferAdapter(offerArrayList)
        offerRecyclerView.adapter = offerAdapter

        getRestaurantData()
        getOfferData()
        return view
    }


    private fun getOfferData() {
        dbref = FirebaseDatabase.getInstance().getReference("Offers")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    offerArrayList.clear()

                    for (offerSnapshot in snapshot.children) {
                        val offer = offerSnapshot.getValue(Offer::class.java)
                        val solidUid = offer?.restaurantUid
                        if (!solidUid.isNullOrEmpty()) {
                            detectSolidOffer(solidUid, object : SolidOfferCallback {
                                override fun onResult(isSolidOffer: Boolean) {
                                    if (isSolidOffer) {
                                        offer?.let {
                                            Log.e("Solid DetectOffer", "Se añadió la oferta sólida")
                                            offerArrayList.add(it)
                                            offerRecyclerView.adapter?.notifyDataSetChanged()
                                        }
                                    } else {
                                        println("El comercio no tiene una oferta sólida o no existe")
                                    }
                                }
                            })
                        } else {
                            println("El solidUid es nulo o vacío")
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun detectSolidOffer(solidUid: String, callback: SolidOfferCallback) {
        val database = FirebaseDatabase.getInstance()
        val commerceRef: DatabaseReference = database.getReference("Commerce").child(solidUid)

        commerceRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    Log.e("Solid DetectOffer", "Por aquí pasó")
                    val commerce = snapshot.getValue(Commerce::class.java)
                    if (commerce != null) {
                        if (commerce.commerceType == "solida") {
                            Log.e("Solid DetectOffer", "Devolvió TRUE")
                            callback.onResult(true)
                        } else {
                            callback.onResult(false)
                        }
                    } else {
                        callback.onResult(false)
                    }
                } else {
                    callback.onResult(false)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Error al obtener los datos: ${error.message}")
                callback.onResult(false)
            }
        })
    }

    interface SolidOfferCallback {
        fun onResult(isSolidOffer: Boolean)
    }


    private fun getRestaurantData() {
        dbref = FirebaseDatabase.getInstance().getReference("Commerce")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    solidGastroArrayList.clear()

                    for (restaurantSnapshot in snapshot.children) {
                        val restaurant = restaurantSnapshot.getValue(Commerce::class.java)

                        if (restaurant?.commerceType == "solida") {
                            restaurant.let { solidGastroArrayList.add(it) }
                        }
                    }

                    solidGastroRecyclerView.adapter?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onItemClick(uid: String) {
        replaceFragment(MenuFragment(),uid)
    }

    private fun replaceFragment(fragment: Fragment, restaurantUid: String) {
        val bundle = Bundle()
        bundle.putString("restaurant_id", restaurantUid)

        fragment.arguments = bundle
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onHeartClick(restaurantUid: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Favorites")
        val userUid = FirebaseAuth.getInstance().currentUser
        val favoriteUid = databaseReference.push().key

        if (favoriteUid != null) {

            val favorite = Favorite(favoriteUid, restaurantUid, userUid!!.uid)


            databaseReference.child(favoriteUid).setValue(favorite)
                .addOnSuccessListener {
                    Toast.makeText(context, "Restaurante guardado en Favoritos", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "Error al generar el ID", Toast.LENGTH_SHORT).show()
        }
    }

    override fun filterData(query: String) {
        val filteredList = solidGastroArrayList.filter { commerce ->
            commerce.name?.contains(query, ignoreCase = true) == true
        }
        solidGastroAdapter.updateData(ArrayList(filteredList))
    }
}