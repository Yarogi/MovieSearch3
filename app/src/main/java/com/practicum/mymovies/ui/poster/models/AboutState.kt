package com.practicum.mymovies.ui.poster.models

import com.practicum.mymovies.domain.models.MovieDetails

sealed interface AboutState {

    data class Content(
        val movie: MovieDetails
    ) : AboutState

    data class Error(
        val message: String
    ) : AboutState

}