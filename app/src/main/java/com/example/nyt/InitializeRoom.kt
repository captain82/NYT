package com.example.nyt

import com.example.nyt.Local.AppDatabase

interface InitializeRoom {

    fun setRoomDatabase(db: AppDatabase?)

}