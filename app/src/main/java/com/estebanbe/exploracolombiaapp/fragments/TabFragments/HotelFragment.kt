package com.estebanbe.exploracolombiaapp.fragments.TabFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.estebanbe.exploracolombiaapp.R


class HotelFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("Fragmento!","Hotel")
        return inflater.inflate(R.layout.fragment_hotel, container,false)
    }

    override fun onStart() {
        super.onStart()
        Log.e("Fragment1", "onStart ejecutado")
    }


}