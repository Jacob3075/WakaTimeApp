package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.Either
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.UserDetails
import com.jacob.wakatimeapp.home.data.local.HomePageCache
import com.jacob.wakatimeapp.home.data.network.HomePageNetworkData
import com.jacob.wakatimeapp.home.domain.InstantProvider
import com.jacob.wakatimeapp.home.domain.models.StreakRange
import io.kotest.assertions.asClue
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone

internal class CalculateLongestStreakUCRobot {
    private lateinit var calculateLongestStreakUC: CalculateLongestStreakUC
    private var result: Either<Error, StreakRange>? = null

    private val mockHomePageNetworkData: HomePageNetworkData = mockk()
    private val mockHomePageCache: HomePageCache = mockk(relaxUnitFun = true)

    fun buildUseCase() = apply {
        clearMocks(mockHomePageCache, mockHomePageNetworkData)
        result = null

        calculateLongestStreakUC = CalculateLongestStreakUC(
            homePageNetworkData = mockHomePageNetworkData,
            homePageCache = mockHomePageCache,
            userDetails = USER_DETAILS,
            instantProvider = object : InstantProvider {
                override val timeZone: TimeZone
                    get() = TimeZone.UTC

                override fun now() = currentInstant
            }
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

    fun verifyHomePageCacheUpdateLongestStreakCalled(count: Int, data: StreakRange? = null) =
        apply {
            coVerify(exactly = count) { mockHomePageCache.updateLongestStreak(data ?: any()) }
        }

    companion object {
        private val USER_DETAILS = UserDetails(
            bio = "",
            email = "",
            id = "",
            timeout = 0,
            timezone = "",
            username = "",
            displayName = "",
            lastProject = "",
            fullName = "",
            durationsSliceBy = "",
            createdAt = LocalDate(2022, 1, 1),
            dateFormat = "",
            photoUrl = ""
        )

        val currentInstant = Instant.parse("2022-01-01T00:00:00Z")
    }
}
