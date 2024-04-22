package com.practicum.mymovies.ui.movies.models

import com.practicum.mymovies.domain.models.Movie

//data class MovieState(
//    val movies: List<Movie>,
//    val isLoading: Boolean,
//    val errorMessage: String?
//)

sealed interface MovieState {

    object Loading : MovieState

    data class Content(
        val movies: List<Movie>
    ) : MovieState

    data class Error(
        val errorMessage: String
    ) : MovieState

    data class Empty(
        val message: String
    ) : MovieState

}