package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.Either
import com.jacob.wakatimeapp.core.common.today
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.home.data.local.HomePageCache
import com.jacob.wakatimeapp.home.domain.models.Last7DaysStats
import com.jacob.wakatimeapp.home.domain.models.StreakRange
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
internal class CalculateCurrentStreakUCRobot {
    private lateinit var useCase: CalculateCurrentStreakUC

    private val results: MutableList<Either<Error, StreakRange>> = mutableListOf()

    private val mockCache: HomePageCache = mockk(relaxUnitFun = true)

    fun buildUseCase() = apply {
        clearMocks(mockCache)
        results.clear()

        useCase = CalculateCurrentStreakUC(
            dispatcher = UnconfinedTestDispatcher(),
            homePageCache = mockCache,
        )
    }

    suspend fun callUseCase() = apply {
        useCase().toList(results)
    }

    fun resultSizeShouldBe(size: Int = 1) = apply {
        results.size shouldBe size
    }

    fun resultsShouldContain(expected: Either<Error, StreakRange>) = apply {
        results shouldContain expected
    }

    fun resultsShouldContain(expected: StreakRange) = apply {
        results.first() shouldBeRight expected
    }

    fun resultShouldBeRight() = apply {
        results.first().shouldBeRight()
    }

    fun resultsShouldContain(expected: Error) = apply {
        results.first() shouldBeLeft expected
    }

    fun resultShouldBeLeft() = apply {
        results.first().shouldBeLeft()
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
