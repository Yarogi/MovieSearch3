package com.practicum.mymovies.data.converters

import com.practicum.mymovies.data.db.entity.MovieEntity
import com.practicum.mymovies.data.dto.MovieDto
import com.practicum.mymovies.domain.models.Movie

class MovieDbConverter {
    fun map(movie: MovieDto): MovieEntity {
        return MovieEntity(movie.id, movie.resultType, movie.image, movie.title, movie.description)
    }

    fun map(movie: MovieEntity): Movie {
        return Movie(movie.id, movie.resultType, movie.image, movie.title, movie.description)
    }
}