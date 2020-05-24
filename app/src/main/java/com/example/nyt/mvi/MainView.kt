package com.example.nyt.mvi

import com.example.nyt.model.MainViewState
import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

interface MainView:MvpView {

    fun render(viewState: MainViewState)

    fun checkLive():Observable<String>

    fun refreshData():Observable<String>

}