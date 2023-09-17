package com.jacob.wakatimeapp.home.data.network

import com.jacob.wakatimeapp.home.data.network.dtos.AllTimeDataDTO
import com.jacob.wakatimeapp.home.data.network.dtos.CreateExtractReqDTO
import com.jacob.wakatimeapp.home.data.network.dtos.CreateExtractResDTO
import com.jacob.wakatimeapp.home.data.network.dtos.GetDailyStatsResDTO
import com.jacob.wakatimeapp.home.data.network.dtos.GetLast7DaysStatsResDTO
import com.jacob.wakatimeapp.home.data.network.dtos.GetStatsForRangeResDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

internal interface HomePageAPI {
    @GET("/api/v1/users/current/all_time_since_today")
    suspend fun getData(@Header("Authorization") token: String): Response<AllTimeDataDTO>

    @GET("/api/v1/users/current/summaries?range=today")
    suspend fun getStatsForToday(@Header("Authorization") token: String): Response<GetDailyStatsResDTO>

    @GET("/api/v1/users/current/summaries?range=last_7_days")
    suspend fun getLast7DaysStats(@Header("Authorization") token: String): Response<GetLast7DaysStatsResDTO>

    @GET("/api/v1/users/current/summaries")
    suspend fun getStatsForRange(
        @Header("Authorization") token: String,
        @Query("start") start: String,
        @Query("end") end: String,
    ): Response<GetStatsForRangeResDTO>

    @POST("/api/v1/users/current/summaries")
    suspend fun createExtract(
        @Header("Authorization") token: String,
        @Body body: CreateExtractReqDTO,
    ): Response<CreateExtractResDTO>

    @GET("/api/v1/users/current/summaries/{id}")
    suspend fun getExtractCreationProgress(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): Response<CreateExtractResDTO>
}
