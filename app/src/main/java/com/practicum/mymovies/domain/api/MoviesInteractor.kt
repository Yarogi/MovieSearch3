package com.practicum.mymovies.domain.api

import com.practicum.mymovies.domain.models.Movie

interface MoviesInteractor {

    fun searchMovies(expression: String, consumer: MoviesConsumer)

    interface MoviesConsumer {
        fun consume(foundMovies: List<Movie>?, errorMessage: String?)
    }

    fun addMovieToFavorites(movie: Movie)
    fun removeMovieFromFavorites(movie: Movie)

}