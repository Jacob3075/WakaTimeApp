package com.jacob.wakatimeapp.login.ui.loading

import com.jacob.wakatimeapp.core.models.Error as CoreModelsError
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import arrow.core.Either
import com.jacob.wakatimeapp.core.common.auth.AuthTokenProvider
import com.jacob.wakatimeapp.login.domain.usecases.UpdateUserStatsInDbUC
import com.jacob.wakatimeapp.login.work.PeriodicUpdateUserDataWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.TimeUnit.DAYS
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
internal class LoadingPageViewModel @Inject constructor(
    application: Application,
    private val authTokenProvider: AuthTokenProvider,
    private val ioDispatcher: CoroutineContext = Dispatchers.IO,
    private val updateUserStatsInDbUC: UpdateUserStatsInDbUC,
) : AndroidViewModel(application) {
    private val _viewState = MutableStateFlow<LoadingPageViewState>(LoadingPageViewState.Loading)
    val viewState = _viewState.asStateFlow()

    init {
        checkIfUserIsLoggedIn()
    }

    private fun checkIfUserIsLoggedIn() {
        when (authTokenProvider.current.isAuthorized) {
            false -> _viewState.value = LoadingPageViewState.LoggedOut
            true -> {
                resetUpdateStatsWorker()
                updateStatsInDB()
            }
        }
    }

    private fun resetUpdateStatsWorker() {
        val workManager = WorkManager.getInstance(getApplication<Application>().applicationContext)
        workManager.cancelAllWorkByTag(PeriodicUpdateUserDataWorker.WORK_NAME)

        val periodicWorkRequestBuilder = PeriodicWorkRequestBuilder<PeriodicUpdateUserDataWorker>(
            PeriodicUpdateUserDataWorker.WORK_INTERVAL_IN_DAYS,
            DAYS,
        ).addTag(PeriodicUpdateUserDataWorker.WORK_NAME)
            .build()

        workManager.enqueue(periodicWorkRequestBuilder)
    }

    private fun updateStatsInDB() {
        viewModelScope.launch(ioDispatcher) {
            when (val result = updateUserStatsInDbUC()) {
                is Either.Left -> _viewState.value = LoadingPageViewState.Error(result.value)
                is Either.Right -> _viewState.value = LoadingPageViewState.LocalDbPopulated
            }
        }
    }

    fun logout() {
        authTokenProvider.logout()
        _viewState.value = LoadingPageViewState.LoggedOut
    }
}

internal sealed class LoadingPageViewState {
    data object Loading : LoadingPageViewState()
    data class Error(val error: CoreModelsError) : LoadingPageViewState()
    data object LoggedOut : LoadingPageViewState()
    data object LocalDbPopulated : LoadingPageViewState()
    data object LocalDbNotPopulated : LoadingPageViewState()
}
