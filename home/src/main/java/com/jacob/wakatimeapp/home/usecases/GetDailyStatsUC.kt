package com.jacob.wakatimeapp.home.usecases

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.jacob.wakatimeapp.core.models.DailyStats
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.Error.NetworkErrors
import com.jacob.wakatimeapp.home.data.HomePageNetworkData
import com.jacob.wakatimeapp.home.data.dtos.GetDailyStatsResDTO
import com.jacob.wakatimeapp.home.data.mappers.toModel
import javax.inject.Inject
import javax.inject.Singleton
import timber.log.Timber

@Singleton
class GetDailyStatsUC @Inject constructor(
    private val homePageNetworkData: HomePageNetworkData,
) {
    suspend operator fun invoke(): Either<Error, DailyStats> = Either.catchAndFlatten {
        val statsForTodayResponse = homePageNetworkData.getStatsForToday()
        if (!statsForTodayResponse.isSuccessful) return@catchAndFlatten NetworkErrors.create(
            statsForTodayResponse.message(),
            statsForTodayResponse.code()
        )
            .left()

        statsForTodayResponse.body()!!
            .run(GetDailyStatsResDTO::toModel)
            .right()
    }
        .mapLeft {
            Timber.e(it.message)
            NetworkErrors.create(it.message!!)
        }
}
