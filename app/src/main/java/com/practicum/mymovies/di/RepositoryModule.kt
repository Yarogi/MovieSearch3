package com.practicum.mymovies.di

import com.practicum.mymovies.data.MoviesRepositoryImpl
import com.practicum.mymovies.data.SearchHistoryRepositoryImpl
import com.practicum.mymovies.data.converters.MovieCastConverter
import com.practicum.mymovies.domain.api.MoviesRepository
import com.practicum.mymovies.domain.api.SearchHistoryRepository
import org.koin.dsl.module

val repositoryModule = module {

    factory { MovieCastConverter() }

    single<MoviesRepository> {
        MoviesRepositoryImpl(
            networkClient = get(),
            movieCastConverter = get(),
            localStorage =  get(),
        )
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl()
    }

}