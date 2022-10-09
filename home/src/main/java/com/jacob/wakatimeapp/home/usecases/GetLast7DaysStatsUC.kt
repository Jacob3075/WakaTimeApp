package com.jacob.wakatimeapp.home.usecases

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.WeeklyStats
import com.jacob.wakatimeapp.home.data.local.HomePageCache
import com.jacob.wakatimeapp.home.data.network.HomePageNetworkData
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

@Singleton
class GetLast7DaysStatsUC @Inject constructor(
    dispatcher: CoroutineContext = Dispatchers.IO,
    private val homePageNetworkData: HomePageNetworkData,
    private val homePageCache: HomePageCache,
) {
    private val ioScope = CoroutineScope(dispatcher)

    operator fun invoke(cacheValidity: Int = DEFAULT): Flow<Either<Error, WeeklyStats>> = flow {
        val lastRequestTime = homePageCache.getLastRequestTime()
        when {
            firstRequestOfDay(lastRequestTime) -> {
                makeRequestAndUpdateCache()
                sendDataFromCache()
            }

            validDataInCache(lastRequestTime, cacheValidity) -> {
                sendDataFromCache()
            }

            else -> {
                sendDataFromCache()
                makeRequestAndUpdateCache()
            }
        }
    }

    private suspend fun FlowCollector<Either<Error, WeeklyStats>>.sendDataFromCache() {
        homePageCache.getCachedData()
            .map { it.right() }
            .catch { throwable ->
                Error.UnknownError(throwable.message!!, throwable)
                    .left()
                    .let { emit(it) }
            }
            .let { emitAll(it) }
    }

    private suspend fun FlowCollector<Either<Error, WeeklyStats>>.makeRequestAndUpdateCache() {
        homePageNetworkData.getLast7DaysStats()
            .tap { it.updateCaches() }
            .tapLeft { emit(it.left()) }
    }

    private suspend fun WeeklyStats.updateCaches() {
        listOf(
            ioScope.async { homePageCache.updateCache(this@updateCaches) },
            ioScope.async { homePageCache.updateLastRequestTime() },
        )
            .awaitAll()
    }

    private fun validDataInCache(
        lastRequestTime: Instant,
        cacheValidityTime: Int,
    ): Boolean {
        val minutesBetweenLastRequest = Duration.between(lastRequestTime, Instant.now())
            .toMinutes()
        return minutesBetweenLastRequest < cacheValidityTime
    }

    private fun firstRequestOfDay(lastRequestTime: Instant) = lastRequestTime.isPreviousDay()

    private fun Instant.isPreviousDay(): Boolean {
        val startOfDay = Instant.now()
            .truncatedTo(ChronoUnit.DAYS)

        return isBefore(startOfDay)
    }

    companion object CacheValidity {
        const val DEFAULT = 15
        const val INVALID_DATA = 0
    }
}
