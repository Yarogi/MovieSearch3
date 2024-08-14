package com.practicum.mymovies.presentation.cast

import com.practicum.mymovies.core.ui.RVItem
import com.practicum.mymovies.domain.models.MovieCastPerson

sealed interface MoviesCastRVItem : RVItem {

    data class HeaderItem(
        val headerText: String,
    ) : MoviesCastRVItem

    data class PersonItem(
        val data: MovieCastPerson,
    ) : MoviesCastRVItem

}