package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.right
import com.jacob.wakatimeapp.home.domain.models.StreakRange
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
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
                .callUseCase()
                .resultShouldBe(currentStreak.right())
                .verifyHomePageCacheUpdateLongestStreakCalled(currentStreak)
        }
}
