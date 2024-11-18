package com.estebanbe.exploracolombiaapp.fragments.TabFragments.GastroFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.estebanbe.exploracolombiaapp.R
import com.estebanbe.exploracolombiaapp.fragments.TabFragments.GastroFragments.Adapters.RestaurantAdapter
import com.estebanbe.exploracolombiaapp.modelo.Offer
import com.estebanbe.exploracolombiaapp.modelo.Restaurant

class RestaurantFragment : Fragment() {

    private lateinit var dbref: DatabaseReference
    private lateinit var restaurantRecyclerView: RecyclerView
    private lateinit var restaurantArrayList: ArrayList<Restaurant>
    private lateinit var offerRecyclerView: RecyclerView
    private lateinit var offerArrayList : ArrayList<Offer>
    private lateinit var linearFiltrado : LinearLayout
    private lateinit var scrollFiltrado : ScrollView
    private lateinit var restaurantAdapter: RestaurantAdapter

    private lateinit var ckPrice1 : CheckBox
    private lateinit var ckPrice2 : CheckBox
    private lateinit var ckPrice3 : CheckBox
    private lateinit var ckPrice4 : CheckBox
    private lateinit var ckPrice5 : CheckBox

    private var filterArray: Array<Boolean> = arrayOf(false, false, false, false,false)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var counter : Int = 0
        val view = inflater.inflate(R.layout.fragment_restaurant, container, false)


        linearFiltrado = view.findViewById(R.id.linearFiltrar)
        scrollFiltrado = view.findViewById(R.id.scrollFiltrar)
        ckPrice1 = view.findViewById(R.id.ckPrice1)
        ckPrice2 = view.findViewById(R.id.ckPrice2)
        ckPrice3 = view.findViewById(R.id.ckPrice3)
        ckPrice4 = view.findViewById(R.id.ckPrice4)
        ckPrice5 = view.findViewById(R.id.ckPrice5)

        //offerRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        //offerRecyclerView = view.findViewById(R.id.offerRecyclerView)
        //offerRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        restaurantRecyclerView = view.findViewById(R.id.restaurantList)
        restaurantRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        restaurantRecyclerView.setHasFixedSize(true)

        restaurantArrayList = arrayListOf()

        restaurantAdapter = RestaurantAdapter(restaurantArrayList)
        restaurantRecyclerView.adapter = restaurantAdapter

        linearFiltrado.setOnClickListener{
            if(counter%2 == 0){
                scrollFiltrado.visibility = View.VISIBLE
                counter++
            } else{
                scrollFiltrado.visibility = View.INVISIBLE
                counter++
            }
        }

        ckPrice1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                restaurantAdapter.addFilter("price:")
            } else{
                restaurantAdapter.removeFilter("price")
            }
        }
        ckPrice2.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                getCheckData(1,true)
            } else{
                getCheckData(1,false)
            }
        }
        ckPrice3.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                getCheckData(2,true)
            } else{
                getCheckData(2,false)
            }
        }
        ckPrice4.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                getCheckData(3,true)
            } else{
                getCheckData(3,false)
            }
        }
        ckPrice5.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                getCheckData(4,true)
            } else{
                getCheckData(4,false)
            }
        }


        getRestaurantData()
        //getOfferData()
        return view
    }

    private fun getCheckData(index : Int, isChecked : Boolean){
        filterArray[index] = isChecked

        filterArray.forEach {

        }
    }



    private fun getOfferData() {
        dbref = FirebaseDatabase.getInstance().getReference("Offers")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    offerArrayList.clear()

                    for (offerSnapshot in snapshot.children) {
                        val offer = offerSnapshot.getValue(Offer::class.java)
                        offer?.let { offerArrayList.add(it) }
                    }

                    restaurantRecyclerView.adapter?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })


    }

    private fun getRestaurantData() {
        dbref = FirebaseDatabase.getInstance().getReference("Restaurants")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    restaurantArrayList.clear()

                    for (restaurantSnapshot in snapshot.children) {
                        val restaurant = restaurantSnapshot.getValue(Restaurant::class.java)
                        restaurant?.let { restaurantArrayList.add(it) }
                    }

                    restaurantRecyclerView.adapter?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
