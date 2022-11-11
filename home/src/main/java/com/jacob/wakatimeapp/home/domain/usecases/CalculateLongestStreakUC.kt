package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.computations.either
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.home.data.local.HomePageCache
import com.jacob.wakatimeapp.home.data.network.HomePageNetworkData
import com.jacob.wakatimeapp.home.domain.models.StreakRange
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first

@Singleton
class CalculateLongestStreakUC @Inject constructor(
    private val homePageNetworkData: HomePageNetworkData,
    private val homePageCache: HomePageCache,
) {
    suspend operator fun invoke() = either<Error, StreakRange> {
        val longestStreak = homePageCache.getLongestStreak().first().bind()
        val currentStreak = homePageCache.getCurrentStreak().first().bind()

        if (currentStreak > longestStreak) {
            homePageCache.updateLongestStreak(currentStreak)
            return@either currentStreak
        }

        TODO()
    }
}
