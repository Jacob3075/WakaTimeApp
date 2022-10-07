package com.jacob.wakatimeapp.home.usecases

import com.jacob.wakatimeapp.home.data.HomePageNetworkData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetLast7DaysStatsUC @Inject constructor(
    private val homePageNetworkData: HomePageNetworkData,
) {
    suspend operator fun invoke() = homePageNetworkData.getLast7DaysStats()
}
