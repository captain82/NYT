package com.example.nyt.api

import com.example.nyt.mvi.MainViewState
import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

interface MainView:MvpView {

    fun showDetailNewsIntent(): Observable<Int>

    fun showInChrome():Observable<Int>

    fun render(viewState: MainViewState)
}