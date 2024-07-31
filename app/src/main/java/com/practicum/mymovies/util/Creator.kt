package com.practicum.mymovies.util

import com.practicum.mymovies.presentation.poster.PosterPresenter
import com.practicum.mymovies.presentation.poster.PosterView

object Creator {

    fun providePosterPresenter(
        posterView: PosterView,
        imageUrl: String
    ): PosterPresenter {
        return PosterPresenter(
            view = posterView,
            imageUrl = imageUrl
        )
    }

}