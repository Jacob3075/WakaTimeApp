package com.jacob.wakatimeapp.projectlist.data.network

import com.jacob.wakatimeapp.core.models.Project
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

internal interface ProjectListAPI {
    @GET("/api/v1/users/current/projects")
    suspend fun getAllProjects(
        @Header("Authorization") token: String,
        @Query("page") pageNumber: Int,
    ): Response<List<Project>>
}
