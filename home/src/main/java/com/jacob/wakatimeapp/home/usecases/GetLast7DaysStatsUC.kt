package com.jacob.wakatimeapp.home.usecases

import com.jacob.wakatimeapp.core.models.ErrorTypes.NetworkError
import com.jacob.wakatimeapp.core.models.Result
import com.jacob.wakatimeapp.core.models.Result.Failure
import com.jacob.wakatimeapp.core.models.Result.Success
import com.jacob.wakatimeapp.core.models.WeeklyStats
import com.jacob.wakatimeapp.home.data.HomePageNetworkData
import com.jacob.wakatimeapp.home.data.dtos.GetLast7DaysStatsResDTO
import com.jacob.wakatimeapp.home.data.mappers.toModel
import javax.inject.Inject
import timber.log.Timber

class GetLast7DaysStatsUC @Inject constructor(
    private val homePageNetworkData: HomePageNetworkData,
) {
    @Suppress("TooGenericExceptionCaught")
    suspend operator fun invoke(): Result<WeeklyStats> {
        try {
            val statsForTodayResponse = homePageNetworkData.getLast7DaysStats()
            if (!statsForTodayResponse.isSuccessful) return Failure(
                NetworkError(Exception(statsForTodayResponse.message()))
            )

            val weeklyStats =
                statsForTodayResponse.body()!!
                    .run(GetLast7DaysStatsResDTO::toModel)

            return Success(weeklyStats)
        } catch (exception: Exception) {
            Timber.e(exception.message)
            return Failure(NetworkError(exception))
        }
    }
}
