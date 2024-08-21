package com.practicum.mymovies.domain.api

import com.practicum.mymovies.domain.models.Person
import com.practicum.mymovies.util.Resource

interface NamesRepository {

    fun searchNames(expression: String): Resource<List<Person>>

}