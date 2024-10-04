package com.practicum.mymovies.di

import com.practicum.mymovies.data.MoviesRepositoryImpl
import com.practicum.mymovies.data.NamesRepositoryImpl
import com.practicum.mymovies.data.SearchHistoryRepositoryImpl
import com.practicum.mymovies.data.converters.MovieCastConverter
import com.practicum.mymovies.data.converters.MovieDbConverter
import com.practicum.mymovies.data.converters.NamesToPersonConverter
import com.practicum.mymovies.data.db.HistoryRepositoryImpl
import com.practicum.mymovies.domain.api.MoviesRepository
import com.practicum.mymovies.domain.api.NamesRepository
import com.practicum.mymovies.domain.api.SearchHistoryRepository
import com.practicum.mymovies.domain.db.HistoryRepository
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
            appDatabase = get(),
            movieDbConvertor = get()
        )
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl()
    }

    single<HistoryRepository> {
        HistoryRepositoryImpl(appDatabase = get(), movieDbConvertor = get())
    }

    single<NamesRepository> {
        NamesRepositoryImpl(
            networkClient = get(),
            converter = get()
        )
    }


}