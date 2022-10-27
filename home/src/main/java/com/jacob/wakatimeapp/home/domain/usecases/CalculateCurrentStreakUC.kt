package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.Either
import arrow.core.computations.either
import com.jacob.wakatimeapp.core.common.toDate
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.home.data.local.HomePageCache
import com.jacob.wakatimeapp.home.domain.InstantProvider
import com.jacob.wakatimeapp.home.domain.RecalculateLatestStreakService
import com.jacob.wakatimeapp.home.domain.models.Last7DaysStats
import com.jacob.wakatimeapp.home.domain.models.StreakRange
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus

@Singleton
class CalculateCurrentStreakUC @Inject constructor(
    private val homePageCache: HomePageCache,
    private val instantProvider: InstantProvider,
    private val recalculateLatestStreakService: RecalculateLatestStreakService,
) {

    suspend operator fun invoke(): Either<Error, StreakRange> = either {
        val last7DaysStats = homePageCache.getLast7DaysStats().first().bind()
        val currentStreak = homePageCache.getCurrentStreak().first().bind()

        val today = instantProvider.now().toDate()
        val todaysStats = last7DaysStats.weeklyTimeSpent[today] ?: Time.ZERO

        val endOfCurrentStreakIsYesterday = currentStreak.end == today.minus(1, DateTimeUnit.DAY)

        val statsForCurrentStreakRange = last7DaysStats.recalculateStreakInLast7Days()

        when {
            isCurrentStreakActive(endOfCurrentStreakIsYesterday, todaysStats) -> currentStreak
            isCurrentStreakOngoing(
                endOfCurrentStreakIsYesterday,
                todaysStats
            ) -> currentStreak.copy(end = today)

            statsForCurrentStreakRange.isEmpty() -> StreakRange.ZERO
            currentStreak.end > statsForCurrentStreakRange.last().key -> StreakRange(
                start = currentStreak.start,
                end = statsForCurrentStreakRange.first().key,
            )

            statsForCurrentStreakRange.size == 7 -> recalculateLatestStreakService.calculate(
                start = instantProvider.now().toDate().minus(7, DateTimeUnit.DAY),
                value = 1,
                unit = DateTimeUnit.MONTH
            )

            else -> StreakRange(
                start = statsForCurrentStreakRange.last().key,
                end = statsForCurrentStreakRange.first().key,
            )
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

    private fun Last7DaysStats.recalculateStreakInLast7Days() = weeklyTimeSpent
        .getLatestStreakInRange()
}

fun Map<LocalDate, Time>.getLatestStreakInRange() = toSortedMap()
    .entries
    .reversed()
    .takeWhile { it.value != Time.ZERO }
