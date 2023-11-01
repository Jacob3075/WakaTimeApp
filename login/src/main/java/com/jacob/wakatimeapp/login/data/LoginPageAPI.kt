package com.jacob.wakatimeapp.login.data

import com.jacob.wakatimeapp.login.data.dtos.GetUserDetailsResDTO
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Streaming
import retrofit2.http.Url

internal interface LoginPageAPI {
    @GET("/api/v1/users/current")
    suspend fun getUserDetails(@Header("Authorization") token: String): Response<GetUserDetailsResDTO>

    @POST("/api/v1/users/current/data_dumps")
    suspend fun createExtract(
        @Header("Authorization") token: String,
        @Body body: com.jacob.wakatimeapp.login.data.dtos.CreateExtractReqDTO,
    ): Response<com.jacob.wakatimeapp.login.data.dtos.CreateExtractResDTO>

    @GET("/api/v1/users/current/data_dumps/{id}")
    suspend fun getExtractCreationProgress(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): Response<com.jacob.wakatimeapp.login.data.dtos.CreateExtractResDTO>

    @GET("/api/v1/users/current/data_dumps")
    suspend fun getCreatedExtracts(
        @Header("Authorization") token: String,
    ): Response<com.jacob.wakatimeapp.login.data.dtos.CreatedExtractResDTO>

    @GET()
    @Streaming
    suspend fun downloadExtract(
        @Url downloadUrl: String,
    ): Response<ResponseBody>

}
