package com.jacob.wakatimeapp.details.data

import com.jacob.wakatimeapp.core.common.data.dtos.ProjectDetailsDTO
import com.jacob.wakatimeapp.details.data.dtos.DetailedProjectStatsDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

internal interface ProjectDetailsPageAPI {
    @GET("/api/v1/users/current/projects")
    suspend fun getProjectDetails(
        @Header("Authorization") token: String,
        @Query("q") projectName: String,
    ): Response<ProjectDetailsDTO>

    @GET("/api/v1/users/current/summaries")
    suspend fun getStatsForProject(
        @Header("Authorization") token: String,
        @Query("start") start: String,
        @Query("end") end: String,
        @Query("project") projectName: String,
    ): Response<DetailedProjectStatsDTO>
}
