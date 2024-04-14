package com.practicum.mymovies.util

import android.app.Activity
import android.content.Context
import com.practicum.mymovies.data.MoviesRepositoryImpl
import com.practicum.mymovies.data.network.RetrofitNetworkClient
import com.practicum.mymovies.domain.api.MoviesInteractor
import com.practicum.mymovies.domain.api.MoviesRepository
import com.practicum.mymovies.domain.impl.MoviesInteractorImpl
import com.practicum.mymovies.presentation.MoviesSearchController
import com.practicum.mymovies.presentation.PosterController
import com.practicum.mymovies.ui.movies.MoviesAdapter

object Creator {
    private fun getMoviesRepository(context: Context): MoviesRepository {
        return MoviesRepositoryImpl(RetrofitNetworkClient(context))
    }

    fun provideMoviesInteractor(context: Context): MoviesInteractor {
        return MoviesInteractorImpl(getMoviesRepository(context))
    }

    fun provideMoviesSearchController(
        activity: Activity,
        adapter: MoviesAdapter
    ): MoviesSearchController {
        return MoviesSearchController(activity, adapter)
    }

    fun providePosterController(activity: Activity): PosterController {
        return PosterController(activity)
    }
}