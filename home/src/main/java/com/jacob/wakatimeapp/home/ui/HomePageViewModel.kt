package com.jacob.wakatimeapp.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.raise.either
import com.jacob.wakatimeapp.core.common.auth.AuthDataStore
import com.jacob.wakatimeapp.core.common.utils.InstantProvider
import com.jacob.wakatimeapp.home.domain.models.toHomePageUserDetails
import com.jacob.wakatimeapp.home.domain.usecases.CalculateCurrentStreakUC
import com.jacob.wakatimeapp.home.domain.usecases.CalculateLongestStreakUC
import com.jacob.wakatimeapp.home.domain.usecases.GetLast7DaysStatsUC
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlin.coroutines.CoroutineContext

@HiltViewModel
internal class HomePageViewModel @Inject constructor(
    private val getLast7DaysStatsUC: GetLast7DaysStatsUC,
    private val calculateCurrentStreakUC: CalculateCurrentStreakUC,
    private val calculateLongestStreakUC: CalculateLongestStreakUC,
    private val authDataStore: AuthDataStore,
    private val instantProvider: InstantProvider,
    ioDispatcher: CoroutineContext = Dispatchers.IO,
) : ViewModel() {

    private val _homePageState = MutableStateFlow<HomePageViewState>(HomePageViewState.Loading)
    val homePageState = _homePageState.asStateFlow()

    init {

        viewModelScope.launch(ioDispatcher) {
            either {
                val last7DaysStats = getLast7DaysStatsUC().bind()
                val currentStreak = calculateCurrentStreakUC(last7DaysStats).bind()
                val longestStreak = calculateLongestStreakUC().bind()
                val userDetails = authDataStore.userDetails.firstOrNull() ?: return@either

                _homePageState.value = HomePageViewState.Loaded(
                    last7DaysStats = last7DaysStats,
                    userDetails = userDetails.toHomePageUserDetails(),
                    longestStreak = longestStreak,
                    currentStreak = currentStreak,
                )
            }
        }
    }

    fun getTodaysDate(): LocalDate = instantProvider.date()
}
