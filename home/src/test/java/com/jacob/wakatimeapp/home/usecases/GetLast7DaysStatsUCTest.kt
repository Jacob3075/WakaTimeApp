package com.jacob.wakatimeapp.home.usecases

import arrow.core.left
import arrow.core.right
import com.jacob.wakatimeapp.core.models.Error.UnknownError
import com.jacob.wakatimeapp.home.domain.InstantProvider
import com.jacob.wakatimeapp.home.domain.usecases.GetLast7DaysStatsUCRobot
import com.jacob.wakatimeapp.home.domain.usecases.GetLast7DaysStatsUCRobot.Companion.homePageUiData
import com.jacob.wakatimeapp.home.domain.usecases.GetLast7DaysStatsUCRobot.Companion.invalidDataInstant
import com.jacob.wakatimeapp.home.domain.usecases.GetLast7DaysStatsUCRobot.Companion.previousDay
import com.jacob.wakatimeapp.home.domain.usecases.GetLast7DaysStatsUCRobot.Companion.validDataInstant
import com.jacob.wakatimeapp.home.domain.usecases.GetLast7DaysStatsUCRobot.Companion.weeklyStats
import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetLast7DaysStatsUCTest {
    private val useCaseRobot: GetLast7DaysStatsUCRobot = GetLast7DaysStatsUCRobot()

    @Test
    internal fun `when making first call of the day, then api call is made`() = runTest {
        useCaseRobot.build()
            .mockCacheLastRequestTime(previousDay)
            .mockNetworkData(weeklyStats.right())
            .mockCachedData(homePageUiData)
            .callUseCase()
            .verifyGetLast7DaysStatsCalled()
    }

    @Test
    internal fun `when making first call of the day, data from cache is sent after getting from api`() =
        runTest {
            val cacheData = homePageUiData.copy()
            useCaseRobot.build()
                .mockCacheLastRequestTime(previousDay)
                .mockNetworkData(weeklyStats.right())
                .mockCachedData(cacheData)
                .callUseCase()
                .resultSizeShouldBe(1)
                .resultsShouldContain(cacheData.right())
                .verifyCacheGetCachedDataCalled()
        }

    @Test
    internal fun `when making first request of the day but data is valid, api request is made`() =
        runTest {
            val startOfDay = LocalDateTime(
                year = 2022,
                monthNumber = 10,
                dayOfMonth = 1,
                hour = 0,
                minute = 0,
                second = 0,
                nanosecond = 0
            ).toInstant(TimeZone.UTC)

            useCaseRobot.build(
                instantProvider = object : InstantProvider {
                    override val timeZone = TimeZone.UTC

                    override fun now() = startOfDay + 5.minutes
                }
            )
                .mockCacheLastRequestTime(startOfDay - 5.minutes)
                .mockNetworkData(weeklyStats.right())
                .mockCachedData(homePageUiData)
                .callUseCase()
                .verifyGetLast7DaysStatsCalled()
        }

    @Test
    internal fun `when valid data is available in cache, no api call is made`() = runTest {
        useCaseRobot.build()
            .mockCacheLastRequestTime(validDataInstant)
            .mockCachedData(homePageUiData)
            .callUseCase()
            .verifyGetLast7DaysStatsCalled(0)
            .verifyCacheUpdateLastRequestTimeCalled(0)
            .verifyCacheGetCachedDataCalled()
    }

    @Test
    internal fun `when invalid data is present in cache, api call is made`() = runTest {
        val cachedStats = homePageUiData.copy()
        useCaseRobot.build()
            .mockCacheLastRequestTime(invalidDataInstant)
            .mockNetworkData(weeklyStats.right())
            .mockCachedData(cachedStats)
            .callUseCase()
            .verifyCacheGetCachedDataCalled()
            .verifyGetLast7DaysStatsCalled()
            .verifyCacheUpdateLastRequestTimeCalled()
    }

    @Test
    internal fun `when invalid data is present in cache, then first old data is sent followed by new data`() =
        runTest {
            val oldCacheData = homePageUiData.copy()
            val newCacheData = homePageUiData.copy()

            useCaseRobot.build()
                .mockCacheLastRequestTime(invalidDataInstant)
                .mockNetworkData(weeklyStats.right())
                .mockCachedData(oldCacheData, newCacheData)
                .mockUpdateCacheData(homePageUiData)
                .callUseCase()
                .resultSizeShouldBe(2)
                .resultsShouldContain(listOf(oldCacheData.right(), newCacheData.right()))
        }

    @Test
    internal fun `when making first request of the day and request fails, then error is propagated`() =
        runTest {
            val error = UnknownError("error").left()

            useCaseRobot.build()
                .mockCacheLastRequestTime(previousDay)
                .mockNetworkData(error)
                .mockCachedData()
                .callUseCase()
                .resultSizeShouldBe(1)
                .resultsShouldContain(error)
        }

    @Test
    internal fun `when api request during invalid data is made and request fails, then old data is shown and error is sent after that`() =
        runTest {
            val cachedStats = homePageUiData.copy()
            val error = UnknownError("error").left()

            useCaseRobot.build()
                .mockCacheLastRequestTime(invalidDataInstant)
                .mockNetworkData(error)
                .mockCachedData(cachedStats)
                .callUseCase()
                .resultSizeShouldBe(2)
                .resultsShouldContain(listOf(cachedStats.right(), error))
        }
}
