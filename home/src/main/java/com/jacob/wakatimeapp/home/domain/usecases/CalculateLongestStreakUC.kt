package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.raise.either
import com.jacob.wakatimeapp.core.common.auth.AuthDataStore
import com.jacob.wakatimeapp.core.common.data.local.WakaTimeAppDB
import com.jacob.wakatimeapp.core.common.data.local.entities.DayWithProjects
import com.jacob.wakatimeapp.core.common.utils.InstantProvider
import com.jacob.wakatimeapp.core.models.DailyStats
import com.jacob.wakatimeapp.core.models.DailyStatsAggregate
import com.jacob.wakatimeapp.core.models.Error.NetworkErrors.Timeout
import com.jacob.wakatimeapp.core.models.Project
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.home.data.local.HomePageCache
import com.jacob.wakatimeapp.home.domain.models.Streak
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.Map.Entry
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.first
import kotlinx.datetime.LocalDate

@Singleton
internal class CalculateLongestStreakUC @Inject constructor(
    private val wakaTimeAppDB: WakaTimeAppDB,
    private val homePageCache: HomePageCache,
    private val authDataStore: AuthDataStore,
    private val instantProvider: InstantProvider,
) {

    suspend operator fun invoke() = either {
        val userDetails = authDataStore.userDetails.first()
        val cachedLongestStreak = homePageCache.getLongestStreak().first().bind()
        val currentStreak = homePageCache.getCurrentStreak().first().bind()

        if (currentStreak > cachedLongestStreak) return@either currentStreak
        if (cachedLongestStreak != Streak.ZERO) return@either cachedLongestStreak

        val currentDay = instantProvider.date()
        val userJoinedData = userDetails.createdAt

        wakaTimeAppDB.getStatsForRange(
            startDate = userJoinedData,
            endDate = currentDay,
        )
            .bind()
            .let(List<DayWithProjects>::toDailyStateAggregate)
            .groupConsecutiveDaysWithStats()
            .filter(List<Entry<LocalDate, Time>>::isNotEmpty)
            .toStreaks()
            .maxByOrNull(Streak::days)
            ?: Streak.ZERO
    }.mapLeft {
        when (it) {
            is Timeout -> Timeout("Network timed out, try again later")
            else -> it
        }
    }
}

fun List<DayWithProjects>.toDailyStateAggregate() = DailyStatsAggregate(
    values = map {
        DailyStats(
            timeSpent = it.day.grandTotal,
            projectsWorkedOn = it.projectsForDay.map { projectPerDay ->
                Project(
                    time = projectPerDay.grandTotal,
                    name = projectPerDay.name,
                    percent = projectPerDay.grandTotal.totalSeconds / it.day.grandTotal.totalSeconds,
                )
            }.toImmutableList(),
            mostUsedLanguage = "",
            mostUsedEditor = "",
            mostUsedOs = "",
            date = it.day.date,
        )
    },
)

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
