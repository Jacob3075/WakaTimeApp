package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.Either
import arrow.core.computations.either
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.home.data.network.HomePageNetworkData
import com.jacob.wakatimeapp.home.domain.getLatestStreakInRange
import com.jacob.wakatimeapp.home.domain.models.StreakRange
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DateTimeUnit.DateBased
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import kotlinx.datetime.minus

@Singleton
internal class RecalculateLatestStreakUC @Inject constructor(
    private val homePageNetworkData: HomePageNetworkData,
) {
    suspend fun calculate(
        start: LocalDate,
        value: Int,
        unit: DateBased,
    ): Either<Error, StreakRange> = either {
        var nextStart = start
        var result = StreakRange.ZERO

        do {
            val end = nextStart.minus(value, unit)
            val count = end.daysUntil(nextStart) + 1

            val latest = getStatsInRange(nextStart, end).bind()
            result += latest

            nextStart = end.minus(1, DateTimeUnit.DAY)
        } while (latest.days == count)

        result
    }

    private suspend fun getStatsInRange(start: LocalDate, end: LocalDate) =
        homePageNetworkData.getStatsForRange(start.toString(), end.toString())
            .map { stats ->
                stats.dailyStats
                    .associate { it.date to it.timeSpent }
                    .getLatestStreakInRange()
            }
}
