package com.example.nyt

import android.util.Log
import com.example.nyt.Local.AppDatabase
import com.example.nyt.api.MainView
import com.example.nyt.api.RetrofitBuilder
import com.example.nyt.model.NewsResponseModel
import com.example.nyt.mvi.MainViewState
import com.example.nyt.mvi.NewsActionState
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import com.xwray.groupie.Section
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainPresenter(private var localdb: AppDatabase) :
    MviBasePresenter<MainView, MainViewState>() {

    lateinit var section:String

    override fun bindIntents() {

       /* val updateDb = intent(MainView::updatedb)
            .switchMap {
                Log.i("update", it)
                RetrofitBuilder.apiService
                    .getTopNewsByCategory(it, "IUlVCCal6Hvyto3wwp1nKjfIzWtizl4q")
                    .doOnError { Log.i("Purge", it.localizedMessage) }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
            }
            .map {
                inflateDataInRoom(it)
                Log.i("purge", it.toString())
                NewsActionState.LoadingState as NewsActionState
            }
            .onErrorReturn {
                Log.i("Purge", it.localizedMessage)
                NewsActionState.ErrorState(it)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
*/

        val checkLive = intent(MainView::checkLive)
            .switchMap { section ->
                this.section = section
                localdb.responseDao().query(section)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .doOnError { Log.i("liveError", "error") }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            }
            .map {
                Log.i("Live", it.toString())
                if (it.isNotEmpty()) {
                    Log.i("live", "notEmpty")
                    NewsActionState.DataState(it[0]) as NewsActionState
                } else {
                    Log.i("update", "db")
                    fetchData(section)
                    NewsActionState.UpdateDb
                }
            }.doOnError { Log.i("error", "liveQuery") }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())

        /* val getRoomQuery = intent(MainView::queryRoom)
             .switchMap {
                 Log.i("Switch", "fired")
                 localdb.responseDao().queryObservable(it)
                     .observeOn(AndroidSchedulers.mainThread())
                     .subscribeOn(Schedulers.io())
                     .toObservable()
                     .doOnError { Log.i("ErrorRoom", it.localizedMessage) }
                     .onErrorResumeNext(
                         RetrofitBuilder.apiService.getTopNewsByCategory(
                             it,
                             "IUlVCCal6Hvyto3wwp1nKjfIzWtizl4q"
                         ).observeOn(AndroidSchedulers.mainThread())
                             .subscribeOn(Schedulers.io())
                     )
             }.map {
                 //inflateDataInRoom(it)
                 NewsActionState.DataState(it) as NewsActionState
             }.onErrorReturn {
                 Log.i("Error", it.localizedMessage)

                 NewsActionState.ErrorState(it)
             }
             .observeOn(AndroidSchedulers.mainThread())
             .subscribeOn(Schedulers.io())*/


       // val finalObservable = Observable.merge(updateDb, checkLive)

        val initialState = MainViewState(isPageLoading = true)

        checkLive.scan(initialState, this::viewStateReducer)?.let {
            subscribeViewState(it, MainView::render)
        }
    }

    fun fetchData(section: String) {
        RetrofitBuilder.apiService
            .getTopNewsByCategory(section, "IUlVCCal6Hvyto3wwp1nKjfIzWtizl4q")
            .doOnError { Log.i("error", "occured") }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({inflateDataInRoom(it)},{Log.i("Error" , "host")})
    }

    private fun inflateDataInRoom(response: NewsResponseModel) {
        Completable.fromAction { localdb.responseDao().insert(response) }
            .doOnError { Log.i("error", "occured") }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe()
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
                previousState.copy(isPageLoading = false, updateDb = false,newsObject = currentState.newsResponse)
            }
            is NewsActionState.ErrorState -> previousState.copy(
                error = true
            )
            is NewsActionState.FinishState -> {
                previousState.copy(isPageLoading = false)
            }
            is NewsActionState.UpdateDb -> {
                previousState.copy(updateDb = true)
            }
        }

    }

}
