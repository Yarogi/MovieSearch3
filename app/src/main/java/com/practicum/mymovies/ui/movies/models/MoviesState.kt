package com.practicum.mymovies.ui.movies.models

import com.practicum.mymovies.domain.models.Movie

//data class MovieState(
//    val movies: List<Movie>,
//    val isLoading: Boolean,
//    val errorMessage: String?
//)

sealed interface MoviesState {

    object Loading : MoviesState

    data class Content(
        val movies: List<Movie>
    ) : MoviesState

    data class Error(
        val message: String
    ) : MoviesState

    data class Empty(
        val message: String
    ) : MoviesState

}