package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.Either
import com.jacob.wakatimeapp.home.domain.usecases.GetCachedHomePageUiDataRobot.Companion.previousDayInstant
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetCachedHomePageUiDataTest {
    private val robot = GetCachedHomePageUiDataRobot()

    @Test
    internal fun `when last request was made the previous day, then use case should return null`() =
        runTest {
            robot.buildUseCase()
                .setLastRequestTime(previousDayInstant)
                .callUseCase()
                .resultsSizeShouldBe(1)
                .resultsShouldBe(listOf(Either.Right(null)))
        }
}
