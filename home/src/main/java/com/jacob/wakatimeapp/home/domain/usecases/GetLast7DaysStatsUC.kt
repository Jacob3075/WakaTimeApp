package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.Either.Left
import arrow.core.Either.Right
import com.jacob.wakatimeapp.home.data.local.HomePageCache
import com.jacob.wakatimeapp.home.data.network.HomePageNetworkData
import com.jacob.wakatimeapp.home.domain.models.Last7DaysStats
import com.jacob.wakatimeapp.home.domain.models.toLoadedStateData
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

@Singleton
class GetLast7DaysStatsUC @Inject constructor(
    dispatcher: CoroutineContext = Dispatchers.IO,
    private val homePageNetworkData: HomePageNetworkData,
    private val homePageCache: HomePageCache,
) {
    private val ioScope = CoroutineScope(dispatcher)

    suspend operator fun invoke() = homePageNetworkData.getLast7DaysStats()
        .map { it.toLoadedStateData() }
        .tap { it.updateCaches() }
        .let {
            when (it) {
                is Left -> it.value
                is Right -> null
            }
        }

    private suspend fun Last7DaysStats.updateCaches() {
        listOf(
            ioScope.async { homePageCache.updateLast7DaysStats(this@updateCaches) },
            ioScope.async { homePageCache.updateLastRequestTime() },
        ).awaitAll()
    }
}
