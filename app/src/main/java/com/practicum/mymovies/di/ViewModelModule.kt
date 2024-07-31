package com.practicum.mymovies.di

import com.practicum.mymovies.presentation.movies.MoviesSearchViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        MoviesSearchViewModel(
            moviesInteractor =  get(),
            application =  androidApplication())
    }

}