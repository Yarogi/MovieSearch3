package com.practicum.mymovies.presentation.poster

import android.content.Context

class PosterPresenter(
    private val view: PosterView,
    private val imageUrl: String
    ) {



    fun onCreate() {
        view.setupPosterImage(imageUrl)
    }

}