package com.example.nyt

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.nyt.api.MainView
import com.example.nyt.mvi.MainViewState
import com.facebook.stetho.Stetho
import com.hannesdorfmann.mosby3.mvi.MviActivity
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random


class MainActivity : MviActivity<MainView, MainPresenter>(), MainView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Stetho.initializeWithDefaults(this)

        //showDetailNewsIntent()


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
                else ->{
                    openFragment(WorldFragment())
                    true
                }
            }
        }

        bottomNaviationView.selectedItemId = R.id.actionScience
        bottomNaviationView.setItemIconTintList(null)
    }

    fun openFragment(fragment:Fragment){
        val manager = supportFragmentManager
        val transaction: FragmentTransaction = manager.beginTransaction()
        transaction.replace(R.id.frameContainer,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun createPresenter(): MainPresenter = MainPresenter()

    override fun showDetailNewsIntent(): Observable<Int> {
        return RxView.clicks(frameContainer).map { click -> Random.nextInt() }
            .doOnNext { Log.i("Click", it.toString()) }
    }

    override fun showInChrome(): Observable<Int> {
        return Observable.just(1)
    }

    override fun render(viewState: MainViewState) {
        when {
            viewState.isPageLoading == true -> {
                Log.i("viewState", "Loading")
            }
            viewState.isPullToRefresh!! -> {
                Log.i("viewState", "isPullToRefresh")
            }
            viewState.finished!! -> {
                Log.i("viewStateFinished", viewState.newsObject.toString())
            }
            viewState.newsObject != null -> {
                Log.i("viewStateObject", viewState.newsObject.toString())
            }
        }
    }
}
