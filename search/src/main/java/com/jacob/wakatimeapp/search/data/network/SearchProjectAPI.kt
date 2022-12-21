package com.jacob.wakatimeapp.search.data.network

import com.jacob.wakatimeapp.core.common.data.dtos.ProjectDetailsDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

internal interface SearchProjectAPI {
    @GET("/api/v1/users/current/projects")
    suspend fun getAllProjects(
        @Header("Authorization") token: String,
        @Query("page") pageNumber: Int,
    ): Response<ProjectDetailsDTO>
}
