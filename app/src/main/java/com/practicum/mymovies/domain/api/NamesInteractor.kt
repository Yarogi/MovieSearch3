package com.practicum.mymovies.domain.api

import com.practicum.mymovies.domain.models.Person
import kotlinx.coroutines.flow.Flow

interface NamesInteractor {

    fun searchNames(
        expression: String,
    ): Flow<Pair<List<Person>?, String?>>

}