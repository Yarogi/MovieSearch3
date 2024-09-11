package com.practicum.mymovies.data

import com.practicum.mymovies.data.dto.Response

interface NetworkClient {

   fun doRequest(dto: Any): Response
   suspend fun doRequestSuspend(dto: Any): Response

}