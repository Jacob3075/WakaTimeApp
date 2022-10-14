package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.Either
import arrow.core.left
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.WeeklyStats
import com.jacob.wakatimeapp.home.data.local.HomePageCache
import com.jacob.wakatimeapp.home.data.network.HomePageNetworkData
import com.jacob.wakatimeapp.home.domain.InstantProvider
import com.jacob.wakatimeapp.home.domain.models.HomePageUiData
import com.jacob.wakatimeapp.home.domain.models.toLoadedStateData
import com.jacob.wakatimeapp.home.domain.usecases.GetLast7DaysStatsUC.CacheValidity.INVALID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge
import kotlinx.datetime.Instant
import kotlinx.datetime.toLocalDateTime

@Singleton
class GetLast7DaysStatsUC @Inject constructor(
    dispatcher: CoroutineContext = Dispatchers.IO,
    private val homePageNetworkData: HomePageNetworkData,
    private val homePageCache: HomePageCache,
    private val instantProvider: InstantProvider,
) {
    private val ioScope = CoroutineScope(dispatcher)

    @SuppressWarnings("kotlin:S6309")
    suspend operator fun invoke(cacheValidity: CacheValidity = INVALID): Flow<Either<Error, HomePageUiData>> {
        val lastRequestTime = homePageCache.getLastRequestTime()
        val dataFromCache = sendDataFromCache()

        return when {
            firstRequestOfDay(lastRequestTime) -> {
                val networkErrors = makeRequestAndUpdateCache()

                val previousDaysData = 1
                merge(dataFromCache.drop(previousDaysData), networkErrors)
            }

            validDataInCache(lastRequestTime, cacheValidity) -> dataFromCache

            else -> {
                val networkErrors = makeRequestAndUpdateCache()

                merge(dataFromCache, networkErrors)
            }
        }
    }

    private fun sendDataFromCache() = homePageCache.getCachedData()
        .catch { throwable ->
            Error.UnknownError(throwable.message!!, throwable)
                .left()
                .let { emit(it) }
        }

    /**
     * Gets data from network and updates the cache is successful.
     *
     * Returns a flow of errors if any.
     */
    private suspend fun makeRequestAndUpdateCache() = flow {
        homePageNetworkData.getLast7DaysStats()
            .map(WeeklyStats::toLoadedStateData)
            .tap { it.updateCaches() }
            .let {
                when (it) {
                    is Either.Left -> emit(it)
                    is Either.Right -> Unit
                }
            }
    }

    private suspend fun HomePageUiData.updateCaches() {
        listOf(
            ioScope.async { homePageCache.updateCache(this@updateCaches) },
            ioScope.async { homePageCache.updateLastRequestTime() },
        )
            .awaitAll()
    }

    private fun validDataInCache(
        lastRequestTime: Instant,
        cacheValidityTime: CacheValidity,
    ): Boolean {
        val minutesBetweenLastRequest = instantProvider.now() - lastRequestTime
        return minutesBetweenLastRequest.inWholeMinutes < cacheValidityTime.minutes
    }

    private fun firstRequestOfDay(lastRequestTime: Instant) = lastRequestTime.isPreviousDay()

    private fun Instant.isPreviousDay(): Boolean {
        val lastRequestDate = this.toLocalDateTime(instantProvider.timeZone).date.toEpochDays()
        val currentDate = instantProvider.now()
            .toLocalDateTime(instantProvider.timeZone).date.toEpochDays()

        return currentDate - lastRequestDate >= 1
    }

    enum class CacheValidity(val minutes: Long) {
        DEFAULT(15L),
        INVALID(0L),
    }
}
