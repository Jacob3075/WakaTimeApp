package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.right
import com.jacob.wakatimeapp.core.common.utils.toDate
import com.jacob.wakatimeapp.core.models.DailyStatsAggregate
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.home.domain.models.Streak
import com.jacob.wakatimeapp.home.domain.usecases.CalculateLongestStreakUCRobot.Companion.dailyStats
import com.jacob.wakatimeapp.home.domain.usecases.CalculateLongestStreakUCRobot.Companion.stats
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import org.junit.jupiter.api.Test

internal class CalculateLongestStreakUCTest {
    private val robot = CalculateLongestStreakUCRobot()

    @Test
    internal fun `when current streak from cache is greater than previous longest streak, then return current streak`() =
        runTest {
            val currentStreak = Streak(
                start = LocalDate(2022, 4, 1),
                end = LocalDate(2022, 4, 10),
            )
            robot.buildUseCase()
                .mockHomePageCacheGetCurrentStreak(currentStreak.right())
                .mockGetUserDetails()
                .mockHomePageCacheGetLongestStreak(
                    Streak(
                        start = LocalDate(2022, 1, 1),
                        end = LocalDate(2022, 1, 5),
                    ).right(),
                )
                .callUseCase()
                .resultShouldBe(currentStreak.right())
        }

    @Test
    internal fun `when cache has value for longest streak, then do not recalculate`() = runTest {
        robot.buildUseCase()
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
            .callUseCase()
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
            robot.buildUseCase(currentInstant)
                .mockGetUserDetails(userCreatedAt)
                .mockHomePageCacheGetLongestStreak()
                .mockHomePageCacheGetCurrentStreak()
                .mockGetStatsForRange(
                    start = userCreatedAt,
                    end = currentInstant.toDate(),
                    data = stats.right(),
                )
                .callUseCase()
                .verifyGetStatsForRangeCalled(1)
        }

    @Test
    internal fun `when there is only 1 batch and it has no streak, then ZERO should be returned`() =
        runTest {
            val userCreatedAt = LocalDate(2022, 1, 1)
            val currentInstant = Instant.parse("2022-01-10T00:00:00Z")

            robot.buildUseCase(currentInstant)
                .mockGetUserDetails(userCreatedAt)
                .mockHomePageCacheGetLongestStreak()
                .mockHomePageCacheGetCurrentStreak()
                .mockGetStatsForRange(
                    start = userCreatedAt,
                    end = currentInstant.toDate(),
                    data = stats.copy(
                        values = List(30) {
                            dailyStats.copy(
                                date = userCreatedAt + 0.days,
                                timeSpent = Time.ZERO,
                            )
                        },
                    ).right(),
                )
                .callUseCase()
                .resultShouldBe(Streak.ZERO.right())
        }

    @Test
    internal fun `when there is only 1 batch and it has only 1 streak, then that streak should be returned`() =
        runTest {
            val userCreatedAt = LocalDate(2022, 1, 1)
            val currentInstant = Instant.parse("2022-01-10T00:00:00Z")
            robot.buildUseCase(currentInstant)
                .mockGetUserDetails(userCreatedAt)
                .mockHomePageCacheGetLongestStreak()
                .mockHomePageCacheGetCurrentStreak()
                .mockGetStatsForRange(
                    start = userCreatedAt,
                    end = currentInstant.toDate(),
                    data =
                    stats.copy(
                        List(10) {
                            dailyStats.copy(
                                date = userCreatedAt + it.days,
                            )
                        },
                    ).right(),
                )
                .callUseCase()
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
                .let(::DailyStatsAggregate)
                .right()

            robot.buildUseCase(currentInstant)
                .mockGetUserDetails(userCreatedAt)
                .mockHomePageCacheGetLongestStreak()
                .mockHomePageCacheGetCurrentStreak()
                .mockGetStatsForRange(
                    start = userCreatedAt,
                    end = currentInstant.toDate(),
                    data = testDailyStats,
                )
                .callUseCase()
                .resultShouldBe(
                    Streak(
                        start = LocalDate(2022, 1, 8),
                        end = LocalDate(2022, 1, 11),
                    ).right(),
                )
        }
}

private val Int.days get() = DatePeriod(days = this)
