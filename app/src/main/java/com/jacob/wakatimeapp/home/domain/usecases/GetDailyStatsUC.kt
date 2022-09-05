package com.jacob.wakatimeapp.home.domain.usecases

import com.jacob.wakatimeapp.core.models.DailyStats
import com.jacob.wakatimeapp.core.models.ErrorTypes
import com.jacob.wakatimeapp.core.models.Result
import com.jacob.wakatimeapp.core.network.HomePageNetworkData
import com.jacob.wakatimeapp.core.network.mappers.GetDailyStatsResMapper
import javax.inject.Inject

class GetDailyStatsUC @Inject constructor(
    private val homePageNetworkData: HomePageNetworkData,
    private val getDailyStatsResMapper: GetDailyStatsResMapper,
) {
    suspend operator fun invoke(): Result<DailyStats> {
        try {
            val statsForTodayResponse = homePageNetworkData.getStatsForToday()
            if (!statsForTodayResponse.isSuccessful) return Result.Failure(
                ErrorTypes.NetworkError(
                    Exception(statsForTodayResponse.message())
                )
            )

            val dailyStats =
                statsForTodayResponse.body()!!.run(getDailyStatsResMapper::fromDtoToModel)

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
