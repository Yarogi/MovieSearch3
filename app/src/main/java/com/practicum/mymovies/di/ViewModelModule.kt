package com.practicum.mymovies.di

import com.practicum.mymovies.presentation.details.AboutViewModel
import com.practicum.mymovies.presentation.movies.MoviesViewModel
import com.practicum.mymovies.presentation.cast.MovieCastViewModel
import com.practicum.mymovies.presentation.details.PosterViewModel
import com.practicum.mymovies.presentation.history.HistoryViewModel
import com.practicum.mymovies.presentation.names.NamesViewModel
import com.practicum.mymovies.presentation.player.PlayerViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
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

    viewModel {
        NamesViewModel(
            context = androidContext(),
            namesInteractor = get()
        )
    }

    viewModel {
        PlayerViewModel()
    }

    viewModel { HistoryViewModel(context = androidContext(), historyInteractor = get()) }

}