package com.estebanbe.exploracolombiaapp.NavBarFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.estebanbe.exploracolombiaapp.R
import com.estebanbe.exploracolombiaapp.controller.TabAdapter
import com.estebanbe.exploracolombiaapp.fragments.TabFragments.GastroFragments.LiquidGastroFragment
import com.estebanbe.exploracolombiaapp.fragments.TabFragments.GastroFragments.RestaurantFragment
import com.estebanbe.exploracolombiaapp.fragments.TabFragments.GastroFragments.SolidGastroFragment


class SearchFragment : Fragment() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var linearRestaurants: LinearLayout
    private lateinit var frameRestaurants: FrameLayout
    private lateinit var frameSolidGastronomy: FrameLayout
    private lateinit var frameLiquidGastronomy: FrameLayout
    private lateinit var imageRestaurant: ImageView
    private var isVisible: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        tabLayout = view.findViewById(R.id.tabLayout)
        viewPager = view.findViewById(R.id.viewPager)



        tabLayout.addTab(tabLayout.newTab().setText("Para ti").setIcon(R.drawable.person_24px))
        tabLayout.addTab(tabLayout.newTab().setText("Gastronomía").setIcon(R.drawable.restaurant_24px))
        tabLayout.addTab(tabLayout.newTab().setText("Hoteles").setIcon(R.drawable.hotel_24px))
        tabLayout.addTab(tabLayout.newTab().setText("Artesanías").setIcon(R.drawable.folded_hands_24px))
        tabLayout.addTab(tabLayout.newTab().setText("Mapa").setIcon(R.drawable.map_24px))

        val adapter = TabAdapter(requireContext(), childFragmentManager, tabLayout.tabCount)
        viewPager.adapter = adapter
        Log.e("Adapter", "Adapter asignado al ViewPager")

        linearRestaurants = view.findViewById(R.id.linearRestaurants)
        frameRestaurants = view.findViewById(R.id.frRestaurants)
        frameLiquidGastronomy = view.findViewById(R.id.frLiquidGastronomy)
        frameSolidGastronomy = view.findViewById(R.id.frSolidGastronomy)
        imageRestaurant = view.findViewById(R.id.imgRestaurants)



        frameRestaurants.setOnClickListener {
            replaceFragment(RestaurantFragment())
        }

        frameLiquidGastronomy.setOnClickListener {
            replaceFragment(LiquidGastroFragment())
        }

        frameSolidGastronomy.setOnClickListener {
            replaceFragment(SolidGastroFragment())
        }

        imageRestaurant.setOnClickListener {
            replaceFragment(RestaurantFragment())
        }


        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.currentItem = tab!!.position
                if (tab.position == 1) {
                    isVisible = true
                    linearRestaurants.visibility = View.VISIBLE
                } else {
                    clearTabContentContainer()
                    //hideTabContentContainer}ddds
                    linearRestaurants.visibility = View.INVISIBLE
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {
                if (tab!!.position == 1) {
                    if (isVisible) {
                        isVisible = false
                        linearRestaurants.visibility = View.INVISIBLE
                    } else {
                        isVisible = true
                        linearRestaurants.visibility = View.VISIBLE
                    }
                } else {
                    linearRestaurants.visibility = View.INVISIBLE
                }
            }
        })

        return view
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.tab_content_container, fragment) // Cambia al nuevo contenedor
        transaction.commit()
    }

    fun clearTabContentContainer() {
        val fragmentManager = childFragmentManager
        val fragment = fragmentManager.findFragmentById(R.id.tab_content_container)
        if (fragment != null) {
            val transaction = fragmentManager.beginTransaction()
            transaction.remove(fragment)
            transaction.commit()
        }
    }
}