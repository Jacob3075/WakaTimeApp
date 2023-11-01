package com.jacob.wakatimeapp.home.data.network

import arrow.core.Either
import com.jacob.wakatimeapp.core.common.auth.AuthTokenProvider
import com.jacob.wakatimeapp.core.common.data.BaseNetworkData
import com.jacob.wakatimeapp.core.models.DailyStats
import com.jacob.wakatimeapp.core.models.DailyStatsAggregate
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.WeeklyStats
import com.jacob.wakatimeapp.home.data.network.dtos.GetDailyStatsResDTO
import com.jacob.wakatimeapp.home.data.network.dtos.GetLast7DaysStatsResDTO
import com.jacob.wakatimeapp.home.data.network.dtos.GetStatsForRangeResDTO
import com.jacob.wakatimeapp.home.data.network.mappers.toModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class HomePageNetworkData @Inject constructor(
    authTokenProvider: AuthTokenProvider,
    private val homePageAPI: HomePageAPI,
) : BaseNetworkData(authTokenProvider) {
    suspend fun getLast7DaysStats(): Either<Error, WeeklyStats> = makeSafeApiCall(
        apiCall = { homePageAPI.getLast7DaysStats(it) },
        methodName = ::getLast7DaysStats.name,
    ).map(GetLast7DaysStatsResDTO::toModel)

    suspend fun getStatsForToday(): Either<Error, DailyStats> = makeSafeApiCall(
        apiCall = { homePageAPI.getStatsForToday(it) },
        methodName = ::getStatsForToday.name,
    ).map(GetDailyStatsResDTO::toModel)

    suspend fun getStatsForRange(start: String, end: String): Either<Error, DailyStatsAggregate> =
        makeSafeApiCall(
            apiCall = {
                homePageAPI.getStatsForRange(
                    it,
                    start = start,
                    end = end,
                )
            },
            methodName = ::getStatsForRange.name,
        ).map(GetStatsForRangeResDTO::toModel)
}
