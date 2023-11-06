package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.right
import com.jacob.wakatimeapp.core.common.utils.toDate
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.home.domain.models.Streak
import com.jacob.wakatimeapp.home.domain.usecases.CalculateLongestStreakUCRobot.Companion.dailyStats
import com.jacob.wakatimeapp.home.domain.usecases.CalculateLongestStreakUCRobot.Companion.stats
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class CalculateLongestStreakUCTest {
    private val robot = CalculateLongestStreakUCRobot()

    @Test
    internal fun `when current streak from cache is greater than previous longest streak, then return current streak`() =
        runTest {
            val currentStreak = Streak(
                start = LocalDate(2022, 4, 1),
                end = LocalDate(2022, 4, 10),
            )
            robot.buildUseCase(dispatcher = UnconfinedTestDispatcher(testScheduler))
                .mockHomePageCacheGetCurrentStreak(currentStreak.right())
                .mockGetUserDetails()
                .mockHomePageCacheGetLongestStreak(
                    Streak(
                        start = LocalDate(2022, 1, 1),
                        end = LocalDate(2022, 1, 5),
                    ).right(),
                )
                .callUseCase(DatePeriod(months = 1))
                .resultShouldBe(currentStreak.right())
        }

    @Test
    internal fun `when cache has value for longest streak, then do not recalculate`() = runTest {
        robot.buildUseCase(dispatcher = UnconfinedTestDispatcher(testScheduler))
            .mockGetUserDetails()
            .mockHomePageCacheGetLongestStreak(
                Streak(
                    start = LocalDate(2022, 1, 1),
                    end = LocalDate(2022, 2, 5),
                ).right(),
            )
            .mockHomePageCacheGetCurrentStreak(
                Streak(
                    start = LocalDate(2022, 1, 1),
                    end = LocalDate(2022, 1, 5),
                ).right(),
            )
            .callUseCase(DatePeriod(months = 1))
            .resultShouldBe(
                Streak(
                    start = LocalDate(2022, 1, 1),
                    end = LocalDate(2022, 2, 5),
                ).right(),
            )
    }

    @Test
    internal fun `when creating batches that is greater than user creation time, then api call should be made from creation date till batch size`() =
        runTest {
            val userCreatedAt = LocalDate(2022, 1, 1)
            val currentInstant = Instant.parse("2022-01-10T00:00:00Z")
            robot.buildUseCase(
                dispatcher = UnconfinedTestDispatcher(testScheduler),
                currentInstant = currentInstant,
            )
                .mockGetUserDetails(userCreatedAt = userCreatedAt)
                .mockHomePageCacheGetLongestStreak()
                .mockHomePageCacheGetCurrentStreak()
                .mockGetStatsForRange(
                    start = userCreatedAt,
                    end = currentInstant.toDate(),
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
                dispatcher = UnconfinedTestDispatcher(testScheduler),
                currentInstant = currentInstant,
            )
                .mockGetUserDetails(userCreatedAt = userCreatedAt)
                .mockHomePageCacheGetLongestStreak()
                .mockHomePageCacheGetCurrentStreak()
                .mockGetStatsForRange(
                    start = userCreatedAt,
                    end = LocalDate(2022, 2, 1),
                    data = stats.right(),
                )
                .mockGetStatsForRange(
                    start = LocalDate(2022, 2, 1),
                    end = currentInstant.toDate(),
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
                dispatcher = UnconfinedTestDispatcher(testScheduler),
                currentInstant = currentInstant,
            )
                .mockGetUserDetails(userCreatedAt = userCreatedAt)
                .mockHomePageCacheGetLongestStreak()
                .mockHomePageCacheGetCurrentStreak()
                .mockGetStatsForRange(
                    start = userCreatedAt,
                    end = currentInstant.toDate(),
                    data = stats.copy(
                        values = List(30) {
                            dailyStats.copy(
                                timeSpent = Time.ZERO,
                                date = userCreatedAt + it.days,
                            )
                        },
                    ).right(),
                )
                .callUseCase(batchSize = DatePeriod(months = 1))
                .resultShouldBe(Streak.ZERO.right())
        }

    @Test
    internal fun `when there is only 1 batch and it has only 1 streak, then that streak should be returned`() =
        runTest {
            val userCreatedAt = LocalDate(2022, 1, 1)
            val currentInstant = Instant.parse("2022-01-10T00:00:00Z")
            robot.buildUseCase(
                dispatcher = UnconfinedTestDispatcher(testScheduler),
                currentInstant = currentInstant,
            )
                .mockGetUserDetails(userCreatedAt = userCreatedAt)
                .mockHomePageCacheGetLongestStreak()
                .mockHomePageCacheGetCurrentStreak()
                .mockGetStatsForRange(
                    start = userCreatedAt,
                    end = currentInstant.toDate(),
                    data = stats.copy(
                        values = List(10) {
                            dailyStats.copy(date = userCreatedAt + it.days)
                        },
                    ).right(),
                )
                .callUseCase(batchSize = DatePeriod(months = 1))
                .resultShouldBe(
                    Streak(
                        start = LocalDate(2022, 1, 1),
                        end = LocalDate(2022, 1, 10),
                    ).right(),
                )
        }

    @Test
    internal fun `when there is only 1 batch and it has multiple streaks, then longest from that should be returned`() =
        runTest {
            val userCreatedAt = LocalDate(2022, 1, 1)
            val currentInstant = Instant.parse("2022-01-12T00:00:00Z")
            val zeroDailyStats = dailyStats.copy(timeSpent = Time.ZERO)

            val testDailyStats = listOf(
                dailyStats.copy(date = userCreatedAt + 0.days),
                dailyStats.copy(date = userCreatedAt + 1.days),
                zeroDailyStats.copy(date = userCreatedAt + 2.days),
                zeroDailyStats.copy(date = userCreatedAt + 3.days),
                zeroDailyStats.copy(date = userCreatedAt + 4.days),
                dailyStats.copy(date = userCreatedAt + 5.days),
                zeroDailyStats.copy(date = userCreatedAt + 6.days),
                dailyStats.copy(date = userCreatedAt + 7.days),
                dailyStats.copy(date = userCreatedAt + 8.days),
                dailyStats.copy(date = userCreatedAt + 9.days),
                dailyStats.copy(date = userCreatedAt + 10.days),
                zeroDailyStats.copy(date = userCreatedAt + 11.days),
            )

            robot.buildUseCase(
                dispatcher = UnconfinedTestDispatcher(testScheduler),
                currentInstant = currentInstant,
            )
                .mockGetUserDetails(userCreatedAt = userCreatedAt)
                .mockHomePageCacheGetLongestStreak()
                .mockHomePageCacheGetCurrentStreak()
                .mockGetStatsForRange(
                    start = userCreatedAt,
                    end = currentInstant.toDate(),
                    data = stats.copy(
                        values = testDailyStats,
                    ).right(),
                )
                .callUseCase(batchSize = DatePeriod(months = 1))
                .resultShouldBe(
                    Streak(
                        start = LocalDate(2022, 1, 8),
                        end = LocalDate(2022, 1, 11),
                    ).right(),
                )
        }

    @Test
    internal fun `when there is only 1 streak and it is spread across multiple batches, then combined streak should be returned`() =
        runTest {
            val userCreatedAt = LocalDate(2022, 1, 1)
            val currentInstant = Instant.parse("2022-01-22T00:00:00Z")

            val testDailyStats1 = List(8) {
                dailyStats.copy(date = userCreatedAt + it.days)
            }
            val testDailyStats2 = List(8) {
                dailyStats.copy(date = userCreatedAt + (it + 7).days)
            }
            val testDailyStats3 = List(8) {
                dailyStats.copy(date = userCreatedAt + (it + 14).days)
            }

            robot.buildUseCase(
                dispatcher = UnconfinedTestDispatcher(testScheduler),
                currentInstant = currentInstant,
            )
                .mockGetUserDetails(userCreatedAt = userCreatedAt)
                .mockHomePageCacheGetLongestStreak()
                .mockHomePageCacheGetCurrentStreak()
                .mockGetStatsForRange(
                    start = userCreatedAt,
                    end = (userCreatedAt + 7.days),
                    data = stats.copy(values = testDailyStats1).right(),
                )
                .mockGetStatsForRange(
                    start = (userCreatedAt + 7.days),
                    end = (userCreatedAt + 14.days),
                    data = stats.copy(values = testDailyStats2).right(),
                )
                .mockGetStatsForRange(
                    start = (userCreatedAt + 14.days),
                    end = (userCreatedAt + 21.days),
                    data = stats.copy(values = testDailyStats3).right(),
                )
                .callUseCase(batchSize = DatePeriod(days = 7))
                .resultShouldBe(
                    Streak(
                        start = userCreatedAt,
                        end = currentInstant.toDate(),
                    ).right(),
                )
        }

    @Test
    internal fun `when there are many batches and streaks with continuous streaks across some batches, then largest streak should be returned`() =
        runTest {
            val userCreatedAt = LocalDate(2022, 1, 1)
            val currentInstant = Instant.parse("2022-02-02T00:00:00Z")
            val zeroDailyStats = dailyStats.copy(timeSpent = Time.ZERO)

            val testDailyStats1 = listOf(
                dailyStats.copy(date = userCreatedAt + 0.days),
                dailyStats.copy(date = userCreatedAt + 1.days),
                dailyStats.copy(date = userCreatedAt + 2.days),
                zeroDailyStats.copy(date = userCreatedAt + 3.days),
                dailyStats.copy(date = userCreatedAt + 4.days),
                zeroDailyStats.copy(date = userCreatedAt + 5.days),
                dailyStats.copy(date = userCreatedAt + 6.days),
                dailyStats.copy(date = userCreatedAt + 7.days),
            )

            val testDailyStats2 = listOf(
                dailyStats.copy(date = userCreatedAt + 7.days),
                dailyStats.copy(date = userCreatedAt + 8.days),
                dailyStats.copy(date = userCreatedAt + 9.days),
                dailyStats.copy(date = userCreatedAt + 10.days),
                dailyStats.copy(date = userCreatedAt + 11.days),
                zeroDailyStats.copy(date = userCreatedAt + 12.days),
                zeroDailyStats.copy(date = userCreatedAt + 13.days),
                dailyStats.copy(date = userCreatedAt + 14.days),
            )

            val testDailyStats3 = listOf(
                dailyStats.copy(date = userCreatedAt + 14.days),
                dailyStats.copy(date = userCreatedAt + 15.days),
                zeroDailyStats.copy(date = userCreatedAt + 16.days),
                dailyStats.copy(date = userCreatedAt + 17.days),
                zeroDailyStats.copy(date = userCreatedAt + 18.days),
                dailyStats.copy(date = userCreatedAt + 19.days),
                zeroDailyStats.copy(date = userCreatedAt + 20.days),
                dailyStats.copy(date = userCreatedAt + 21.days),
            )

            val testDailyStats4 = listOf(
                dailyStats.copy(date = userCreatedAt + 21.days),
                dailyStats.copy(date = userCreatedAt + 22.days),
                zeroDailyStats.copy(date = userCreatedAt + 23.days),
                zeroDailyStats.copy(date = userCreatedAt + 24.days),
                dailyStats.copy(date = userCreatedAt + 25.days),
                dailyStats.copy(date = userCreatedAt + 26.days),
                zeroDailyStats.copy(date = userCreatedAt + 27.days),
                zeroDailyStats.copy(date = userCreatedAt + 28.days),
            )

            val testDailyStats5 = listOf(
                zeroDailyStats.copy(date = userCreatedAt + 28.days),
                dailyStats.copy(date = userCreatedAt + 29.days),
                dailyStats.copy(date = userCreatedAt + 30.days),
                zeroDailyStats.copy(date = userCreatedAt + 31.days),
                zeroDailyStats.copy(date = userCreatedAt + 32.days),
                dailyStats.copy(date = userCreatedAt + 33.days),
            )

            robot.buildUseCase(
                dispatcher = UnconfinedTestDispatcher(testScheduler),
                currentInstant = currentInstant,
            )
                .mockGetUserDetails(userCreatedAt = userCreatedAt)
                .mockHomePageCacheGetLongestStreak()
                .mockHomePageCacheGetCurrentStreak()
                .mockGetStatsForRange(
                    start = userCreatedAt,
                    end = (userCreatedAt + 7.days),
                    data = stats.copy(values = testDailyStats1).right(),
                )
                .mockGetStatsForRange(
                    start = (userCreatedAt + 7.days),
                    end = (userCreatedAt + 14.days),
                    data = stats.copy(values = testDailyStats2).right(),
                )
                .mockGetStatsForRange(
                    start = (userCreatedAt + 14.days),
                    end = (userCreatedAt + 21.days),
                    data = stats.copy(values = testDailyStats3).right(),
                )
                .mockGetStatsForRange(
                    start = (userCreatedAt + 21.days),
                    end = (userCreatedAt + 28.days),
                    data = stats.copy(values = testDailyStats4).right(),
                )
                .mockGetStatsForRange(
                    start = (userCreatedAt + 28.days),
                    end = currentInstant.toDate(),
                    data = stats.copy(values = testDailyStats5).right(),
                )
                .callUseCase(batchSize = DatePeriod(days = 7))
                .resultShouldBe(
                    Streak(
                        start = LocalDate(2022, 1, 7),
                        end = LocalDate(2022, 1, 12),
                    ).right(),
                )
        }
}

private val Int.days get() = DatePeriod(days = this)
