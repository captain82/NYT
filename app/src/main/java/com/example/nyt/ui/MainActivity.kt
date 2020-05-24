package com.example.nyt.ui

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.nyt.R
import com.facebook.stetho.Stetho
import kotlinx.android.synthetic.main.activity_main.*


open class MainActivity : AppCompatActivity() {

    private val techFrag = TechFragment()
    private var businessFrag = BuissenssFragment()
    private val movieFrag = MoviesFragment()
    private val scienceFrag = ScienceFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().add(R.id.frameContainer, scienceFrag)
            .hide(scienceFrag).commit()
        supportFragmentManager.beginTransaction().add(R.id.frameContainer, movieFrag)
            .hide(movieFrag).commit()
        supportFragmentManager.beginTransaction().add(R.id.frameContainer, businessFrag)
            .hide(businessFrag).commit()
        supportFragmentManager.beginTransaction().add(R.id.frameContainer, techFrag).commit()

        var fragActive:Fragment = techFrag

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
                    supportFragmentManager.beginTransaction().hide(fragActive).show(techFrag)
                        .commit()
                    fragActive = techFrag
                    true
                }
                R.id.actionBuisseness -> {
                    supportFragmentManager.beginTransaction().hide(fragActive).show(businessFrag)
                        .commit()
                    fragActive = businessFrag
                    true
                }
                R.id.actionMovies -> {
                    supportFragmentManager.beginTransaction().hide(fragActive).show(movieFrag)
                        .commit()
                    fragActive = movieFrag
                    true
                }
                else -> {
                    supportFragmentManager.beginTransaction().hide(fragActive).show(scienceFrag)
                        .commit()
                    fragActive = scienceFrag
                    true
                }}
        }
        bottomNaviationView.selectedItemId = R.id.actionScience
    }
}
