package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.right
import com.jacob.wakatimeapp.core.common.toDate
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.home.data.local.HomePageCache
import com.jacob.wakatimeapp.home.domain.InstantProvider
import com.jacob.wakatimeapp.home.domain.getLatestStreakInRange
import com.jacob.wakatimeapp.home.domain.models.StreakRange
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus

@Singleton
class CalculateCurrentStreakUC @Inject constructor(
    private val homePageCache: HomePageCache,
    private val instantProvider: InstantProvider,
    private val recalculateLatestStreakUC: RecalculateLatestStreakUC,
) {

    suspend operator fun invoke(): Either<Error, StreakRange> = either {
        val last7DaysStats =
            homePageCache.getLast7DaysStats().first().bind() ?: return@either StreakRange.ZERO
        val currentStreak = homePageCache.getCurrentStreak().first().bind()

        val today = instantProvider.now().toDate()
        val todaysStats = last7DaysStats.weeklyTimeSpent[today] ?: Time.ZERO

        val endOfCurrentStreakIsYesterday = currentStreak.end == today.minus(1, DateTimeUnit.DAY)

        val recalculatedStreakForLast7Days = last7DaysStats.weeklyTimeSpent
            .getLatestStreakInRange()

        val combinedStreak = currentStreak + recalculatedStreakForLast7Days
        val failedToCombine = !currentStreak.canBeCombinedWith(recalculatedStreakForLast7Days)

        when {
            endOfCurrentStreakIsYesterday -> whenEndOfCurrentStreakIsYesterday(
                currentStreak,
                todaysStats
            )

            failedToCombine -> whenFailedToCombine(recalculatedStreakForLast7Days).bind()
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
        7 -> recalculateLatestStreakUC.calculate(
            start = instantProvider.now().toDate().minus(8, DateTimeUnit.DAY),
            value = 1,
            unit = DateTimeUnit.MONTH
        )

        else -> recalculatedStreakForLast7Days.right()
    }
}
