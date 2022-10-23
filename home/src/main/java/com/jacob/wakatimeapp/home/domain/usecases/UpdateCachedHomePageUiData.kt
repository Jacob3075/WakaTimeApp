package com.jacob.wakatimeapp.home.domain.usecases

import com.jacob.wakatimeapp.home.data.local.HomePageCache
import com.jacob.wakatimeapp.home.domain.models.Last7DaysStats
import com.jacob.wakatimeapp.home.domain.models.Streaks
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Singleton
class UpdateCachedHomePageUiData @Inject constructor(
    dispatcher: CoroutineContext = Dispatchers.IO,
    private val homePageCache: HomePageCache,
) {
    private val ioScope = CoroutineScope(dispatcher)

    suspend operator fun invoke(last7DaysStats: Last7DaysStats, streaks: Streaks) {
        ioScope.launch { homePageCache.updateLast7DaysStats(last7DaysStats) }
        ioScope.launch { homePageCache.updateCurrentStreak(streaks.currentStreak) }
    }
}
