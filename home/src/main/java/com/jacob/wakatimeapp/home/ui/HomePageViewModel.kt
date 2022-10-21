package com.jacob.wakatimeapp.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.jacob.wakatimeapp.home.domain.usecases.CalculateCurrentStreakUC
import com.jacob.wakatimeapp.home.domain.usecases.GetCachedHomePageUiData
import com.jacob.wakatimeapp.home.domain.usecases.GetLast7DaysStatsUC
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
) : ViewModel() {

    private val _homePageState =
        MutableStateFlow<HomePageViewState>(HomePageViewState.Loading)
    val homePageState = _homePageState.asStateFlow()

    init {
        viewModelScope.launch(ioDispatcher) {

            getCachedHomePageUiData().collect { eitherCachedData ->
                if (eitherCachedData is Either.Left) {
                    _homePageState.value = HomePageViewState.Error(eitherCachedData.value)
                    return@collect
                }

                val cachedData = (eitherCachedData as Either.Right).value

                when {
                    cachedData == null || cachedData.isStateData -> {
                        getLast7DaysStatsUC()?.let {
                            _homePageState.value = HomePageViewState.Error(it)
                        }
                        calculateCurrentStreakUC()?.let {
                            _homePageState.value = HomePageViewState.Error(it)
                        }
                    }

                    else -> _homePageState.value = HomePageViewState.Loaded(
                        last7DaysStats = cachedData.last7DaysStats,
                        userDetails = cachedData.userDetails,
                        streaks = cachedData.streaks,
                    )
                }
            }
        }
    }
}
