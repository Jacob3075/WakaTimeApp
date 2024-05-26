package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.home.domain.models.HomePageUiData
import com.jacob.wakatimeapp.home.domain.models.Last7DaysStats
import com.jacob.wakatimeapp.home.domain.models.Streak
import com.jacob.wakatimeapp.home.domain.models.toHomePageUserDetails
import com.jacob.wakatimeapp.home.domain.usecases.GetCachedHomePageUiDataUCRobot.Companion.currentDayInstant
import com.jacob.wakatimeapp.home.domain.usecases.GetCachedHomePageUiDataUCRobot.Companion.currentStreak
import com.jacob.wakatimeapp.home.domain.usecases.GetCachedHomePageUiDataUCRobot.Companion.last7DaysStats
import com.jacob.wakatimeapp.home.domain.usecases.GetCachedHomePageUiDataUCRobot.Companion.longestStreak
import com.jacob.wakatimeapp.home.domain.usecases.GetCachedHomePageUiDataUCRobot.Companion.previousDayInstant
import com.jacob.wakatimeapp.home.domain.usecases.GetCachedHomePageUiDataUCRobot.Companion.startOfDay
import com.jacob.wakatimeapp.home.domain.usecases.GetCachedHomePageUiDataUCRobot.Companion.userDetails
import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.junit.jupiter.api.Test

internal class GetHomePageUiDataUCTest {
    private val robot = GetCachedHomePageUiDataUCRobot()

    @Test
    internal fun `when last request was made the previous day and time difference is more than cache lifetime, then use case should return null`() =
        runTest {
            robot.buildUseCase()
                .mockAllFunctions()
                .callUseCase(this)
                .sendLastRequestTime(previousDayInstant)
                .sendUserDetails(userDetails)
                .sendLast7DaysStats(last7DaysStats.right())
                .sendCurrentStreak(currentStreak.right())
                .sendLongestStreak(longestStreak.right())
                .withNextItem {
                    itemShouldBeNull()
                }
                .expectNoMoreItems()
        }

    @Test
    internal fun `when last request was made the previous day and time difference is less than cache lifetime, then use case should return null`() =
        runTest {
            robot.buildUseCase()
                .mockAllFunctions()
                .callUseCase(this)
                .sendLastRequestTime(startOfDay - 5.minutes)
                .sendUserDetails(userDetails)
                .sendLast7DaysStats(last7DaysStats.right())
                .sendCurrentStreak(currentStreak.right())
                .sendLongestStreak(longestStreak.right())
                .withNextItem {
                    itemShouldBeNull()
                }
                .expectNoMoreItems()
        }

    @Test
    internal fun `when last request time is more than default cache lifetime, then cached data should be returned with is stale set to true`() =
        runTest {
            robot.buildUseCase()
                .mockAllFunctions()
                .callUseCase(this)
                .sendLastRequestTime(currentDayInstant - 20.minutes)
                .sendUserDetails(userDetails)
                .sendLast7DaysStats(last7DaysStats.right())
                .sendCurrentStreak(currentStreak.right())
                .sendLongestStreak(longestStreak.right())
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
                .mockAllFunctions()
                .callUseCase(this)
                .sendLastRequestTime(currentDayInstant - 10.minutes)
                .sendUserDetails(userDetails)
                .sendLast7DaysStats(last7DaysStats.right())
                .sendCurrentStreak(currentStreak.right())
                .sendLongestStreak(longestStreak.right())
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
                .mockAllFunctions()
                .callUseCase(this)
                .sendLastRequestTime(currentDayInstant - 20.minutes)
                .sendUserDetails(userDetails)
                .sendLast7DaysStats(networkError)
                .sendCurrentStreak(databaseError)
                .sendLongestStreak(longestStreak.right())
                .withNextItem {
                    itemShouldBeLeft()
                        .itemShouldBe(networkError)
                }
                .expectNoMoreItems()
        }

    @Test
    internal fun `when last 7 days stats sends multiple emissions, then use case sends multiple values`() =
        runTest {
            val cachedData = HomePageUiData(
                userDetails = userDetails.toHomePageUserDetails(),
                last7DaysStats = last7DaysStats,
                currentStreak = currentStreak,
                longestStreak = Streak.ZERO,
                isStaleData = true,
            )
            val copy1 = last7DaysStats.copy(timeSpentToday = Time.fromDecimal(2.0f))
            val copy2 = last7DaysStats.copy(timeSpentToday = Time.fromDecimal(3.0f))

            robot.buildUseCase()
                .mockAllFunctions()
                .callUseCase(this)
                .sendLastRequestTime(previousDayInstant)
                .sendUserDetails(userDetails)
                .sendLast7DaysStats(last7DaysStats.right())
                .sendCurrentStreak(currentStreak.right())
                .sendLongestStreak(longestStreak.right())
                .withNextItem {
                    itemShouldBeRight()
                        .itemShouldBeNull()
                }
                .`when new data is sent, then new item should be emitted with correct values for previous data`(
                    currentDayInstant - 20.minutes,
                    cachedData,
                )
                .`when new data is sent, then new item should be emitted with correct values for previous data`(
                    copy1.right(),
                    cachedData.copy(last7DaysStats = copy1),
                )
                .`when new data is sent, then new item should be emitted with correct values for previous data`(
                    currentDayInstant,
                    cachedData.copy(last7DaysStats = copy1, isStaleData = false),
                )
                .`when new data is sent, then new item should be emitted with correct values for previous data`(
                    copy2.right(),
                    cachedData.copy(last7DaysStats = copy2, isStaleData = false),
                )
                .expectNoMoreItems()
        }

    @Test
    internal fun `when there is no data in cache for last 7 days stats, then should return null`() =
        runTest {
            robot.buildUseCase()
                .mockAllFunctions()
                .callUseCase(this)
                .sendLast7DaysStats(null.right())
                .sendLastRequestTime(Instant.DISTANT_PAST)
                .sendUserDetails(userDetails)
                .sendCurrentStreak(Streak.ZERO.right())
                .sendLongestStreak(longestStreak.right())
                .withNextItem {
                    itemShouldBeRight()
                        .itemShouldBeNull()
                }
                .sendLastRequestTime(currentDayInstant)
                .withNextItem {
                    itemShouldBeRight()
                        .itemShouldBeNull()
                }
                .expectNoMoreItems()
        }

    private suspend fun GetCachedHomePageUiDataUCRobot.`when new data is sent, then new item should be emitted with correct values for previous data`(
        newData: Either<Error, Last7DaysStats>,
        result: HomePageUiData,
    ) = apply {
        sendLast7DaysStats(newData)
            .withNextItem {
                itemShouldBeRight()
                    .itemShouldNotBeNull()
                    .itemShouldBe(result.right())
            }
    }

    private suspend fun GetCachedHomePageUiDataUCRobot.`when new data is sent, then new item should be emitted with correct values for previous data`(
        newData: Instant,
        result: HomePageUiData,
    ) = apply {
        sendLastRequestTime(newData)
            .withNextItem {
                itemShouldBeRight()
                    .itemShouldNotBeNull()
                    .itemShouldBe(result.right())
            }
    }
}
