package com.jacob.wakatimeapp.search.data.network

import arrow.core.Either
import com.jacob.wakatimeapp.core.common.auth.AuthTokenProvider
import com.jacob.wakatimeapp.core.common.data.BaseNetworkData
import com.jacob.wakatimeapp.core.common.data.dtos.ProjectDetailsDTO
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.search.data.network.mappers.toModelWithPageNumber
import com.jacob.wakatimeapp.search.domain.models.ProjectListWithPageNumber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class SearchProjectNetworkData @Inject constructor(
    authTokenProvider: AuthTokenProvider,
    private val searchProjectAPI: SearchProjectAPI,
) : BaseNetworkData(authTokenProvider) {
    suspend fun getProjects(pageNumber: Int): Either<Error, ProjectListWithPageNumber> =
        makeSafeApiCall(
            apiCall = { searchProjectAPI.getAllProjects(token = "Bearer $token", pageNumber) },
            methodName = ::getProjects.name,
        ).map(ProjectDetailsDTO::toModelWithPageNumber)
}
