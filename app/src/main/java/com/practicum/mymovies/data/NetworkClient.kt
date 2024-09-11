package com.practicum.mymovies.data

import com.practicum.mymovies.data.dto.Response

interface NetworkClient {

   suspend fun doRequestSuspend(dto: Any): Response

}