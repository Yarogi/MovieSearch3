package com.practicum.mymovies.data.db

import com.practicum.mymovies.data.converters.MovieDbConverter
import com.practicum.mymovies.data.db.entity.MovieEntity
import com.practicum.mymovies.domain.db.HistoryRepository
import com.practicum.mymovies.domain.models.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HistoryRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val movieDbConvertor: MovieDbConverter,
) : HistoryRepository {
    override fun historyMovies(): Flow<List<Movie>> = flow {
        val movies = appDatabase.movieDao().getMovies()
        emit(convertFromMovieEntity(movies))
    }

    private fun convertFromMovieEntity(movies: List<MovieEntity>): List<Movie> {
        return movies.map { movie -> movieDbConvertor.map(movie) }
    }

}