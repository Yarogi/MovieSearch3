package com.practicum.mymovies.domain.impl

import com.practicum.mymovies.domain.api.MoviesInteractor
import com.practicum.mymovies.domain.api.MoviesRepository
import com.practicum.mymovies.domain.models.Movie
import com.practicum.mymovies.domain.models.MovieCast
import com.practicum.mymovies.domain.models.MovieDetails
import com.practicum.mymovies.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.Executors

class MoviesInteractorImpl(private val repository: MoviesRepository) : MoviesInteractor {

    override fun searchMovies(expression: String): Flow<Pair<List<Movie>?, String?>> {

        return repository
            .searchMovies(expression)
            .map { result ->
                when (result) {
                    is Resource.Success -> Pair(result.data, null)
                    is Resource.Error -> Pair(null, result.message)
                }
            }

    }

    override fun getMovieDetails(
        movieId: String,
    ): Flow<Pair<MovieDetails?, String?>> {
        return repository.getMovieDetails(movieId)
            .map { result -> mapResult(result) }
    }

    override fun getMoviesCast(movieId: String): Flow<Pair<MovieCast?, String?>> {

        return repository.getMovieCast(movieId)
            .map { result -> mapResult(result) }

    }

    override fun addMovieToFavorites(movie: Movie) {
        repository.addMovieToFavorites(movie)
    }

    override fun removeMovieFromFavorites(movie: Movie) {
        repository.removeMovieFromFavorites(movie)
    }

    private fun <T> mapResult(result: Resource<T>): Pair<T?, String?> {
        return when (result) {
            is Resource.Success -> Pair(result.data, null)
            is Resource.Error -> Pair(null, result.message)
        }
    }

}