package com.example.nyt.api

import android.content.Context
import com.example.nyt.Local.AppDatabase
import com.example.nyt.Local.ResponseDAO
import com.example.nyt.model.NewsResponseModel
import com.example.nyt.mvi.MainViewState
import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Maybe
import io.reactivex.Observable

interface MainView:MvpView {

    fun showDetailNewsIntent(): Observable<String>

    fun showInChrome():Observable<Int>

    fun render(viewState: MainViewState)

    fun queryRoom(): Observable<String>
}