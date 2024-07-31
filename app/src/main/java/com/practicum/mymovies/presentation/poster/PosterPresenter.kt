package com.practicum.mymovies.presentation.poster

class PosterPresenter(
    private val view: PosterView,
    private val imageUrl: String
) {

    fun onCreate() {
        view.setupPosterImage(imageUrl)
    }

}