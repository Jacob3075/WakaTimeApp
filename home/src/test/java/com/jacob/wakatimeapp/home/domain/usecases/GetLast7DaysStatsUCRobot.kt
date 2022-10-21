package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.Either
import com.jacob.wakatimeapp.core.models.DailyStats
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.StatsRange
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.WeeklyStats
import com.jacob.wakatimeapp.home.data.local.HomePageCache
import com.jacob.wakatimeapp.home.data.network.HomePageNetworkData
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetLast7DaysStatsUCRobot {
    private lateinit var useCase: GetLast7DaysStatsUC

    private var result: Error? = null

    private val networkDataMock: HomePageNetworkData = mockk(relaxUnitFun = true)
    private val cacheMock: HomePageCache = mockk(relaxUnitFun = true)

    fun buildViewModel() = apply {
        clearMocks(networkDataMock, cacheMock)
        result = null

        useCase = GetLast7DaysStatsUC(
            dispatcher = UnconfinedTestDispatcher(),
            homePageNetworkData = networkDataMock,
            homePageCache = cacheMock,
        )
    }

    suspend fun callUseCase() = apply {
        result = useCase()
    }

    fun resultsShouldBe(expected: Error?) = apply {
        result shouldBe expected
    }

    fun mockNetworkData(data: Either<Error, WeeklyStats>) = apply {
        coEvery { networkDataMock.getLast7DaysStats() } returns data
    }

    fun verifyUpdateLast7DaysStatsCacheCalled(count: Int = 1) = apply {
        coVerify(exactly = count) { cacheMock.updateLast7DaysStats(any()) }
    }

    companion object {
        private val todaysDate = LocalDate(2022, 10, 10)

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
