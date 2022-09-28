package com.jacob.wakatimeapp.home.usecases

import com.jacob.wakatimeapp.core.models.DailyStats
import com.jacob.wakatimeapp.core.models.ErrorTypes
import com.jacob.wakatimeapp.core.models.Result
import com.jacob.wakatimeapp.home.data.HomePageNetworkData
import com.jacob.wakatimeapp.home.data.dtos.GetDailyStatsResDTO
import com.jacob.wakatimeapp.home.data.mappers.toModel
import javax.inject.Inject

class GetDailyStatsUC @Inject constructor(
    private val homePageNetworkData: HomePageNetworkData,
) {
    @Suppress("TooGenericExceptionCaught")
    suspend operator fun invoke(): Result<DailyStats> {
        try {
            val statsForTodayResponse = homePageNetworkData.getStatsForToday()
            if (!statsForTodayResponse.isSuccessful) return Result.Failure(
                ErrorTypes.NetworkError(
                    Exception(statsForTodayResponse.message())
                )
            )

            val dailyStats = statsForTodayResponse.body()!!
                .run(GetDailyStatsResDTO::toModel)

            return Result.Success(dailyStats)
        } catch (exception: Exception) {
            return Result.Failure(
                ErrorTypes.NetworkError(
                    exception
                )
            )
        }
    }
}
