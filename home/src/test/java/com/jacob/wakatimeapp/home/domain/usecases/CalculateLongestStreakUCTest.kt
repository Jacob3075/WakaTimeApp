package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.right
import com.jacob.wakatimeapp.core.common.toDate
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.home.domain.models.StreakRange
import com.jacob.wakatimeapp.home.domain.usecases.CalculateLongestStreakUCRobot.Companion.dailyStats
import com.jacob.wakatimeapp.home.domain.usecases.CalculateLongestStreakUCRobot.Companion.stats
import kotlin.collections.Map.Entry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class CalculateLongestStreakUCTest {
    private val robot = CalculateLongestStreakUCRobot()

    @Test
    internal fun `when current streak from cache is greater than previous longest streak, then update longest streak with current`() =
        runTest {
            val currentStreak = StreakRange(
                start = LocalDate(2022, 4, 1),
                end = LocalDate(2022, 4, 10)
            )
            robot.buildUseCase()
                .mockHomePageCacheGetCurrentStreak(currentStreak.right())
                .mockHomePageCacheGetLongestStreak(
                    StreakRange(
                        start = LocalDate(2022, 1, 1),
                        end = LocalDate(2022, 1, 5)
                    ).right()
                )
                .callUseCase(DatePeriod(months = 1))
                .resultShouldBe(currentStreak.right())
                .verifyHomePageCacheUpdateLongestStreakCalled(1, currentStreak)
        }

    @Test
    internal fun `when cache has value for longest streak, then do not recalculate`() = runTest {
        robot.buildUseCase()
            .mockHomePageCacheGetLongestStreak(
                StreakRange(
                    start = LocalDate(2022, 1, 1),
                    end = LocalDate(2022, 2, 5)
                ).right()
            )
            .mockHomePageCacheGetCurrentStreak(
                StreakRange(
                    start = LocalDate(2022, 1, 1),
                    end = LocalDate(2022, 1, 5)
                ).right()
            )
            .callUseCase(DatePeriod(months = 1))
            .resultShouldBe(
                StreakRange(
                    start = LocalDate(2022, 1, 1),
                    end = LocalDate(2022, 2, 5)
                ).right()
            )
            .verifyHomePageCacheUpdateLongestStreakCalled(count = 0)
    }

    @Test
    internal fun `when creating batches that is greater than user creation time, then api call should be made from creation date till batch size`() =
        runTest {
            val userCreatedAt = LocalDate(2022, 1, 1)
            val currentInstant = Instant.parse("2022-01-10T00:00:00Z")
            robot.buildUseCase(
                userCreatedAt = userCreatedAt,
                currentInstant = currentInstant
            )
                .mockHomePageCacheGetLongestStreak()
                .mockHomePageCacheGetCurrentStreak()
                .mockGetStatsForRange(
                    start = userCreatedAt.toString(),
                    end = currentInstant.toDate().toString(),
                    data = stats.right(),
                )
                .callUseCase(batchSize = DatePeriod(months = 1))
                .verifyGetStatsForRangeCalled(1)
        }

    @Test
    internal fun `when creating batches and there are some days that are not part of the initial batch, then current day should be added at the end`() =
        runTest {
            val userCreatedAt = LocalDate(2022, 1, 1)
            val currentInstant = Instant.parse("2022-02-10T00:00:00Z")
            robot.buildUseCase(
                userCreatedAt = userCreatedAt,
                currentInstant = currentInstant
            )
                .mockHomePageCacheGetLongestStreak()
                .mockHomePageCacheGetCurrentStreak()
                .mockGetStatsForRange(
                    start = userCreatedAt.toString(),
                    end = LocalDate(2022, 2, 1).toString(),
                    data = stats.right(),
                )
                .mockGetStatsForRange(
                    start = LocalDate(2022, 2, 1).toString(),
                    end = currentInstant.toDate().toString(),
                    data = stats.right(),
                )
                .callUseCase(batchSize = DatePeriod(months = 1))
                .verifyGetStatsForRangeCalled(2)
        }

    @Test
    internal fun `when there is only 1 batch and it has no streak, then ZERO should be returned`() =
        runTest {
            val userCreatedAt = LocalDate(2022, 1, 1)
            val currentInstant = Instant.parse("2022-01-10T00:00:00Z")
            robot.buildUseCase(
                userCreatedAt = userCreatedAt,
                currentInstant = currentInstant
            )
                .mockHomePageCacheGetLongestStreak()
                .mockHomePageCacheGetCurrentStreak()
                .mockGetStatsForRange(
                    start = userCreatedAt.toString(),
                    end = currentInstant.toDate().toString(),
                    data = stats.copy(
                        dailyStats = List(30) {
                            dailyStats.copy(
                                timeSpent = Time.ZERO,
                                date = LocalDate(2022, 1, it + 1)
                            )
                        },

                    ).right(),
                )
                .callUseCase(batchSize = DatePeriod(months = 1))
                .resultShouldBe(StreakRange.ZERO.right())
        }

    @Test
    internal fun `when there is only 1 batch and it has only 1 streak, then that streak should be returned`() =
        runTest {
            val userCreatedAt = LocalDate(2022, 1, 1)
            val currentInstant = Instant.parse("2022-01-10T00:00:00Z")
            robot.buildUseCase(
                userCreatedAt = userCreatedAt,
                currentInstant = currentInstant
            )
                .mockHomePageCacheGetLongestStreak()
                .mockHomePageCacheGetCurrentStreak()
                .mockGetStatsForRange(
                    start = userCreatedAt.toString(),
                    end = currentInstant.toDate().toString(),
                    data = stats.copy(
                        dailyStats = List(10) {
                            dailyStats.copy(
                                date = LocalDate(2022, 1, it + 1)
                            )
                        },
                    ).right(),
                )
                .callUseCase(batchSize = DatePeriod(months = 1))
                .resultShouldBe(
                    StreakRange(
                        start = LocalDate(2022, 1, 1),
                        end = LocalDate(2022, 1, 10)
                    ).right()
                )
        }

    @Test
    internal fun `when there is only 1 batch and it has multiple streaks, then longest from that should be returned`() =
        runTest {
        }

    @Test
    internal fun `when there is only 1 streak and it is spread across multiple batches, then combined streak should be returned`() =
        runTest {
        }

    @Test
    internal fun `when there are many streaks and batches but no continuous streaks across batches, then largest streak should be returned`() =
        runTest {
        }

    @Test
    internal fun name() {
        listOf(
            mapOf(
                LocalDate(2022, 1, 1) to Time.ZERO,
                LocalDate(2022, 1, 2) to Time.fromDecimal(1f),
                LocalDate(2022, 1, 3) to Time.fromDecimal(1f),
                LocalDate(2022, 1, 4) to Time.ZERO,
                LocalDate(2022, 1, 5) to Time.fromDecimal(1f),
                LocalDate(2022, 1, 6) to Time.fromDecimal(1f),
            ),

            mapOf(
                LocalDate(2022, 1, 7) to Time.fromDecimal(1f),
                LocalDate(2022, 1, 8) to Time.fromDecimal(1f),
                LocalDate(2022, 1, 9) to Time.ZERO,
                LocalDate(2022, 1, 10) to Time.fromDecimal(1f),
            ),

            mapOf(
                LocalDate(2022, 1, 11) to Time.ZERO,
                LocalDate(2022, 1, 12) to Time.fromDecimal(1f),
                LocalDate(2022, 1, 13) to Time.fromDecimal(1f),
                LocalDate(2022, 1, 14) to Time.ZERO,
                LocalDate(2022, 1, 15) to Time.fromDecimal(1f),
                LocalDate(2022, 1, 16) to Time.ZERO,
            ),

            mapOf(
                LocalDate(2022, 1, 17) to Time.ZERO,
                LocalDate(2022, 1, 18) to Time.ZERO,
                LocalDate(2022, 1, 19) to Time.ZERO,
                LocalDate(2022, 1, 20) to Time.ZERO,
                LocalDate(2022, 1, 21) to Time.ZERO,
                LocalDate(2022, 1, 22) to Time.fromDecimal(1f),
            ),

            mapOf(
                LocalDate(2022, 1, 23) to Time.fromDecimal(1f),
                LocalDate(2022, 1, 24) to Time.fromDecimal(1f),
                LocalDate(2022, 1, 25) to Time.fromDecimal(1f),
                LocalDate(2022, 1, 26) to Time.fromDecimal(1f),
            ),

            mapOf(
                LocalDate(2022, 1, 27) to Time.fromDecimal(1f),
                LocalDate(2022, 1, 28) to Time.fromDecimal(1f),
                LocalDate(2022, 1, 29) to Time.fromDecimal(1f),
                LocalDate(2022, 1, 30) to Time.fromDecimal(1f),
            )
        ).flatMap { map ->
            map.entries
                .groupConsecutiveBy()
                .filter { it.isNotEmpty() }
                .toStreaks()
        }
            .combineStreaks()
            .forEach(::println)
    }
}

/**
 * [Source](https://stackoverflow.com/a/65357359/13181948)
 */
private fun Iterable<Entry<LocalDate, Time>>.groupConsecutiveBy() =
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
    drop(1)
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
