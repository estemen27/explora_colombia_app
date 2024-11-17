package com.estebanbe.exploracolombiaapp.TabFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.estebanbe.exploracolombiaapp.R


class GastronomyFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("Fragmento!","Gastronomia")
        return inflater.inflate(R.layout.fragment_gastronomy, container, false)
    }
}