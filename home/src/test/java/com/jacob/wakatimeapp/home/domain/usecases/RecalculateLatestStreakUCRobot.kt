package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.Either
import com.jacob.wakatimeapp.core.models.DailyStats
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.Stats
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.home.data.network.HomePageNetworkData
import com.jacob.wakatimeapp.home.domain.models.StreakRange
import io.kotest.assertions.asClue
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

internal class RecalculateLatestStreakUCRobot {
    private lateinit var useCase: RecalculateLatestStreakUC

    private val mockHomePageNetworkData: HomePageNetworkData = mockk()

    private var result: Either<Error, StreakRange>? = null

    fun buildService() = apply {
        clearMocks(mockHomePageNetworkData)
        result = null

        useCase = RecalculateLatestStreakUC(mockHomePageNetworkData)
    }

    suspend fun calculate(start: LocalDate, value: Int, unit: DateTimeUnit.DateBased) = apply {
        result = useCase.calculate(
            start,
            value,
            unit,
        )
    }

    fun resultShouldBe(expected: Either<Error, StreakRange>) = apply {
        result.asClue {
            result shouldBe expected
        }
    }

    fun mockGetDataForRange(start: String, end: String, data: Either<Error, Stats>) = apply {
        coEvery { mockHomePageNetworkData.getStatsForRange(start, end) } returns data
    }

    fun verifyGetDataForRange(start: String, end: String, count: Int = 1) = apply {
        coVerify(exactly = count) { mockHomePageNetworkData.getStatsForRange(start, end) }
    }

    companion object {
        fun createDailyStats(size: Int, days: List<Int>, end: LocalDate) = List(size) {
            DailyStats(
                timeSpent = if (it in days) Time.fromDecimal(1f) else Time.ZERO,
                projectsWorkedOn = emptyList(),
                mostUsedLanguage = "",
                mostUsedEditor = "",
                mostUsedOs = "",
                date = end.plus(it, DateTimeUnit.DAY)
            )
        }
    }
}
