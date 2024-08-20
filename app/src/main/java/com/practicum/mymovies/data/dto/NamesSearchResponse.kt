package com.practicum.mymovies.data.dto

class NamesSearchResponse(
    val errorMessage: String,
    val expression: String,
    val results: List<NamesDto>,
    val searchType: String,
) : Response()