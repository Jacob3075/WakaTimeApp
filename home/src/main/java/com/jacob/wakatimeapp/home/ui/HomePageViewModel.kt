package com.jacob.wakatimeapp.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.raise.either
import com.jacob.wakatimeapp.core.common.utils.InstantProvider
import com.jacob.wakatimeapp.core.common.utils.log
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
import kotlinx.datetime.LocalDate

@HiltViewModel
internal class HomePageViewModel @Inject constructor(
    private val getCachedHomePageUiDataUC: GetCachedHomePageUiDataUC,
    private val getLast7DaysStatsUC: GetLast7DaysStatsUC,
    private val calculateCurrentStreakUC: CalculateCurrentStreakUC,
    private val calculateLongestStreakUC: CalculateLongestStreakUC,
    private val updateCachedHomePageUiData: UpdateCachedHomePageUiData,
    private val instantProvider: InstantProvider,
    ioDispatcher: CoroutineContext = Dispatchers.IO,
) : ViewModel() {

    private val _homePageState = MutableStateFlow<HomePageViewState>(HomePageViewState.Loading)
    val homePageState = _homePageState.asStateFlow()

    init {
        viewModelScope.launch(ioDispatcher) {
            getCachedHomePageUiDataUC().log("cachedData")
                .collect { eitherCachedData ->
                    eitherCachedData
                        .onLeft { _homePageState.value = HomePageViewState.Error(it) }
                        .onRight { cachedData ->
                            when {
                                cachedData == null -> updateCacheWithNewData().onLeft {
                                    _homePageState.value = HomePageViewState.Error(it)
                                }

                                cachedData.isStaleData -> {
                                    _homePageState.value = HomePageViewState.Loaded(
                                        last7DaysStats = cachedData.last7DaysStats,
                                        userDetails = cachedData.userDetails,
                                        longestStreak = cachedData.longestStreak,
                                        currentStreak = cachedData.currentStreak,
                                    )
                                    updateCacheWithNewData().onLeft {
                                        _homePageState.value = HomePageViewState.Error(it)
                                    }
                                }

                                else -> _homePageState.value = HomePageViewState.Loaded(
                                    last7DaysStats = cachedData.last7DaysStats,
                                    userDetails = cachedData.userDetails,
                                    longestStreak = cachedData.longestStreak,
                                    currentStreak = cachedData.currentStreak,
                                )
                            }
                        }
                }
        }
    }

    private suspend fun updateCacheWithNewData() = either {
        val last7DaysStats = getLast7DaysStatsUC().bind()
        val streakRange = calculateCurrentStreakUC(last7DaysStats).bind()
        val longestStreak = calculateLongestStreakUC().bind()

        updateCachedHomePageUiData.invoke(
            last7DaysStats = last7DaysStats,
            currentStreak = streakRange,
            longestStreak = longestStreak,
        )
    }

    fun getTodaysDate(): LocalDate = instantProvider.date()
}
