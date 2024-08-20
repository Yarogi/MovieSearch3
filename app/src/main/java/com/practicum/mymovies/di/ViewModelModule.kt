package com.practicum.mymovies.di

import com.practicum.mymovies.presentation.details.AboutViewModel
import com.practicum.mymovies.presentation.movies.MoviesViewModel
import com.practicum.mymovies.presentation.cast.MovieCastViewModel
import com.practicum.mymovies.presentation.details.PosterViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        MoviesViewModel(
            moviesInteractor = get(),
            application = androidApplication()
        )
    }

    viewModel { (movieId: String) ->
        AboutViewModel(movieId, get())
    }

    viewModel { (posterUrl: String) ->
        PosterViewModel(posterUrl)
    }

    viewModel { (movieId: String) ->
        MovieCastViewModel(
            movieId = movieId,
            moviesInteractor = get()
        )
    }

}