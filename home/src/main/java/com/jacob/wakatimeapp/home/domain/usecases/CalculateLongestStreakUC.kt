package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.computations.either
import com.jacob.wakatimeapp.core.common.toDate
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.UserDetails
import com.jacob.wakatimeapp.home.data.local.HomePageCache
import com.jacob.wakatimeapp.home.data.network.HomePageNetworkData
import com.jacob.wakatimeapp.home.domain.InstantProvider
import com.jacob.wakatimeapp.home.domain.models.StreakRange
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.Map.Entry
import kotlinx.coroutines.flow.first
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

@Singleton
class CalculateLongestStreakUC @Inject constructor(
    private val homePageNetworkData: HomePageNetworkData,
    private val homePageCache: HomePageCache,
    private val userDetails: UserDetails,
    private val instantProvider: InstantProvider,
) {
    suspend operator fun invoke(batchSize: DatePeriod = DEFAULT_BATCH_SIZE) = either {
        val cachedLongestStreak = homePageCache.getLongestStreak().first().bind()
        val currentStreak = homePageCache.getCurrentStreak().first().bind()

        if (currentStreak > cachedLongestStreak) return@either currentStreak
        if (cachedLongestStreak != StreakRange.ZERO) return@either cachedLongestStreak

        val currentDay = instantProvider.now().toDate(timeZone = instantProvider.timeZone)
        val userJoinedData = userDetails.createdAt

        generateSequence(userJoinedData) { it + batchSize }
            .takeWhile { it < currentDay }
            .plusElement(currentDay)
            .zipWithNext()
            .toList()
            .map {
                homePageNetworkData.getStatsForRange(
                    start = it.first.toString(),
                    end = it.second.toString(),
                ).bind()
            }
            .flatMap { stats ->
                stats.dailyStats
                    .associate { it.date to it.timeSpent }
                    .entries
                    .groupConsecutive()
            }
            .filter { it.isNotEmpty() }
            .toStreaks()
            .combineStreaks()
            .maxByOrNull { it.days }
            ?: StreakRange.ZERO
    }

    companion object {
        private val DEFAULT_BATCH_SIZE = DatePeriod(months = 6)
    }
}

/**
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

private fun List<List<Entry<LocalDate, Time>>>.toStreaks(): List<StreakRange> = map {
    StreakRange(it.first().key, it.last().key)
}

private fun List<StreakRange>.combineStreaks(): List<StreakRange> =
    if (isEmpty()) this
    else drop(1)
        .fold(mutableListOf(first())) { acc, streakRange ->
            val last = acc.last()
            when (last.canBeCombinedWith(streakRange)) {
                true -> acc.replaceLast(last + streakRange)
                false -> acc.add(streakRange)
            }
            acc
        }

private fun MutableList<StreakRange>.replaceLast(e: StreakRange) {
    removeLast()
    add(e)
}
