package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.Either
import arrow.core.right
import com.jacob.wakatimeapp.core.common.auth.AuthDataStore
import com.jacob.wakatimeapp.core.common.utils.InstantProvider
import com.jacob.wakatimeapp.core.models.DailyStats
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.Project
import com.jacob.wakatimeapp.core.models.Stats
import com.jacob.wakatimeapp.core.models.StatsRange
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.UserDetails
import com.jacob.wakatimeapp.home.data.local.HomePageCache
import com.jacob.wakatimeapp.home.data.network.HomePageNetworkData
import com.jacob.wakatimeapp.home.domain.models.Streak
import io.kotest.assertions.asClue
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone

@OptIn(ExperimentalCoroutinesApi::class)
internal class CalculateLongestStreakUCRobot {
    private lateinit var calculateLongestStreakUC: CalculateLongestStreakUC
    private var result: Either<Error, Streak>? = null

    private val mockHomePageNetworkData: HomePageNetworkData = mockk()
    private val mockHomePageCache: HomePageCache = mockk(relaxUnitFun = true)
    private val mockAuthDataStore: AuthDataStore = mockk(relaxUnitFun = true)

    fun buildUseCase(
        currentInstant: Instant = defaultCurrentInstant,
        dispatcher: TestDispatcher,
    ) = apply {
        clearMocks(mockHomePageCache, mockHomePageNetworkData, mockAuthDataStore)
        result = null

        calculateLongestStreakUC = CalculateLongestStreakUC(
            homePageNetworkData = mockHomePageNetworkData,
            homePageCache = mockHomePageCache,
            authDataStore = mockAuthDataStore,
            dispatcher = dispatcher,
            instantProvider = object : InstantProvider {
                override val timeZone: TimeZone
                    get() = TimeZone.UTC

                override fun now() = currentInstant
            },
        )
    }

    suspend fun callUseCase(batchSize: DatePeriod) = apply {
        result = calculateLongestStreakUC(batchSize)
    }

    fun resultShouldBe(expected: Either<Error, Streak>) = apply {
        result.asClue {
            it shouldBe expected
        }
    }

    fun mockGetUserDetails(userCreatedAt: LocalDate? = null) = apply {
        coEvery { mockAuthDataStore.userDetails } returns flowOf(
            userCreatedAt?.let { USER_DETAILS.copy(createdAt = it) }
                ?: USER_DETAILS,
        )
    }

    fun mockHomePageCacheGetLongestStreak(
        data: Either<Error, Streak> = Streak.ZERO.right(),
    ) =
        apply {
            coEvery { mockHomePageCache.getLongestStreak() } returns flowOf(data)
        }

    fun mockHomePageCacheGetCurrentStreak(
        data: Either<Error, Streak> = Streak.ZERO.right(),
    ) =
        apply {
            coEvery { mockHomePageCache.getCurrentStreak() } returns flowOf(data)
        }

    fun mockGetStatsForRange(
        start: String? = null,
        end: String? = null,
        data: Either<Error, Stats>,
    ) = apply {
        coEvery {
            mockHomePageNetworkData.getStatsForRange(
                start ?: any(),
                end ?: any(),
            )
        } returns data
    }

    fun verifyGetStatsForRangeCalled(count: Int, start: String? = null, end: String? = null) =
        apply {
            coVerify(exactly = count) {
                mockHomePageNetworkData.getStatsForRange(
                    start ?: any(),
                    end ?: any(),
                )
            }
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
            photoUrl = "",
        )

        val defaultCurrentInstant = Instant.parse("2022-10-01T00:00:00Z")

        val dailyStats = DailyStats(
            timeSpent = Time.fromDecimal(1f),
            projectsWorkedOn = listOf<Project>().toImmutableList(),
            mostUsedLanguage = "",
            mostUsedEditor = "",
            mostUsedOs = "",
            date = LocalDate(2022, 1, 1),
        )

        val stats = Stats(
            totalTime = Time.ZERO,
            dailyStats = List(30) {
                dailyStats.copy(
                    date = LocalDate(2022, 1, it + 1),
                )
            },
            range = StatsRange(
                startDate = LocalDate(1, 1, 1),
                endDate = LocalDate(1, 1, 1),
            ),
        )
    }
}
