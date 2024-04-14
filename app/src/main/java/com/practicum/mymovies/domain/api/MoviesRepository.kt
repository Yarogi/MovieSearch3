package com.practicum.mymovies.domain.api

import com.practicum.mymovies.domain.models.Movie
import com.practicum.mymovies.util.Resource

interface MoviesRepository {
    fun searchMovies(expression: String): Resource<List<Movie>>
}