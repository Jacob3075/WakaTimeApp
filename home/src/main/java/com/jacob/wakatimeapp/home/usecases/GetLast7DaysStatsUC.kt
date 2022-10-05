package com.jacob.wakatimeapp.home.usecases

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.Error.NetworkErrors
import com.jacob.wakatimeapp.core.models.WeeklyStats
import com.jacob.wakatimeapp.home.data.HomePageNetworkData
import com.jacob.wakatimeapp.home.data.dtos.GetLast7DaysStatsResDTO
import com.jacob.wakatimeapp.home.data.mappers.toModel
import javax.inject.Inject
import timber.log.Timber

class GetLast7DaysStatsUC @Inject constructor(
    private val homePageNetworkData: HomePageNetworkData,
) {
    suspend operator fun invoke(): Either<Error, WeeklyStats> = Either.catchAndFlatten {
        val statsForTodayResponse = homePageNetworkData.getLast7DaysStats()

        if (!statsForTodayResponse.isSuccessful) {
            return@catchAndFlatten NetworkErrors.create(
                statsForTodayResponse.message(),
                statsForTodayResponse.code()
            )
                .left()
        }

        val weeklyStats = statsForTodayResponse.body()!!
            .run(GetLast7DaysStatsResDTO::toModel)

        weeklyStats.right()
    }
        .mapLeft {
            Timber.e(it.message)
            NetworkErrors.create(it.message!!)
        }
}
