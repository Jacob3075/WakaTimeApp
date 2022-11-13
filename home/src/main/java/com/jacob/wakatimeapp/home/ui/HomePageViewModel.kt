package com.jacob.wakatimeapp.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.computations.either
import com.jacob.wakatimeapp.home.domain.models.Streaks
import com.jacob.wakatimeapp.home.domain.usecases.CalculateCurrentStreakUC
import com.jacob.wakatimeapp.home.domain.usecases.CalculateLongestStreakUC
import com.jacob.wakatimeapp.home.domain.usecases.GetCachedHomePageUiDataUC
import com.jacob.wakatimeapp.home.domain.usecases.GetLast7DaysStatsUC
import com.jacob.wakatimeapp.home.domain.usecases.UpdateCachedHomePageUiData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
internal class HomePageViewModel @Inject constructor(
    private val getCachedHomePageUiDataUC: GetCachedHomePageUiDataUC,
    private val getLast7DaysStatsUC: GetLast7DaysStatsUC,
    private val calculateCurrentStreakUC: CalculateCurrentStreakUC,
    private val calculateLongestStreakUC: CalculateLongestStreakUC,
    private val updateCachedHomePageUiData: UpdateCachedHomePageUiData,
    ioDispatcher: CoroutineContext = Dispatchers.IO,
) : ViewModel() {

    private val _homePageState =
        MutableStateFlow<HomePageViewState>(HomePageViewState.Loading)
    val homePageState = _homePageState.asStateFlow()

    init {
        viewModelScope.launch(ioDispatcher) {
            getCachedHomePageUiDataUC().collect { eitherCachedData ->
                either {
                    val cachedData = eitherCachedData.bind()
                    Timber.d("cachedData: $cachedData")

                    when {
                        cachedData == null -> updateCacheWithNewData().bind()
                        cachedData.isStaleData -> {
                            _homePageState.value = HomePageViewState.Loaded(
                                last7DaysStats = cachedData.last7DaysStats,
                                userDetails = cachedData.userDetails,
                                streaks = cachedData.streaks,
                            )
                            updateCacheWithNewData().bind()
                        }

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
        val longestStreak = calculateLongestStreakUC().bind()

        updateCachedHomePageUiData(
            last7DaysStats = last7DaysStats,
            streaks = Streaks(
                currentStreak = streakRange,
                longestStreak = longestStreak,
            )
        )
    }
}
