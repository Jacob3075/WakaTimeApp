package com.jacob.wakatimeapp.home.data.network

import com.jacob.wakatimeapp.home.data.network.dtos.AllTimeDataDTO
import com.jacob.wakatimeapp.home.data.network.dtos.GetDailyStatsResDTO
import com.jacob.wakatimeapp.home.data.network.dtos.GetLast7DaysStatsResDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface HomePageAPI {
    @GET("/api/v1/users/current/all_time_since_today")
    suspend fun getData(@Header("Authorization") token: String): Response<AllTimeDataDTO>

    @GET("/api/v1/users/current/summaries?range=today")
    suspend fun getStatsForToday(@Header("Authorization") token: String): Response<GetDailyStatsResDTO>

    @GET("/api/v1/users/current/summaries?range=last_7_days")
    suspend fun getLast7DaysStats(@Header("Authorization") token: String): Response<GetLast7DaysStatsResDTO>
}
