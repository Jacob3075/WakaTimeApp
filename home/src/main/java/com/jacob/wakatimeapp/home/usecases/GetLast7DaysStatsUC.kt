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

    /**
     * Gets the stats for the last 7 days from the cache if the last request was made within the
     * last 15 minutes, otherwise it will fetch the data from the network and update the cache.
     *
     * If the latest value in the cache is from the previous day, it will fetch the data from the
     * network and not show the cached data.
     *
     * Data is always sent from the database, network data is sent to the db which then sends to
     * the UI
     *
     * @return A flow of either a [WeeklyStats] or an [Error]
     */
    operator fun invoke(): Flow<Either<Error, WeeklyStats>> = flow {
        /*
        *   (1)                        (2)                                  (3)
        ----------|-----------------------------------------------|-------------------|---
             start of day                                    15 mins aga             now
        *
        * check if last request falls in (1), if it does make request and update cache
        * if it falls in (2) or (3) then get data from cache,
        * if its in (1) or (3) return.
        * if its in (2) then make request and update cache
        */
        val lastRequestTime = homePageCache.getLastRequestTime()

        if (lastRequestTime.isPreviousDay()) makeRequestAndUpdateCache()

        homePageCache.getCachedData()
            .map { it.right() }
            .catch { throwable ->
                Error.UnknownError(throwable)
                    .left()
                    .let { emit(it) }
            }
            .let { emitAll(it) }

        if (lastRequestTime.isLessThan15Mins()) return@flow
        if (lastRequestTime.isPreviousDay()) return@flow

        makeRequestAndUpdateCache()
    }

    private suspend fun FlowCollector<Either<Error, WeeklyStats>>.makeRequestAndUpdateCache() {
        homePageNetworkData.getLast7DaysStats()
            .tap { it.updateCaches() }
            .tapLeft { emit(it.left()) }
    }

    private fun Instant.isPreviousDay(): Boolean {
        val startOfDay = Instant.now()
            .plus(1, ChronoUnit.DAYS)
            .truncatedTo(ChronoUnit.DAYS)

        return isBefore(startOfDay)
    }

    private fun Instant.isLessThan15Mins(): Boolean {
        val minutesBetweenLastRequest = Duration.between(this, Instant.now())
            .toMinutes()
        return minutesBetweenLastRequest < 15
    }

    private suspend fun WeeklyStats.updateCaches() {
        listOf(
            ioScope.async { homePageCache.updateCache(this@updateCaches) },
            ioScope.async { homePageCache.updateLastRequestTime() },
        )
            .awaitAll()
    }
}
