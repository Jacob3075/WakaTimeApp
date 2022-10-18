package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.Either
import arrow.core.computations.either
import com.jacob.wakatimeapp.core.common.today
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.home.data.local.HomePageCache
import com.jacob.wakatimeapp.home.domain.models.StreakRange
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

@Singleton
class CalculateCurrentStreakUC @Inject constructor(
    dispatcher: CoroutineContext = Dispatchers.IO,
    private val homePageCache: HomePageCache,
) {

    operator fun invoke(): Flow<Either<Error, StreakRange>> = channelFlow {
        val currentStreakFlow = homePageCache.getCurrentStreak()
        val last7DaysStatsFlow = homePageCache.getLast7DaysStats()

        launch { currentStreakFlow.collect { send(it) } }

        last7DaysStatsFlow.collect { last7DaysStatsEither ->
            either {
                val last7DaysStats = last7DaysStatsEither.bind()
                val currentStreak = currentStreakFlow.first().bind()

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
    }
}
