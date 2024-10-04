package com.practicum.mymovies.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.mymovies.data.db.dao.MovieDao
import com.practicum.mymovies.data.db.entity.MovieEntity

@Database(version = 2, entities = [MovieEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}