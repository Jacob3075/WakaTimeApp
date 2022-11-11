package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.Either
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.home.data.local.HomePageCache
import com.jacob.wakatimeapp.home.data.network.HomePageNetworkData
import com.jacob.wakatimeapp.home.domain.models.StreakRange
import io.kotest.assertions.asClue
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf

internal class CalculateLongestStreakUCRobot {
    private lateinit var calculateLongestStreakUC: CalculateLongestStreakUC
    private var result: Either<Error, StreakRange>? = null

    private val mockHomePageNetworkData: HomePageNetworkData = mockk()
    private val mockHomePageCache: HomePageCache = mockk(relaxUnitFun = true)

    fun buildUseCase() = apply {
        clearMocks(mockHomePageCache, mockHomePageNetworkData)
        result = null

        calculateLongestStreakUC = CalculateLongestStreakUC(
            mockHomePageNetworkData,
            mockHomePageCache
        )
    }

    suspend fun callUseCase() = apply {
        result = calculateLongestStreakUC()
    }

    fun resultShouldBe(expected: Either<Error, StreakRange>) = apply {
        result.asClue {
            it shouldBe expected
        }
    }

    fun mockHomePageCacheGetLongestStreak(data: Either<Error, StreakRange>) = apply {
        coEvery { mockHomePageCache.getLongestStreak() } returns flowOf(data)
    }

    fun mockHomePageCacheGetCurrentStreak(data: Either<Error, StreakRange>) = apply {
        coEvery { mockHomePageCache.getCurrentStreak() } returns flowOf(data)
    }

    fun verifyHomePageCacheUpdateLongestStreakCalled(data: StreakRange) = apply {
        coVerify { mockHomePageCache.updateLongestStreak(data) }
    }
}
