package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.right
import com.jacob.wakatimeapp.core.common.today
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.home.data.local.HomePageCache
import com.jacob.wakatimeapp.home.domain.models.StreakRange
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.datetime.LocalDate

@Singleton
class CalculateCurrentStreakUC @Inject constructor(
    dispatcher: CoroutineContext = Dispatchers.IO,
    private val homePageCache: HomePageCache,
) {

    suspend operator fun invoke(): Either<Error, StreakRange> {
        val currentStreakFlow = homePageCache.getCurrentStreak().first()
        val last7DaysStatsFlow = homePageCache.getLast7DaysStats()

        last7DaysStatsFlow.collect { last7DaysStatsEither ->
            either {
                val last7DaysStats = last7DaysStatsEither.bind()
                val currentStreak = currentStreakFlow.bind()

                val todaysStats = last7DaysStats.weeklyTimeSpent[LocalDate.today] ?: Time.ZERO

                if (todaysStats == Time.ZERO) return@either

                val updatedStreakRange = if (currentStreak == StreakRange.ZERO) {
                    StreakRange(
                        start = LocalDate.today,
                        end = LocalDate.today,
                    )
                } else {
                    StreakRange(
                        start = currentStreak.start,
                        end = LocalDate.today,
                    )
                }

                homePageCache.updateCurrentStreak(updatedStreakRange)
            }
        }

        return StreakRange.ZERO.right()
    }
}
