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
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_tech.*

/**
 * A simple [Fragment] subclass.
 */
class ScienceFragment : MviFragment<MainView, MainPresenter>(), MainView {

    private val section = Section()
    private val groupAdpater = GroupAdapter<ViewHolder>()
    private val localdb by lazy { AppDatabase.getDatabase(context!!) }
    private lateinit var presenter: MainPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_science, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView.adapter = groupAdpater
        groupAdpater.add(section)

        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    override fun createPresenter(): MainPresenter {
        presenter = MainPresenter(localdb!!)
        return presenter
    }

    override fun queryRoom(): Observable<String> {
        return Observable.just("Science")
    }

    override fun render(viewState: MainViewState) {
        when {
            viewState.isPageLoading -> {
                //progressDialog.showDialog()
            }

            viewState.newsObject != null -> {
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
                section.add(NewsItem(newsItem) {
                    val intent = Intent(activity, DetailsActivity::class.java)
                    intent.putExtra("IMAGE_URL", newsItem.multimedia?.get(0)?.imageUrl)
                    intent.putExtra("TITLE", newsItem.title)
                    intent.putExtra("DATE", newsItem.publishDate)
                    intent.putExtra("ABSTRACT", newsItem.abstract)
                    intent.putExtra("LINK", newsItem.webUrl)
                    intent.putExtra("AUTHOR", newsItem.author)
                    intent.putExtra("SECTION", newsObject.section)

                    startActivity(intent)
                })
            }
        }
    }

}
