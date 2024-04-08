package com.practicum.mymovies.domain.api

import com.practicum.mymovies.domain.models.Movie

interface MoviesRepository {
    fun searchMovies(expression: String): List<Movie>
}