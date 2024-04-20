package com.practicum.mymovies.presentation.movies

import com.practicum.mymovies.domain.models.Movie

interface MoviesView {
    fun showPlaceholderMessage(isVisible: Boolean)
    fun showMoviesList(isVisible: Boolean)
    fun showProgressBar(isVisible: Boolean)
    fun changePlaceholderText(newPlaceholderText: String)
    fun updateMoviesList(newMoviesList: List<Movie>)
    fun showMessage(message: String)
}