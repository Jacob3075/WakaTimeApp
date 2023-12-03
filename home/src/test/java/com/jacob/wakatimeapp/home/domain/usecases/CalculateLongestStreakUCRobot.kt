package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.Either
import arrow.core.right
import com.jacob.wakatimeapp.core.common.auth.AuthDataStore
import com.jacob.wakatimeapp.core.common.data.local.WakaTimeAppDB
import com.jacob.wakatimeapp.core.common.data.local.entities.DayEntity
import com.jacob.wakatimeapp.core.common.data.local.entities.DayWithProjects
import com.jacob.wakatimeapp.core.common.utils.InstantProvider
import com.jacob.wakatimeapp.core.models.DailyStats
import com.jacob.wakatimeapp.core.models.DailyStatsAggregate
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.Project
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.UserDetails
import com.jacob.wakatimeapp.core.models.secondarystats.Editors
import com.jacob.wakatimeapp.core.models.secondarystats.Languages
import com.jacob.wakatimeapp.core.models.secondarystats.OperatingSystems
import com.jacob.wakatimeapp.home.data.local.HomePageCache
import com.jacob.wakatimeapp.home.domain.models.Streak
import io.kotest.assertions.asClue
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone

internal class CalculateLongestStreakUCRobot {
    private lateinit var calculateLongestStreakUC: CalculateLongestStreakUC
    private var result: Either<Error, Streak>? = null

    private val mockHomePageCache: HomePageCache = mockk(relaxUnitFun = true)
    private val mockAuthDataStore: AuthDataStore = mockk(relaxUnitFun = true)
    private val mockWakaTimeAppDB: WakaTimeAppDB = mockk(relaxUnitFun = true)

    fun buildUseCase(
        currentInstant: Instant = defaultCurrentInstant,
    ) = apply {
        clearMocks(mockHomePageCache, mockAuthDataStore)
        result = null

        calculateLongestStreakUC = CalculateLongestStreakUC(
            homePageCache = mockHomePageCache,
            authDataStore = mockAuthDataStore,
            wakaTimeAppDB = mockWakaTimeAppDB,
            instantProvider = object : InstantProvider {
                override val timeZone: TimeZone
                    get() = TimeZone.UTC

                override fun now() = currentInstant
            },
        )
    }

    suspend fun callUseCase() = apply {
        result = calculateLongestStreakUC()
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
        start: LocalDate? = null,
        end: LocalDate? = null,
        data: Either<Error, List<DayWithProjects>>,
    ) = apply {
        coEvery {
            mockWakaTimeAppDB.getStatsForRange(
                start ?: any(),
                end ?: any(),
            )
        } returns data
    }

    fun verifyGetStatsForRangeCalled(count: Int, start: LocalDate? = null, end: LocalDate? = null) =
        apply {
            coVerify(exactly = count) {
                mockWakaTimeAppDB.getStatsForRange(
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
            timezone = TimeZone.currentSystemDefault(),
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

        val dailyStats1 = DailyStats(
            timeSpent = Time.fromDecimal(1f),
            projectsWorkedOn = listOf<Project>().toImmutableList(),
            mostUsedLanguage = "",
            mostUsedEditor = "",
            mostUsedOs = "",
            date = LocalDate(2022, 1, 1),
        )

        val dailyStats = DayEntity(
            dayId = 0,
            date = LocalDate(2022, 1, 1),
            grandTotal = Time.fromDecimal(1f),
            editors = Editors(listOf()),
            languages = Languages(listOf()),
            operatingSystems = OperatingSystems(listOf()),
            machines = listOf(),
        )
        val stats = listOf(
            DayWithProjects(
                day = dailyStats,
                projectsForDay = listOf(),

            ),
        )

        val stats1 = DailyStatsAggregate(
            values = List(30) {
                dailyStats1.copy(
                    date = LocalDate(2022, 1, it + 1),
                )
            },
        )
    }
}
