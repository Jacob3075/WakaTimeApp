package com.jacob.wakatimeapp.home.domain.usecases

import com.jacob.wakatimeapp.core.models.ErrorTypes.NetworkError
import com.jacob.wakatimeapp.core.models.Result
import com.jacob.wakatimeapp.core.models.Result.Failure
import com.jacob.wakatimeapp.core.models.Result.Success
import com.jacob.wakatimeapp.core.models.WeeklyStats
import com.jacob.wakatimeapp.home.data.HomePageNetworkData
import com.jacob.wakatimeapp.core.data.mappers.GetWeeklyStatsResMapper
import timber.log.Timber
import javax.inject.Inject

class GetLast7DaysStatsUC @Inject constructor(
    private val getWeeklyStatsResMapper: GetWeeklyStatsResMapper,
    private val homePageNetworkData: HomePageNetworkData,
) {
    suspend operator fun invoke(): Result<WeeklyStats> {
        try {
            val statsForTodayResponse = homePageNetworkData.getLast7DaysStats()
            if (!statsForTodayResponse.isSuccessful) return Failure(
                NetworkError(Exception(statsForTodayResponse.message()))
            )

            val weeklyStats =
                statsForTodayResponse.body()!!.run(getWeeklyStatsResMapper::fromDtoToModel)

            return Success(weeklyStats)
        } catch (exception: Exception) {
            Timber.e(exception.message)
            return Failure(NetworkError(exception))
        }
    }
}
