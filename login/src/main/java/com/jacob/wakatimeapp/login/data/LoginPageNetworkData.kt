package com.jacob.wakatimeapp.login.data

import arrow.core.Either
import com.jacob.wakatimeapp.core.common.auth.AuthTokenProvider
import com.jacob.wakatimeapp.core.common.data.BaseNetworkData
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.project.DetailedProjectStatsForDay
import com.jacob.wakatimeapp.login.data.dtos.CreateExtractReqDTO
import com.jacob.wakatimeapp.login.data.dtos.CreateExtractResDTO
import com.jacob.wakatimeapp.login.data.dtos.CreatedExtractResDTO
import com.jacob.wakatimeapp.login.data.dtos.GetStatsForRangeResDTO
import com.jacob.wakatimeapp.login.data.mappers.toDetailedProjectStatsForDayModel
import com.jacob.wakatimeapp.login.data.mappers.toModel
import com.jacob.wakatimeapp.login.domain.models.ExtractCreationProgress
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.datetime.LocalDate
import okhttp3.ResponseBody

@Singleton
internal class LoginPageNetworkData @Inject constructor(
    authTokenProvider: AuthTokenProvider,
    private val loginPageAPI: LoginPageAPI,
) : BaseNetworkData(authTokenProvider) {

    suspend fun createExtract(): Either<Error, ExtractCreationProgress> = makeSafeApiCall(
        apiCall = {
            loginPageAPI.createExtract(
                it,
                body = CreateExtractReqDTO("daily"),
            )
        },
        methodName = ::createExtract.name,
    ).map(CreateExtractResDTO::toModel)

    suspend fun getExtractCreationProgress(id: String): Either<Error, ExtractCreationProgress> =
        makeSafeApiCall(
            apiCall = {
                loginPageAPI.getExtractCreationProgress(
                    it,
                    id = id,
                )
            },
            methodName = ::getExtractCreationProgress.name,
        ).map(CreateExtractResDTO::toModel)

    suspend fun getCreatedExtracts(): Either<Error, List<ExtractCreationProgress>> =
        makeSafeApiCall(
            apiCall = { loginPageAPI.getCreatedExtracts(it) },
            methodName = ::getExtractCreationProgress.name,
        ).map(CreatedExtractResDTO::toModel)

    suspend fun downloadExtract(downloadUrl: String): Either<Error, ResponseBody> =
        makeSafeApiCall(
            apiCall = { loginPageAPI.downloadExtract(downloadUrl) },
            methodName = ::getExtractCreationProgress.name,
        )

    suspend fun getStatsForRange(start: LocalDate, end: LocalDate): Either<Error, GetStatsForRangeResDTO> =
        makeSafeApiCall(
            apiCall = {
                loginPageAPI.getStatsForRange(
                    it,
                    start = start.toString(),
                    end = end.toString(),
                )
            },
            methodName = ::getStatsForRange.name,
        )

    suspend fun getStatsForProject(
        projectName: String,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Either<Error, List<DetailedProjectStatsForDay>> = makeSafeApiCall(
        apiCall = {
            loginPageAPI.getStatsForProject(
                it,
                projectName = projectName,
                start = startDate.toString(),
                end = endDate.toString(),
            )
        },
        methodName = ::getStatsForProject.name,
    ).map { it.toDetailedProjectStatsForDayModel(projectName) }
}
