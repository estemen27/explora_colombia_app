package com.estebanbe.exploracolombiaapp.controller


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.estebanbe.exploracolombiaapp.NavBarFragments.FavoriteFragment
import com.estebanbe.exploracolombiaapp.NavBarFragments.PointsFragment
import com.estebanbe.exploracolombiaapp.NavBarFragments.ProfileFragment
import com.estebanbe.exploracolombiaapp.NavBarFragments.ScheduleFragment
import com.estebanbe.exploracolombiaapp.NavBarFragments.SearchFragment
import com.estebanbe.exploracolombiaapp.R

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                    replaceFragment(PointsFragment())
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