package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.Either
import arrow.core.raise.either
import com.jacob.wakatimeapp.core.common.auth.AuthDataStore
import com.jacob.wakatimeapp.core.common.data.local.WakaTimeAppDB
import com.jacob.wakatimeapp.core.common.utils.InstantProvider
import com.jacob.wakatimeapp.core.models.DailyStatsAggregate
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.home.data.local.HomePageCache
import com.jacob.wakatimeapp.home.data.mappers.toDailyStateAggregate
import com.jacob.wakatimeapp.home.domain.models.Streak
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first
import kotlinx.datetime.LocalDate
import kotlin.collections.Map.Entry

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
            .groupConsecutiveDaysWithStats()
            .filter(List<Entry<LocalDate, Time>>::isNotEmpty)
            .toStreaks()
            .maxByOrNull(Streak::days)
            ?: Streak.ZERO
    }
}

private fun DailyStatsAggregate.groupConsecutiveDaysWithStats() = values
    .associate { it.date to it.timeSpent }
    .entries
    .groupConsecutive()

/**
 * Given a list of days, groups consecutive days with non-zero stats into a list and adds that to a list
 *
 * [Source](https://stackoverflow.com/a/65357359/13181948)
 */
private fun Iterable<Entry<LocalDate, Time>>.groupConsecutive() =
    fold(mutableListOf(mutableListOf<Entry<LocalDate, Time>>())) { groups, dateTimeEntry ->
        when (dateTimeEntry.value) {
            Time.ZERO -> groups.add(mutableListOf())
            else -> groups.last().add(dateTimeEntry)
        }
        groups
    }

private fun List<List<Entry<LocalDate, Time>>>.toStreaks() = map {
    Streak(it.first().key, it.last().key)
}
