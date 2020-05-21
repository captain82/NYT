package com.example.nyt


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nyt.api.MainView
import com.example.nyt.model.NewsResponseModel
import com.example.nyt.mvi.MainViewState
import com.hannesdorfmann.mosby3.mvi.MviFragment
import com.jakewharton.rxbinding2.view.RxView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_science.*

/**
 * A simple [Fragment] subclass.
 */
class BuissenssFragment : MviFragment<MainView, MainPresenter>(), MainView {

    val section = Section()
    val groupAdpater = GroupAdapter<ViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buissenss, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView.adapter = groupAdpater
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    override fun createPresenter(): MainPresenter = MainPresenter()

    override fun showDetailNewsIntent(): Observable<String> {

        return RxView.clicks(button).map { click -> "business" }
            .doOnNext { Log.i("Click", it.toString()) }
    }

    override fun showInChrome(): Observable<Int> {

        return Observable.just(1)
    }

    override fun render(viewState: MainViewState) {
        when {
            viewState.isPageLoading == true -> {
                Log.i("viewState", "Loading")
            }
            viewState.isPullToRefresh!! -> {
                Log.i("viewState", "isPullToRefresh")
            }
            viewState.finished!! -> {
                Log.i("viewStateFinished", viewState.newsObject.toString())
            }
            viewState.newsObject != null -> {
                inflateData(viewState.newsObject)
                Log.i("viewStateObject", viewState.newsObject.toString())
            }
        }
    }

    private fun inflateData(newsObject: NewsResponseModel?) {
        newsObject?.results?.forEach { newsItem ->
            section.add(NewsItem(newsItem))
        }

        groupAdpater.add(section)
    }



}
