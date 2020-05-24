package com.example.nyt.ui


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nyt.Data.data.local.AppDatabase
import com.example.nyt.R
import com.example.nyt.mvi.MainView
import com.example.nyt.model.NewsResponseModel
import com.example.nyt.model.MainViewState
import com.example.nyt.mvi.MainPresenter
import com.hannesdorfmann.mosby3.mvi.MviFragment
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_tech.*

/**
 * A simple [Fragment] subclass.
 */
class BuissenssFragment : MviFragment<MainView, MainPresenter>(), MainView {

    private lateinit var recyclerAdapter: NewsRecyclerAdapter
    private val localdb by lazy { AppDatabase.getDatabase(context!!) }
    private lateinit var presenter: MainPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buissenss, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerAdapter = NewsRecyclerAdapter {
            val intent = Intent(activity, DetailsActivity::class.java)
            intent.putExtra("IMAGE_URL", it.multimedia?.find { it.format=="superJumbo" }?.imageUrl)
            intent.putExtra("TITLE", it.title)
            intent.putExtra("DATE", it.publishDate)
            intent.putExtra("ABSTRACT", it.abstract)
            intent.putExtra("LINK", it.webUrl)
            intent.putExtra("AUTHOR", it.author)
            intent.putExtra("SECTION", "Business")
            startActivity(intent)
        }

        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    override fun createPresenter(): MainPresenter {
        presenter = MainPresenter(localdb!!)
        return presenter
    }

    override fun refreshData(): Observable<String> {
        return RxSwipeRefreshLayout.refreshes(swipeLayout)
            .map { "Business" }
    }

    override fun queryRoom(): Observable<String> {
        return Observable.just("Business")
    }

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
        newsObject?.let { recyclerAdapter.bindData(it) }
    }
}
