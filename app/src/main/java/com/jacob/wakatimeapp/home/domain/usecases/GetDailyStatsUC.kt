package com.jacob.wakatimeapp.home.domain.usecases

import com.jacob.wakatimeapp.common.models.ErrorTypes
import com.jacob.wakatimeapp.common.models.Result
import com.jacob.wakatimeapp.home.data.HomePageAPI
import com.jacob.wakatimeapp.home.data.mappers.GetDailyStatsResMapper
import com.jacob.wakatimeapp.home.domain.models.DailyStats
import javax.inject.Inject

class GetDailyStatsUC @Inject constructor(
    private val homePageAPI: HomePageAPI,
    private val getDailyStatsResMapper: GetDailyStatsResMapper,
) {
    suspend operator fun invoke(token: String): Result<DailyStats> {
        val statsForTodayResponse = homePageAPI.getStatsForToday("Bearer $token")
        if (!statsForTodayResponse.isSuccessful) return Result.Failure(
            ErrorTypes.NetworkError(
                Exception(statsForTodayResponse.message())
            )
        )

        val dailyStats = statsForTodayResponse.body()!!.run(getDailyStatsResMapper::fromDtoToModel)

        return Result.Success(dailyStats)
    }
}
