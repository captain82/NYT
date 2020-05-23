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
import com.jakewharton.rxbinding2.view.RxView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_tech.*

/**
 * A simple [Fragment] subclass.
 */
class TechFragment : MviFragment<MainView, MainPresenter>(), MainView {
    private val section = Section()
    private val groupAdpater = GroupAdapter<ViewHolder>()
    private val localdb by lazy { AppDatabase.getDatabase(context!!) }
    private lateinit var presenter: MainPresenter

    private lateinit var progressDialog: ProgressDialog
    private var shouldFetch: Boolean = false

    lateinit var newsItem: NewsItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        groupAdpater.add(section)
        return inflater.inflate(R.layout.fragment_tech, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //progressDialog = ProgressDialog(context!!, "Loading...")

        swipeLayout.setOnRefreshListener {
            presenter.fetchData("Technology")
        }

        newsItem = NewsItem {
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


        recyclerView.adapter = groupAdpater
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

    }

    override fun createPresenter(): MainPresenter {
        presenter = MainPresenter(localdb!!)
        return presenter
    }

    override fun queryRoom(): Observable<String> {
        swipeLayout.isRefreshing = true

        return Observable.just("Technology")
        //return Observable.just("Technology")
    }

    override fun updatedb(): Observable<String> {
        return Observable.just("Technology")
    }

    override fun checkLive(): Observable<String> {
        return Observable.just("Technology")
    }

    override fun render(viewState: MainViewState) {
        when {
            viewState.updateDb -> {
                Log.i("UPDATE", "DB")
            }
            viewState.isPageLoading -> {
                //progressDialog.showDialog()
            }

            viewState.newsObject != null -> {
                swipeLayout.isRefreshing = false
                inflateData(viewState.newsObject)
                ////progressDialog.dismissDialog()
            }

            viewState.error -> {
                //progressDialog.dismissDialog()

            }

        }
    }


    private fun inflateData(newsObject: NewsResponseModel?) {
        Log.i("count", section.itemCount.toString())

        if (section.itemCount == 0) {
            newsObject?.results?.forEach { newsItem ->
                section.add(this.newsItem)
                this.newsItem.bindData(newsItem)
                this.newsItem.notifyChanged()

            }
        } else {
            newsObject?.results?.forEach {
                this.newsItem.bindData(it)
                this.newsItem.notifyChanged()
            }
        }
    }
}
