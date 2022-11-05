package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.left
import arrow.core.right
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.home.domain.usecases.GetLast7DaysStatsUCRobot.Companion.last7DaysStats
import com.jacob.wakatimeapp.home.domain.usecases.GetLast7DaysStatsUCRobot.Companion.weeklyStats
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetLast7DaysStatsUCTest {
    private val useCaseRobot: GetLast7DaysStatsUCRobot = GetLast7DaysStatsUCRobot()

    @Test
    internal fun `when network request returns valid data, then return null`() = runTest {
        useCaseRobot.buildViewModel()
            .mockNetworkData(weeklyStats.right())
            .callUseCase()
            .resultsShouldBe(last7DaysStats.right())
    }

    @Test
    internal fun `when network request fails with an error, then return the error`() = runTest {
        val error = Error.UnknownError("error").left()

        useCaseRobot.buildViewModel()
            .mockNetworkData(error)
            .callUseCase()
            .resultsShouldBe(error)
    }
}
