package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.right
import com.jacob.wakatimeapp.core.common.utils.InstantProvider
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.home.data.local.HomePageCache
import com.jacob.wakatimeapp.home.domain.getLatestStreakInRange
import com.jacob.wakatimeapp.home.domain.models.Last7DaysStats
import com.jacob.wakatimeapp.home.domain.models.Streak
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus

@Singleton
internal class CalculateCurrentStreakUC @Inject constructor(
    private val homePageCache: HomePageCache,
    private val instantProvider: InstantProvider,
    private val recalculateLatestStreakUC: RecalculateLatestStreakUC,
) {

    suspend operator fun invoke(last7DaysStats: Last7DaysStats): Either<Error, Streak> = either {
        val currentStreak = homePageCache.getCurrentStreak().first().bind()

        val today = instantProvider.date()
        val todaysStats = last7DaysStats.weeklyTimeSpent[today] ?: Time.ZERO

        val endOfCurrentStreakIsYesterday = currentStreak.end == today.minus(1, DateTimeUnit.DAY)

        val recalculatedStreakForLast7Days = last7DaysStats.weeklyTimeSpent
            .getLatestStreakInRange()

        val combinedStreak = currentStreak + recalculatedStreakForLast7Days
        val failedToCombine = !currentStreak.canBeCombinedWith(recalculatedStreakForLast7Days)

        when {
            endOfCurrentStreakIsYesterday -> whenEndOfCurrentStreakIsYesterday(
                currentStreak,
                todaysStats,
            )

            failedToCombine -> whenFailedToCombine(recalculatedStreakForLast7Days).bind()
            else -> combinedStreak
        }
    }

    private fun whenEndOfCurrentStreakIsYesterday(
        currentStreak: Streak,
        todaysStats: Time,
    ) = when (todaysStats) {
        Time.ZERO -> currentStreak
        else -> currentStreak.copy(end = instantProvider.date())
    }

    @Suppress("MagicNumber")
    private suspend fun whenFailedToCombine(
        recalculatedStreakForLast7Days: Streak,
    ) = when (recalculatedStreakForLast7Days.days) {
        7 -> recalculateLatestStreakUC.calculate(
            start = instantProvider.date().minus(8, DateTimeUnit.DAY),
            batchSize = DatePeriod(months = 1),
        )

        else -> recalculatedStreakForLast7Days.right()
    }
}
