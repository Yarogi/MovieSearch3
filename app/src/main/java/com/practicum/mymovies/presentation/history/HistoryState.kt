package com.practicum.mymovies.presentation.history

import com.practicum.mymovies.domain.models.Movie

sealed interface HistoryState {
    object Loading : HistoryState

    data class Content(
        val movies: List<Movie>,
    ) : HistoryState

    data class Empty(
        val message: String,
    ) : HistoryState
}