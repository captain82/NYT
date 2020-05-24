package com.example.nyt.Data.data.remote

import com.example.nyt.model.NewsResponseModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("v2/{section}.json")
    fun getTopNewsByCategory(
        @Path("section") section: String,
        @Query("api-key") apiKey: String
    ): Observable<NewsResponseModel>

}