package com.jacob.wakatimeapp.home.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either.Left
import arrow.core.Either.Right
import com.jacob.wakatimeapp.core.common.auth.AuthDataStore
import com.jacob.wakatimeapp.home.domain.usecases.GetLast7DaysStatsUC
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@HiltViewModel
class HomePageViewModel @Inject constructor(
    application: Application,
    authDataStore: AuthDataStore,
    ioDispatcher: CoroutineContext,
    private val getLast7DaysStatsUC: GetLast7DaysStatsUC,
) : AndroidViewModel(application) {

    private val _homePageState =
        MutableStateFlow<HomePageViewState>(HomePageViewState.Loading)
    val homePageState = _homePageState.asStateFlow()
    private val userDetailsFlow = authDataStore.getUserDetails()
        .distinctUntilChanged()

    init {
        viewModelScope.launch(ioDispatcher) {
            getLast7DaysStatsUC(userDetailsFlow.first()).collect {
                _homePageState.value = when (it) {
                    is Left -> HomePageViewState.Error(it.value)
                    is Right -> HomePageViewState.Loaded(
                        contentData = it.value,
                    )
                }
            }
        }
    }
}
