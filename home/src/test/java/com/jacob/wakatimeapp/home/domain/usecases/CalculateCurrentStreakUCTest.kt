package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.right
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.home.domain.models.StreakRange
import com.jacob.wakatimeapp.home.domain.usecases.CalculateCurrentStreakUCRobot.Companion.last7DaysStats
import com.jacob.wakatimeapp.home.domain.usecases.CalculateCurrentStreakUCRobot.Companion.streakRange
import com.jacob.wakatimeapp.home.domain.usecases.CalculateCurrentStreakUCRobot.Companion.today
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class CalculateCurrentStreakUCTest {
    private val robot = CalculateCurrentStreakUCRobot()

    @Test
    internal fun `when there is no value in the cache, then set current streak value to 0`() =
        runTest {
            val map = mutableMapOf<LocalDate, Time>()
            for (i in 0 until 7) {
                map[today.minus(DatePeriod(years = 0, months = 0, days = i))] = Time.ZERO
            }

            val updatedLast7DaysStats = last7DaysStats.copy(weeklyTimeSpent = map)

            robot.buildUseCase()
                .mockGetCurrentStreak(StreakRange.ZERO.right())
                .mockGetLast7DaysStats(updatedLast7DaysStats.right())
                .callUseCase()
                .resultSizeShouldBe(1)
                .resultsShouldContain(streakRange.right())
        }

    @Test
    internal fun `when there is a value in the cache, then increase the value by 1`() {
        TODO("Not yet implemented")
    }

    @Test
    internal fun `when last 7 days stats cache sends multiple values, then streak should only increase by 1`() {
        TODO("Not yet implemented")
    }

    @Test
    internal fun `when there is non 0 value in cache and todays stats arrives later, then streak should increase by 1`() {
        TODO("Not yet implemented")
    }
}
