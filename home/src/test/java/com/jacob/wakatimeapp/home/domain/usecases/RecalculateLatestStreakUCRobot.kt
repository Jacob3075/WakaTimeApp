package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.Either
import com.jacob.wakatimeapp.core.common.data.local.WakaTimeAppDB
import com.jacob.wakatimeapp.core.models.DailyStats
import com.jacob.wakatimeapp.core.models.DailyStatsAggregate
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.project.Project
import com.jacob.wakatimeapp.home.domain.models.Streak
import io.kotest.assertions.asClue
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

internal class RecalculateLatestStreakUCRobot {
    private lateinit var useCase: RecalculateLatestStreakUC

    private val mockWakaTimeAppDb: WakaTimeAppDB = mockk()

    private var result: Either<Error, Streak>? = null

    fun buildService() = apply {
        clearMocks(mockWakaTimeAppDb)
        result = null

        useCase = RecalculateLatestStreakUC(mockWakaTimeAppDb)
    }

    suspend fun calculate(start: LocalDate, batchSize: DatePeriod) = apply {
        result = useCase(
            start,
            batchSize = batchSize,
        )
    }

    fun resultShouldBe(expected: Either<Error, Streak>) = apply {
        result.asClue {
            result shouldBe expected
        }
    }

    fun mockGetDataForRange(start: LocalDate, end: LocalDate, data: Either<Error, DailyStatsAggregate>) =
        apply {
            coEvery { mockWakaTimeAppDb.getAggregateStatsForRange(start, end) } returns data
        }

    fun verifyGetDataForRange(start: LocalDate, end: LocalDate, count: Int = 1) = apply {
        coVerify(exactly = count) { mockWakaTimeAppDb.getAggregateStatsForRange(start, end) }
    }

    companion object {
        fun createDayWithProjects(size: Int, days: List<Int>, end: LocalDate) = DailyStatsAggregate(
            List(size) {
                DailyStats(
                    date = end.plus(it, DateTimeUnit.DAY),
                    timeSpent = if (it in days) Time.fromDecimal(1f) else Time.ZERO,
                    projectsWorkedOn = emptyList<Project>().toImmutableList(),
                    mostUsedLanguage = "",
                    mostUsedEditor = "",
                    mostUsedOs = "",
                )
            }
        )
    }
}
