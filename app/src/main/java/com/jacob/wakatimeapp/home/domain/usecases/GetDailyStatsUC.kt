package com.jacob.wakatimeapp.home.domain.usecases

import com.jacob.wakatimeapp.core.models.ErrorTypes
import com.jacob.wakatimeapp.core.models.Result
import com.jacob.wakatimeapp.home.data.HomePageNetworkData
import com.jacob.wakatimeapp.core.network.mappers.GetDailyStatsResMapper
import com.jacob.wakatimeapp.core.models.DailyStats
import javax.inject.Inject

class GetDailyStatsUC @Inject constructor(
    private val homePageNetworkData: HomePageNetworkData,
    private val getDailyStatsResMapper: GetDailyStatsResMapper,
) {
    suspend operator fun invoke(): com.jacob.wakatimeapp.core.models.Result<com.jacob.wakatimeapp.core.models.DailyStats> {
        try {
            val statsForTodayResponse = homePageNetworkData.getStatsForToday()
            if (!statsForTodayResponse.isSuccessful) return com.jacob.wakatimeapp.core.models.Result.Failure(
                com.jacob.wakatimeapp.core.models.ErrorTypes.NetworkError(
                    Exception(statsForTodayResponse.message())
                )
            )

            val dailyStats =
                statsForTodayResponse.body()!!.run(getDailyStatsResMapper::fromDtoToModel)

            return com.jacob.wakatimeapp.core.models.Result.Success(dailyStats)
        } catch (exception: Exception) {
            return com.jacob.wakatimeapp.core.models.Result.Failure(com.jacob.wakatimeapp.core.models.ErrorTypes.NetworkError(exception))
        }
    }
}
