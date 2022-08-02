package com.jacob.wakatimeapp.home.domain.usecases

import com.jacob.wakatimeapp.core.models.ErrorTypes
import com.jacob.wakatimeapp.core.models.Result
import com.jacob.wakatimeapp.home.data.HomePageNetworkData
import com.jacob.wakatimeapp.core.network.mappers.GetWeeklyStatsResMapper
import com.jacob.wakatimeapp.core.models.WeeklyStats
import timber.log.Timber
import javax.inject.Inject

class GetLast7DaysStatsUC @Inject constructor(
    private val getWeeklyStatsResMapper: GetWeeklyStatsResMapper,
    private val homePageNetworkData: HomePageNetworkData,
) {
    suspend operator fun invoke(): com.jacob.wakatimeapp.core.models.Result<com.jacob.wakatimeapp.core.models.WeeklyStats> {
        try {
            val statsForTodayResponse = homePageNetworkData.getLast7DaysStats()
            if (!statsForTodayResponse.isSuccessful) return com.jacob.wakatimeapp.core.models.Result.Failure(
                com.jacob.wakatimeapp.core.models.ErrorTypes.NetworkError(Exception(statsForTodayResponse.message()))
            )

            val weeklyStats =
                statsForTodayResponse.body()!!.run(getWeeklyStatsResMapper::fromDtoToModel)

            return com.jacob.wakatimeapp.core.models.Result.Success(weeklyStats)
        } catch (exception: Exception) {
            Timber.e(exception.message)
            return com.jacob.wakatimeapp.core.models.Result.Failure(com.jacob.wakatimeapp.core.models.ErrorTypes.NetworkError(exception))
        }
    }
}
