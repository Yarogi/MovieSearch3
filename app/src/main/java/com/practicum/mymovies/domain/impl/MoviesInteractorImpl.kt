package com.practicum.mymovies.domain.impl

import com.practicum.mymovies.domain.api.MoviesInteractor
import com.practicum.mymovies.domain.api.MoviesRepository
import com.practicum.mymovies.domain.models.Movie
import com.practicum.mymovies.util.Resource
import java.util.concurrent.Executors

class MoviesInteractorImpl(private val repository: MoviesRepository) : MoviesInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchMovies(expression: String, consumer: MoviesInteractor.MoviesSearchConsumer) {
        executor.execute {
            when (val resource = repository.searchMovies(expression)) {
                is Resource.Success -> {
                    consumer.consume(resource.data, null)
                }

                is Resource.Error -> {
                    consumer.consume(null, resource.message)
                }
            }
        }
    }

    override fun getMovieDetails(
        movieId: String,
        consumer: MoviesInteractor.MoviesDetailsConsumer
    ) {
        executor.execute {
            when (val resource = repository.getMovieDetails(movieId)) {
                is Resource.Success -> {
                    consumer.consume(resource.data, null)
                }

                is Resource.Error -> {
                    consumer.consume(null, resource.message)
                }
            }
        }
    }

    override fun addMovieToFavorites(movie: Movie) {
        repository.addMovieToFavorites(movie)
    }

    override fun removeMovieFromFavorites(movie: Movie) {
        repository.removeMovieFromFavorites(movie)
    }

}