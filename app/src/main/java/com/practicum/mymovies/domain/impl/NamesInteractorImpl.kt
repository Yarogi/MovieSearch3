package com.practicum.mymovies.domain.impl

import com.practicum.mymovies.domain.api.NamesInteractor
import com.practicum.mymovies.domain.api.NamesRepository
import com.practicum.mymovies.domain.models.Person
import com.practicum.mymovies.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NamesInteractorImpl(private val repository: NamesRepository) : NamesInteractor {
    override fun searchNames(expression: String): Flow<Pair<List<Person>?, String?>> {
        return repository.searchNames(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }
}