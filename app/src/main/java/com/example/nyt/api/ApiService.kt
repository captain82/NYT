package com.example.nyt.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("v2/{section}.json")
    fun getTopNewsByCategory(@Path("section") section: String,
                             @Query("api-key") apiKey: String)


}