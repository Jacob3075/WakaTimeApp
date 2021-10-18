package com.jacob.data.repositories

import com.jacob.data.dtos.AllTimeDataDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface WakaTimeAPI {
    @GET("/api/v1/users/current/all_time_since_today")
    suspend fun getData(@Header("Authorization") token: String): Response<AllTimeDataDTO>
}