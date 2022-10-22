package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.Either
import com.jacob.wakatimeapp.core.common.auth.AuthDataStore
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.home.data.local.HomePageCache
import com.jacob.wakatimeapp.home.domain.InstantProvider
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlin.time.Duration.Companion.days
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone

internal class GetCachedHomePageUiDataRobot {
    private lateinit var useCase: GetCachedHomePageUiData

    private val results = mutableListOf<Either<Error, CachedHomePageUiData?>>()

    private val mockHomePageCache: HomePageCache = mockk()
    private val mockAuthDataStore: AuthDataStore = mockk()

    fun buildUseCase(instantProvider: InstantProvider = defaultTestInstantProvider) = apply {
        clearMocks(mockHomePageCache, mockAuthDataStore)
        results.clear()

        useCase = GetCachedHomePageUiData(
            instantProvider = instantProvider,
            homePageCache = mockHomePageCache,
            authDataStore = mockAuthDataStore,
        )
    }

    suspend fun callUseCase() = apply {
        useCase().toList(results)
    }

    fun setLastRequestTime(previousDay: Instant) = apply {
        coEvery { mockHomePageCache.getLastRequestTime() } returns flowOf(previousDay)
    }

    fun resultsSizeShouldBe(size: Int) = apply {
        results.size shouldBe size
    }

    fun resultsShouldBe(expected: List<Either<Error, CachedHomePageUiData?>>) = apply {
        results shouldContainExactly expected
    }

    companion object {
        val defaultTestInstantProvider = object : InstantProvider {
            override val timeZone = TimeZone.UTC

            override fun now() = currentDayInstant
        }

        /**
         * Start of a random day
         *
         * Value:
         *  - date: 11/10/2022 (dd/mm/yyyy)
         *  - time: 00:00:00 (hh:mm::ss)
         */
        val currentDayInstant = Instant.parse("2022-10-11T00:00:00Z")

        /**
         * Takes [currentDayInstant] and subtracts 1 day from it
         */
        val previousDayInstant = currentDayInstant.minus(1.days)
    }
}
