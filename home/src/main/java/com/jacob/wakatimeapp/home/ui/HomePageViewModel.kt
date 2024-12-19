package com.jacob.wakatimeapp.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.jacob.wakatimeapp.core.common.utils.InstantProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.LocalDate

@HiltViewModel
internal class HomePageViewModel @Inject constructor(
    private val instantProvider: InstantProvider,
    private val dataLoaderWrapper: DataLoaderWrapper,
) : ViewModel() {

    private val _homePageState = MutableStateFlow<HomePageViewState>(HomePageViewState.Loading)
    val homePageState = _homePageState
        .onStart { getPreloadedData() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            HomePageViewState.Loading,
        )

    private suspend fun getPreloadedData() {
        val loadedDataResult = dataLoaderWrapper.loadedDataResult
        _homePageState.value = when (loadedDataResult) {
            is Either.Left -> HomePageViewState.Error(loadedDataResult.value)
            is Either.Right -> loadedDataResult.value
        }
    }

    fun getTodaysDate(): LocalDate = instantProvider.date()
}
