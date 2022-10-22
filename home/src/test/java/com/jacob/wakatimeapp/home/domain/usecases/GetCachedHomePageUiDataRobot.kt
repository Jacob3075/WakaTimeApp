package com.jacob.wakatimeapp.home.domain.usecases

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.testIn
import arrow.core.Either
import com.jacob.wakatimeapp.core.common.auth.AuthDataStore
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.UserDetails
import com.jacob.wakatimeapp.home.data.local.HomePageCache
import com.jacob.wakatimeapp.home.domain.InstantProvider
import com.jacob.wakatimeapp.home.domain.models.Last7DaysStats
import com.jacob.wakatimeapp.home.domain.models.StreakRange
import com.jacob.wakatimeapp.home.domain.usecases.GetCachedHomePageUiDataRobot.Companion.currentDayInstant
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetCachedHomePageUiDataRobot {
    private lateinit var useCase: GetCachedHomePageUiData

    private var receiveTurbine: ReceiveTurbine<Either<Error, CachedHomePageUiData?>>? = null

    private val mockHomePageCache: HomePageCache = mockk()
    private val mockAuthDataStore: AuthDataStore = mockk()

    fun buildUseCase(currentInstant: Instant = currentDayInstant) = apply {
        clearMocks(mockHomePageCache, mockAuthDataStore)
        receiveTurbine = null

        useCase = GetCachedHomePageUiData(
            instantProvider = object : InstantProvider {
                override val timeZone = TimeZone.UTC

                override fun now() = currentInstant
            },
            homePageCache = mockHomePageCache,
            authDataStore = mockAuthDataStore,
        )
    }

    fun callUseCase(testScope: TestScope) = apply {
        receiveTurbine = useCase().testIn(testScope)
    }

    suspend fun withNextItem(
        block: context(ItemAssertionContext) GetCachedHomePageUiDataRobot.() -> Unit,
    ) = apply {
        val item = receiveTurbine!!.awaitItem()
        val context = object : ItemAssertionContext {
            override val item = item
        }

        block(context, this@GetCachedHomePageUiDataRobot)
    }

    context (ItemAssertionContext)
    fun itemShouldBe(expected: Either<Error, CachedHomePageUiData?>) = apply {
        item shouldBe expected
    }

    context (ItemAssertionContext)
    fun itemShouldBeRight() = apply {
        item.shouldBeRight()
    }

    context (ItemAssertionContext)
    fun itemShouldBeLeft() = apply {
        item.shouldBeLeft()
    }

    context (ItemAssertionContext)
    fun itemShouldNotBeNull() = apply {
        item.fold(
            ifLeft = { 1 shouldBe 2 },
            ifRight = { it.shouldNotBeNull() }
        )
    }

    context (ItemAssertionContext)
    fun itemShouldBeNull() = apply {
        item.fold(
            ifLeft = { 1 shouldBe 2 },
            ifRight = { it.shouldBeNull() }
        )
    }

    context (ItemAssertionContext)
    fun itemShouldNotBeStale() = apply {
        item.map { it!!.isStateData } shouldBeRight false
    }

    context (ItemAssertionContext)
    fun itemShouldBeStale() = apply {
        item.map { it!!.isStateData } shouldBeRight true
    }

    suspend fun expectNoMoreItems() = apply {
        receiveTurbine!!.awaitComplete()
    }

    fun setLastRequestTime(previousDay: Instant) = apply {
        coEvery { mockHomePageCache.getLastRequestTime() } returns flowOf(previousDay)
    }

    fun mockUserDetails(userDetails: UserDetails) = apply {
        coEvery { mockAuthDataStore.getUserDetails() } returns flowOf(userDetails)
    }

    fun mockLast7DaysStats(last7DaysStats: Either<Error, Last7DaysStats>) = apply {
        coEvery { mockHomePageCache.getLast7DaysStats() } returns flowOf(last7DaysStats)
    }

    fun mockCurrentStreak(currentStreak: Either<Error, StreakRange>) = apply {
        coEvery { mockHomePageCache.getCurrentStreak() } returns flowOf(currentStreak)
    }

    companion object {

        /**
         * Start of a random day
         *
         * Value:
         *  - date: 11/10/2022 (dd/mm/yyyy)
         *  - time: 00:00:00 (hh:mm::ss)
         */
        val startOfDay = Instant.parse("2022-10-11T00:00:00Z")

        val currentDayInstant = startOfDay + 1.hours + 30.minutes

        /**
         * Takes [currentDayInstant] and subtracts 1 day from it
         */
        val previousDayInstant = currentDayInstant.minus(1.days)

        val currentStreak = StreakRange.ZERO

        val last7DaysStats = Last7DaysStats(
            timeSpentToday = Time.ZERO,
            projectsWorkedOn = listOf(),
            weeklyTimeSpent = mapOf(),
            mostUsedLanguage = "",
            mostUsedEditor = "",
            mostUsedOs = ""

        )

        val userDetails = UserDetails(
            fullName = "John Doe",
            photoUrl = "https://example.com/photo.jpg",
            email = "",
            bio = "",
            id = "",
            timeout = 0,
            timezone = "",
            username = "",
            displayName = "",
            lastProject = "",
            durationsSliceBy = "",
            createdAt = "",
            dateFormat = "",
        )
    }

    interface ItemAssertionContext {
        val item: Either<Error, CachedHomePageUiData?>
    }
}
