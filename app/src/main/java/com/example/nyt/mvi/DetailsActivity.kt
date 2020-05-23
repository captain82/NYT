package com.example.nyt.mvi

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.nyt.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail.*
import java.text.SimpleDateFormat
import java.util.*


class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_detail)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decor = window.decorView
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        val imageUrl = intent.getStringExtra("IMAGE_URL")
        val title = intent.getStringExtra("TITLE")
        var date = intent.getStringExtra("DATE")
        val abstract = intent.getStringExtra("ABSTRACT")
        val link = intent.getStringExtra("LINK")
        val author = intent.getStringExtra("AUTHOR")
        val section = intent.getStringExtra("SECTION")


        var spf = SimpleDateFormat("yyyy-MM-dd")
        val newDate: Date = spf.parse(date)
        spf = SimpleDateFormat("dd-MMM-yyyy")
        date = spf.format(newDate)
        println(date)

        Picasso.get().load(imageUrl).into(detailsImageView)
        titleTextView.text = title
        publishdateTextView.text = date
        abstractTextView.text = abstract
        linkTextView.text = "Link : ${link}"
        authorTextView.text = author

        toolbarTitle.text = section

        iconBack.setOnClickListener {
            onBackPressed()
        }



    }
}
