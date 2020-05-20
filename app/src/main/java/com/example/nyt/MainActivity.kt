package com.example.nyt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nyt.api.MainView
import com.example.nyt.mvi.MainViewState
import com.example.nyt.mvi.NewsActionState
import com.hannesdorfmann.mosby3.mvi.MviActivity
import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

class MainActivity : MviActivity<MainView,MainPresenter>(),MainView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun createPresenter(): MainPresenter = MainPresenter()

    override fun showDetailNewsIntent(): Observable<NewsActionState> {
    }

    override fun showInChrome(): Observable<Int> {
    }

    override fun render(viewState: MainViewState) {
    }
}
