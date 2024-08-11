package com.practicum.mymovies.domain.api

import com.practicum.mymovies.domain.models.Movie
import com.practicum.mymovies.domain.models.MovieDetails

interface MoviesInteractor {

    fun searchMovies(expression: String, consumer: MoviesSearchConsumer)
    fun getMovieDetails(movieId: String, consumer: MoviesDetailsConsumer)

    interface MoviesSearchConsumer {
        fun consume(foundMovies: List<Movie>?, errorMessage: String?)
    }

    interface MoviesDetailsConsumer {
        fun consume(movieDetails: MovieDetails?, errorMessage: String?)
    }

    fun addMovieToFavorites(movie: Movie)
    fun removeMovieFromFavorites(movie: Movie)
}