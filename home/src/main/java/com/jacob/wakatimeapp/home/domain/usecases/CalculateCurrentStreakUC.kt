package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.getOrElse
import com.jacob.wakatimeapp.core.common.toDate
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.home.data.local.HomePageCache
import com.jacob.wakatimeapp.home.data.network.HomePageNetworkData
import com.jacob.wakatimeapp.home.domain.InstantProvider
import com.jacob.wakatimeapp.home.domain.models.Last7DaysStats
import com.jacob.wakatimeapp.home.domain.models.StreakRange
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import kotlinx.datetime.minus

@Singleton
class CalculateCurrentStreakUC @Inject constructor(
    private val homePageCache: HomePageCache,
    private val instantProvider: InstantProvider,
    private val homePageNetworkData: HomePageNetworkData,
) {

    suspend operator fun invoke(): Either<Error, StreakRange> = either {
        val last7DaysStats = homePageCache.getLast7DaysStats().first().bind()
        val currentStreak = homePageCache.getCurrentStreak().first().bind()

        val today = instantProvider.now().toDate()
        val todaysStats = last7DaysStats.weeklyTimeSpent[today] ?: Time.ZERO

        val endOfCurrentStreakIsYesterday = currentStreak.end == today.minus(1, DateTimeUnit.DAY)

        when {
            isCurrentStreakActive(endOfCurrentStreakIsYesterday, todaysStats) -> currentStreak

            isCurrentStreakOngoing(
                endOfCurrentStreakIsYesterday,
                todaysStats
            ) -> currentStreak.copy(end = today)

            else -> getNewCurrentStreak(currentStreak, last7DaysStats)
        }
    }

    /**
     * Streak is considered active if the last date in the streak is the previous day but
     * there is no stats for the current day.
     */
    private fun isCurrentStreakActive(endOfCurrentStreakIsYesterday: Boolean, todaysStats: Time) =
        endOfCurrentStreakIsYesterday && todaysStats == Time.ZERO

    /**
     * Streak is considered on-going if there is an active streak and there is stats for the current day.
     */
    private fun isCurrentStreakOngoing(endOfCurrentStreakIsYesterday: Boolean, todaysStats: Time) =
        endOfCurrentStreakIsYesterday && todaysStats != Time.ZERO

    private suspend fun getNewCurrentStreak(
        currentStreak: StreakRange,
        last7DaysStats: Last7DaysStats,
    ): StreakRange {
        val statsForCurrentStreakRange = last7DaysStats.weeklyTimeSpent
            .getStreakInRange()

        if (statsForCurrentStreakRange.isEmpty()) return StreakRange.ZERO

        if (statsForCurrentStreakRange.size == 7) {
            val start = instantProvider.now().toDate().minus(7, DateTimeUnit.DAY)
            calculateStreak(
                start,
                start.minus(1, DateTimeUnit.MONTH)
            )
        }

        val start = if (currentStreak.end > statsForCurrentStreakRange.last().key) {
            currentStreak.start
        } else {
            statsForCurrentStreakRange.last().key
        }

        return StreakRange(
            start = start,
            end = statsForCurrentStreakRange.first().key,
        )
    }

    private suspend fun calculateStreak(start: LocalDate, end: LocalDate): StreakRange {
        val daysInRange = start.daysUntil(end)
        return homePageNetworkData.getStatsForRange(start.toString(), end.toString())
            .map { stats ->
                stats.dailyStats
                    .associate { it.date to it.timeSpent }
                    .getStreakInRange()
            }
            .map { StreakRange(start = it.last().key, end = it.first().key) }
            .map {
                if (it.days == daysInRange) {
                    it + calculateStreak(
                        start.minus(daysInRange, DateTimeUnit.DAY),
                        end.minus(daysInRange, DateTimeUnit.DAY)
                    )
                } else {
                    it
                }
            }
            .getOrElse { StreakRange.ZERO }
    }

    private fun Map<LocalDate, Time>.getStreakInRange() = toSortedMap()
        .entries
        .reversed()
        .takeWhile { it.value != Time.ZERO }
}
