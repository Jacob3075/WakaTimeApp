package com.jacob.wakatimeapp.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.computations.either
import com.jacob.wakatimeapp.home.domain.models.StreakRange
import com.jacob.wakatimeapp.home.domain.models.Streaks
import com.jacob.wakatimeapp.home.domain.usecases.CalculateCurrentStreakUC
import com.jacob.wakatimeapp.home.domain.usecases.GetCachedHomePageUiData
import com.jacob.wakatimeapp.home.domain.usecases.GetLast7DaysStatsUC
import com.jacob.wakatimeapp.home.domain.usecases.UpdateCachedHomePageUiData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomePageViewModel @Inject constructor(
    ioDispatcher: CoroutineContext,
    private val getLast7DaysStatsUC: GetLast7DaysStatsUC,
    private val calculateCurrentStreakUC: CalculateCurrentStreakUC,
    private val getCachedHomePageUiData: GetCachedHomePageUiData,
    private val updateCachedHomePageUiData: UpdateCachedHomePageUiData,
) : ViewModel() {

    private val _homePageState =
        MutableStateFlow<HomePageViewState>(HomePageViewState.Loading)
    val homePageState = _homePageState.asStateFlow()

    init {
        viewModelScope.launch(ioDispatcher) {
            getCachedHomePageUiData().collect { eitherCachedData ->
                either {
                    val cachedData = eitherCachedData.bind()

                    when {
                        cachedData == null || cachedData.isStateData -> updateCacheWithNewData().bind()
                        else -> _homePageState.value = HomePageViewState.Loaded(
                            last7DaysStats = cachedData.last7DaysStats,
                            userDetails = cachedData.userDetails,
                            streaks = cachedData.streaks,
                        )
                    }
                }.tapLeft { _homePageState.value = HomePageViewState.Error(it) }
            }
        }
    }

    private suspend fun updateCacheWithNewData() = either {
        val last7DaysStats = getLast7DaysStatsUC().bind()
        val streakRange = calculateCurrentStreakUC().bind()

        updateCachedHomePageUiData(
            last7DaysStats = last7DaysStats,
            streaks = Streaks(
                currentStreak = streakRange,
                longestStreak = StreakRange.ZERO
            )
        )
    }
}
