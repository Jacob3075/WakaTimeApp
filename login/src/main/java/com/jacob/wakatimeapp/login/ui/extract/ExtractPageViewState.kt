package com.jacob.wakatimeapp.login.ui.extract

import com.jacob.wakatimeapp.core.models.Error as CoreModelsError
import com.jacob.wakatimeapp.login.domain.models.ExtractCreationProgress

internal sealed class ExtractPageViewState {
    data object Idle : ExtractPageViewState()

    data class CreatingExtract(val progress: Float) : ExtractPageViewState()

    data class ExtractCreated(val createdExtract: ExtractCreationProgress) : ExtractPageViewState()

    data object DownloadingExtract : ExtractPageViewState()

    data class Error(val error: CoreModelsError) : ExtractPageViewState()

    data object ExtractLoaded : ExtractPageViewState()
}
