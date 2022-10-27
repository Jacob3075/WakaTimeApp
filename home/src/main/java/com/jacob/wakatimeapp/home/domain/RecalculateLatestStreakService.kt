package com.jacob.wakatimeapp.home.domain

import arrow.core.getOrElse
import com.jacob.wakatimeapp.home.data.network.HomePageNetworkData
import com.jacob.wakatimeapp.home.domain.models.StreakRange
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DateTimeUnit.DateBased
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus

@Singleton
class RecalculateLatestStreakService @Inject constructor(
    private val homePageNetworkData: HomePageNetworkData,
) {
    suspend fun calculate(
        start: LocalDate,
        value: Int,
        unit: DateBased,
    ): StreakRange {
        val end = start.minus(value, unit)
        return homePageNetworkData.getStatsForRange(start.toString(), end.toString())
            .map { stats ->
                stats.dailyStats
                    .associate { it.date to it.timeSpent }
                    .getLatestStreakInRange()
            }
            .map { StreakRange(start = it.last().key, end = it.first().key) }
            .map {
                if (it.days == value) {
                    it + calculate(
                        start = end.minus(1, DateTimeUnit.DAY),
                        value = value,
                        unit = unit
                    )
                } else {
                    it
                }
            }
            .getOrElse { StreakRange.ZERO }
    }
}
