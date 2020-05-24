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
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.jakewharton.rxbinding2.view.RxView

import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_tech.*

/**
 * A simple [Fragment] subclass.
 */
class ScienceFragment : MviFragment<MainView, MainPresenter>(), MainView {
    private val localdb by lazy { AppDatabase.getDatabase(context!!) }
    private lateinit var presenter: MainPresenter
    private lateinit var adapter: NewsRecyclerAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_science, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = NewsRecyclerAdapter {
            val intent = Intent(activity, DetailsActivity::class.java)
            intent.putExtra("IMAGE_URL", it.multimedia?.get(0)?.imageUrl)
            intent.putExtra("TITLE", it.title)
            intent.putExtra("DATE", it.publishDate)
            intent.putExtra("ABSTRACT", it.abstract)
            intent.putExtra("LINK", it.webUrl)
            intent.putExtra("AUTHOR", it.author)
            intent.putExtra("SECTION", "Technology")
            startActivity(intent)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    override fun createPresenter(): MainPresenter {
        presenter = MainPresenter(localdb!!)
        return presenter
    }

    override fun checkLive(): Observable<String> {
        return Observable.just("Science")
    }

    override fun refreshData(): Observable<String> {
        return RxSwipeRefreshLayout.refreshes(swipeLayout)
            .map {"Science" }}


    override fun render(viewState: MainViewState) {
        when {
            viewState.updateDb -> {
                swipeLayout.isRefreshing = false
            }
            viewState.isPageLoading -> {
                swipeLayout.isRefreshing = true
            }

            viewState.newsObject != null -> {
                swipeLayout.isRefreshing = false
                inflateData(viewState.newsObject)
            }

            viewState.error -> {
                swipeLayout.isRefreshing = false
            }
        }
    }

    private fun inflateData(newsObject: NewsResponseModel?) {
        newsObject?.let { adapter.bindData(it) }
    }

}
