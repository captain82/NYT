package com.example.nyt.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class NewsResponseModel(
    @SerializedName("status")
    val status: String?,
    @PrimaryKey
    @SerializedName("section")
    val section: String,
    @SerializedName("results")
    val results: List<Results>?
) {
    data class Results(
        @SerializedName("title")
        val title: String?,
        @SerializedName("abstract")
        val abstract: String?,
        @SerializedName("byline")
        val author: String?,
        @SerializedName("published_date")
        val publishDate: String?,
        @SerializedName("url")
        val webUrl: String?,
        @SerializedName("multimedia")
        val multimedia: List<Multimedia>?
    )

    data class Multimedia(
        @SerializedName("url")
        val imageUrl: String?,
        @SerializedName("format")
        val format: String?
    )
}