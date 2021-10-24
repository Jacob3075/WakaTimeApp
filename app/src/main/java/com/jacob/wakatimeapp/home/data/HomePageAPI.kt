package com.jacob.wakatimeapp.home.data

import com.jacob.wakatimeapp.home.data.dtos.AllTimeDataDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface HomePageAPI {
    @GET("/api/v1/users/current/all_time_since_today")
    suspend fun getData(@Header("Authorization") token: String): Response<AllTimeDataDTO>
}