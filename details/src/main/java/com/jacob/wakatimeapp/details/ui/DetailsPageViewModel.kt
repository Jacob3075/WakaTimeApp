package com.jacob.wakatimeapp.details.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacob.wakatimeapp.details.domain.usecases.GetProjectStatsUC
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
internal class DetailsPageViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getProjectStatsUC: GetProjectStatsUC,
) : ViewModel() {
    private val args = savedStateHandle.navArgs<DetailsPageNavArgs>()

    private val _viewState = MutableStateFlow<DetailsPageViewState>(DetailsPageViewState.Loading)
    val viewState = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            val data = getProjectStatsUC(args.projectName)
            Timber.w(data.toString())
        }
    }
}
