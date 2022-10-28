package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.Either
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.home.data.local.HomePageCache
import com.jacob.wakatimeapp.home.domain.InstantProvider
import com.jacob.wakatimeapp.home.domain.RecalculateLatestStreakService
import com.jacob.wakatimeapp.home.domain.models.Last7DaysStats
import com.jacob.wakatimeapp.home.domain.models.StreakRange
import io.kotest.assertions.asClue
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

internal class CalculateCurrentStreakUCRobot {
    private lateinit var useCase: CalculateCurrentStreakUC

    private var result: Either<Error, StreakRange>? = null

    private val mockCache: HomePageCache = mockk(relaxUnitFun = true)
    private val mockRecalculateStreak: RecalculateLatestStreakService = mockk()

    fun buildUseCase() = apply {
        clearMocks(mockCache, mockRecalculateStreak)
        result = null

        useCase = CalculateCurrentStreakUC(
            instantProvider = object : InstantProvider {
                override val timeZone = TimeZone.UTC

                override fun now() = currentDayInstant
            },
            homePageCache = mockCache,
            recalculateLatestStreakService = mockRecalculateStreak,
        )
    }

    suspend fun callUseCase() = apply {
        result = useCase()
    }

    fun resultsShouldBe(expected: Either<Error, StreakRange>) = apply {
        result.asClue {
            it shouldBe expected
        }
    }

    fun mockGetCurrentStreak(data: Either<Error, StreakRange>) = apply {
        coEvery { mockCache.getCurrentStreak() } returns flowOf(data)
    }

    fun mockGetLast7DaysStats(data: Either<Error, Last7DaysStats>) = apply {
        coEvery { mockCache.getLast7DaysStats() } returns flowOf(data)
    }

    fun mockRecalculateStreak(start: LocalDate, result: Either<Error, StreakRange>) = apply {
        coEvery { mockRecalculateStreak.calculate(start, any(), any()) } returns result
    }

    internal companion object {

        /**
         * Start of a random day
         *
         * Value:
         *  - date: 11/10/2022 (dd/mm/yyyy)
         *  - time: 00:00:00 (hh:mm::ss)
         */
        private val startOfDay = Instant.parse("2022-10-11T00:00:00Z")

        val currentDayInstant = startOfDay + 1.hours + 30.minutes

        val currentDay = currentDayInstant.toLocalDateTime(TimeZone.UTC).date

        val noWeeklyStats = mapOf(
            currentDay to Time.ZERO,
            currentDay.minus(1, DateTimeUnit.DAY) to Time.ZERO,
            currentDay.minus(2, DateTimeUnit.DAY) to Time.ZERO,
            currentDay.minus(3, DateTimeUnit.DAY) to Time.ZERO,
            currentDay.minus(4, DateTimeUnit.DAY) to Time.ZERO,
            currentDay.minus(5, DateTimeUnit.DAY) to Time.ZERO,
            currentDay.minus(6, DateTimeUnit.DAY) to Time.ZERO,
        )

        val continuousWeeklyStats = mutableMapOf(
            currentDay to Time.fromDecimal(1f),
            currentDay.minus(1, DateTimeUnit.DAY) to Time.fromDecimal(1f),
            currentDay.minus(2, DateTimeUnit.DAY) to Time.fromDecimal(1f),
            currentDay.minus(3, DateTimeUnit.DAY) to Time.fromDecimal(1f),
            currentDay.minus(4, DateTimeUnit.DAY) to Time.fromDecimal(1f),
            currentDay.minus(5, DateTimeUnit.DAY) to Time.fromDecimal(1f),
            currentDay.minus(6, DateTimeUnit.DAY) to Time.fromDecimal(1f),
        )

        val last7DaysStats = Last7DaysStats(
            timeSpentToday = Time.ZERO,
            projectsWorkedOn = listOf(),
            weeklyTimeSpent = noWeeklyStats,
            mostUsedLanguage = "",
            mostUsedEditor = "",
            mostUsedOs = ""
        )
    }
}
