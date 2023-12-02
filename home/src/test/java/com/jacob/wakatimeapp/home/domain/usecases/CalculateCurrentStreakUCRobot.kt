package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.Either
import com.jacob.wakatimeapp.core.common.utils.InstantProvider
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.Project
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.home.data.local.HomePageCache
import com.jacob.wakatimeapp.home.domain.models.Last7DaysStats
import com.jacob.wakatimeapp.home.domain.models.Streak
import io.kotest.assertions.asClue
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

internal class CalculateCurrentStreakUCRobot {
    private lateinit var useCase: CalculateCurrentStreakUC

    private var result: Either<Error, Streak>? = null

    private val mockCache: HomePageCache = mockk(relaxUnitFun = true)
    private val mockRecalculateStreak: RecalculateLatestStreakUC = mockk()

    fun buildUseCase() = apply {
        clearMocks(mockCache, mockRecalculateStreak)
        result = null

        useCase = CalculateCurrentStreakUC(
            instantProvider = object : InstantProvider {
                override val timeZone = TimeZone.UTC

                override fun now() = currentDayInstant
            },
            homePageCache = mockCache,
            recalculateLatestStreakUC = mockRecalculateStreak,
        )
    }

    suspend fun callUseCase(last7DaysStats: Last7DaysStats) = apply {
        result = useCase(last7DaysStats)
    }

    fun resultsShouldBe(expected: Either<Error, Streak>) = apply {
        result.asClue {
            it shouldBe expected
        }
    }

    fun mockGetCurrentStreak(data: Either<Error, Streak>) = apply {
        coEvery { mockCache.getCurrentStreak() } returns flowOf(data)
    }

    fun mockRecalculateStreak(start: LocalDate, result: Either<Error, Streak>) = apply {
        coEvery { mockRecalculateStreak.calculate(start, any()) } returns result
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
        ).toImmutableMap()

        val continuousWeeklyStats = mutableMapOf(
            currentDay to Time.fromDecimal(1f),
            currentDay.minus(1, DateTimeUnit.DAY) to Time.fromDecimal(1f),
            currentDay.minus(2, DateTimeUnit.DAY) to Time.fromDecimal(1f),
            currentDay.minus(3, DateTimeUnit.DAY) to Time.fromDecimal(1f),
            currentDay.minus(4, DateTimeUnit.DAY) to Time.fromDecimal(1f),
            currentDay.minus(5, DateTimeUnit.DAY) to Time.fromDecimal(1f),
            currentDay.minus(6, DateTimeUnit.DAY) to Time.fromDecimal(1f),
        ).toImmutableMap()

        val last7DaysStats = Last7DaysStats(
            timeSpentToday = Time.ZERO,
            projectsWorkedOn = emptyList<Project>().toImmutableList(),
            weeklyTimeSpent = noWeeklyStats.toImmutableMap(),
            mostUsedLanguage = "",
            mostUsedEditor = "",
            mostUsedOs = "",
        )
    }
}
