package com.jacob.wakatimeapp.home.usecases

import arrow.core.getOrHandle
import arrow.core.left
import arrow.core.right
import com.jacob.wakatimeapp.core.models.DailyStats
import com.jacob.wakatimeapp.core.models.Error.UnknownError
import com.jacob.wakatimeapp.core.models.StatsRange
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.WeeklyStats
import com.jacob.wakatimeapp.home.data.local.HomePageCache
import com.jacob.wakatimeapp.home.data.network.HomePageNetworkData
import com.jacob.wakatimeapp.home.domain.InstantProvider
import com.jacob.wakatimeapp.home.domain.models.HomePageUiData
import com.jacob.wakatimeapp.home.domain.usecases.GetLast7DaysStatsUC
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.mockk
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetLast7DaysStatsUCTest {
    private lateinit var useCase: GetLast7DaysStatsUC

    private val networkDataMock: HomePageNetworkData = mockk(relaxUnitFun = true)
    private val cacheMock: HomePageCache = mockk(relaxUnitFun = true)

    private val previousDay = Clock.System.now()
        .minus(1.days)

    private val validDataInstant = Clock.System.now()
        .minus(10.minutes)

    private val invalidDataInstant = Clock.System.now()
        .minus(20.minutes)

    private val homePageUiData = HomePageUiData(
        timeSpentToday = Time.ZERO,
        projectsWorkedOn = listOf(),
        weeklyTimeSpent = mapOf(),
        mostUsedLanguage = "",
        mostUsedEditor = "",
        mostUsedOs = ""
    )

    private val weeklyStats = WeeklyStats(
        totalTime = Time.ZERO,
        dailyStats = listOf(),
        range = StatsRange(
            startDate = LocalDate(2022, 10, 10),
            endDate = LocalDate(2022, 10, 10)
        ),
        todaysStats = DailyStats(
            timeSpent = Time.fromDecimal(1.0f),
            projectsWorkedOn = listOf(),
            mostUsedLanguage = "",
            mostUsedEditor = "",
            mostUsedOs = "",
            date = LocalDate(2022, 10, 10),
        )
    )

    @BeforeEach
    internal fun setUp() {
        clearMocks(networkDataMock, cacheMock)

        useCase = GetLast7DaysStatsUC(
            dispatcher = UnconfinedTestDispatcher(),
            homePageNetworkData = networkDataMock,
            homePageCache = cacheMock,
            instantProvider = object : InstantProvider {
                override val timeZone = TimeZone.UTC

                override fun now() = Clock.System.now()
            },
        )
    }

    @Test
    internal fun `when making first call of the day, then api call is made`() = runTest {
        coEvery { cacheMock.getLastRequestTime() } returns previousDay
        coEvery { networkDataMock.getLast7DaysStats() } returns weeklyStats.right()
        coEvery { cacheMock.getCachedData() } returns flowOf(homePageUiData.right())

        useCase().collect()

        coVerify(exactly = 1) {
            networkDataMock.getLast7DaysStats()
        }
    }

    @Test
    internal fun `when making first call of the day, data from cache is sent after getting from api`() =
        runTest {
            val cachedStats = homePageUiData.copy()

            coEvery { cacheMock.getLastRequestTime() } returns previousDay
            coEvery { networkDataMock.getLast7DaysStats() } returns weeklyStats.right()
            coEvery { cacheMock.getCachedData() } returns flowOf(cachedStats.right())

            val results = useCase().toList()

            val cachedStatsResult = results.first()
                .getOrHandle { error("failed") }

            results.size shouldBe 1
            cachedStatsResult shouldBeSameInstanceAs cachedStats
            cachedStatsResult shouldNotBeSameInstanceAs homePageUiData

            coVerify(exactly = 1) {
                cacheMock.getCachedData()
            }
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

            useCase = GetLast7DaysStatsUC(
                dispatcher = UnconfinedTestDispatcher(),
                homePageNetworkData = networkDataMock,
                homePageCache = cacheMock,
                instantProvider = object : InstantProvider {
                    override val timeZone = TimeZone.UTC

                    override fun now() = startOfDay + 5.minutes
                },
            )

            coEvery { cacheMock.getLastRequestTime() } returns startOfDay - 5.minutes
            coEvery { networkDataMock.getLast7DaysStats() } returns weeklyStats.right()
            coEvery { cacheMock.getCachedData() } returns flowOf(homePageUiData.right())

            useCase().collect()

            coVerify(exactly = 1) {
                networkDataMock.getLast7DaysStats()
            }
        }

    @Test
    internal fun `when valid data is available in cache, no api call is made`() = runTest {
        coEvery { cacheMock.getLastRequestTime() } returns validDataInstant
        coEvery { cacheMock.getCachedData() } returns flowOf(homePageUiData.right())

        useCase().collect()

        coVerify(exactly = 0) {
            networkDataMock.getLast7DaysStats()
            cacheMock.updateLastRequestTime(any())
        }

        coVerify(exactly = 1) {
            cacheMock.getCachedData()
        }
    }

    @Test
    internal fun `when invalid data is present in cache, api call is made`() = runTest {
        val cachedStats = homePageUiData.copy()

        coEvery { cacheMock.getLastRequestTime() } returns invalidDataInstant
        coEvery { networkDataMock.getLast7DaysStats() } returns weeklyStats.right()
        coEvery { cacheMock.getCachedData() } returns flowOf(cachedStats.right())

        useCase().collect()

        coVerifyOrder {
            cacheMock.getCachedData()
            networkDataMock.getLast7DaysStats()
            cacheMock.updateLastRequestTime(any())
        }
    }

    @Test
    @Disabled("cannot test with hard coded flow, need instrumented test")
    internal fun `when invalid data is present in cache, then first updated data is sent followed by new data`() =
        runTest {
            val cachedStats = homePageUiData.copy(timeSpentToday = Time.fromDecimal(2.0f))

            coEvery { cacheMock.getLastRequestTime() } returns invalidDataInstant
            coEvery { networkDataMock.getLast7DaysStats() } returns weeklyStats.right()
            coEvery { cacheMock.getCachedData() } returns flowOf(cachedStats.right())

            useCase().collect {
                println(it)
            }

            coVerifyOrder {
                cacheMock.getCachedData()
                networkDataMock.getLast7DaysStats()
                cacheMock.updateCache(homePageUiData)
                cacheMock.updateLastRequestTime(any())
            }
        }

    @Test
    internal fun `when making first request of the day and request fails, then error is propagated`() =
        runTest {
            val error = UnknownError("error")
                .left()

            coEvery { cacheMock.getLastRequestTime() } returns previousDay
            coEvery { networkDataMock.getLast7DaysStats() } returns error
            coEvery { cacheMock.getCachedData() } returns flowOf()

            val responses = useCase().toList()

            responses.size shouldBe 1
            responses shouldContain error
        }

    @Test
    internal fun `when api request during invalid data is made and request fails, then old data is shown and error is sent after that`() =
        runTest {
            val cachedStats = homePageUiData.copy()
            val error = UnknownError("error")
                .left()

            coEvery { cacheMock.getLastRequestTime() } returns invalidDataInstant
            coEvery { networkDataMock.getLast7DaysStats() } returns error
            coEvery { cacheMock.getCachedData() } returns flowOf(cachedStats.right())

            val results = useCase().toList()

            results.size shouldBe 2
            results[0] shouldBe cachedStats.right()
            results[1] shouldBe error
        }
}
