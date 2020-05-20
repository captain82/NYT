package com.example.nyt

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
            .switchMap { RetrofitBuilder.apiService.getTopNewsByCategory("science", TODO()) }
            .map { NewsActionState.DataState(it) as NewsActionState }
            .startWith { NewsActionState.LoadingState }
            .onErrorReturn { error -> NewsActionState.ErrorState(error) }
            .doOnComplete { NewsActionState.FinishState }
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

        return when (currentState) {
            is NewsActionState.LoadingState -> {
                previousState.copy(isPageLoading = true)
            }
            is NewsActionState.DataState -> {
                previousState.copy(
                    isPageLoading = false,
                    newsObject = currentState.newsResponse
                )
            }
            is NewsActionState.ErrorState -> previousState.copy(error = currentState.throwable)
            is NewsActionState.FinishState -> previousState.copy(finished = true)
        }
    }
}