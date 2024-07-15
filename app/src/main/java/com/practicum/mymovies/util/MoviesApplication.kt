package com.practicum.mymovies.util

import android.app.Application
import com.practicum.mymovies.presentation.movies.MoviesSearchViewModel

class MoviesApplication : Application() {

    var moviesSearchViewModel: MoviesSearchViewModel? = null

}