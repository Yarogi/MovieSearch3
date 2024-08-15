package com.practicum.mymovies.di

import com.practicum.mymovies.core.navigation.Router
import com.practicum.mymovies.core.navigation.impl.RouterImpl
import org.koin.dsl.module

val navigationModule = module {

    val router = RouterImpl()

    single<Router> {router}
    single { router.navigatorHolder }

}