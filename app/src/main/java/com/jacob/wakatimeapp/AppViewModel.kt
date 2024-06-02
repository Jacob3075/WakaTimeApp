package com.jacob.wakatimeapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacob.wakatimeapp.home.ui.destinations.HomePageDestination
import com.jacob.wakatimeapp.login.ui.destinations.LoginPageDestination
import com.jacob.wakatimeapp.login.ui.loading.SplashScreenLoader
import com.ramcosta.composedestinations.spec.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AppViewModel @Inject constructor(
    private val splashScreenLoader: SplashScreenLoader,
) : ViewModel() {
    private var _startRoute: Route = HomePageDestination
    val startRoute: Route
        get() = _startRoute

    private val viewState: MutableStateFlow<LoadingState> = MutableStateFlow(LoadingState.Loading)

    init {
        startLoading()
    }

    fun isStillLoading(): Boolean {
//        Timber.d("viewstate: ${viewState.value}")
        return viewState.value == LoadingState.Loading
    }

    private fun startLoading() {
        viewModelScope.launch {
            val doneLoading = splashScreenLoader.loadData()
            if (!doneLoading) {
                viewState.value = LoadingState.Loaded
                _startRoute = LoginPageDestination
                return@launch
            }

            viewState.value = LoadingState.Loaded
        }
    }

    sealed class LoadingState {
        data object Loading : LoadingState()
        data object Loaded : LoadingState()
    }
}
