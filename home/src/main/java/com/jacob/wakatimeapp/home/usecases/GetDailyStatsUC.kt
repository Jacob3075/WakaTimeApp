package com.jacob.wakatimeapp.home.usecases

import com.jacob.wakatimeapp.home.data.HomePageNetworkData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetDailyStatsUC @Inject constructor(
    private val homePageNetworkData: HomePageNetworkData,
) {
    suspend operator fun invoke() = homePageNetworkData.getStatsForToday()
}
