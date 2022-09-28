package com.jacob.wakatimeapp.home.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jacob.wakatimeapp.core.common.auth.AuthDataStore
import com.jacob.wakatimeapp.core.models.Result
import com.jacob.wakatimeapp.home.usecases.GetLast7DaysStatsUC
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class HomePageViewModel @Inject constructor(
    application: Application,
    authDataStore: AuthDataStore,
    private val ioDispatcher: CoroutineContext,
    private val getLast7DaysStatsUC: GetLast7DaysStatsUC
) : AndroidViewModel(application) {

    private val _homePageState =
        MutableStateFlow<HomePageViewState>(HomePageViewState.Loading)
    val homePageState = _homePageState.asStateFlow()

    init {
        viewModelScope.launch(ioDispatcher) {
            val result = getLast7DaysStatsUC()

            authDataStore.getUserDetails()
                .distinctUntilChanged()
                .collect { userDetails ->
                    when (result) {
                        is Result.Success -> _homePageState.value = HomePageViewState.Loaded(
                            result.value,
                            userDetails
                        )
                        is Result.Failure -> _homePageState.value = result.getErrorMessage()
                            .let(HomePageViewState::Error)
                        is Result.Empty -> Timber.e("EMPTY")
                    }
                }
        }
    }
}
