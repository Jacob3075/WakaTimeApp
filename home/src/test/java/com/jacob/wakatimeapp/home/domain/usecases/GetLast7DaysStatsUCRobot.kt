package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.Either
import arrow.core.right
import com.jacob.wakatimeapp.core.models.DailyStats
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.StatsRange
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.WeeklyStats
import com.jacob.wakatimeapp.home.data.local.HomePageCache
import com.jacob.wakatimeapp.home.data.network.HomePageNetworkData
import com.jacob.wakatimeapp.home.domain.InstantProvider
import com.jacob.wakatimeapp.home.domain.models.HomePageUiData
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetLast7DaysStatsUCRobot {
    private val results: MutableList<Either<Error, HomePageUiData>> = mutableListOf()
    private lateinit var useCase: GetLast7DaysStatsUC

    private val networkDataMock: HomePageNetworkData = mockk(relaxUnitFun = true)
    private val cacheMock: HomePageCache = mockk(relaxUnitFun = true)

    fun build(instantProvider: InstantProvider? = null) = apply {
        clearMocks(networkDataMock, cacheMock)

        useCase = GetLast7DaysStatsUC(
            dispatcher = UnconfinedTestDispatcher(),
            homePageNetworkData = networkDataMock,
            homePageCache = cacheMock,
            instantProvider = instantProvider ?: object : InstantProvider {
                override val timeZone = TimeZone.UTC

                override fun now() = Clock.System.now()
            },
        )
    }

    suspend fun callUseCase() = apply {
        useCase().toList(results)
    }

    fun resultSizeShouldBe(size: Int = 1) = apply {
        results.size shouldBe size
    }

    fun resultsShouldContain(expected: Either<Error, HomePageUiData>) = apply {
        results shouldContain expected
    }

    fun resultsShouldContain(expected: List<Either<Error, HomePageUiData>>) = apply {
        results shouldContainExactly expected
    }

    fun mockCacheLastRequestTime(instant: Instant) = apply {
        coEvery { cacheMock.getLastRequestTime() } returns instant
    }

    fun mockCachedData(data: HomePageUiData? = null) = apply {
        coEvery { cacheMock.getCachedData() } returns (
            data?.right()
                ?.let { flowOf(it) }
                ?: emptyFlow()
            )
    }

    fun mockNetworkData(data: Either<Error, WeeklyStats>) = apply {
        coEvery { networkDataMock.getLast7DaysStats() } returns data
    }

    fun verifyCacheGetCachedDataCalled(count: Int = 1) = apply {
        coVerify(exactly = count) { cacheMock.getCachedData() }
    }

    fun verifyCacheSetCachedDataCalled(data: HomePageUiData, count: Int = 1) = apply {
        coVerify(exactly = count) { cacheMock.updateCache(data) }
    }

    fun verifyCacheUpdateLastRequestTimeCalled(count: Int = 1) = apply {
        coVerify(exactly = count) { cacheMock.updateLastRequestTime(any()) }
    }

    fun verifyGetLast7DaysStatsCalled(count: Int = 1) = apply {
        coVerify(exactly = count) { networkDataMock.getLast7DaysStats() }
    }

    companion object {

        val previousDay = Clock.System.now()
            .minus(1.days)

        val validDataInstant = Clock.System.now()
            .minus(10.minutes)

        val invalidDataInstant = Clock.System.now()
            .minus(20.minutes)

        private val todaysDate = LocalDate(2022, 10, 10)

        val homePageUiData = HomePageUiData(
            timeSpentToday = Time.ZERO,
            projectsWorkedOn = listOf(),
            weeklyTimeSpent = mapOf(),
            mostUsedLanguage = "",
            mostUsedEditor = "",
            mostUsedOs = ""
        )

        val weeklyStats = WeeklyStats(
            totalTime = Time.ZERO,
            dailyStats = listOf(),
            range = StatsRange(
                startDate = todaysDate,
                endDate = todaysDate,
            ),
            todaysStats = DailyStats(
                timeSpent = Time.fromDecimal(1.0f),
                projectsWorkedOn = listOf(),
                mostUsedLanguage = "",
                mostUsedEditor = "",
                mostUsedOs = "",
                date = todaysDate,
            )
        )
    }
}
