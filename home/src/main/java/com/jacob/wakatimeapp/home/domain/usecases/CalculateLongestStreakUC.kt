package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.Either
import arrow.core.raise.either
import com.jacob.wakatimeapp.core.common.auth.AuthDataStore
import com.jacob.wakatimeapp.core.common.data.local.WakaTimeAppDB
import com.jacob.wakatimeapp.core.common.utils.InstantProvider
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.Streak
import com.jacob.wakatimeapp.home.data.local.HomePageCache
import com.jacob.wakatimeapp.home.data.mappers.toDailyStateAggregate
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first
import kotlinx.datetime.LocalDate

@Singleton
internal class CalculateLongestStreakUC @Inject constructor(
    private val wakaTimeAppDB: WakaTimeAppDB,
    private val homePageCache: HomePageCache,
    private val authDataStore: AuthDataStore,
    private val instantProvider: InstantProvider,
) {

    suspend operator fun invoke(): Either<Error, Streak> {
        val longestStreak = homePageCache.getLongestStreak().first()
        val currentStreak = homePageCache.getCurrentStreak().first()
        val userDetails = authDataStore.userDetails.first()
        return either {
            val longerStreak = maxOf(currentStreak.bind(), longestStreak.bind())
            if (longerStreak == Streak.ZERO) {
                calculateLongestStreak(userJoinedData = userDetails.createdAt, instantProvider.date()).bind()
            } else {
                longerStreak
            }
        }
    }

    private suspend fun calculateLongestStreak(
        userJoinedData: LocalDate,
        currentDay: LocalDate,
    ): Either<Error, Streak> = wakaTimeAppDB.getStatsForRange(
        startDate = userJoinedData,
        endDate = currentDay,
    ).map {
        it.toDailyStateAggregate()
            .values
            .associate { dailyStats -> dailyStats.date to dailyStats.timeSpent }
            .let(Streak::getStreaksIn)
            .maxByOrNull(Streak::days)
            ?: Streak.ZERO
    }
}
