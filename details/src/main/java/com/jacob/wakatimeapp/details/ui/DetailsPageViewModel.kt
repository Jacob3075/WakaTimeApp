package com.jacob.wakatimeapp.details.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.raise.either
import com.jacob.wakatimeapp.core.common.utils.InstantProvider
import com.jacob.wakatimeapp.details.domain.usecases.GetProjectStatsUC
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import timber.log.Timber

@HiltViewModel
internal class DetailsPageViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getProjectStatsUC: GetProjectStatsUC,
    private val instantProvider: InstantProvider,
) : ViewModel() {
    private val args = savedStateHandle.navArgs<DetailsPageNavArgs>()

    private val _viewState = MutableStateFlow<DetailsPageViewState>(DetailsPageViewState.Loading)
    val viewState = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            either {
                val data = getProjectStatsUC(args.projectName).bind()
                Timber.w(data.toString())
                _viewState.value = DetailsPageViewState.Loaded(
                    projectName = args.projectName,
                    statsForProject = data.dailyProjectStats,
                )
            }.mapLeft {
                Timber.e("Error while getting stats for project ${args.projectName}: $it")
                _viewState.value = DetailsPageViewState.Error(it)
            }
        }
    }

    fun getTodaysDate(): LocalDate = instantProvider.date()
}
