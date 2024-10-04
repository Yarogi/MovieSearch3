package com.practicum.mymovies.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_table")
data class MovieEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "result_type", typeAffinity = ColumnInfo.TEXT)
    val resultType: String,
    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.TEXT)
    val image: String,
    @ColumnInfo(name = "title", typeAffinity = ColumnInfo.TEXT)
    val title: String,
    @ColumnInfo(name = "description", typeAffinity = ColumnInfo.TEXT)
    val description: String,
)