package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.Either
import com.jacob.wakatimeapp.core.common.today
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.home.data.local.HomePageCache
import com.jacob.wakatimeapp.home.domain.models.Last7DaysStats
import com.jacob.wakatimeapp.home.domain.models.StreakRange
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
internal class CalculateCurrentStreakUCRobot {
    private lateinit var useCase: CalculateCurrentStreakUC

    private var result: Either<Error, StreakRange>? = null

    private val mockCache: HomePageCache = mockk(relaxUnitFun = true)

    fun buildUseCase() = apply {
        clearMocks(mockCache)
        result = null

        useCase = CalculateCurrentStreakUC(
            dispatcher = UnconfinedTestDispatcher(),
            homePageCache = mockCache,
        )
    }

    suspend fun callUseCase() = apply {
        result = useCase()
    }

    fun resultsShouldBe(expected: Error?) = apply {
        result shouldBe expected
    }

    fun mockGetCurrentStreak(data: Either<Error, StreakRange>) = apply {
        coEvery { mockCache.getCurrentStreak() } returns flowOf(data)
    }

    fun mockGetLast7DaysStats(data: Either<Error, Last7DaysStats>) = apply {
        coEvery { mockCache.getLast7DaysStats() } returns flowOf(data)
    }

    fun verifyUpdateCurrentStreakCalled(data: StreakRange, count: Int = 1) = apply {
        coVerify(exactly = count) { mockCache.updateCurrentStreak(data) }
    }

    companion object {
        val streakRange = StreakRange.ZERO
        val today = LocalDate.today

        val last7DaysStats = Last7DaysStats(
            timeSpentToday = Time.ZERO,
            projectsWorkedOn = listOf(),
            weeklyTimeSpent = mutableMapOf(),
            mostUsedLanguage = "",
            mostUsedEditor = "",
            mostUsedOs = ""
        )
    }
}
