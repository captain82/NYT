package com.example.nyt.Local

import android.content.Context
import androidx.room.*
import com.example.nyt.model.NewsResponseModel

@Database(entities = [NewsResponseModel::class], version = 1)
@TypeConverters(ListTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun responseDao(): ResponseDAO

    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, "responseDB"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}