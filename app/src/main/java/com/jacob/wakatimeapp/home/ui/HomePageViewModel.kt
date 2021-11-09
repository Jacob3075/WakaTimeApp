package com.jacob.wakatimeapp.home.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jacob.wakatimeapp.core.data.OfflineDataStore
import com.jacob.wakatimeapp.core.models.ErrorTypes
import com.jacob.wakatimeapp.core.models.Result
import com.jacob.wakatimeapp.core.utils.Utils
import com.jacob.wakatimeapp.home.domain.usecases.GetLast7DaysStatsUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
@HiltViewModel
class HomePageViewModel @Inject constructor(
    application: Application,
    offlineDataStore: OfflineDataStore,
    utils: Utils,
    private val ioDispatcher: CoroutineContext,
    private val getLast7DaysStatsUC: GetLast7DaysStatsUC,
) : AndroidViewModel(application) {
    private val _homePageState =
        MutableStateFlow<HomePageViewState>(HomePageViewState.Loading)
    val homePageState = _homePageState.asStateFlow()

    private val _errors = MutableSharedFlow<HomePageViewState.Error>()
    val errors = _errors.asSharedFlow()

    init {
        utils.getFreshToken(getApplication())?.let {
            viewModelScope.launch(ioDispatcher) {
                val userDetailsFlow =
                    offlineDataStore.getUserDetails(getApplication()).stateIn(viewModelScope)

                when (val last7DaysStatsResult = getLast7DaysStatsUC(it)) {
                    is Result.Success -> {
                        _homePageState.value = HomePageViewState.Loaded(
                            last7DaysStatsResult.value,
                            userDetailsFlow.value
                        )
                    }
                    is Result.Failure -> {
                        val error = getErrorMessage(last7DaysStatsResult)
                        _homePageState.value = error
                        _errors.emit(error)
                    }
                    is Result.Empty -> Timber.e("EMPTY")
                }
            }
        }
    }

    private fun getErrorMessage(last7DaysStatsResult: Result.Failure): HomePageViewState.Error =
        when (val errorType = last7DaysStatsResult.errorHolder) {
            is ErrorTypes.NetworkError -> HomePageViewState.Error(errorType.throwable.message ?: "")
        }
}
