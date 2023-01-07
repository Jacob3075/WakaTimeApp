package com.jacob.wakatimeapp.details.data

import arrow.core.Either
import com.jacob.wakatimeapp.core.common.auth.AuthTokenProvider
import com.jacob.wakatimeapp.core.common.data.BaseNetworkData
import com.jacob.wakatimeapp.core.common.data.dtos.ProjectDetailsDTO
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.details.data.dtos.DetailedProjectStatsDTO
import com.jacob.wakatimeapp.details.data.dtos.TotalTimeForProjectDTO
import com.jacob.wakatimeapp.details.data.mappers.toModel
import com.jacob.wakatimeapp.details.domain.models.ProjectDetails
import com.jacob.wakatimeapp.details.domain.models.ProjectStats
import com.jacob.wakatimeapp.details.domain.models.TotalProjectTime
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
                    projectName = projectName,
                )
            },
            methodName = ::getDetailsForProject.name,
        ).map(ProjectDetailsDTO::toModel)

    suspend fun getStatsForProject(
        projectName: String,
        startDate: String,
        endDate: String,
    ): Either<Error, ProjectStats> = makeSafeApiCall(
        apiCall = {
            projectDetailsPageAPI.getStatsForProject(
                "Bearer $token",
                projectName = projectName,
                start = startDate,
                end = endDate,
            )
        },
        methodName = ::getStatsForProject.name,
    ).map(DetailedProjectStatsDTO::toModel)

    suspend fun getTotalTimeForProject(name: String): Either<Error, TotalProjectTime> =
        makeSafeApiCall(
            apiCall = {
                projectDetailsPageAPI.getTotalTimeForProject(
                    "Bearer $token",
                    projectName = name,
                )
            },
            methodName = ::getTotalTimeForProject.name,
        ).map(TotalTimeForProjectDTO::toModel)
}
