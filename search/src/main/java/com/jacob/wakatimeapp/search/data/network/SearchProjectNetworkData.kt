package com.jacob.wakatimeapp.search.data.network

import arrow.core.Either
import com.jacob.wakatimeapp.core.common.auth.AuthTokenProvider
import com.jacob.wakatimeapp.core.common.data.BaseNetworkData
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.search.data.network.dto.ProjectListDTO
import com.jacob.wakatimeapp.search.data.network.mappers.ProjectDetails
import com.jacob.wakatimeapp.search.data.network.mappers.toModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class SearchProjectNetworkData @Inject constructor(
    authTokenProvider: AuthTokenProvider,
    private val searchProjectAPI: SearchProjectAPI,
) : BaseNetworkData(authTokenProvider) {
    suspend fun getAllProjects(): Either<Error, List<ProjectDetails>> = makeSafeApiCall(
        apiCall = { searchProjectAPI.getAllProjects(token = "Bearer $token", pageNumber = 1) },
        methodName = ::getAllProjects.name,
    ).map(ProjectListDTO::toModel)
}
