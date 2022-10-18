package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.computations.either
import arrow.core.right
import com.jacob.wakatimeapp.core.common.auth.AuthDataStore
import com.jacob.wakatimeapp.home.data.local.HomePageCache
import com.jacob.wakatimeapp.home.domain.InstantProvider
import com.jacob.wakatimeapp.home.domain.models.HomePageUserDetails
import com.jacob.wakatimeapp.home.domain.models.Last7DaysStats
import com.jacob.wakatimeapp.home.domain.models.StreakRange
import com.jacob.wakatimeapp.home.domain.models.Streaks
import com.jacob.wakatimeapp.home.domain.models.toHomePageUserDetails
import com.jacob.wakatimeapp.home.domain.usecases.CacheState.FirstRequest
import com.jacob.wakatimeapp.home.domain.usecases.CacheState.InvalidData
import com.jacob.wakatimeapp.home.domain.usecases.CacheState.ValidData
import com.jacob.wakatimeapp.home.domain.usecases.GetCachedHomePageUiData.CacheValidity.DEFAULT
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.Instant
import kotlinx.datetime.toLocalDateTime

@Singleton
class GetCachedHomePageUiData @Inject constructor(
    private val instantProvider: InstantProvider,
    private val homePageCache: HomePageCache,
    private val authDataStore: AuthDataStore,
) {
    operator fun invoke(cacheValidity: CacheValidity = DEFAULT) = channelFlow {
        val initialLastRequestTime = homePageCache.getLastRequestTime().first()

        if (firstRequestOfDay(initialLastRequestTime)) {
            send(FirstRequest.right())
            return@channelFlow
        }

        combine(
            getLast7DaysStats(),
            getHomePageUserDetails(),
            getStreaks(),
            homePageCache.getLastRequestTime(),
        ) { last7DaysStatsEither, userDetails, streaksEither, lastRequestTime ->
            either {
                val last7DaysStats = last7DaysStatsEither.bind()
                val streakRange = streaksEither.bind()
                val streaks = Streaks(
                    currentStreak = streakRange,
                    longestStreak = StreakRange.ZERO
                )

                when {
                    validDataInCache(lastRequestTime, cacheValidity) -> ValidData(
                        last7DaysStats = last7DaysStats,
                        userDetails = userDetails.toHomePageUserDetails(),
                        streaks = streaks
                    )

                    else -> InvalidData(
                        last7DaysStats = last7DaysStats,
                        userDetails = userDetails.toHomePageUserDetails(),
                        streaks = streaks
                    )
                }
            }
        }.onEach { send(it) }
            .flowOn(this.coroutineContext)
    }

    private fun getHomePageUserDetails() = authDataStore.getUserDetails()

    private fun getLast7DaysStats() = homePageCache.getLast7DaysStats()

    private fun getStreaks() = homePageCache.getCurrentStreak()

    private fun firstRequestOfDay(lastRequestTime: Instant) = lastRequestTime.isPreviousDay()

    private fun validDataInCache(
        lastRequestTime: Instant,
        cacheValidityTime: CacheValidity,
    ): Boolean {
        val minutesBetweenLastRequest = instantProvider.now() - lastRequestTime
        return minutesBetweenLastRequest.inWholeMinutes < cacheValidityTime.minutes
    }

    private fun Instant.isPreviousDay(): Boolean {
        val lastRequestDate = this.toLocalDateTime(instantProvider.timeZone).date.toEpochDays()
        val currentDate =
            instantProvider.now().toLocalDateTime(instantProvider.timeZone).date.toEpochDays()

        return currentDate - lastRequestDate >= 1
    }

    enum class CacheValidity(val minutes: Long) {
        DEFAULT(15L),
        INVALID(0L),
    }
}

sealed class CacheState {
    object FirstRequest : CacheState()
    data class ValidData(
        val last7DaysStats: Last7DaysStats,
        val userDetails: HomePageUserDetails,
        val streaks: Streaks,
    ) : CacheState()

    data class InvalidData(
        val last7DaysStats: Last7DaysStats,
        val userDetails: HomePageUserDetails,
        val streaks: Streaks,
    ) : CacheState()
}
