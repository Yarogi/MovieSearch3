package com.practicum.mymovies.domain.api

import com.practicum.mymovies.domain.models.Person

interface NamesInteractor {

    fun search(expression: String, consumer: NamesSearchConsumer)

    interface NamesSearchConsumer {
        fun consume(foundNames: List<Person>?, errorMessage: String?)
    }

}