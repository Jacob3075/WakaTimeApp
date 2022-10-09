package com.jacob.wakatimeapp.home.usecases

import arrow.core.getOrHandle
import arrow.core.right
import com.jacob.wakatimeapp.core.models.DailyStats
import com.jacob.wakatimeapp.core.models.StatsRange
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.WeeklyStats
import com.jacob.wakatimeapp.home.data.local.HomePageCache
import com.jacob.wakatimeapp.home.data.network.HomePageNetworkData
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.mockk
import java.time.Instant
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Date
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetLast7DaysStatsUCTest {
    private lateinit var useCase: GetLast7DaysStatsUC

    private val networkDataMock: HomePageNetworkData = mockk(relaxUnitFun = true)
    private val cacheMock: HomePageCache = mockk(relaxUnitFun = true)

    private val previousDay = Instant.now()
        .minus(1, ChronoUnit.DAYS)

    private val validDataInstant = Instant.now()
        .minus(10, ChronoUnit.MINUTES)

    private val invalidDataInstant = Instant.now()
        .minus(20, ChronoUnit.MINUTES)

    private val weeklyStats = WeeklyStats(
        totalTime = Time.fromDecimal(1.0f),
        dailyStats = listOf(),
        range = StatsRange(
            startDate = Date.from(Instant.now()),
            endDate = Date.from(Instant.now())
        ),
        todaysStats = DailyStats(
            timeSpent = Time.fromDecimal(1.0f),
            projectsWorkedOn = listOf(),
            mostUsedLanguage = "",
            mostUsedEditor = "",
            mostUsedOs = "",
            date = LocalDate.now(),
        )
    )

    @BeforeEach
    internal fun setUp() {
        clearMocks(networkDataMock, cacheMock)

        useCase = GetLast7DaysStatsUC(
            dispatcher = UnconfinedTestDispatcher(),
            homePageNetworkData = networkDataMock,
            homePageCache = cacheMock,
        )
    }

    @Test
    internal fun `when network api is called, cache should always be updated`() = runTest {
        coEvery { cacheMock.getLastRequestTime() } returns previousDay
        coEvery { networkDataMock.getLast7DaysStats() } returns weeklyStats.right()
        coEvery { cacheMock.getCachedData() } returns flowOf(weeklyStats)

        val responses = useCase().toList()

        responses.size shouldBe 1
        responses shouldContain weeklyStats.right()

        coVerifyOrder {
            cacheMock.getLastRequestTime()
            networkDataMock.getLast7DaysStats()
            cacheMock.updateCache(weeklyStats)
            cacheMock.updateLastRequestTime(any())
            cacheMock.getCachedData()
        }
    }

    @Test
    internal fun `when making first call of the day, then api call is made`() = runTest {
        coEvery { cacheMock.getLastRequestTime() } returns previousDay
        coEvery { networkDataMock.getLast7DaysStats() } returns weeklyStats.right()
        coEvery { cacheMock.getCachedData() } returns flowOf(weeklyStats)

        useCase().collect()

        coVerify(exactly = 1) {
            networkDataMock.getLast7DaysStats()
        }
    }

    @Test
    internal fun `when making first call of the day, data from cache is sent after getting from api`() =
        runTest {
            val cachedStats = weeklyStats.copy()

            coEvery { cacheMock.getLastRequestTime() } returns previousDay
            coEvery { networkDataMock.getLast7DaysStats() } returns weeklyStats.right()
            coEvery { cacheMock.getCachedData() } returns flowOf(cachedStats)

            val results = useCase().toList()

            val cachedStatsResult = results.first()
                .getOrHandle { error("failed") }

            results.size shouldBe 1
            cachedStatsResult shouldBeSameInstanceAs cachedStats
            cachedStatsResult shouldNotBeSameInstanceAs weeklyStats

            coVerify(exactly = 1) {
                cacheMock.getCachedData()
            }
        }

    @Test
    @Disabled
    internal fun `when making first request of the day but data is valid, api request is made`() =
        runTest {
            TODO("add parameter to provide instant into use case")
        }

    @Test
    internal fun `when valid data is available in cache, no api call is made`() = runTest {
        coEvery { cacheMock.getLastRequestTime() } returns validDataInstant
        coEvery { cacheMock.getCachedData() } returns flowOf(weeklyStats)

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
        val cachedStats = weeklyStats.copy()

        coEvery { cacheMock.getLastRequestTime() } returns invalidDataInstant
        coEvery { networkDataMock.getLast7DaysStats() } returns weeklyStats.right()
        coEvery { cacheMock.getCachedData() } returns flowOf(cachedStats)

        useCase().collect()

        coVerifyOrder {
            cacheMock.getCachedData()
            networkDataMock.getLast7DaysStats()
            cacheMock.updateLastRequestTime(any())
        }
    }

    @Test
    @Disabled
    internal fun `when invalid data is present in cache, then first updated data is sent followed by new data`() =
        runTest {
            val cachedStats = weeklyStats.copy(totalTime = Time.fromDecimal(2.0f))

            coEvery { cacheMock.getLastRequestTime() } returns invalidDataInstant
            coEvery { networkDataMock.getLast7DaysStats() } returns weeklyStats.right()
            coEvery { cacheMock.getCachedData() } returns flowOf(cachedStats)

            useCase().collect() {
                println(it)
            }

            coVerifyOrder {
                cacheMock.getCachedData()
                networkDataMock.getLast7DaysStats()
                cacheMock.updateCache(weeklyStats)
                cacheMock.updateLastRequestTime(any())
            }
        }

    // todo error cases
}
