package com.practicum.mymovies.util

import android.app.Application
import com.practicum.mymovies.presentation.movies.MoviesSearchPresenter

class MoviesApplication : Application() {

//    Шаг 1.4. Сохранить Presenter в Application
    var moviesSearchPresenter: MoviesSearchPresenter? = null

}