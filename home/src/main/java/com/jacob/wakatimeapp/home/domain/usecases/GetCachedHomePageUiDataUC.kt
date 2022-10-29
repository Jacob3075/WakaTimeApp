package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.Either.Right
import arrow.core.computations.either
import com.jacob.wakatimeapp.core.common.auth.AuthDataStore
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.home.data.local.HomePageCache
import com.jacob.wakatimeapp.home.domain.InstantProvider
import com.jacob.wakatimeapp.home.domain.models.CachedHomePageUiData
import com.jacob.wakatimeapp.home.domain.models.StreakRange
import com.jacob.wakatimeapp.home.domain.models.Streaks
import com.jacob.wakatimeapp.home.domain.models.toHomePageUserDetails
import com.jacob.wakatimeapp.home.domain.usecases.GetCachedHomePageUiData.CacheValidity.DEFAULT
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.combine
import kotlinx.datetime.Instant
import kotlinx.datetime.toLocalDateTime

@Singleton
class GetCachedHomePageUiData @Inject constructor(
    private val instantProvider: InstantProvider,
    private val homePageCache: HomePageCache,
    private val authDataStore: AuthDataStore,
) {

    /**
     * @return [CachedHomePageUiData] if there is data for the current day in the cache, otherwise null
     */
    operator fun invoke(cacheValidity: CacheValidity = DEFAULT) = channelFlow {
        combine(
            getLast7DaysStats(),
            getHomePageUserDetails(),
            getStreaks(),
            homePageCache.getLastRequestTime(),
        ) { last7DaysStatsEither, userDetails, streaksEither, lastRequestTime ->
            if (lastRequestTime.isFirstRequestOfDay()) return@combine Right(null)

            either<Error, CachedHomePageUiData> {
                val last7DaysStats = last7DaysStatsEither.bind()
                val streakRange = streaksEither.bind()
                val streaks = Streaks(
                    currentStreak = streakRange,
                    longestStreak = StreakRange.ZERO
                )

                CachedHomePageUiData(
                    last7DaysStats = last7DaysStats,
                    userDetails = userDetails.toHomePageUserDetails(),
                    streaks = streaks,
                    isStateData = !validDataInCache(
                        lastRequestTime = lastRequestTime,
                        cacheValidityTime = cacheValidity
                    )
                )
            }
        }.collect { send(it) }
    }

    private fun getHomePageUserDetails() = authDataStore.getUserDetails()

    private fun getLast7DaysStats() = homePageCache.getLast7DaysStats()

    private fun getStreaks() = homePageCache.getCurrentStreak()

    private fun Instant.isFirstRequestOfDay(): Boolean {
        val lastRequestDate = toLocalDateTime(instantProvider.timeZone).date.toEpochDays()
        val currentDate =
            instantProvider.now().toLocalDateTime(instantProvider.timeZone).date.toEpochDays()
        return currentDate - lastRequestDate >= 1
    }

    private fun validDataInCache(
        lastRequestTime: Instant,
        cacheValidityTime: CacheValidity,
    ): Boolean {
        val minutesBetweenLastRequest = instantProvider.now() - lastRequestTime
        return minutesBetweenLastRequest.inWholeMinutes < cacheValidityTime.minutes
    }

    enum class CacheValidity(val minutes: Long) {
        DEFAULT(15L),
        INVALID(0L),
    }
}
