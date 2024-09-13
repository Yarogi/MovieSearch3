package com.practicum.mymovies.di

import com.practicum.mymovies.data.MoviesRepositoryImpl
import com.practicum.mymovies.data.NamesRepositoryImpl
import com.practicum.mymovies.data.SearchHistoryRepositoryImpl
import com.practicum.mymovies.data.converters.MovieCastConverter
import com.practicum.mymovies.data.converters.MovieDbConverter
import com.practicum.mymovies.data.converters.NamesToPersonConverter
import com.practicum.mymovies.domain.api.MoviesRepository
import com.practicum.mymovies.domain.api.NamesRepository
import com.practicum.mymovies.domain.api.SearchHistoryRepository
import org.koin.dsl.module

val repositoryModule = module {

    factory { MovieCastConverter() }

    factory { NamesToPersonConverter() }

    factory { MovieDbConverter() }

    single<MoviesRepository> {
        MoviesRepositoryImpl(
            networkClient = get(),
            movieCastConverter = get(),
            localStorage = get(),
        )
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl()
    }

    single<NamesRepository> {
        NamesRepositoryImpl(
            networkClient = get(),
            converter = get()
        )
    }


}