package com.example.nyt.Data.data.local

import androidx.room.*
import com.example.nyt.model.NewsResponseModel
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface ResponseDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(newsResponseModel: NewsResponseModel)

    @Query("DELETE FROM newsresponsemodel WHERE section LIKE :type")
    fun delete(type: String)

    @Query("SELECT * FROM newsresponsemodel WHERE section LIKE :type ")
    fun query(type: String): Observable<List<NewsResponseModel>>

    @Query("SELECT * FROM newsresponsemodel WHERE section LIKE :type ")
    fun queryObservable(type: String): Single<NewsResponseModel>

}