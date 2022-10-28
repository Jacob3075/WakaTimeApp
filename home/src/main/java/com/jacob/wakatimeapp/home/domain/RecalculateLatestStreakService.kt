package com.jacob.wakatimeapp.home.domain

import arrow.core.Either
import arrow.core.computations.either
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.home.data.network.HomePageNetworkData
import com.jacob.wakatimeapp.home.domain.models.StreakRange
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DateTimeUnit.DateBased
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import kotlinx.datetime.minus

@Singleton
class RecalculateLatestStreakService @Inject constructor(
    private val homePageNetworkData: HomePageNetworkData,
) {
    suspend fun calculate(
        start: LocalDate,
        value: Int,
        unit: DateBased,
    ): Either<Error, StreakRange> = either {
        val end = start.minus(value, unit)
        val count = end.daysUntil(start) + 1
        homePageNetworkData.getStatsForRange(start.toString(), end.toString())
            .map { stats ->
                stats.dailyStats
                    .associate { it.date to it.timeSpent }
                    .getLatestStreakInRange()
            }
            .map {
                when (it.days) {
                    count -> it + calculate(
                        start = end.minus(1, DateTimeUnit.DAY),
                        value = value,
                        unit = unit
                    ).bind()

                    else -> it
                }
            }
            .bind()
    }
}
