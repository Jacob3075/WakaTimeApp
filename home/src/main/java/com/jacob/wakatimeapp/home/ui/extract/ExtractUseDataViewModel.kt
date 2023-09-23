package com.jacob.wakatimeapp.home.ui.extract

import com.jacob.wakatimeapp.core.models.Error.DomainError.UnknownError as UnknownDomainError
import com.jacob.wakatimeapp.home.ui.extract.ExtractPageViewState as ViewState
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import arrow.core.raise.either
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.home.data.network.HomePageNetworkData
import com.jacob.wakatimeapp.home.domain.models.ExtractCreationProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import okhttp3.ResponseBody
import timber.log.Timber

@HiltViewModel
internal class ExtractUseDataViewModel @Inject constructor(
    private val homePageNetworkData: HomePageNetworkData,
    private val ioDispatcher: CoroutineContext = Dispatchers.IO,
    private val application: Application,
) : AndroidViewModel(application) {
    private val _extractPageState = MutableStateFlow<ViewState>(ViewState.Idle)
    val extractPageState = _extractPageState.asStateFlow()

    fun demo() {
        viewModelScope.launch(ioDispatcher) {
            var progress = 0f
            while (true) {
                delay(1000)
                progress += 0.2f

                _extractPageState.value = ViewState.CreatingExtract(progress)

                if (progress == 1f) {
                    delay(1000)
                    _extractPageState.value = ViewState.ExtractCreated(
                        ExtractCreationProgress(
                            createdAt = LocalDateTime(0, 0, 0, 0, 0, 0, 0),
                            downloadUrl = "",
                            hasFailed = false,
                            isProcessing = false,
                            isStuck = false,
                            percentComplete = 100.0F,
                            id = "",
                            expires = null,
                            status = "",
                            type = "",
                        ),
                    )
                    break
                }
            }
        }
    }

    fun createExtract() {
        viewModelScope.launch(ioDispatcher) {
            either {
                val extractCreationProgress = homePageNetworkData.createExtract().bind()
                Timber.d("Extract creation progress: ${extractCreationProgress.id}")
                _extractPageState.value =
                    ViewState.CreatingExtract(extractCreationProgress.percentComplete)

                async {
                    monitorExtractCreationProgress(extractCreationProgress.id)
                }
            }.mapLeft { error -> _extractPageState.value = ViewState.Error(error) }
        }
    }

    private suspend fun monitorExtractCreationProgress(id: String) = either {
        while (true) {
            val extractCreationProgress = homePageNetworkData.getExtractCreationProgress(id).bind()

            when {
                extractCreationProgress.isProcessing ->
                    _extractPageState.value =
                        ViewState.CreatingExtract(extractCreationProgress.percentComplete)

                extractCreationProgress.status == CompletedStatusString -> {
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

            delay(ProgressPollingDelay)
        }
    }.mapLeft { error -> _extractPageState.value = ViewState.Error(error) }

    fun downloadExtract(extractCreationProgress: ExtractCreationProgress) {
        val downloadUrl = extractCreationProgress.downloadUrl ?: run {
            Timber.e("downloadUrl is null for id: ${extractCreationProgress.id}")
            return
        }

        val downloadLocation = application.filesDir.absolutePath + "WakaTimeExtract.json"

        Timber.d("Download location: $downloadLocation")
        viewModelScope.launch(ioDispatcher) {
            either {
                val downloadExtract = homePageNetworkData.downloadExtract(downloadUrl).bind()
                val filePath = saveFile(downloadExtract, downloadLocation).bind()
                // TODO: PARSE JSON FILE INTO DTOs
                // TODO: SAVE TO DB
                // TODO: DELETE FILE
            }.mapLeft { _extractPageState.value = ViewState.Error(it) }
        }
    }

    fun downloadExistingExtract() {
        viewModelScope.launch(ioDispatcher) {
            either {
                val firstOrNull = homePageNetworkData.getCreatedExtracts()
                    .bind()
                    .sortedBy(ExtractCreationProgress::createdAt)
                    .firstOrNull()
                    ?: run {
                        _extractPageState.value =
                            ViewState.Error(Error.UnknownError("No extracts found, try creating one now"))
                        return@either
                    }

                Timber.d("found extract: $firstOrNull")
                async {
                    monitorExtractCreationProgress(firstOrNull.id)
                }
            }.mapLeft { error -> _extractPageState.value = ViewState.Error(error) }
        }
    }

    private fun saveFile(
        body: ResponseBody,
        pathWhereYouWantToSaveFile: String,
    ): Either<UnknownDomainError, String> {
        var input: InputStream? = null
        return Either.catch {
            input = body.byteStream()
            val fos = FileOutputStream(pathWhereYouWantToSaveFile)
            fos.use { output ->
                val buffer = ByteArray(BufferSize)
                var read: Int
                while (input!!.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
            input!!.close()
            pathWhereYouWantToSaveFile
        }.mapLeft { error ->
            input?.close()
            Timber.tag("saveFile").e("Error while downloading extract, error: $error")
            UnknownDomainError("Could not download extract, error: $error")
        }
    }

    internal companion object Constants {
        const val AnimationDuration = 250
        const val ProgressPollingDelay = 1500L
        const val BufferSize = 4096
        const val CompletedStatusString = "Completed"
    }
}
