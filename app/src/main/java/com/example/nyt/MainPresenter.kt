package com.example.nyt

import com.example.nyt.Local.AppDatabase
import com.example.nyt.api.MainView
import com.example.nyt.api.RetrofitBuilder
import com.example.nyt.model.NewsResponseModel
import com.example.nyt.mvi.MainViewState
import com.example.nyt.mvi.NewsActionState
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainPresenter(private var localdb: AppDatabase) :
    MviBasePresenter<MainView, MainViewState>() {
    lateinit var getRoomQuery: Observable<NewsActionState>

    override fun bindIntents() {

        getRoomQuery = intent(MainView::queryRoom)
            .switchMap {
                localdb.responseDao().queryObservable(it)
                    .toObservable()
                    .onErrorResumeNext(
                        RetrofitBuilder.apiService.getTopNewsByCategory(
                            it,
                            "IUlVCCal6Hvyto3wwp1nKjfIzWtizl4q"
                        ).observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                    )
            }.map {
                inflateDataInRoom(it)
                NewsActionState.DataState(it) as NewsActionState
            }.onErrorReturn {
                NewsActionState.ErrorState(it)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())

        val initialState = MainViewState(isPageLoading = true)

        getRoomQuery.scan(initialState, this::viewStateReducer)?.let {
            subscribeViewState(it, MainView::render)
        }
    }

    private fun inflateDataInRoom(response: NewsResponseModel) {
        Completable.fromAction { localdb.responseDao().insert(response) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe { NewsActionState.FinishState }
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
                previousState.copy(isPageLoading = false, newsObject = currentState.newsResponse)
            }
            is NewsActionState.ErrorState -> previousState.copy(
                error = true
            )
            is NewsActionState.FinishState -> {
                previousState.copy(isPageLoading = false)
            }
        }

    }

}
