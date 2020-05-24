package com.example.nyt.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nyt.R
import com.example.nyt.model.NewsResponseModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.news_recycler_item.view.*

class NewsRecyclerAdapter(val itemClick: (NewsResponseModel.Results) -> Unit) : RecyclerView.Adapter<NewsRecyclerAdapter.ViewHolder>() {

    private var newsList: NewsResponseModel? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.news_recycler_item,
                parent,
                false
            )
        )
    }


    override fun getItemCount(): Int {

        if(newsList?.results!=null){
            return newsList?.results?.size!!
        }else
        {
            return 0
        }
    }

    fun bindData(newsList: NewsResponseModel) {
        this.newsList = newsList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        newsList.let {
            viewHolder.itemView.authorTextView.text = newsList?.results?.get(position)?.author
            viewHolder.itemView.titleTextView.text = newsList?.results?.get(position)?.title
            Picasso.get().load(newsList?.results?.get(position)?.multimedia?.get(3)?.imageUrl)
                .into(viewHolder.itemView.imageView)

            viewHolder.itemView.rootLayout.setOnClickListener {
                newsList?.results?.get(position)?.let { it1 -> itemClick.invoke(it1) }
            }
        }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}
