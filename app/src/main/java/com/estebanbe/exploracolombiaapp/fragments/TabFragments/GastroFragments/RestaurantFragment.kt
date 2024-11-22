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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.estebanbe.exploracolombiaapp.NavBarFragments.SearchFragment
import com.estebanbe.exploracolombiaapp.R
import com.estebanbe.exploracolombiaapp.fragments.TabFragments.GastroFragments.Adapters.OfferAdapter
import com.estebanbe.exploracolombiaapp.fragments.TabFragments.GastroFragments.Adapters.RestaurantAdapter
import com.estebanbe.exploracolombiaapp.fragments.TabFragments.GastroFragments.Entities.Commerce
import com.estebanbe.exploracolombiaapp.fragments.TabFragments.GastroFragments.Entities.Favorite
import com.estebanbe.exploracolombiaapp.fragments.TabFragments.GastroFragments.Entities.Offer
import com.google.firebase.auth.FirebaseAuth
import com.joelalba.exploracolombia2.TabFragments.GastroFragments.MenuFragment.MenuFragment

class RestaurantFragment : Fragment(),RestaurantAdapter.RecyclerViewEvent,
    SearchFragment.Filterable {

    private lateinit var dbref: DatabaseReference
    private lateinit var restaurantRecyclerView: RecyclerView
    private lateinit var restaurantArrayList: ArrayList<Commerce>
    private lateinit var offerRecyclerView: RecyclerView
    private lateinit var offerArrayList : ArrayList<Offer>
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var offerAdapter : OfferAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var counter : Int = 0
        val view = inflater.inflate(R.layout.fragment_restaurant, container, false)



        offerRecyclerView = view.findViewById(R.id.offerRecyclerView)
        offerRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        offerRecyclerView.setHasFixedSize(true)

        restaurantRecyclerView = view.findViewById(R.id.restaurantList)
        restaurantRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        restaurantRecyclerView.setHasFixedSize(true)

        restaurantArrayList = arrayListOf()
        offerArrayList = arrayListOf()

        restaurantAdapter = RestaurantAdapter(restaurantArrayList,this)
        restaurantRecyclerView.adapter = restaurantAdapter

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
                        val restaurantUid = offer?.restaurantUid
                        if (!restaurantUid.isNullOrEmpty()) {
                            detectRestaurantOffer(restaurantUid, object : RestaurantOfferCallback {
                                override fun onResult(isRestaurantOffer: Boolean) {
                                    if (isRestaurantOffer) {
                                        offer?.let {
                                            Log.e("Restaurant DetectOffer", "Se añadió el restaurante")
                                            offerArrayList.add(it)
                                            offerRecyclerView.adapter?.notifyDataSetChanged()
                                        }
                                    } else {
                                        println("El restaurante no tiene una oferta especial o no existe")
                                    }
                                }
                            })

                        } else {
                            println("El restaurantUid es nulo o vacío")
                        }
                    }


                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun detectRestaurantOffer(restaurantUid: String, callback: RestaurantOfferCallback) {
        val database = FirebaseDatabase.getInstance()
        val commerceRef: DatabaseReference = database.getReference("Commerce").child(restaurantUid)

        commerceRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    Log.e("Restaurant DetectOffer", "Por aquí pasó")
                    val commerce = snapshot.getValue(Commerce::class.java)
                    if (commerce != null) {
                        if (commerce.commerceType == "restaurante") {
                            Log.e("Restaurant DetectOffer", "Devolvió TRUE")
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
                // En caso de error al obtener los datos, devolvemos false
                println("Error al obtener los datos: ${error.message}")
                callback.onResult(false)
            }
        })
    }


    private fun getRestaurantData() {
        dbref = FirebaseDatabase.getInstance().getReference("Commerce")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    restaurantArrayList.clear()

                    for (restaurantSnapshot in snapshot.children) {
                        val restaurant = restaurantSnapshot.getValue(Commerce::class.java)
                        Log.d("DebugRestaurant", "Restaurant UID: ${restaurant?.uid}")
                        if (restaurant?.commerceType == "restaurante") {
                            restaurant.let { restaurantArrayList.add(it) }
                        }
                    }

                    restaurantRecyclerView.adapter?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onItemClick(uid: String) {
        replaceFragment(MenuFragment(),uid)
    }

    private fun replaceFragment(fragment: Fragment,restaurantUid: String) {
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
        val filteredList = restaurantArrayList.filter { commerce ->
            commerce.name?.contains(query, ignoreCase = true) == true
        }
        restaurantAdapter.updateData(ArrayList(filteredList))
    }

    interface RestaurantOfferCallback {
        fun onResult(isRestaurantOffer: Boolean)
    }
}
