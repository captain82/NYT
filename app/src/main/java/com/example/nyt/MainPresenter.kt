package com.example.nyt

import android.util.Log
import com.example.nyt.api.MainView
import com.example.nyt.api.RetrofitBuilder
import com.example.nyt.mvi.MainViewState
import com.example.nyt.mvi.NewsActionState
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainPresenter : MviBasePresenter<MainView, MainViewState>() {
    private val compositeDisposable = CompositeDisposable()

    override fun bindIntents() {

        val getNewsResults: Observable<NewsActionState> = intent(MainView::showDetailNewsIntent)
            .switchMap { it ->
                Log.i("switch", it.toString())
                RetrofitBuilder.apiService.getTopNewsByCategory(
                    "science",
                    "IUlVCCal6Hvyto3wwp1nKjfIzWtizl4q"
                ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            }
            .map { NewsActionState.DataState(it) as NewsActionState }
            .onErrorReturn { NewsActionState.ErrorState(it) }
            .doOnNext { Log.i("onNext", "Clicked") }
            .doOnError { Log.i("onError", "Clicked") }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())


        val initialState = MainViewState(isPageLoading = false, isPullToRefresh = false)

        subscribeViewState(
            getNewsResults.scan(initialState, this::viewStateReducer),
            MainView::render
        )
    }

    private fun viewStateReducer(
        previousState: MainViewState,
        currentState: NewsActionState
    ): MainViewState {
        val newState = previousState

        when (currentState) {
            is NewsActionState.LoadingState -> {
                newState.isPageLoading = true
            }
            is NewsActionState.DataState -> {
                newState.newsObject = currentState.newsResponse
            }
            is NewsActionState.ErrorState -> newState.error = currentState.throwable
            is NewsActionState.FinishState -> newState.finished = true
        }

        return newState
    }
}