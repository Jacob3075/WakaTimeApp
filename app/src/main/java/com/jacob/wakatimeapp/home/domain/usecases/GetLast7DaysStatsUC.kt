package com.jacob.wakatimeapp.home.domain.usecases

import com.jacob.wakatimeapp.core.models.ErrorTypes
import com.jacob.wakatimeapp.core.models.Result
import com.jacob.wakatimeapp.home.data.HomePageAPI
import com.jacob.wakatimeapp.home.data.mappers.GetWeeklyStatsResMapper
import com.jacob.wakatimeapp.home.domain.models.WeeklyStats
import timber.log.Timber
import javax.inject.Inject

class GetLast7DaysStatsUC @Inject constructor(
    private val homePageAPI: HomePageAPI,
    private val getWeeklyStatsResMapper: GetWeeklyStatsResMapper,
) {
    suspend operator fun invoke(token: String): Result<WeeklyStats> {
        try {
            val statsForTodayResponse = homePageAPI.getLast7DaysStats("Bearer $token")
            if (!statsForTodayResponse.isSuccessful) return Result.Failure(
                ErrorTypes.NetworkError(Exception(statsForTodayResponse.message()))
            )

            val weeklyStats =
                statsForTodayResponse.body()!!.run(getWeeklyStatsResMapper::fromDtoToModel)

            return Result.Success(weeklyStats)
        } catch (exception: Exception) {
            Timber.e(exception.message)
            return Result.Failure(ErrorTypes.NetworkError(exception))
        }
    }
}
