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

        val recalculatedStreakForLast7Days = last7DaysStats.recalculateStreakInLast7Days()

        val combinedStreak = currentStreak + recalculatedStreakForLast7Days
        val failedToCombine = combinedStreak == StreakRange.ZERO

        when {
            endOfCurrentStreakIsYesterday -> whenEndOfCurrentStreakIsYesterday(
                currentStreak,
                todaysStats
            )

            failedToCombine -> whenFailedToCombine(recalculatedStreakForLast7Days)
            else -> combinedStreak
        }
    }

    private fun whenEndOfCurrentStreakIsYesterday(
        currentStreak: StreakRange,
        todaysStats: Time,
    ) = when (todaysStats) {
        Time.ZERO -> currentStreak
        else -> currentStreak.copy(end = instantProvider.now().toDate())
    }

    private suspend fun whenFailedToCombine(
        recalculatedStreakForLast7Days: StreakRange,
    ) = when (recalculatedStreakForLast7Days.days) {
        7 -> recalculateLatestStreakService.calculate(
            start = instantProvider.now().toDate().minus(7, DateTimeUnit.DAY),
            value = 1,
            unit = DateTimeUnit.MONTH
        )

        else -> recalculatedStreakForLast7Days
    }

    private fun Last7DaysStats.recalculateStreakInLast7Days() = weeklyTimeSpent
        .getLatestStreakInRange()
        .let {
            if (it.isEmpty()) StreakRange.ZERO else StreakRange(
                start = it.last().key,
                end = it.first().key,
            )
        }
}

fun Map<LocalDate, Time>.getLatestStreakInRange() = toSortedMap()
    .entries
    .reversed()
    .takeWhile { it.value != Time.ZERO }
