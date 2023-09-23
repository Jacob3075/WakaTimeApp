package com.jacob.wakatimeapp.home.data.network

import arrow.core.Either
import com.jacob.wakatimeapp.core.common.auth.AuthTokenProvider
import com.jacob.wakatimeapp.core.common.data.BaseNetworkData
import com.jacob.wakatimeapp.core.models.DailyStats
import com.jacob.wakatimeapp.core.models.DailyStatsAggregate
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.WeeklyStats
import com.jacob.wakatimeapp.home.data.network.dtos.AllTimeDataDTO
import com.jacob.wakatimeapp.home.data.network.dtos.CreateExtractReqDTO
import com.jacob.wakatimeapp.home.data.network.dtos.CreateExtractResDTO
import com.jacob.wakatimeapp.home.data.network.dtos.CreatedExtractResDTO
import com.jacob.wakatimeapp.home.data.network.dtos.GetDailyStatsResDTO
import com.jacob.wakatimeapp.home.data.network.dtos.GetLast7DaysStatsResDTO
import com.jacob.wakatimeapp.home.data.network.dtos.GetStatsForRangeResDTO
import com.jacob.wakatimeapp.home.data.network.mappers.toModel
import com.jacob.wakatimeapp.home.domain.models.ExtractCreationProgress
import javax.inject.Inject
import javax.inject.Singleton
import okhttp3.ResponseBody

@Singleton
internal class HomePageNetworkData @Inject constructor(
    authTokenProvider: AuthTokenProvider,
    private val homePageAPI: HomePageAPI,
) : BaseNetworkData(authTokenProvider) {
    suspend fun getLast7DaysStats(): Either<Error, WeeklyStats> = makeSafeApiCall(
        apiCall = { homePageAPI.getLast7DaysStats("Bearer $token") },
        methodName = ::getLast7DaysStats.name,
    ).map(GetLast7DaysStatsResDTO::toModel)

    suspend fun getStatsForToday(): Either<Error, DailyStats> = makeSafeApiCall(
        apiCall = { homePageAPI.getStatsForToday("Bearer $token") },
        methodName = ::getStatsForToday.name,
    ).map(GetDailyStatsResDTO::toModel)

    suspend fun getData(): Either<Error, String> = makeSafeApiCall(
        apiCall = { homePageAPI.getData("Bearer $token") },
        methodName = ::getData.name,
    ).map(AllTimeDataDTO::toString)

    suspend fun getStatsForRange(start: String, end: String): Either<Error, DailyStatsAggregate> =
        makeSafeApiCall(
            apiCall = {
                homePageAPI.getStatsForRange(
                    token = "Bearer $token",
                    start = start,
                    end = end,
                )
            },
            methodName = ::getStatsForRange.name,
        ).map(GetStatsForRangeResDTO::toModel)

    suspend fun createExtract(): Either<Error, ExtractCreationProgress> = makeSafeApiCall(
        apiCall = {
            homePageAPI.createExtract(
                "Bearer $token",
                body = CreateExtractReqDTO("daily"),
            )
        },
        methodName = ::createExtract.name,
    ).map(CreateExtractResDTO::toModel)

    suspend fun getExtractCreationProgress(id: String): Either<Error, ExtractCreationProgress> =
        makeSafeApiCall(
            apiCall = {
                homePageAPI.getExtractCreationProgress(
                    "Bearer $token",
                    id = id,
                )
            },
            methodName = ::getExtractCreationProgress.name,
        ).map(CreateExtractResDTO::toModel)

    suspend fun getCreatedExtracts(): Either<Error, List<ExtractCreationProgress>> =
        makeSafeApiCall(
            apiCall = { homePageAPI.getCreatedExtracts("Bearer $token") },
            methodName = ::getExtractCreationProgress.name,
        ).map(CreatedExtractResDTO::toModel)

    suspend fun downloadExtract(downloadUrl: String): Either<Error, ResponseBody> =
        makeSafeApiCall(
            apiCall = {
                homePageAPI.downloadExtract(
                    downloadUrl,
                )
            },
            methodName = ::getExtractCreationProgress.name,
        )

}
