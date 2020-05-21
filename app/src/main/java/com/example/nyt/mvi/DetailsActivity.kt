package com.example.nyt.mvi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nyt.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_detail)

        val imageUrl = intent.getStringExtra("IMAGE_URL")
        val title = intent.getStringExtra("TITLE")
        val date = intent.getStringExtra("DATE")
        val abstract = intent.getStringExtra("ABSTRACT")
        val link = intent.getStringExtra("LINK")
        val author = intent.getStringExtra("AUTHOR")

        Picasso.get().load(imageUrl).into(detailsImageView)
        titleTextView.text = title
        publishdateTextView.text = date
        abstractTextView.text = abstract
        linkTextView.text = link
        authorTextView.text = author

    }
}
