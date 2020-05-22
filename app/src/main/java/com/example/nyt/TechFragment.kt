package com.example.nyt

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nyt.Local.AppDatabase
import com.example.nyt.api.MainView
import com.example.nyt.model.NewsResponseModel
import com.example.nyt.mvi.DetailsActivity
import com.example.nyt.mvi.MainViewState
import com.hannesdorfmann.mosby3.mvi.MviFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_science.*

/**
 * A simple [Fragment] subclass.
 */
class TechFragment : MviFragment<MainView, MainPresenter>(), MainView {
    private val section = Section()
    private val groupAdpater = GroupAdapter<ViewHolder>()
    private val localdb by lazy { AppDatabase.getDatabase(context!!)  }
    private var responseModel: NewsResponseModel? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        groupAdpater.add(section)
        return inflater.inflate(R.layout.fragment_science, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView.adapter = groupAdpater



        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        getRoomData()
        inflateData(responseModel)
    }

    override fun createPresenter(): MainPresenter = MainPresenter(localdb!!)

    override fun showDetailNewsIntent(): Observable<String> {

        return if (responseModel != null) Observable.never() else Observable.never()
    }

    override fun queryRoom(): Observable<String> {
        return Observable.just("science")
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
                //inflateRoom(viewState.newsObject)
                Log.i("viewStateObject", viewState.newsObject.toString())
            }
        }
    }

    private fun inflateRoom(newsObject: NewsResponseModel?) {
        newsObject?.let {
            Completable.fromAction { localdb?.responseDao()?.insert(it) }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({
                    getRoomData()
                }, { t -> Log.i("Locally", t.localizedMessage) })
        }
    }

    private fun getRoomData() {
        localdb?.responseDao()?.query("science")
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribe({ t ->
                responseModel = t
                inflateData(t)
            }, {
                responseModel = null
            })
    }

    private fun inflateData(newsObject: NewsResponseModel?) {

        newsObject?.results?.forEach { newsItem ->
            section.add(NewsItem(newsItem) {
                val intent = Intent(activity, DetailsActivity::class.java)
                intent.putExtra("IMAGE_URL", newsItem.multimedia?.get(0)?.imageUrl)
                intent.putExtra("TITLE", newsItem.title)
                intent.putExtra("DATE", newsItem.publishDate)
                intent.putExtra("ABSTRACT", newsItem.abstract)
                intent.putExtra("LINK", newsItem.webUrl)
                intent.putExtra("AUTHOR", newsItem.author)
                startActivity(intent)
            })
        }
    }
}
