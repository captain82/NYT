package com.example.nyt

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nyt.api.MainView
import com.example.nyt.model.NewsResponseModel
import com.example.nyt.mvi.MainViewState
import com.facebook.stetho.Stetho
import com.hannesdorfmann.mosby3.mvi.MviActivity
import com.jakewharton.rxbinding2.view.RxView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_science.*
import kotlin.random.Random


class MainActivity : AppCompatActivity(){



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Stetho.initializeWithDefaults(this)

        //showDetailNewsIntent()
        supportActionBar?.setTitle(null)

        supportActionBar?.setBackgroundDrawable(getDrawable(R.drawable.ic_nytimes))


        bottomNaviationView.setOnNavigationItemSelectedListener { it ->
            when (it.itemId) {
                R.id.actionScience -> {
                    openFragment(ScienceFragment())
                    true
                }
                R.id.actionBuisseness -> {
                    openFragment(BuissenssFragment())
                    true
                }
                R.id.actionMovies -> {
                    openFragment(MoviesFragment())
                    true
                }
                else -> {
                    openFragment(WorldFragment())
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
