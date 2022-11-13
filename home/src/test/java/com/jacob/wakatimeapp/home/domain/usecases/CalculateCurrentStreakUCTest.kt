package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.right
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.home.domain.models.Streak
import com.jacob.wakatimeapp.home.domain.usecases.CalculateCurrentStreakUCRobot.Companion.continuousWeeklyStats
import com.jacob.wakatimeapp.home.domain.usecases.CalculateCurrentStreakUCRobot.Companion.currentDay
import com.jacob.wakatimeapp.home.domain.usecases.CalculateCurrentStreakUCRobot.Companion.last7DaysStats
import com.jacob.wakatimeapp.home.domain.usecases.CalculateCurrentStreakUCRobot.Companion.noWeeklyStats
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class CalculateCurrentStreakUCTest {
    private val robot = CalculateCurrentStreakUCRobot()

    @Test
    internal fun `when there is no value in the cache, then set current streak value to 0`() =
        runTest {
            robot.buildUseCase()
                .mockGetCurrentStreak(Streak.ZERO.right())
                .mockGetLast7DaysStats(last7DaysStats.right())
                .callUseCase()
                .resultsShouldBe(Streak.ZERO.right())
        }

    @Test
    internal fun `when there is a streak in the cache that ends the previous day and there is no coding stats for that day, then do not change to value`() =
        runTest {
            val streak = Streak(
                start = currentDay.minus(3, DateTimeUnit.DAY),
                end = currentDay.minus(1, DateTimeUnit.DAY)
            )
            robot.buildUseCase()
                .mockGetCurrentStreak(streak.right())
                .mockGetLast7DaysStats(last7DaysStats.copy(weeklyTimeSpent = noWeeklyStats).right())
                .callUseCase()
                .resultsShouldBe(streak.right())
        }

    @Test
    internal fun `when there is a streak in the cache that ends the previous day and there is coding stats for that day, then streak should increase by 1 day`() =
        runTest {
            val streak = Streak(
                start = currentDay.minus(3, DateTimeUnit.DAY),
                end = currentDay.minus(1, DateTimeUnit.DAY)
            )
            robot.buildUseCase()
                .mockGetCurrentStreak(streak.right())
                .mockGetLast7DaysStats(
                    last7DaysStats.copy(weeklyTimeSpent = continuousWeeklyStats).right()
                )
                .callUseCase()
                .resultsShouldBe(streak.copy(end = currentDay).right())
        }

    @Test
    internal fun `when there is a streak in the cache but it ended within the last 7 days and there is no coding stats since then, then set current streak to zero`() =
        runTest {
            val streak = Streak(
                start = currentDay.minus(8, DateTimeUnit.DAY),
                end = currentDay.minus(4, DateTimeUnit.DAY)
            )
            robot.buildUseCase()
                .mockGetCurrentStreak(streak.right())
                .mockGetLast7DaysStats(
                    last7DaysStats.copy(weeklyTimeSpent = noWeeklyStats).right()
                )
                .callUseCase()
                .resultsShouldBe(Streak.ZERO.right())
        }

    @Test
    internal fun `when there is a streak in the cache but it ended within the last 7 days and there is coding stats since then, then re-calculate the streak`() =
        runTest {
            val initialStreak = Streak(
                start = currentDay.minus(8, DateTimeUnit.DAY),
                end = currentDay.minus(4, DateTimeUnit.DAY)
            )

            val result = Streak(
                start = currentDay.minus(2, DateTimeUnit.DAY),
                end = currentDay,
            )

            val updatedWeeklyStats = continuousWeeklyStats.toMutableMap().also {
                it[currentDay.minus(3, DateTimeUnit.DAY)] = Time.ZERO
                it[currentDay.minus(4, DateTimeUnit.DAY)] = Time.ZERO
            }

            robot.buildUseCase()
                .mockGetCurrentStreak(initialStreak.right())
                .mockGetLast7DaysStats(
                    last7DaysStats.copy(weeklyTimeSpent = updatedWeeklyStats).right()
                )
                .callUseCase()
                .resultsShouldBe(result.right())
        }

    @Test
    internal fun `when the current streak has not been updated for less than a week and there is more coding activity since then, then calculating should update the original streak`() =
        runTest {
            val initialStreak = Streak(
                start = currentDay.minus(10, DateTimeUnit.DAY),
                end = currentDay.minus(4, DateTimeUnit.DAY)
            )

            val result = Streak(
                start = currentDay.minus(10, DateTimeUnit.DAY),
                end = currentDay,
            )

            robot.buildUseCase()
                .mockGetCurrentStreak(initialStreak.right())
                .mockGetLast7DaysStats(
                    last7DaysStats.copy(weeklyTimeSpent = continuousWeeklyStats).right()
                )
                .callUseCase()
                .resultsShouldBe(result.right())
        }

    @Test
    internal fun `when the current streak has not been updated for over a week and there is more coding activity since then, then calculating should update the original streak`() =
        runTest {
            val initialStreak = Streak(
                start = currentDay.minus(15, DateTimeUnit.DAY),
                end = currentDay.minus(10, DateTimeUnit.DAY)
            )

            val result = Streak(
                start = currentDay.minus(15, DateTimeUnit.DAY),
                end = currentDay,
            )

            robot.buildUseCase()
                .mockGetCurrentStreak(initialStreak.right())
                .mockGetLast7DaysStats(
                    last7DaysStats.copy(weeklyTimeSpent = continuousWeeklyStats).right()
                )
                .mockRecalculateStreak(currentDay.minus(8, DateTimeUnit.DAY), result.right())
                .callUseCase()
                .resultsShouldBe(result.right())
        }

    @Test
    internal fun `when last 7 days stats is null, then return zero`() = runTest {
        robot.buildUseCase()
            .mockGetLast7DaysStats(null.right())
            .mockGetCurrentStreak(Streak.ZERO.right())
            .callUseCase()
            .resultsShouldBe(Streak.ZERO.right())
    }
}
