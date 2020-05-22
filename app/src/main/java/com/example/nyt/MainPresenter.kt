package com.example.nyt

import android.util.Log
import com.example.nyt.Local.AppDatabase
import com.example.nyt.api.MainView
import com.example.nyt.api.RetrofitBuilder
import com.example.nyt.model.NewsResponseModel
import com.example.nyt.mvi.MainViewState
import com.example.nyt.mvi.NewsActionState
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainPresenter(private var localdb: AppDatabase) :
    MviBasePresenter<MainView, MainViewState>() {


    override fun bindIntents() {

        val getNewsResults: Observable<NewsActionState>? = intent(MainView::showDetailNewsIntent)
            .switchMap {
                RetrofitBuilder.apiService.getTopNewsByCategory(
                    it,
                    "IUlVCCal6Hvyto3wwp1nKjfIzWtizl4q"
                ).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
            }
            .map { NewsActionState.DataState(it) as NewsActionState }
            .startWith(NewsActionState.LoadingState)
            .onErrorReturn { NewsActionState.ErrorState(it) }
            .doOnComplete { NewsActionState.FinishState }
            .doOnError { Log.i("onError", "Clicked") }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())

        val getRoomQuery = intent(MainView::queryRoom)
            .switchMap { localdb.responseDao().queryObservable(it)
                .toObservable()
                .onErrorResumeNext(
                    RetrofitBuilder.apiService.getTopNewsByCategory(
                    it,
                    "IUlVCCal6Hvyto3wwp1nKjfIzWtizl4q")
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io()))
                }.map {Log.i("Success" , it.results.toString())
                NewsActionState.DataState(it) as NewsActionState }
            .startWith(NewsActionState.LoadingState)
            .onErrorReturn { NewsActionState.ErrorState(it) }
            .doOnComplete { NewsActionState.FinishState }
            .doOnError { Log.i("onError", "Clicked") }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())






            /*.switchMap { localdb?.responseDao()?.queryObservable(it)
                ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribeOn(Schedulers.io())
                    }?.doOnError { Log.i("error",it.toString())}?.map {
                Log.i("success",it.toString())
                NewsActionState.DataState(it) as NewsActionState
            }
            ?.onErrorReturn { Log.i("error",it.toString())
                NewsActionState.ErrorState(it)
            }
            ?.doOnError {  Log.i("error",it.toString()) }
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())*/


        val allViewState: Observable<NewsActionState>? = Observable.merge(
            getNewsResults,getRoomQuery)
        val initialState = MainViewState(isPageLoading = false, isPullToRefresh = false)

        allViewState?.scan(initialState,this::viewStateReducer)?.let{
            subscribeViewState(it,MainView::render)
        }


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
                isPageLoading = false,
                error = currentState.throwable
            )
            is NewsActionState.FinishState -> previousState.copy(
                isPageLoading = false,
                finished = true
            )
        }

    }

}
