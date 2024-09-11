package com.practicum.mymovies.data

import com.practicum.mymovies.data.converters.NamesToPersonConverter
import com.practicum.mymovies.data.dto.NamesSearchRequest
import com.practicum.mymovies.data.dto.NamesSearchResponse
import com.practicum.mymovies.domain.api.NamesRepository
import com.practicum.mymovies.domain.models.Person
import com.practicum.mymovies.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NamesRepositoryImpl(
    private val networkClient: NetworkClient,
    private val converter: NamesToPersonConverter,
) : NamesRepository {
    override fun searchNames(expression: String): Flow<Resource<List<Person>>> = flow {

        val response = networkClient.doRequest(NamesSearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }

            200 -> {
                Resource.Success(converter.convert(response as NamesSearchResponse))
            }

            else -> {
                Resource.Error("Ошибка сервера")
            }
        }

    }
}