package com.practicum.mymovies.presentation.movies

import com.practicum.mymovies.core.ui.RVItem
import com.practicum.mymovies.domain.models.Movie

sealed class MovieRVItem : RVItem {
    data class MovieItem(val movie: Movie) : RVItem
}