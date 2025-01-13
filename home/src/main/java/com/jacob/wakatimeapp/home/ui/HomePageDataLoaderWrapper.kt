package com.jacob.wakatimeapp.home.ui

import arrow.core.Either
import arrow.core.raise.either
import com.jacob.wakatimeapp.core.common.auth.AuthDataStore
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.home.domain.models.toHomePageUserDetails
import com.jacob.wakatimeapp.home.domain.usecases.CalculateCurrentStreakUC
import com.jacob.wakatimeapp.home.domain.usecases.CalculateLongestStreakUC
import com.jacob.wakatimeapp.home.domain.usecases.GetLast7DaysStatsUC
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber

@Singleton
class HomePageDataLoaderWrapper @Inject constructor() {
    private lateinit var _loadedDataResult: Either<Error, HomePageViewState.Loaded>
    internal val loadedDataResult: Either<Error, HomePageViewState.Loaded>
        get() = _loadedDataResult

    @Inject
    internal lateinit var getLast7DaysStatsUC: GetLast7DaysStatsUC

    @Inject
    internal lateinit var calculateCurrentStreakUC: CalculateCurrentStreakUC

    @Inject
    internal lateinit var calculateLongestStreakUC: CalculateLongestStreakUC

    @Inject
    internal lateinit var authDataStore: AuthDataStore

    suspend fun loadData() {
        _loadedDataResult = either {
            Timber.i("Preloading home page data")
            val last7DaysStats = getLast7DaysStatsUC().bind()
            val currentStreak = calculateCurrentStreakUC(last7DaysStats).bind()
            val longestStreak = calculateLongestStreakUC().bind()
            val userDetails = authDataStore.userDetails.firstOrNull() ?: raise(Error.UnknownError("User not logged in"))

            return@either HomePageViewState.Loaded(
                last7DaysStats = last7DaysStats,
                userDetails = userDetails.toHomePageUserDetails(),
                longestStreak = longestStreak,
                currentStreak = currentStreak,
            )
        }
    }
}
