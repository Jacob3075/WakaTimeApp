package com.jacob.wakatimeapp.home.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.jacob.wakatimeapp.core.common.auth.AuthDataStore
import com.jacob.wakatimeapp.home.domain.usecases.CacheState.FirstRequest
import com.jacob.wakatimeapp.home.domain.usecases.CacheState.StaleData
import com.jacob.wakatimeapp.home.domain.usecases.CacheState.ValidData
import com.jacob.wakatimeapp.home.domain.usecases.CalculateCurrentStreakUC
import com.jacob.wakatimeapp.home.domain.usecases.GetCachedHomePageUiData
import com.jacob.wakatimeapp.home.domain.usecases.GetLast7DaysStatsUC
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@HiltViewModel
class HomePageViewModel @Inject constructor(
    application: Application,
    authDataStore: AuthDataStore,
    ioDispatcher: CoroutineContext,
    private val getLast7DaysStatsUC: GetLast7DaysStatsUC,
    private val calculateCurrentStreakUC: CalculateCurrentStreakUC,
    private val getCachedHomePageUiData: GetCachedHomePageUiData,
) : AndroidViewModel(application) {

    private val _homePageState =
        MutableStateFlow<HomePageViewState>(HomePageViewState.Loading)
    val homePageState = _homePageState.asStateFlow()
    private val userDetailsFlow = authDataStore.getUserDetails()
        .distinctUntilChanged()

    init {
        viewModelScope.launch(ioDispatcher) {
            userDetailsFlow

            getCachedHomePageUiData().collect { eitherCachedData ->
                if (eitherCachedData is Either.Left) {
                    _homePageState.value = HomePageViewState.Error(eitherCachedData.value)
                    return@collect
                }

                val cachedData = (eitherCachedData as Either.Right).value

                if ((cachedData is ValidData) or (cachedData is StaleData)) {
                    cachedData as ValidData
                    _homePageState.value = HomePageViewState.Loaded(
                        last7DaysStats = cachedData.last7DaysStats,
                        userDetails = cachedData.userDetails,
                        streaks = cachedData.streaks,
                    )
                }

                if ((cachedData is FirstRequest) or (cachedData is StaleData)) {
                    getLast7DaysStatsUC()?.let {
                        _homePageState.value = HomePageViewState.Error(it)
                    }
                    calculateCurrentStreakUC()?.let {
                        _homePageState.value = HomePageViewState.Error(it)
                    }
                }
            }
        }
    }
}
