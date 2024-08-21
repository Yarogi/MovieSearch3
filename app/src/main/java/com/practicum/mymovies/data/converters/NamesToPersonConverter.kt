package com.practicum.mymovies.data.converters

import com.practicum.mymovies.data.dto.PersonDto
import com.practicum.mymovies.data.dto.NamesSearchResponse
import com.practicum.mymovies.domain.models.Person

class NamesToPersonConverter {

    fun convert(response: NamesSearchResponse): List<Person> {
        return response.results.map { nameDto ->
            nameToPerson(nameDto)
        }
    }

    fun nameToPerson(namesDto: PersonDto): Person {
        return Person(
            id = namesDto.id,
            title = namesDto.title,
            description = namesDto.description,
            photoUrl = namesDto.image
        )
    }

}