package com.estebanbe.exploracolombiaapp.controller


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.estebanbe.exploracolombiaapp.NavBarFragments.FavoriteFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.estebanbe.exploracolombiaapp.NavBarFragments.PointsFragment
import com.estebanbe.exploracolombiaapp.NavBarFragments.ProfileFragment
import com.estebanbe.exploracolombiaapp.NavBarFragments.ScheduleFragment
import com.estebanbe.exploracolombiaapp.NavBarFragments.SearchFragment
import com.estebanbe.exploracolombiaapp.R
import com.estebanbe.exploracolombiaapp.controller.puntosyrecompensas.PuntosRecompensasFragment

class MainActivityUsuarios : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_usuarios)

        bottomNavigationView = findViewById(R.id.bottom_navigation)


        bottomNavigationView.setOnItemSelectedListener{ menuItem ->
            when(menuItem.itemId){
                R.id.btnSearch ->{
                    replaceFragment(SearchFragment())
                    true
                }
                R.id.btnFavorite ->{
                    replaceFragment(FavoriteFragment())
                    true
                }
                R.id.btnSchedule ->{
                    replaceFragment(ScheduleFragment())
                    true
                }
                R.id.btnProfile ->{
                    replaceFragment(ProfileFragment())
                    true
                }
                R.id.btnPuntos ->{
                    replaceFragment(PuntosRecompensasFragment())
                    true
                }
                else -> false
            }
        }

        replaceFragment(SearchFragment())
    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container,fragment).commit()
    }

}