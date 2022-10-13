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
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
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

    operator fun invoke(cacheValidity: CacheValidity = INVALID) = flow {
        val lastRequestTime = homePageCache.getLastRequestTime()
        when {
            firstRequestOfDay(lastRequestTime) -> {
                makeRequestAndUpdateCache()
                sendDataFromCache()
            }

            validDataInCache(lastRequestTime, cacheValidity) -> sendDataFromCache()

            else -> {
                sendDataFromCache()
                makeRequestAndUpdateCache()
            }
        }
    }

    private suspend fun FlowCollector<Either<Error, HomePageUiData>>.sendDataFromCache() {
        homePageCache.getCachedData()
            .onEach {
                when (it) {
                    is Either.Left -> emit(it)
                    else -> Unit
                }
            }
            .catch { throwable ->
                Error.UnknownError(throwable.message!!, throwable)
                    .left()
                    .let { emit(it) }
            }
            .let { emitAll(it) }
    }

    private suspend fun FlowCollector<Either<Error, HomePageUiData>>.makeRequestAndUpdateCache() {
        homePageNetworkData.getLast7DaysStats()
            .map(WeeklyStats::toLoadedStateData)
            .tap { it.updateCaches() }
            .tapLeft { emit(it.left()) }
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
