package com.practicum.mymovies.data.converters

import com.practicum.mymovies.data.dto.NamesDto
import com.practicum.mymovies.data.dto.NamesSearchResponse
import com.practicum.mymovies.domain.models.Person

class NamesToPersonConverter {

    fun convert(response: NamesSearchResponse): List<Person> {
        return response.results.map { nameDto ->
            nameToPerson(nameDto)
        }
    }

    fun nameToPerson(namesDto: NamesDto): Person {
        return Person(
            id = namesDto.id,
            title = namesDto.title,
            description = namesDto.description,
            image = namesDto.image
        )
    }

}