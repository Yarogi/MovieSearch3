package com.practicum.mymovies.di

import android.content.Context
import com.google.gson.Gson
import com.practicum.mymovies.data.NetworkClient
import com.practicum.mymovies.data.SearchHistoryStorage
import com.practicum.mymovies.data.local.SharedPreferencesSearchHistoryStorage
import com.practicum.mymovies.data.network.IMDbApiService
import com.practicum.mymovies.data.network.RetrofitNetworkClient
import com.practicum.mymovies.util.LocalStorage
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<IMDbApiService> {
        Retrofit.Builder()
            .baseUrl("https:/tv-api.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IMDbApiService::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences("local_storage", Context.MODE_PRIVATE)
    }

    factory { Gson() }

    single<LocalStorage> {
        LocalStorage(
            sharedPreferences = get()
        )
    }

    single<SearchHistoryStorage> {
        SharedPreferencesSearchHistoryStorage(prefs = get(), gson = get())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(imdbService = get(), context = androidContext())
    }

}