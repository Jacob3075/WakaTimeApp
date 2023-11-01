package com.jacob.wakatimeapp.login.ui.loading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacob.wakatimeapp.core.common.auth.AuthTokenProvider
import com.jacob.wakatimeapp.core.common.data.local.WakaTimeAppDB
import com.jacob.wakatimeapp.core.common.utils.InstantProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.minus

@HiltViewModel
internal class LoadingPageViewModel @Inject constructor(
    private val authTokenProvider: AuthTokenProvider,
    private val wakaTimeAppDB: WakaTimeAppDB,
    private val ioDispatcher: CoroutineContext = Dispatchers.IO,
    private val instantProvider: InstantProvider,
) : ViewModel() {
    private val _viewState = MutableStateFlow<ViewState>(ViewState.Loading)
    val viewState = _viewState.asStateFlow()

    init {
        checkIfUserIsLoggedIn()
    }

    private fun checkIfUserIsLoggedIn() {
        when (authTokenProvider.current.isAuthorized) {
            false -> _viewState.value = ViewState.LoggedOut
            true -> checkIfLocalDbIsPopulated()
        }
    }

    private fun checkIfLocalDbIsPopulated() {
        viewModelScope.launch(ioDispatcher) {
            val dateRangeInDb = wakaTimeAppDB.getDateRangeInDb()
            val yesterday = instantProvider.date().minus(DatePeriod(days = 1))

            if (dateRangeInDb == null || dateRangeInDb.endDate < yesterday) {
                _viewState.value = ViewState.LocalDbNotPopulated
            } else {
                _viewState.value = ViewState.LocalDbPopulated
            }
        }
    }
}

internal sealed class ViewState {
    data object Loading : ViewState()
    data object Error : ViewState()
    data object LoggedOut : ViewState()
    data object LocalDbPopulated : ViewState()
    data object LocalDbNotPopulated : ViewState()
}
