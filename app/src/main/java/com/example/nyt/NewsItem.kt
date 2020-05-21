package com.example.nyt

import com.bumptech.glide.Glide
import com.example.nyt.model.NewsResponseModel
import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.news_recycler_item.view.*

class NewsItem(val newsItem: NewsResponseModel.Results) :Item(){
    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.authorTextView.text = newsItem.author
        viewHolder.itemView.titleTextView.text = newsItem.title

        Picasso.get().load(newsItem.multimedia?.get(3)?.imageUrl).into(viewHolder.itemView.imageView)

    }

    override fun getLayout(): Int = R.layout.news_recycler_item
}