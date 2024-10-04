package com.practicum.mymovies.domain.impl

import com.practicum.mymovies.domain.api.HistoryInteractor
import com.practicum.mymovies.domain.db.HistoryRepository
import com.practicum.mymovies.domain.models.Movie
import kotlinx.coroutines.flow.Flow

class HistoryInteractorImpl(private val historyRepository: HistoryRepository) : HistoryInteractor {
    override fun historyMovies(): Flow<List<Movie>> {
        return historyRepository.historyMovies()
    }

}