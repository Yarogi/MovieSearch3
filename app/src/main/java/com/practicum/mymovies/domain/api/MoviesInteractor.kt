package com.practicum.mymovies.domain.api

import com.practicum.mymovies.domain.models.Movie
import com.practicum.mymovies.domain.models.MovieDetails
import com.practicum.mymovies.domain.models.MovieCast

interface MoviesInteractor {

    fun searchMovies(expression: String, consumer: MoviesSearchConsumer)
    fun getMovieDetails(movieId: String, consumer: MoviesDetailsConsumer)
    fun getMoviesCast(movieId: String, consumer: MovieCastConsumer)

    interface MoviesSearchConsumer {
        fun consume(foundMovies: List<Movie>?, errorMessage: String?)
    }

    interface MoviesDetailsConsumer {
        fun consume(movieDetails: MovieDetails?, errorMessage: String?)
    }

    interface MovieCastConsumer {
        fun  consume(movieCast: MovieCast?, errorMessage: String?)
    }

    fun addMovieToFavorites(movie: Movie)
    fun removeMovieFromFavorites(movie: Movie)
}