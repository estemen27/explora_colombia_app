package com.estebanbe.exploracolombiaapp.TabFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.estebanbe.exploracolombiaapp.R


class MapFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("Error", "AQUIII EN mapa")
        return inflater.inflate(R.layout.fragment_map, container, false)
    }
    override fun onStart() {
        super.onStart()
        Log.e("MAPA", "onStart ejecutado")
    }
}