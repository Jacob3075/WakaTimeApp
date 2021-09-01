package com.jacob.data.repositories

import retrofit2.Response
import retrofit2.http.GET

interface WakaTimeAPI {
    @GET("")
    suspend fun login(): Response<Void>
}