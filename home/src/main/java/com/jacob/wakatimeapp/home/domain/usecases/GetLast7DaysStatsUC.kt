package com.jacob.wakatimeapp.home.domain.usecases

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
import kotlinx.coroutines.flow.channelFlow

@Singleton
class GetLast7DaysStatsUC @Inject constructor(
    dispatcher: CoroutineContext = Dispatchers.IO,
    private val homePageNetworkData: HomePageNetworkData,
    private val homePageCache: HomePageCache,
) {
    private val ioScope = CoroutineScope(dispatcher)

    // todo: remove flows
    operator fun invoke() = channelFlow {
        homePageNetworkData.getLast7DaysStats()
            .map { it.toLoadedStateData() }
            .fold(ifLeft = { send(it) }, ifRight = { it.updateCaches() })
    }

    private suspend fun Last7DaysStats.updateCaches() {
        listOf(
            ioScope.async { homePageCache.updateLast7DaysStats(this@updateCaches) },
            ioScope.async { homePageCache.updateLastRequestTime() },
        ).awaitAll()
    }
}
