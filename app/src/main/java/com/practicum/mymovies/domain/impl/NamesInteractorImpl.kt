package com.practicum.mymovies.domain.impl

import com.practicum.mymovies.domain.api.NamesInteractor
import com.practicum.mymovies.domain.api.NamesRepository
import com.practicum.mymovies.util.Resource
import java.util.concurrent.Executors

class NamesInteractorImpl(private val repository: NamesRepository) : NamesInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun search(expression: String, consumer: NamesInteractor.NamesSearchConsumer) {
        executor.execute {
            when (val resource = repository.search(expression)) {
                is Resource.Error -> consumer.consume(null, resource.message)
                is Resource.Success -> consumer.consume(resource.data, null)
            }
        }
    }
}