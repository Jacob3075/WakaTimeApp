package com.jacob.wakatimeapp.home.domain.usecases

import com.jacob.wakatimeapp.home.data.network.HomePageNetworkData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class GetDailyStatsUC @Inject constructor(
    private val homePageNetworkData: HomePageNetworkData,
) {
    suspend operator fun invoke() = homePageNetworkData.getStatsForToday()
}
