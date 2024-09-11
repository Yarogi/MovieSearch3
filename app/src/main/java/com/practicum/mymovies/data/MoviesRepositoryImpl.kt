package com.practicum.mymovies.data

import com.practicum.mymovies.data.converters.MovieCastConverter
import com.practicum.mymovies.data.dto.MovieDetailsResponse
import com.practicum.mymovies.data.dto.MovieDetailsRequest
import com.practicum.mymovies.data.dto.MovieCastRequest
import com.practicum.mymovies.data.dto.MovieCastResponse
import com.practicum.mymovies.data.dto.MoviesSearchRequest
import com.practicum.mymovies.data.dto.MoviesSearchResponse
import com.practicum.mymovies.domain.api.MoviesRepository
import com.practicum.mymovies.domain.models.Movie
import com.practicum.mymovies.domain.models.MovieDetails
import com.practicum.mymovies.domain.models.MovieCast
import com.practicum.mymovies.util.LocalStorage
import com.practicum.mymovies.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MoviesRepositoryImpl(
    private val networkClient: NetworkClient,
    private val movieCastConverter: MovieCastConverter,
    private val localStorage: LocalStorage,
) : MoviesRepository {

    override fun searchMovies(expression: String): Flow<Resource<List<Movie>>>  = flow{
        val response = networkClient.doRequestSuspend(MoviesSearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }

            200 -> {
                val stored = localStorage.getSavedFavorites()

                Resource.Success((response as MoviesSearchResponse).results.map {
                    Movie(
                        id = it.id,
                        resultType = it.resultType,
                        image = it.image,
                        title = it.title,
                        description = it.description,
                        inFavorite = stored.contains(it.id),
                    )
                })
            }

            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }

    override fun getMovieDetails(movieId: String): Flow<Resource<MovieDetails>> = flow{
        val response = networkClient.doRequestSuspend(MovieDetailsRequest(movieId))
        when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }

            200 -> {
                with(response as MovieDetailsResponse) {
                    Resource.Success(
                        MovieDetails(
                            id, title, imDbRating, year,
                            countries, genres, directors, writers, stars, plot
                        )
                    )
                }
            }

            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }

    override fun getMovieCast(movieId: String): Flow<Resource<MovieCast>> = flow{
        val response = networkClient.doRequestSuspend(MovieCastRequest(movieId))
        when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }
            200 -> {
                    Resource.Success(
                        data = movieCastConverter.convert(response as MovieCastResponse)
                    )
            }
            else -> {
                Resource.Error("Ошибка сервера")
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