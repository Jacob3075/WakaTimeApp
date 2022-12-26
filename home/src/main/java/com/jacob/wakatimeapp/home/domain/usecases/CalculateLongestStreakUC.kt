package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.continuations.either
import com.jacob.wakatimeapp.core.common.auth.AuthDataStore
import com.jacob.wakatimeapp.core.common.toDate
import com.jacob.wakatimeapp.core.common.utils.InstantProvider
import com.jacob.wakatimeapp.core.models.Error.NetworkErrors.Timeout
import com.jacob.wakatimeapp.core.models.Stats
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.home.data.local.HomePageCache
import com.jacob.wakatimeapp.home.data.network.HomePageNetworkData
import com.jacob.wakatimeapp.home.domain.models.Streak
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.Map.Entry
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

@Singleton
internal class CalculateLongestStreakUC @Inject constructor(
    private val homePageNetworkData: HomePageNetworkData,
    private val homePageCache: HomePageCache,
    private val authDataStore: AuthDataStore,
    private val instantProvider: InstantProvider,
    dispatcher: CoroutineContext = Dispatchers.IO,
) {
    private val ioScope = CoroutineScope(dispatcher)

    suspend operator fun invoke(batchSize: DatePeriod = DEFAULT_BATCH_SIZE) = either {
        val userDetails = authDataStore.userDetails.first()
        val cachedLongestStreak = homePageCache.getLongestStreak().first().bind()
        val currentStreak = homePageCache.getCurrentStreak().first().bind()

        if (currentStreak > cachedLongestStreak) return@either currentStreak
        if (cachedLongestStreak != Streak.ZERO) return@either cachedLongestStreak

        val currentDay = instantProvider.now().toDate(timeZone = instantProvider.timeZone)
        val userJoinedData = userDetails.createdAt

        generateSequence(userJoinedData) { it + batchSize }
            .takeWhile { it < currentDay }
            .plusElement(currentDay)
            .zipWithNext()
            .toList()
            .map(::getStreaksInBatchAsync)
            .awaitAll()
            .flatMap { it.bind() }
            .combineStreaks()
            .maxByOrNull(Streak::days)
            ?: Streak.ZERO
    }.mapLeft {
        when (it) {
            is Timeout -> Timeout("Network timed out, try again later")
            else -> it
        }
    }

    private fun getStreaksInBatchAsync(batchStartEnd: Pair<LocalDate, LocalDate>) = ioScope.async {
        homePageNetworkData.getStatsForRange(
            start = batchStartEnd.first.toString(),
            end = batchStartEnd.second.toString(),
        ).map {
            it.groupConsecutiveDaysWithStats()
                .filter(List<Entry<LocalDate, Time>>::isNotEmpty)
                .toStreaks()
        }
    }

    companion object {
        private val DEFAULT_BATCH_SIZE = DatePeriod(months = 6)
    }
}

private fun Stats.groupConsecutiveDaysWithStats() = dailyStats
    .associate { it.date to it.timeSpent }
    .entries
    .groupConsecutive()

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

private fun List<List<Entry<LocalDate, Time>>>.toStreaks() = map {
    Streak(it.first().key, it.last().key)
}

private fun List<Streak>.combineStreaks() = if (isEmpty()) this
else drop(1)
    .fold(mutableListOf(first())) { acc, streakRange ->
        val last = acc.last()
        when (last.canBeCombinedWith(streakRange)) {
            true -> acc.replaceLast(last + streakRange)
            false -> acc.add(streakRange)
        }
        acc
    }

private fun MutableList<Streak>.replaceLast(e: Streak) {
    removeLast()
    add(e)
}
