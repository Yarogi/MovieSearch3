package com.practicum.mymovies.domain.api

import com.practicum.mymovies.domain.models.Movie
import com.practicum.mymovies.domain.models.MovieDetails
import com.practicum.mymovies.domain.models.MovieCast
import kotlinx.coroutines.flow.Flow

interface MoviesInteractor {

    fun searchMovies(expression: String): Flow<Pair<List<Movie>?, String?>>
    fun getMovieDetails(movieId: String): Flow<Pair<MovieDetails?, String?>>
    fun getMoviesCast(movieId: String): Flow<Pair<MovieCast?, String?>>

    fun addMovieToFavorites(movie: Movie)
    fun removeMovieFromFavorites(movie: Movie)
}