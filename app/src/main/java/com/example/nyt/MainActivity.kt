package com.example.nyt

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.facebook.stetho.Stetho
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(){

    val techFrag = TechFragment()
    val buisenessFrag = BuissenssFragment()
    val movieFrag = MoviesFragment()
    val worldFrag = WorldFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Stetho.initializeWithDefaults(this)
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decor = window.decorView
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        bottomNaviationView.setOnNavigationItemSelectedListener { it ->
            when (it.itemId) {
                R.id.actionScience -> {
                    if (supportFragmentManager.findFragmentById(R.id.frameContainer) !is TechFragment) {
                        openFragment(techFrag)
                    }
                    true
                }
                R.id.actionBuisseness -> {
                    if (supportFragmentManager.findFragmentById(R.id.frameContainer) !is BuissenssFragment) {
                        openFragment(buisenessFrag)
                    }
                    true
                }
                R.id.actionMovies -> {
                    if (supportFragmentManager.findFragmentById(R.id.frameContainer) !is MoviesFragment) {
                        openFragment(movieFrag)
                    }
                    true
                }
                else -> {
                    if (supportFragmentManager.findFragmentById(R.id.frameContainer) !is WorldFragment) {
                        openFragment(worldFrag)
                    }
                    true
                }
            }
        }

        bottomNaviationView.selectedItemId = R.id.actionScience
    }

    fun openFragment(fragment: Fragment) {
        val manager = supportFragmentManager
        val transaction: FragmentTransaction = manager.beginTransaction()
        transaction.replace(R.id.frameContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }




}
