package com.example.nyt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

    }

    override fun createPresenter(): MainPresenter = MainPresenter()

    override fun showDetailNewsIntent(): Observable<Int> {
        return RxView.clicks(helloWorld).map { click -> Random.nextInt() }.
            doOnNext { Log.i("Click" , it.toString()) }
    }

    override fun showInChrome(): Observable<Int> {
        return Observable.just(1)
    }

    override fun render(viewState: MainViewState) {
        when {
            viewState.isPageLoading==true -> {
                Log.i("viewState", "Loading")
            }
            viewState.isPullToRefresh!! -> {
                Log.i("viewState", "isPullToRefresh")
            }
            viewState.finished!! -> {
                Log.i("viewStateFinished", viewState.newsObject.toString())
            }
            viewState.newsObject!=null -> {
                Log.i("viewStateObject", viewState.newsObject.toString())
            }
        }
    }
}
