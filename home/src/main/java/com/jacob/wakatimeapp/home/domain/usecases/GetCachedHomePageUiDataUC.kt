package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.Either.Right
import arrow.core.continuations.either
import com.jacob.wakatimeapp.core.common.auth.AuthDataStore
import com.jacob.wakatimeapp.core.common.utils.InstantProvider
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.home.data.local.HomePageCache
import com.jacob.wakatimeapp.home.domain.models.HomePageUiData
import com.jacob.wakatimeapp.home.domain.models.toHomePageUserDetails
import com.jacob.wakatimeapp.home.domain.usecases.GetCachedHomePageUiDataUC.CacheValidity.DEFAULT
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.combine
import kotlinx.datetime.Instant
import kotlinx.datetime.toLocalDateTime

@Singleton
internal class GetCachedHomePageUiDataUC @Inject constructor(
    private val instantProvider: InstantProvider,
    private val homePageCache: HomePageCache,
    private val authDataStore: AuthDataStore,
) {

    /**
     * @return [HomePageUiData] if there is data for the current day in the cache, otherwise null
     */
    operator fun invoke(cacheValidity: CacheValidity = DEFAULT) = channelFlow {
        combine(
            authDataStore.userDetails,
            homePageCache.getLast7DaysStats(),
            homePageCache.getCurrentStreak(),
            homePageCache.getLongestStreak(),
            homePageCache.getLastRequestTime(),
        ) { userDetails, last7DaysStatsEither, currentStreakEither, longestStreakEither, lastRequestTime ->
            if (lastRequestTime.isFirstRequestOfDay()) return@combine Right(null)

            either<Error, HomePageUiData?> {
                val last7DaysStats = last7DaysStatsEither.bind() ?: return@either null
                val currentStreak = currentStreakEither.bind()
                val longestStreak = longestStreakEither.bind()

                HomePageUiData(
                    last7DaysStats = last7DaysStats,
                    userDetails = userDetails.toHomePageUserDetails(),
                    currentStreak = currentStreak,
                    longestStreak = longestStreak,
                    isStaleData = !validDataInCache(
                        lastRequestTime = lastRequestTime,
                        cacheValidityTime = cacheValidity,
                    ),
                )
            }
        }.collect { send(it) }
    }

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
