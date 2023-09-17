package com.jacob.wakatimeapp.home.ui.extract

import com.jacob.wakatimeapp.home.ui.extract.ExtractPageViewState as ViewState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.raise.either
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.home.data.network.HomePageNetworkData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
internal class ExtractUseDataViewModel @Inject constructor(
    private val homePageNetworkData: HomePageNetworkData,
    private val ioDispatcher: CoroutineContext = Dispatchers.IO,
) : ViewModel() {
    private val _extractPageState = MutableStateFlow<ViewState>(ViewState.Idle)
    val extractPageState = _extractPageState.asStateFlow()

    fun createExtract() {
        viewModelScope.launch(ioDispatcher) {
            either {
                val extractCreationProgress = homePageNetworkData.createExtract().bind()
                _extractPageState.value =
                    ViewState.CreatingExtract(extractCreationProgress.percentComplete)

                async {
                    monitorExtractCreationProgress(extractCreationProgress.id)
                }
            }
        }
    }

    private suspend fun monitorExtractCreationProgress(id: String) = either {
        while (true) {
            val extractCreationProgress = homePageNetworkData.getExtractCreationProgress(id).bind()

            when {
                extractCreationProgress.isProcessing -> {
                    if (extractCreationProgress.percentComplete == CompletedPercentage) {
                        delay(AnimationDuration.toLong())
                        _extractPageState.value = ViewState.ExtractCreated
                        break
                    }

                    _extractPageState.value =
                        ViewState.CreatingExtract(extractCreationProgress.percentComplete)
                }

                extractCreationProgress.isStuck -> {
                    _extractPageState.value = ViewState.Error(
                        Error.UnknownError("Extract creation process is stuck: ${extractCreationProgress.status}"),
                    )
                    break
                }

                extractCreationProgress.hasFailed -> {
                    _extractPageState.value = ViewState.Error(
                        Error.UnknownError("Error while trying to create extract: ${extractCreationProgress.status}"),
                    )
                    break
                }
            }

            delay(ProgressPollingDelay)
        }
    }.mapLeft { error -> _extractPageState.value = ViewState.Error(error) }

    fun downloadExtract() {
        TODO("Not yet implemented")
    }

    internal companion object Constants {
        const val AnimationDuration = 250
        const val CompletedPercentage = 100.0f
        const val ProgressPollingDelay = 1500L
    }
}
