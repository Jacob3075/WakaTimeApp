package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.left
import arrow.core.right
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.home.domain.models.StreakRange
import com.jacob.wakatimeapp.home.domain.models.Streaks
import com.jacob.wakatimeapp.home.domain.models.toHomePageUserDetails
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
                .mockLastRequestTime()
                .mockUserDetails()
                .mockLast7DaysStats()
                .mockCurrentStreak()
                .callUseCase(this)
                .sendLastRequestTime(currentDayInstant - 2.hours)
                .sendUserDetails(userDetails)
                .sendLast7DaysStats(last7DaysStats.right())
                .sendCurrentStreak(currentStreak.right())
                .withNextItem {
                    itemShouldBeNull()
                }
                .expectNoMoreItems()
        }

    @Test
    internal fun `when last request was made the previous day and time difference is less than cache lifetime, then use case should return null`() =
        runTest {
            robot.buildUseCase()
                .mockLastRequestTime()
                .mockUserDetails()
                .mockLast7DaysStats()
                .mockCurrentStreak()
                .callUseCase(this)
                .sendLastRequestTime(startOfDay - 5.minutes)
                .sendUserDetails(userDetails)
                .sendLast7DaysStats(last7DaysStats.right())
                .sendCurrentStreak(currentStreak.right())
                .withNextItem {
                    itemShouldBeNull()
                }
                .expectNoMoreItems()
        }

    @Test
    internal fun `when last request time is more than default cache lifetime, then cached data should be returned with is stale set to true`() =
        runTest {
            robot.buildUseCase()
                .mockLastRequestTime()
                .mockUserDetails()
                .mockLast7DaysStats()
                .mockCurrentStreak()
                .callUseCase(this)
                .sendLastRequestTime(currentDayInstant - 20.minutes)
                .sendUserDetails(userDetails)
                .sendLast7DaysStats(last7DaysStats.right())
                .sendCurrentStreak(currentStreak.right())
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
                .mockLastRequestTime()
                .mockUserDetails()
                .mockLast7DaysStats()
                .mockCurrentStreak()
                .callUseCase(this)
                .sendLastRequestTime(currentDayInstant - 10.minutes)
                .sendUserDetails(userDetails)
                .sendLast7DaysStats(last7DaysStats.right())
                .sendCurrentStreak(currentStreak.right())
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
                .mockLastRequestTime()
                .mockUserDetails()
                .mockLast7DaysStats()
                .mockCurrentStreak()
                .callUseCase(this)
                .sendLastRequestTime(currentDayInstant - 20.minutes)
                .sendUserDetails(userDetails)
                .sendLast7DaysStats(networkError)
                .sendCurrentStreak(databaseError)
                .withNextItem {
                    itemShouldBeLeft()
                        .itemShouldBe(networkError)
                }
                .expectNoMoreItems()
        }

    @Test
    internal fun `when last 7 days stats sends multiple emissions, then use case sends multiple values`() =
        runTest {
            val cachedData = CachedHomePageUiData(
                userDetails = userDetails.toHomePageUserDetails(),
                last7DaysStats = last7DaysStats,
                streaks = Streaks(currentStreak, StreakRange.ZERO),
                isStateData = true
            )
            val copy1 = last7DaysStats.copy(timeSpentToday = Time.fromDecimal(2.0f))
            val copy2 = last7DaysStats.copy(timeSpentToday = Time.fromDecimal(3.0f))

            robot.buildUseCase()
                .mockLastRequestTime()
                .mockUserDetails()
                .mockLast7DaysStats()
                .mockCurrentStreak()
                .callUseCase(this)
                .sendLastRequestTime(currentDayInstant - 20.minutes)
                .sendUserDetails(userDetails)
                .sendLast7DaysStats(last7DaysStats.right())
                .sendCurrentStreak(currentStreak.right())
                .withNextItem {
                    itemShouldBeRight()
                        .itemShouldNotBeNull()
                        .itemShouldBeStale()
                        .itemShouldBe(cachedData.right())
                }
                .sendLast7DaysStats(copy1.right())
                .withNextItem {
                    itemShouldBeRight()
                        .itemShouldNotBeNull()
                        .itemShouldBeStale()
                        .itemShouldBe(cachedData.copy(last7DaysStats = copy1).right())
                }
                .sendLastRequestTime(currentDayInstant - 0.minutes)
                .withNextItem {
                    itemShouldBeRight()
                        .itemShouldNotBeNull()
                        .itemShouldNotBeStale()
                        .itemShouldBe(
                            cachedData.copy(last7DaysStats = copy1, isStateData = false).right()
                        )
                }
                .sendLast7DaysStats(copy2.right())
                .withNextItem {
                    itemShouldBeRight()
                        .itemShouldNotBeNull()
                        .itemShouldNotBeStale()
                        .itemShouldBe(
                            cachedData.copy(last7DaysStats = copy2, isStateData = false).right()
                        )
                }
                .expectNoMoreItems()
        }
}
