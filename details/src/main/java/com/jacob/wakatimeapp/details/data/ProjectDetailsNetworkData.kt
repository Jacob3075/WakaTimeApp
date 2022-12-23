package com.jacob.wakatimeapp.details.data

import arrow.core.Either
import com.jacob.wakatimeapp.core.common.auth.AuthTokenProvider
import com.jacob.wakatimeapp.core.common.data.BaseNetworkData
import com.jacob.wakatimeapp.core.common.data.dtos.ProjectDetailsDTO
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.details.data.mappers.toModel
import com.jacob.wakatimeapp.details.domain.models.ProjectDetails
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ProjectDetailsNetworkData @Inject constructor(
    authTokenProvider: AuthTokenProvider,
    private val projectDetailsPageAPI: ProjectDetailsPageAPI,
) : BaseNetworkData(authTokenProvider) {
    suspend fun getDetailsForProject(projectName: String): Either<Error, ProjectDetails> =
        makeSafeApiCall(
            apiCall = {
                projectDetailsPageAPI.getProjectDetails(
                    token = "Bearer $token",
                    projectName = projectName
                )
            },
            methodName = ::getDetailsForProject.name,
        ).map(ProjectDetailsDTO::toModel)
}
