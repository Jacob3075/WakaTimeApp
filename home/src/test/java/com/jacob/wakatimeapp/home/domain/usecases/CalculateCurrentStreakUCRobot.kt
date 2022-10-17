package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.Either
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.home.data.local.HomePageCache
import com.jacob.wakatimeapp.home.domain.models.Last7DaysStats
import com.jacob.wakatimeapp.home.domain.models.StreakRange
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
internal class CalculateCurrentStreakUCRobot {
    private lateinit var useCase: CalculateCurrentStreakUC

    private val results: MutableList<Either<Error, StreakRange>> = mutableListOf()

    private val mockCache: HomePageCache = mockk(relaxUnitFun = true)

    fun buildUseCase() = apply {
        clearMocks(mockCache)

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

    fun resultsShouldContain(expected: List<Either<Error, StreakRange>>) = apply {
        results shouldContainExactly expected
    }

    fun mockGetCurrentStreak(data: Either<Error, StreakRange>) = apply {
        coEvery { mockCache.getCurrentStreak() } returns flowOf(data)
    }

    fun mockGetLast7DaysStats(data: Either<Error, Last7DaysStats>) = apply {
        coEvery { mockCache.getLast7DaysStats() } returns flowOf(data)
    }

    fun verifyUpdateCurrentStreakCalled(data: StreakRange) = apply {
        coVerify { mockCache.updateCurrentStreak(data) }
    }
}
