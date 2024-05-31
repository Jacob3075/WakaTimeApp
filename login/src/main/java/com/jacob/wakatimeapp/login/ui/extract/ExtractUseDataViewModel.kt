package com.jacob.wakatimeapp.login.ui.extract

import com.jacob.wakatimeapp.login.ui.extract.ExtractPageViewState as ViewState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.raise.either
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.login.data.LoginPageNetworkData
import com.jacob.wakatimeapp.login.domain.models.ExtractCreationProgress
import com.jacob.wakatimeapp.login.domain.usecases.LoadExtractedDataIntoDbUC
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
internal class ExtractUseDataViewModel @Inject constructor(
    private val loginPageNetworkData: LoginPageNetworkData,
    private val loadExtractedDataIntoDbUC: LoadExtractedDataIntoDbUC,
    private val ioDispatcher: CoroutineContext = Dispatchers.IO,
) : ViewModel() {
    private val _extractPageState = MutableStateFlow<ViewState>(ViewState.Idle)
    val extractPageState = _extractPageState.asStateFlow()

    fun createExtract() {
        viewModelScope.launch(ioDispatcher) {
            either {
                val extractCreationProgress = loginPageNetworkData.createExtract().bind()
                Timber.d("Extract creation progress: ${extractCreationProgress.id}")
                _extractPageState.value =
                    ViewState.CreatingExtract(extractCreationProgress.percentComplete)

                async {
                    monitorExtractCreationProgress(extractCreationProgress.id)
                }
            }.mapLeft { error -> _extractPageState.value = ViewState.Error(error) }
        }
    }

    private suspend fun monitorExtractCreationProgress(id: String) {
        either {
            while (true) {
                val extractCreationProgress = loginPageNetworkData.getExtractCreationProgress(id).bind()

                when {
                    extractCreationProgress.isProcessing ->
                        _extractPageState.value = ViewState.CreatingExtract(extractCreationProgress.percentComplete)

                    extractCreationProgress.status == COMPLETED_STATUS_STRING -> {
                        _extractPageState.value = ViewState.ExtractCreated(extractCreationProgress)
                        break
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

                delay(PROGRESS_POLLING_DELAY)
            }
        }.mapLeft { error -> _extractPageState.value = ViewState.Error(error) }
    }

    fun downloadExtract(extractCreationProgress: ExtractCreationProgress) {
        val downloadUrl = extractCreationProgress.downloadUrl ?: run {
            Timber.e("downloadUrl is null for id: ${extractCreationProgress.id}")
            return
        }

        viewModelScope.launch(ioDispatcher) {
            either {
                _extractPageState.value = ViewState.DownloadingExtract
                val downloadExtract = loginPageNetworkData.downloadExtract(downloadUrl).bind()
                val fileContents = downloadExtract.bytes()
                loadExtracted(fileContents)
            }.mapLeft { _extractPageState.value = ViewState.Error(it) }
        }
    }

    fun downloadExistingExtract() {
        viewModelScope.launch(ioDispatcher) {
            either {
                val firstOrNull = loginPageNetworkData.getCreatedExtracts()
                    .bind()
                    .sortedBy(ExtractCreationProgress::createdAt)
                    .firstOrNull()
                    ?: run {
                        _extractPageState.value = ViewState.Error(Error.UnknownError("No extracts found, try creating one now"))
                        return@either
                    }

                Timber.d("found extract: $firstOrNull")
                async {
                    monitorExtractCreationProgress(firstOrNull.id)
                }
            }.mapLeft { error -> _extractPageState.value = ViewState.Error(error) }
        }
    }

    fun loadExtracted(bytes: ByteArray?) {
        viewModelScope.launch(ioDispatcher) {
            either {
                loadExtractedDataIntoDbUC(bytes).bind()
                _extractPageState.value = ViewState.ExtractLoaded
            }.mapLeft { error -> _extractPageState.value = ViewState.Error(error) }
        }
    }

    companion object Constants {
        const val ANIMATION_DURATION = 250
        const val PROGRESS_POLLING_DELAY = 1500L
        const val COMPLETED_STATUS_STRING = "Completed"
    }
}
