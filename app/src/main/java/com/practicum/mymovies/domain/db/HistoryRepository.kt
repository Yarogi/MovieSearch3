package com.practicum.mymovies.domain.db

import com.practicum.mymovies.domain.models.Movie
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun historyMovies(): Flow<List<Movie>>
}