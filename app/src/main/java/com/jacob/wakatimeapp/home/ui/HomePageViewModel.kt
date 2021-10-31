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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val _dailyStats: MutableStateFlow<Result<DailyStats>> = MutableStateFlow(Result.Empty)
    val dailyStats: StateFlow<Result<DailyStats>> = _dailyStats

    init {
        utils.getFreshToken(getApplication())?.let {
            viewModelScope.launch(ioDispatcher) {
                _dailyStats.value = getDailyStatsUC(it)
            }
        }
    }
}
