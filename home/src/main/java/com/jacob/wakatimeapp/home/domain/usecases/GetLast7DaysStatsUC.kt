package com.jacob.wakatimeapp.home.domain.usecases

import com.jacob.wakatimeapp.home.data.network.HomePageNetworkData
import com.jacob.wakatimeapp.home.domain.models.toLoadedStateData
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class GetLast7DaysStatsUC @Inject constructor(
    private val homePageNetworkData: HomePageNetworkData,
) {

    suspend operator fun invoke() = homePageNetworkData.getLast7DaysStats()
        .map { it.toLoadedStateData() }
}
