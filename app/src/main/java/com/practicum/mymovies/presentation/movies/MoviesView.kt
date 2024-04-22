package com.practicum.mymovies.presentation.movies

import com.practicum.mymovies.domain.models.Movie
import com.practicum.mymovies.ui.movies.models.MovieState

interface MoviesView {
    fun render(state: MovieState)
    fun showToast(additionalMessage: String)
}