package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.left
import arrow.core.right
import com.jacob.wakatimeapp.core.models.Error
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
                    itemShouldBeNull()
                }
                .expectNoMoreItems()
        }

    @Test
    internal fun `when last request was made the previous day and time difference is less than cache lifetime, then use case should return null`() =
        runTest {
            robot.buildUseCase()
                .setLastRequestTime(startOfDay - 5.minutes)
                .callUseCase(this)
                .withNextItem {
                    itemShouldBeNull()
                }
                .expectNoMoreItems()
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
                        .itemShouldBeStale()
                }
                .expectNoMoreItems()
        }

    @Test
    internal fun `when last request time is less than default cache lifetime, the caches data should be returned with is stale set to false`() =
        runTest {
            robot.buildUseCase()
                .setLastRequestTime(currentDayInstant - 10.minutes)
                .mockUserDetails(userDetails)
                .mockLast7DaysStats(last7DaysStats.right())
                .mockCurrentStreak(currentStreak.right())
                .callUseCase(this)
                .withNextItem {
                    itemShouldBeRight()
                        .itemShouldNotBeNull()
                        .itemShouldNotBeStale()
                }
                .expectNoMoreItems()
        }

    @Test
    internal fun `when an error occurs while getting data from the cache, then the errors should be propagated up`() =
        runTest {
            val networkError = Error.NetworkErrors.create("Error", 400).left()
            val databaseError = Error.DatabaseError.UnknownError("Error", Throwable()).left()
            robot.buildUseCase()
                .setLastRequestTime(currentDayInstant - 20.minutes)
                .mockUserDetails(userDetails)
                .mockLast7DaysStats(networkError)
                .mockCurrentStreak(databaseError)
                .callUseCase(this)
                .withNextItem {
                    itemShouldBeLeft()
                        .itemShouldBe(networkError)
                }
                .expectNoMoreItems()
        }
}
