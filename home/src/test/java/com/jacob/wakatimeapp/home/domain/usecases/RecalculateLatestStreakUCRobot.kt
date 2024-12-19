package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.Either
import com.jacob.wakatimeapp.core.common.data.local.WakaTimeAppDB
import com.jacob.wakatimeapp.core.common.data.local.entities.DayEntity
import com.jacob.wakatimeapp.core.common.data.local.entities.DayWithProjects
import com.jacob.wakatimeapp.core.common.data.local.entities.ProjectPerDay
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.secondarystats.Editors
import com.jacob.wakatimeapp.core.models.secondarystats.Languages
import com.jacob.wakatimeapp.core.models.secondarystats.OperatingSystems
import com.jacob.wakatimeapp.core.models.Streak
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

    fun mockGetDataForRange(start: LocalDate, end: LocalDate, data: Either<Error, List<DayWithProjects>>) =
        apply {
            coEvery { mockWakaTimeAppDb.getStatsForRange(start, end) } returns data
        }

    fun verifyGetDataForRange(start: LocalDate, end: LocalDate, count: Int = 1) = apply {
        coVerify(exactly = count) { mockWakaTimeAppDb.getStatsForRange(start, end) }
    }

    companion object {
        fun createDayWithProjects(size: Int, days: List<Int>, end: LocalDate): List<DayWithProjects> {
            return List(size) {
                DayWithProjects(
                    projectsForDay = emptyList<ProjectPerDay>().toImmutableList(),
                    day = DayEntity(
                        date = end.plus(it, DateTimeUnit.DAY),
                        grandTotal = if (it in days) Time.fromDecimal(1f) else Time.ZERO,
                        languages = Languages(values = emptyList()),
                        editors = Editors(values = emptyList()),
                        operatingSystems = OperatingSystems(values = emptyList()),
                        machines = listOf(),
                    ),
                )
            }
        }
    }
}
