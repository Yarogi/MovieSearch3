package com.practicum.mymovies.data

import android.util.Log
import com.practicum.mymovies.data.converters.MovieCastConverter
import com.practicum.mymovies.data.converters.MovieDbConverter
import com.practicum.mymovies.data.db.AppDatabase
import com.practicum.mymovies.data.dto.MovieDetailsResponse
import com.practicum.mymovies.data.dto.MovieDetailsRequest
import com.practicum.mymovies.data.dto.MovieCastRequest
import com.practicum.mymovies.data.dto.MovieCastResponse
import com.practicum.mymovies.data.dto.MovieDto
import com.practicum.mymovies.data.dto.MoviesSearchRequest
import com.practicum.mymovies.data.dto.MoviesSearchResponse
import com.practicum.mymovies.di.repositoryModule
import com.practicum.mymovies.domain.api.MoviesRepository
import com.practicum.mymovies.domain.models.Movie
import com.practicum.mymovies.domain.models.MovieDetails
import com.practicum.mymovies.domain.models.MovieCast
import com.practicum.mymovies.util.LocalStorage
import com.practicum.mymovies.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Appendable

class MoviesRepositoryImpl(
    private val networkClient: NetworkClient,
    private val movieCastConverter: MovieCastConverter,
    private val localStorage: LocalStorage,
    //Зависимости db
    private val appDatabase: AppDatabase,
    private val movieDbConvertor: MovieDbConverter,
) : MoviesRepository {

    override fun searchMovies(expression: String): Flow<Resource<List<Movie>>> = flow {

        val response = networkClient.doRequestSuspend(MoviesSearchRequest(expression))

        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }

            200 -> {
                val stored = localStorage.getSavedFavorites()
                with(response as MoviesSearchResponse) {
                    val data = results.map {
                        Movie(
                            it.id,
                            it.resultType,
                            it.image,
                            it.title,
                            it.description,
                            stored.contains(it.id)
                        )
                    }

                    //сохраняем список фильмов в базу данных
                    saveMovies(results)
                    emit(Resource.Success(data))

                }
            }

            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }

    }

    private suspend fun saveMovies(movies: List<MovieDto>) {
        appDatabase.movieDao().insertMovies(
            movies.map { movie -> movieDbConvertor.map(movie) }
        )
    }

    override fun getMovieDetails(movieId: String): Flow<Resource<MovieDetails>> = flow {
        val response = networkClient.doRequestSuspend(MovieDetailsRequest(movieId))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }

            200 -> {
                with(response as MovieDetailsResponse) {
                    emit(
                        Resource.Success(
                            MovieDetails(
                                id, title, imDbRating, year,
                                countries, genres, directors, writers, stars, plot
                            )
                        )
                    )
                }
            }

            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }

    override fun getMovieCast(movieId: String): Flow<Resource<MovieCast>> = flow {
        val response = networkClient.doRequestSuspend(MovieCastRequest(movieId))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }

            200 -> {
                emit(
                    Resource.Success(
                        data = movieCastConverter.convert(response as MovieCastResponse)
                    )
                )
            }

            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }

    override fun addMovieToFavorites(movie: Movie) {
        localStorage.addToFavorites(movie.id)
    }

    override fun removeMovieFromFavorites(movie: Movie) {
        localStorage.removeFromFavorites(movie.id)
    }

}