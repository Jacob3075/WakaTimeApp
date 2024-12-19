package com.jacob.wakatimeapp.home.domain.usecases

import com.jacob.wakatimeapp.home.data.local.HomePageCache
import com.jacob.wakatimeapp.home.domain.models.Last7DaysStats
import com.jacob.wakatimeapp.core.models.Streak
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class UpdateCachedHomePageUiData @Inject constructor(
    private val homePageCache: HomePageCache,
) {
    suspend operator fun invoke(
        last7DaysStats: Last7DaysStats,
        currentStreak: Streak,
        longestStreak: Streak,
    ) {
        homePageCache.updateLast7DaysStats(last7DaysStats)
        homePageCache.updateCurrentStreak(currentStreak)
        homePageCache.updateLongestStreak(longestStreak)
        homePageCache.updateLastRequestTime()
    }
}
