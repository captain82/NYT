package com.example.nyt.Local

import androidx.room.TypeConverter
import com.example.nyt.model.NewsResponseModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListTypeConverter {

    @TypeConverter
    fun fromString(value: String): List<NewsResponseModel.Results> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromListLisr(list: List<NewsResponseModel.Results>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromMultimediaString(value: String): List<NewsResponseModel.Multimedia> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toMultimediaString(list: List<NewsResponseModel.Multimedia>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

}