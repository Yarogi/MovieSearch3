package com.practicum.mymovies.presentation.names

import com.practicum.mymovies.domain.models.Person

sealed interface NamesState {

    object Loading : NamesState
    data class Content(val persons: List<Person>) : NamesState
    data class Error(val message: String) : NamesState
    data class Empty(val message: String) : NamesState

}