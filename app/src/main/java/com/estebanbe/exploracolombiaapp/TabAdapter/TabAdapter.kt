package com.estebanbe.exploracolombiaapp.TabAdapter

import android.content.Context
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.estebanbe.exploracolombiaapp.R
import com.estebanbe.exploracolombiaapp.TabFragments.ForYouFragment
import com.estebanbe.exploracolombiaapp.TabFragments.GastronomyFragment
import com.estebanbe.exploracolombiaapp.TabFragments.HandMadeFragment
import com.estebanbe.exploracolombiaapp.TabFragments.HotelFragment
import com.estebanbe.exploracolombiaapp.TabFragments.MapFragment

internal class TabAdapter(var contexto: Context, fm: FragmentManager, var totalTabs: Int): FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        Log.e("Error", "AQUIII $position")
        return when(position) {
            0 -> ForYouFragment()

            2 -> HotelFragment()
            3 -> HandMadeFragment()
            4 -> MapFragment()
            else -> {
                Log.e("Error", "No existe esa pestaña: $position")
                ForYouFragment()
            }
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }


}
