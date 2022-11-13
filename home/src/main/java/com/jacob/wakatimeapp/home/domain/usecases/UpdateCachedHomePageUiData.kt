package com.jacob.wakatimeapp.home.domain.usecases

import com.jacob.wakatimeapp.home.data.local.HomePageCache
import com.jacob.wakatimeapp.home.domain.models.Last7DaysStats
import com.jacob.wakatimeapp.home.domain.models.Streaks
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateCachedHomePageUiData @Inject constructor(
    private val homePageCache: HomePageCache,
) {
    suspend operator fun invoke(last7DaysStats: Last7DaysStats, streaks: Streaks) {
        homePageCache.updateLast7DaysStats(last7DaysStats)
        homePageCache.updateCurrentStreak(streaks.currentStreak)
        homePageCache.updateLongestStreak(streaks.longestStreak)
        homePageCache.updateLastRequestTime()
    }
}
