package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.Either
import com.jacob.wakatimeapp.core.models.DailyStats
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.Project
import com.jacob.wakatimeapp.core.models.Range
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.WeeklyStats
import com.jacob.wakatimeapp.home.data.network.HomePageNetworkData
import com.jacob.wakatimeapp.home.domain.models.Last7DaysStats
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.datetime.LocalDate

internal class GetLast7DaysStatsUCRobot {
    private lateinit var useCase: GetLast7DaysStatsUC

    private var result: Either<Error, Last7DaysStats>? = null

    private val networkDataMock: HomePageNetworkData = mockk(relaxUnitFun = true)

    fun buildViewModel() = apply {
        clearMocks(networkDataMock)
        result = null

        useCase = GetLast7DaysStatsUC(
            homePageNetworkData = networkDataMock,
        )
    }

    suspend fun callUseCase() = apply {
        result = useCase()
    }

    fun resultsShouldBe(expected: Either<Error, Last7DaysStats>) = apply {
        result shouldBe expected
    }

    fun mockNetworkData(data: Either<Error, WeeklyStats>) = apply {
        coEvery { networkDataMock.getLast7DaysStats() } returns data
    }

    companion object {
        private val todaysDate = LocalDate(2022, 10, 10)

        val weeklyStats = WeeklyStats(
            totalTime = Time.ZERO,
            dailyStats = listOf<DailyStats>().toImmutableList(),
            range = Range(
                startDate = todaysDate,
                endDate = todaysDate,
            ),
            todaysStats = DailyStats(
                timeSpent = Time.fromDecimal(1.0f),
                projectsWorkedOn = listOf<Project>().toImmutableList(),
                mostUsedLanguage = "",
                mostUsedEditor = "",
                mostUsedOs = "",
                date = todaysDate,
            ),
        )

        val last7DaysStats = Last7DaysStats(
            timeSpentToday = Time.fromDecimal(1.0f),
            projectsWorkedOn = listOf<Project>().toImmutableList(),
            weeklyTimeSpent = mapOf<LocalDate, Time>().toImmutableMap(),
            mostUsedLanguage = "",
            mostUsedEditor = "",
            mostUsedOs = "",
        )
    }
}
