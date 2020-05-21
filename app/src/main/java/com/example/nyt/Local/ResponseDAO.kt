package com.example.nyt.Local

import androidx.room.*
import com.example.nyt.model.NewsResponseModel
import io.reactivex.Maybe

@Dao
interface ResponseDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(newsResponseModel: NewsResponseModel)

    @Delete
    fun delete(newsResponseModel: NewsResponseModel)

    @Query("SELECT * FROM newsresponsemodel WHERE section LIKE :type ")
    fun query(type: String): Maybe<NewsResponseModel>

}