package com.jacob.wakatimeapp.home.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import arrow.core.computations.either
import com.jacob.wakatimeapp.core.common.auth.AuthDataStore
import com.jacob.wakatimeapp.home.domain.models.toHomePageUserDetails
import com.jacob.wakatimeapp.home.domain.usecases.CalculateCurrentStreakUC
import com.jacob.wakatimeapp.home.domain.usecases.GetLast7DaysStatsUC
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@HiltViewModel
class HomePageViewModel @Inject constructor(
    application: Application,
    authDataStore: AuthDataStore,
    ioDispatcher: CoroutineContext,
    private val getLast7DaysStatsUC: GetLast7DaysStatsUC,
    private val calculateCurrentStreakUC: CalculateCurrentStreakUC,
) : AndroidViewModel(application) {

    private val _homePageState =
        MutableStateFlow<HomePageViewState>(HomePageViewState.Loading)
    val homePageState = _homePageState.asStateFlow()
    private val userDetailsFlow = authDataStore.getUserDetails()
        .distinctUntilChanged()

    init {
        viewModelScope.launch(ioDispatcher) {
            userDetailsFlow

            combine(
                userDetailsFlow,
                getLast7DaysStatsUC(),
                calculateCurrentStreakUC(),
            ) { userDetails, eitherLast7DaysStats, eitherStreaks ->
                either {
                    val last7DaysStats = eitherLast7DaysStats.bind()
                    val streaks = eitherStreaks.bind()

                    HomePageViewState.Loaded(
                        last7DaysStats = last7DaysStats,
                        userDetails = userDetails.toHomePageUserDetails(),
                        streaks = streaks,
                    )
                }
                    .mapLeft(HomePageViewState::Error)
            }
                .collect {
                    when (it) {
                        is Either.Left -> _homePageState.value = it.value
                        is Either.Right -> _homePageState.value = it.value
                    }
                }
        }
    }
}
