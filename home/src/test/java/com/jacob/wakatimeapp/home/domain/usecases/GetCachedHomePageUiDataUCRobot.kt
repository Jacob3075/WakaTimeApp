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
import com.jacob.wakatimeapp.home.domain.models.CachedHomePageUiData
import com.jacob.wakatimeapp.home.domain.models.Last7DaysStats
import com.jacob.wakatimeapp.home.domain.models.StreakRange
import com.jacob.wakatimeapp.home.domain.usecases.GetCachedHomePageUiDataUCRobot.Companion.currentDayInstant
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.assertions.asClue
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.TestScope
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetCachedHomePageUiDataUCRobot {
    private lateinit var useCase: GetCachedHomePageUiDataUC

    private var receiveTurbine: ReceiveTurbine<Either<Error, CachedHomePageUiData?>>? = null

    private val mockHomePageCache: HomePageCache = mockk()
    private val mockAuthDataStore: AuthDataStore = mockk()

    private lateinit var last7DaysStatsFlow: MutableSharedFlow<Either<Error, Last7DaysStats?>>
    private lateinit var userDetailsFlow: MutableSharedFlow<UserDetails>
    private lateinit var lastRequestTimeFlow: MutableSharedFlow<Instant>
    private lateinit var currentStreakFlow: MutableSharedFlow<Either<Error, StreakRange>>
    private lateinit var longestStreakFlow: MutableSharedFlow<Either<Error, StreakRange>>

    fun buildUseCase(currentInstant: Instant = currentDayInstant) = apply {
        clearMocks(mockHomePageCache, mockAuthDataStore)
        receiveTurbine = null
        last7DaysStatsFlow = MutableSharedFlow()
        userDetailsFlow = MutableSharedFlow()
        lastRequestTimeFlow = MutableSharedFlow(replay = 1)
        currentStreakFlow = MutableSharedFlow()
        longestStreakFlow = MutableSharedFlow()

        useCase = GetCachedHomePageUiDataUC(
            instantProvider = object : InstantProvider {
                override val timeZone = TimeZone.UTC

                override fun now() = currentInstant
            },
            homePageCache = mockHomePageCache,
            authDataStore = mockAuthDataStore,
        )
    }

    fun callUseCase(testScope: TestScope) = apply {
        receiveTurbine = useCase().testIn(testScope, timeout = 5.seconds)
    }

    suspend fun withNextItem(
        block: context(ItemAssertionContext) GetCachedHomePageUiDataUCRobot.() -> Unit,
    ) = apply {
        val item = receiveTurbine!!.awaitItem()
        val context = object : ItemAssertionContext {
            override val item = item
        }

        block(context, this@GetCachedHomePageUiDataUCRobot)
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
        item shouldBeRight null
    }

    context (ItemAssertionContext)
    fun itemShouldNotBeStale() = apply {
        item.asClue {
            item.map { it!!.isStaleData } shouldBeRight false
        }
    }

    context (ItemAssertionContext)
    fun itemShouldBeStale() = apply {
        item.asClue {
            item.map { it!!.isStaleData } shouldBeRight true
        }
    }

    suspend fun expectNoMoreItems() = apply {
        receiveTurbine!!.cancelAndConsumeRemainingEvents().asClue {
            it.size shouldBe 0
        }
    }

    fun mockAllFunctions() = apply {
        coEvery { mockHomePageCache.getLast7DaysStats() } returns last7DaysStatsFlow
        coEvery { mockAuthDataStore.getUserDetails() } returns userDetailsFlow
        coEvery { mockHomePageCache.getLastRequestTime() } returns lastRequestTimeFlow
        coEvery { mockHomePageCache.getCurrentStreak() } returns currentStreakFlow
        coEvery { mockHomePageCache.getLongestStreak() } returns longestStreakFlow
    }

    suspend fun sendLastRequestTime(value: Instant) = apply {
        lastRequestTimeFlow.emit(value)
    }

    suspend fun sendUserDetails(value: UserDetails) = apply {
        userDetailsFlow.emit(value)
    }

    suspend fun sendLast7DaysStats(value: Either<Error, Last7DaysStats?>) = apply {
        last7DaysStatsFlow.emit(value)
    }

    suspend fun sendCurrentStreak(value: Either<Error, StreakRange>) = apply {
        currentStreakFlow.emit(value)
    }

    suspend fun sendLongestStreak(value: Either<Nothing, StreakRange>) = apply {
        longestStreakFlow.emit(value)
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
        val longestStreak = StreakRange.ZERO

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
            createdAt = LocalDate(2021, 1, 1),
            dateFormat = "",
        )
    }

    interface ItemAssertionContext {
        val item: Either<Error, CachedHomePageUiData?>
    }
}
