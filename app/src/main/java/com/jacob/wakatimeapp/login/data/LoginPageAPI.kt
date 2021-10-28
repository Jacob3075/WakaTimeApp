package com.jacob.wakatimeapp.login.data

import com.jacob.wakatimeapp.login.data.dtos.GetUserDetailsResDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface LoginPageAPI {
    @GET("/api/v1/users/current")
    suspend fun getUserDetails(@Header("Authorization") token: String): Response<GetUserDetailsResDTO>
}
