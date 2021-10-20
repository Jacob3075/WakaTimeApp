package com.jacob.wakatimeapp.repositories

import com.jacob.wakatimeapp.dtos.AllTimeDataDTO
import com.jacob.wakatimeapp.dtos.GetUserDetailsResDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface WakaTimeAPI {
    @GET("/api/v1/users/current/all_time_since_today")
    suspend fun getData(@Header("Authorization") token: String): Response<AllTimeDataDTO>

    @GET("/api/v1/users/current")
    suspend fun getUserDetails(@Header("Authorization") token: String): Response<GetUserDetailsResDTO>
}