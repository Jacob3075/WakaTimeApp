package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.Either
import arrow.core.right
import com.jacob.wakatimeapp.home.domain.usecases.GetCachedHomePageUiDataRobot.Companion.currentDayInstant
import com.jacob.wakatimeapp.home.domain.usecases.GetCachedHomePageUiDataRobot.Companion.currentStreak
import com.jacob.wakatimeapp.home.domain.usecases.GetCachedHomePageUiDataRobot.Companion.last7DaysStats
import com.jacob.wakatimeapp.home.domain.usecases.GetCachedHomePageUiDataRobot.Companion.startOfDay
import com.jacob.wakatimeapp.home.domain.usecases.GetCachedHomePageUiDataRobot.Companion.userDetails
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetCachedHomePageUiDataTest {
    private val robot = GetCachedHomePageUiDataRobot()

    @Test
    internal fun `when last request was made the previous day and time difference is more than cache lifetime, then use case should return null`() =
        runTest {
            robot.buildUseCase()
                .setLastRequestTime(startOfDay - 2.hours)
                .callUseCase(this)
                .withNextItem {
                    itemShouldBe(Either.Right(null))
                }
        }

    @Test
    internal fun `when last request was made the previous day and time difference is less than cache lifetime, then use case should return null`() =
        runTest {
            robot.buildUseCase()
                .setLastRequestTime(startOfDay - 5.minutes)
                .callUseCase(this)
                .withNextItem {
                    itemShouldBe(Either.Right(null))
                }
        }

    @Test
    internal fun `when last request time is more than default cache lifetime, then cached data should be returned with is stale set to true`() =
        runTest {
            robot.buildUseCase()
                .setLastRequestTime(currentDayInstant - 20.minutes)
                .mockUserDetails(userDetails)
                .mockLast7DaysStats(last7DaysStats.right())
                .mockCurrentStreak(currentStreak.right())
                .callUseCase(this)
                .withNextItem {
                    itemShouldBeRight()
                        .itemShouldNotBeNull()
                        .itemShouldNotBeStale()
                }
        }
}
