package com.jacob.wakatimeapp.home.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jacob.wakatimeapp.common.data.OfflineDataStore
import com.jacob.wakatimeapp.common.models.Result
import com.jacob.wakatimeapp.common.utils.Utils
import com.jacob.wakatimeapp.home.domain.models.DailyStats
import com.jacob.wakatimeapp.home.domain.usecases.GetDailyStatsUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class HomePageViewModel @Inject constructor(
    application: Application,
    offlineDataStore: OfflineDataStore,
    utils: Utils,
    private val ioDispatcher: CoroutineContext,
    private val getDailyStatsUC: GetDailyStatsUC,
) : AndroidViewModel(application) {
    @ExperimentalCoroutinesApi
    val userDetails = offlineDataStore.getUserDetails(getApplication())

    private val _dailyStats: MutableStateFlow<DailyStats?> = MutableStateFlow(null)
    val dailyStats = _dailyStats.asStateFlow()

    private val _errors = MutableSharedFlow<String>()
    val errors = _errors.asSharedFlow()

    init {
        utils.getFreshToken(getApplication())?.let {
            viewModelScope.launch(ioDispatcher) {
                when (val dailyStatsResult = getDailyStatsUC(it)) {
                    is Result.Success -> _dailyStats.value = dailyStatsResult.value
                    is Result.Empty -> _errors.emit("Empty")
                    is Result.Failure -> _errors.emit(dailyStatsResult.errorHolder.toString())
                }
            }
        }
    }
}
