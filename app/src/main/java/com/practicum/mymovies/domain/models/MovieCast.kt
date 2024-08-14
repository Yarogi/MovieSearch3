package com.practicum.mymovies.domain.models

class MovieCast(
    val iMDbId: String,
    val fullTitle: String,
    val directors: List<MovieCastPerson>,
    val writers: List<MovieCastPerson>,
    val actors: List<MovieCastPerson>,
    val others: List<MovieCastPerson>,
)

data class MovieCastPerson(
    val id: String,
    val name: String,
    val description: String,
    val image: String?,
)